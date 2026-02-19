package org.soyaga.examples.Tetris.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.Tetris.GA.TetrisIndividual;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;
import org.soyaga.ga.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.ga.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.HashMapGenome;
import org.soyaga.ga.Individual;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

@AllArgsConstructor
public class TetrisInitializer extends GAInitializer {
    private final BoardEvaluationFunction boardEvaluationFunction;
    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        HashMapGenome<String, Double> genome = new HashMapGenome<>();
        for(Map.Entry<String, Double> entry: this.boardEvaluationFunction.getWeights().entrySet()){
            genome.add(new AbstractMap.SimpleEntry<>(entry.getKey(), random.nextDouble(-10,10)));
        }
        TetrisIndividual individual = new TetrisIndividual(genome,10000., 200, this.boardEvaluationFunction.newInstance());
        individual.setWeights(genome);
        return  individual;
    }
}
