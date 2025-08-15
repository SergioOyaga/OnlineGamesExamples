package org.soyaga.examples.AllOut.GA.Evaluable;

import org.soyaga.ga.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.ga.GeneticInformationContainer.Chromosome.ArrayListChromosome;
import org.soyaga.ga.GeneticInformationContainer.Gen.GenericGen;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;
import java.util.Random;

public class AllOutFeasibilityFunction implements FeasibilityFunction {
    private final Boolean[][] imageGrid;
    private final int rows, cols;

    public AllOutFeasibilityFunction(Boolean[][] imageGrid, int rows, int cols) {
        this.imageGrid = imageGrid;
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * This function computes the feasibility function of a Genome.
     *
     * @param genome          Genome Object to evaluate.
     * @param feasibilityArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the objective function.
     */
    @Override
    public Double evaluate(Genome<?> genome, Object... feasibilityArgs) {
        return evaluate(genome);
    }
    private Double evaluate(Genome<?> genome){
        Boolean[][] imageGridComputed = new Boolean[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.imageGrid[i], 0, imageGridComputed[i], 0, this.imageGrid[i].length);
        }
        ArrayList<ArrayListChromosome<GenericGen<Boolean>>> genomeContainer = (ArrayList<ArrayListChromosome<GenericGen<Boolean>>>) genome.getGeneticInformation();
        double objective = 0.;
        int row=0;
        int col;
        for(ArrayListChromosome chromosomeRow:genomeContainer){
            ArrayList<GenericGen<Boolean>> genContainer = chromosomeRow.getGeneticInformation();
            col=0;
            for(GenericGen<Boolean> genCell:genContainer){
                if(genCell.getGeneticInformation()){
                    if(row>0) { //North
                        imageGridComputed[row-1][col] = !imageGridComputed[row-1][col];
                    }
                    if(row<this.rows-1) { //South
                        imageGridComputed[row+1][col] = !imageGridComputed[row+1][col];
                    }
                    if(col>0) { //West
                        imageGridComputed[row][col-1] = !imageGridComputed[row][col-1];
                    }
                    if(col<this.cols-1) { //East
                        imageGridComputed[row][col+1] = !imageGridComputed[row][col+1];
                    }
                    //Center
                    imageGridComputed[row][col] = !imageGridComputed[row][col];
                }
                col++;
            }
            row++;
        }
        for(row=0; row<this.rows;row++){
            for(col=0; col<this.cols;col++){
                if(imageGridComputed[row][col]) objective++;
            }
        }
        return objective;
    }
}
