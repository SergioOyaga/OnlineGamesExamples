package org.soyaga.examples.JellyDoods.Board;


import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Board {
    private final int rows;
    private final int cols;

    private final Cell [][] boardGrid;
    @Getter
    private final LinkedHashMap<String, Piece> jelliesById;
    private final LinkedHashMap <String, Piece> blocksById;


    public Board(ArrayList<ArrayList<Integer>> level) {
        this.rows = 8;
        this.cols = 8;
        this.boardGrid = new Cell[this.rows][this.cols];
        this.jelliesById = new LinkedHashMap <>();
        this.blocksById = new LinkedHashMap <>();
        this.computeBoard();
        this.computePieces(level);
        this.mergePieces();
    }

    private void computeBoard(){
        for(int row=0;row<this.rows;row++){
            for(int col=0;col<this.cols;col++){
                this.boardGrid[row][col] = new Cell(row, col);
            }
        }
    }

    private void computePieces(ArrayList<ArrayList<Integer>> level){
        for(int row=0;row<this.rows; row++){
            for(int col=0;col<this.cols;col++){
                int type = level.get(row).get(col);
                if (type==1) {
                    Piece block = new Piece("block("+row+","+col+")", type);
                    block.addCell(this.boardGrid[row][col]);
                    this.blocksById.put(block.getId(),block);
                }
                else if (type!=0){
                    Piece jelly = new Piece("jelly("+row+","+col+")", type);
                    jelly.addCell(this.boardGrid[row][col]);
                    this.jelliesById.put(jelly.getId(), jelly);
                }
            }
        }
    }

    private void mergePieces(){
        HashSet<Piece> mergedBlocks = new HashSet<>();
        for(Piece block: this.blocksById.values()){
            if(!mergedBlocks.contains(block)) {
                mergedBlocks.addAll(this.mergePiece(block));
            }
        }
        for(Piece mergedPiece:mergedBlocks){
            this.blocksById.remove(mergedPiece.getId());
        }
        HashSet<Piece> mergedJellies = new HashSet<>();
        for(Piece jelly: this.jelliesById.values()){
            if(!mergedJellies.contains(jelly)) {
                HashSet<Piece> stepMerged = this.mergePiece(jelly);
                while (!stepMerged.isEmpty()) {
                    mergedJellies.addAll(stepMerged);
                    stepMerged = this.mergePiece(jelly);
                }
            }
        }
        for(Piece mergedPiece:mergedJellies){
            this.jelliesById.remove(mergedPiece.getId());
        }
    }

    public void movePiece(Piece piece, String direction){
        if (piece==null) return;
        //Compute movement distance
        int movementDistance = computeMovementDistance(piece,direction);
        //Compute to be cells
        HashSet<Cell> tobeCells = computeToBeCells(piece, direction, movementDistance);
        //Clear and set new piece cells
        piece.clearCells();
        piece.addCells(tobeCells);
        //Merge pieces
        HashSet<Piece> mergedPieces = this.mergePiece(piece);
        //Remove merged pieces
        for(Piece mergedPiece:mergedPieces){
            this.jelliesById.remove(mergedPiece.getId());
        }
    }

    public int computeMovementDistance(Piece piece, String direction){
        int movementDistance=rows;
        switch (direction){
            case "N":{
                for(Cell pieceCell: piece.getCurrentCells()){
                    for(int row= pieceCell.getRow()-1;row>=0;row--){
                        if (this.boardGrid[row][pieceCell.getCol()].getPiece()!=null) {
                            if (!piece.equals(this.boardGrid[row][pieceCell.getCol()].getPiece())) {
                                movementDistance = Math.min(movementDistance, pieceCell.getRow() - row - 1);
                            }
                            break;
                        }
                    }
                    movementDistance = Math.min(movementDistance,pieceCell.getRow());
                }
                break;
            }
            case "E":{
                for(Cell pieceCell: piece.getCurrentCells()){
                    for(int col= pieceCell.getCol()+1; col<this.cols;col++){
                        if (this.boardGrid[pieceCell.getRow()][col].getPiece()!=null) {
                            if (!piece.equals(this.boardGrid[pieceCell.getRow()][col].getPiece())) {
                                movementDistance = Math.min(movementDistance, col - pieceCell.getCol() - 1);
                            }
                            break;
                        }
                    }
                    movementDistance = Math.min(movementDistance,this.cols-pieceCell.getCol()-1);
                }
                break;
            }
            case "S":{
                for(Cell pieceCell: piece.getCurrentCells()){
                    for(int row= pieceCell.getRow()+1;row<this.rows;row++){
                        if (this.boardGrid[row][pieceCell.getCol()].getPiece()!=null) {
                            if (!piece.equals(this.boardGrid[row][pieceCell.getCol()].getPiece())) {
                                movementDistance = Math.min(movementDistance, row - pieceCell.getRow() - 1);
                            }
                            break;
                        }
                    }
                    movementDistance = Math.min(movementDistance,this.rows-pieceCell.getRow()-1);
                }
                break;
            }
            case "W":{
                for(Cell pieceCell: piece.getCurrentCells()){
                    for(int col= pieceCell.getCol()-1; col>=0;col--){
                        if (this.boardGrid[pieceCell.getRow()][col].getPiece()!=null) {
                            if (!piece.equals(this.boardGrid[pieceCell.getRow()][col].getPiece())) {
                                movementDistance = Math.min(movementDistance, pieceCell.getCol() - col - 1);
                            }
                            break;
                        }
                    }
                    movementDistance = Math.min(movementDistance,pieceCell.getCol());
                }
                break;
            }
            default:{
                System.out.println("Not recognized movement");
            }
        }
        return movementDistance;
    }

    private HashSet<Cell> computeToBeCells(Piece piece, String direction, int movementDistance){
        HashSet<Cell> tobeCells = new HashSet<>();
        for(Cell currentCell: piece.getCurrentCells()){
            int nextCellRow = currentCell.getRow();
            int nextCellCol = currentCell.getCol();
            switch (direction){
                case "N":{
                    nextCellRow-=movementDistance;
                    break;
                }
                case "E":{
                    nextCellCol+=movementDistance;
                    break;
                }
                case "S":{
                    nextCellRow+=movementDistance;
                    break;
                }
                case "W":{
                    nextCellCol-=movementDistance;
                    break;
                }
                default:{
                    System.out.println("Not recognized movement");
                }
            }
            tobeCells.add(this.boardGrid[nextCellRow][nextCellCol]);
        }
        return tobeCells;
    }

    private HashSet<Piece> mergePiece(Piece piece){
        HashSet<Piece> toMergePieces = new HashSet<>();
        for(Cell cell:piece.getCurrentCells()){
            int currentCellRow=cell.getRow();
            int currentCellCol = cell.getCol();
            if(currentCellRow>0) toMergePieces.add(this.boardGrid[currentCellRow-1][currentCellCol].getPiece());
            if(currentCellCol<this.cols-1) toMergePieces.add(this.boardGrid[currentCellRow][currentCellCol+1].getPiece());
            if(currentCellRow<this.rows-1) toMergePieces.add(this.boardGrid[currentCellRow+1][currentCellCol].getPiece());
            if(currentCellCol>0) toMergePieces.add(this.boardGrid[currentCellRow][currentCellCol-1].getPiece());
        }
        toMergePieces.remove(null);
        toMergePieces.remove(piece);
        return piece.mergePieces(toMergePieces);
    }

    public int solutionDistance() {
        return this.jelliesById.size() - this.jelliesById.values().stream().map(Piece::getType).collect(Collectors.toSet()).size();
    }
}