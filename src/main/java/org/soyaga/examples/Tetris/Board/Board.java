package org.soyaga.examples.Tetris.Board;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter @Setter
public class Board {
    private final int rows,cols;
    private final boolean[][] grid;
    private boolean isActive;
    private double score;
    private int iterations;

    // Evaluation function parameters
    private double meanHeight;
    private double stdHeight;
    private double maxHeight;
    private double smoothnessHeight;
    private double holeNumber;
    private double meanHoleDepth;
    private double stdHoleDepth;
    private double lastClearedLinesNumber;


    public Board() {
        this.rows = 20;
        this.cols = 10;
        this.grid = new boolean[this.rows][this.cols];
        this.isActive = true;
        this.score = 0.;
        this.iterations = 0;
        // Evaluation function parameters
        this.meanHeight = 0;
        this.stdHeight = 0;
        this.maxHeight = 0;
        this.smoothnessHeight = 0;
        this.holeNumber = 0;
        this.meanHoleDepth = 0;
        this.stdHoleDepth = 0;
        this.lastClearedLinesNumber = 0;
    }

    public Board copyBoard(){
        Board b =  new Board();
        for(int r=0; r<this.rows; r++){
            for(int c=0; c<this.cols; c++){
                b.grid[r][c] = this.grid[r][c];
            }
        }
        b.isActive = this.isActive;
        b.score = this.score;
        b.iterations = this.iterations;
        b.meanHeight = this.meanHeight;
        b.stdHeight = this.stdHeight;
        b.maxHeight = this.maxHeight;
        b.smoothnessHeight = this.smoothnessHeight;
        b.holeNumber = this.holeNumber;
        b.meanHoleDepth = this.meanHoleDepth;
        b.stdHoleDepth = this.stdHoleDepth;
        b.lastClearedLinesNumber = this.lastClearedLinesNumber;
        return b;
    }

    public void moveDownPiece(ArrayList<int[]> initialPosition){
        if(!this.isActive)return;
        int maxMovement = this.rows;
        for(int[] piecePart:initialPosition){
            int piecePartRow= piecePart[0];
            int piecePartCol= piecePart[1];
            int partMovement =-1;
            for(int r=piecePartRow; r<this.rows; r++){
                if(this.grid[r][piecePartCol]) break;
                partMovement++;
            }
            if(partMovement<0){
                this.isActive=false;
                return;
            }
            if(partMovement<maxMovement) maxMovement=partMovement;
        }
        if (maxMovement<0) return;
        for(int[] piecePart:initialPosition){
            this.grid[piecePart[0]+maxMovement][piecePart[1]] = true;
        }
        this.updateBoard();
        this.iterations++;
    }

    private void updateBoard(){
        this.clearLines();
        double[] heights = this.evaluateHeightFeatures();
        this.evaluateHoleFeatures(heights);
    }

    /**
     * Clears completed lines and updates lastClearedLinesNumber number.
     */
    private void clearLines() {
        int clearedRows = 0;
        for(int row=0; row<this.rows; row++){
            boolean toClear = true;
            for(int col=0; col<this.cols; col++){
                if (!this.grid[row][col]) {
                    toClear = false;
                    break;
                }
            }
            if(toClear){
                clearedRows++;
                for(int replaceRow=row; replaceRow>0; replaceRow--){
                    boolean nextIsEmpty = true;
                    for(int replaceCol=0; replaceCol<this.cols;replaceCol++){
                        boolean next = this.grid[replaceRow-1][replaceCol];
                        this.grid[replaceRow][replaceCol] = next;
                        if(next) nextIsEmpty=false;
                    }
                    if(nextIsEmpty) break;
                }
            }
        }
        this.lastClearedLinesNumber = clearedRows;
        this.score+=this.lastClearedLinesNumber;
    }

    /**
     * Evaluates all related height features: meanHeight, stdHeight, maxHeight and smoothnessHeight
     */
    private double[] evaluateHeightFeatures() {
        double[] heights= new double[this.cols];
        for(int col=0; col<this.cols; col++){
            heights[col] = this.rows;
            for(int row=0; row<this.rows; row++){
                if(this.grid[row][col]){
                    heights[col]=row;
                    break;
                }
            }
        }
        this.meanHeight = 0;
        this.maxHeight = this.rows;
        for(int col=0; col<this.cols; col++){
            double value = heights[col];
            this.meanHeight +=value;if
            (this.maxHeight>value) this.maxHeight=value;
            if(col<this.cols-1){
                double nextValue = heights[col+1];
                this.smoothnessHeight+= Math.abs(value-nextValue);
            }
        }
        this.meanHeight /= this.cols;
        this.stdHeight = Math.sqrt(Arrays.stream(heights).map(x -> Math.pow(x - this.meanHeight, 2)).sum() / this.cols);
        this.smoothnessHeight /= this.cols;
        //Normalize
        this.meanHeight /= this.rows;
        this.stdHeight /=this.rows;
        this.maxHeight /= this.rows;
        this.smoothnessHeight /= this.rows;

        return  heights;
    }

    /**
     * Evaluates all related hole features: holeNumber, meanHoleDepth and stdHoleDepth
     */
    private void evaluateHoleFeatures(double[] heights) {
        ArrayList<Double> holes= new ArrayList<>();
        for(int col=0; col<this.cols; col++){
            int firstPieceRow = (int) heights[col];
            for(int row=firstPieceRow+1; row<this.rows; row++){
                if(!this.grid[row][col]){
                    holes.add((double) row);
                }
            }
        }
        this.holeNumber = holes.size();
        this.meanHoleDepth = 0;
        this.stdHoleDepth = 0;
        if(this.holeNumber!=0) {
            this.meanHoleDepth = holes.stream().reduce(0., Double::sum) / this.holeNumber;
            this.stdHoleDepth = Math.sqrt(holes.stream().map(x -> Math.pow(x - this.meanHoleDepth, 2)).reduce(0., Double::sum) / this.holeNumber);
        }
        //Normalize
        this.holeNumber /= (this.rows*this.cols);
        this.meanHoleDepth  /= this.rows;
        this.stdHoleDepth  /= this.rows;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     * @apiNote In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * The string output is not necessarily stable over time or across
     * JVM invocations.
     * @implSpec The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int row=0; row<this.rows;row++){
            for(int col=0; col<this.cols; col++){
               if(this.grid[row][col]){
                   str.append(" # ");
               }
               else str.append("   ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
