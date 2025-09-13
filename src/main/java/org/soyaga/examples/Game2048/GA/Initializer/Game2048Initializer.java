package org.soyaga.examples.Game2048.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.Game2048.Board.Board;
import org.soyaga.examples.Game2048.GA.Evaluable.Game2048ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.*;

@AllArgsConstructor
public class Game2048Initializer extends GAInitializer {
    private final Game2048ObjectiveFunction objectiveFunction;
    private final Integer[][] gridNumber;
    private final Integer maxMoves;


    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<Object[]> genome = new ArrayListGenome<>();
        Board board = new Board(this.gridNumber);
        for(int genIndex=0; genIndex<this.maxMoves; genIndex++) {
            HashMap<String,Double> movementProbabilities = board.getMovementsProbabilities();
            double selection = random.nextDouble();
            double cumulative = 0.;
            String movement = null;
            for(Map.Entry<String,Double> probEntry: movementProbabilities.entrySet()){
                cumulative+=probEntry.getValue();
                if(cumulative>=selection) {
                    movement= probEntry.getKey();
                    break;
                }
            }
            if(movement==null){
               break;
            }
            board.move(movement);
            genome.add(new Object[]{movement,board.getBoardGrid()});
        }
        return  new Individual(genome,null,this.objectiveFunction,0.);
    }
}
