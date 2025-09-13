package org.soyaga.examples.Game2048.GA;


import org.soyaga.examples.Game2048.GA.Evaluable.Game2048ObjectiveFunction;
import org.soyaga.examples.Game2048.GA.Initializer.Game2048Initializer;
import org.soyaga.ga.GeneticAlgorithm.SimpleGeneticAlgorithm;
import org.soyaga.ga.Individual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class Game2048GA extends SimpleGeneticAlgorithm {

    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     */
    public Game2048GA(String ID, Integer[][] gridNumber, Integer maxMoves,Integer populationSize){
        super(ID,
                populationSize,
                null,
                null,
                null,
                null,
                null,
                new Game2048Initializer(
                        new Game2048ObjectiveFunction(gridNumber.length,gridNumber[0].length),
                        gridNumber,
                        maxMoves
                )
        );
    }


    /**
     * Function that performs the typical optimization for a genetic algorithm.
     * <ol>
     *     <li>Initialize population.</li>
     *     <li>Evaluate population.</li>
     *     <li>Measure stats.</li>
     *     <li>while (evaluate stopping condition):</li>
     *     <ol type="I">
     *         <li>Cross population &rarr; new population.</li>
     *         <li>Mutate new population.</li>
     *         <li>Apply elitism.</li>
     *         <li>Apply newborns.</li>
     *         <li>Reassign population to new population.</li>
     *         <li>Evaluate population.</li>
     *         <li>Measure stats.</li>
     *     </ol>
     *     <li>Close stats.</li>
     * </ol>
     */
    @Override
    public void optimize() {
        this.gaInitializer.initialize(this);
        this.population.evaluate();
    }

    /**
     * Transform the genome of the best individual into a String representing its value.
     *
     * @return String.
     */
    @Override
    public HashMap<String, Double> getResult(Object... resultArgs) {
        double total=0;
        HashMap<String, Double> solutionMap = new HashMap<>();
        HashMap<String, Integer> countMap = new HashMap<>();
        for(Individual individual:this.population.getPopulation()){
            double value = individual.getFitnessValue();
            String direction = (String)((ArrayList<Object[]>)individual.getGenome().getGeneticInformation()).get(0)[0];
            solutionMap.putIfAbsent(direction, 0.);
            solutionMap.put(direction, solutionMap.get(direction)+value);
            countMap.putIfAbsent(direction, 0);
            countMap.put(direction, countMap.get(direction)+1);
        }
        for(Map.Entry<String,Double> entry: solutionMap.entrySet()){
            entry.setValue(entry.getValue()/countMap.get(entry.getKey()));
            entry.setValue(entry.getValue()*entry.getValue());
            total+=entry.getValue();
        }
        for(Map.Entry<String,Double> entry: solutionMap.entrySet()){
            entry.setValue(entry.getValue()/total);
        }
        return solutionMap;
    }

}
