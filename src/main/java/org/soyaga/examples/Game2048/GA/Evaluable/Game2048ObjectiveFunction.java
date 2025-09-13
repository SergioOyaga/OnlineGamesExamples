package org.soyaga.examples.Game2048.GA.Evaluable;

import org.soyaga.ga.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;

public class Game2048ObjectiveFunction implements ObjectiveFunction {

    private final int rows;
    private final int cols;
    private final Double[][] boardRelevancy;

    public Game2048ObjectiveFunction(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.boardRelevancy = this.computeBoardRelevancy();
    }

    private Double[][] computeBoardRelevancy() {
        Double[][] boardRelevancy= new Double[this.rows][this.cols];
        int total = this.rows*this.cols;
        double step = 1.0/total;
        double cumulative = 0.;
        for(int row=0; row<this.rows; row++){
            if(row%2==0){
                for(int col=this.cols-1; col>=0; col--){
                    boardRelevancy[row][col]=cumulative;
                    cumulative+=Math.pow(step,2.);
                }

            }
            else {
                for(int col=0; col<this.cols; col++){
                    boardRelevancy[row][col]=cumulative;
                    cumulative+=Math.pow(step,2.);
                }
            }
        }
        return boardRelevancy;
    }

    /**
     * This function computes the objective function of a Genome.
     *
     * @param genome        Genome Object to evaluate.
     * @param objectiveArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the objective function.
     */
    @Override
    public Double evaluate(Genome<?> genome, Object... objectiveArgs) {
        return evaluate(genome);
    }
    private Double evaluate(Genome<?> genome){
        Integer[][] board = (Integer[][])((ArrayList<Object[]>)genome.getGeneticInformation()).get(genome.getGeneticInformation().size()-1)[1];
        double numberRelevancy = 0.;
        for (int row=0; row<this.rows; row++) {
            for (int col=0; col<this.cols; col++) {
                Integer value = board[row][col];
                if (value != null) {
                    numberRelevancy+= value*this.boardRelevancy[row][col];
                }
            }
        }
        return numberRelevancy + 1000*genome.getGeneticInformation().size();
    }
}
