package org.soyaga.examples.Tetris.GA.CrossoverPolicy.ParentCross;

import org.soyaga.examples.Tetris.GA.TetrisIndividual;
import org.soyaga.ga.CrossoverPolicy.ParentCross.Crossover;
import org.soyaga.ga.GeneticInformationContainer.Genome.HashMapGenome;
import org.soyaga.ga.Individual;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

public class MapMixCross implements Crossover {
    /**
     * Function that computes an individual given two parents.
     *
     * @param parent1   Individual with the first parent.
     * @param parent2   Individual with the second parent.
     * @param crossArgs Undefined Array of elements to perform the crossover procedure.
     * @return Individual containing the offspring information.
     */
    @Override
    public Individual computeChild(Individual parent1, Individual parent2, Object... crossArgs) {

        assert parent1 instanceof TetrisIndividual;
        assert parent2 instanceof TetrisIndividual;
        assert parent1.getGenome() instanceof HashMapGenome<?,?>;
        assert parent2.getGenome() instanceof HashMapGenome<?,?>;
        TetrisIndividual tetrisP1 = (TetrisIndividual) parent1;
        TetrisIndividual tetrisP2 = (TetrisIndividual) parent2;
        HashMapGenome<Object,Object> genome = new HashMapGenome<>();
        Individual newIndividual = new TetrisIndividual(genome, tetrisP1.getPenalization(), tetrisP1.getMaxIterations(), ((TetrisIndividual) parent1).getPlayer());
        HashMapGenome<Object,?> parent1Genome = (HashMapGenome<Object,?>)tetrisP1.getGenome();
        HashMapGenome<Object,?> parent2Genome = (HashMapGenome<Object,?>)tetrisP2.getGenome();
        Random random = new Random();
        for(Map.Entry<?,?> parent1Entry:parent1Genome.getGeneticInformation()){
            Object parent1Value = parent1Genome.getValue(parent1Entry.getKey());
            Object parent2Value = parent2Genome.getValue(parent1Entry.getKey());
            genome.add(new AbstractMap.SimpleEntry<>(parent1Entry.getKey(),random.nextBoolean()?parent1Value:parent2Value));
        }

        return newIndividual;
    }
}
