package org.soyaga.examples.MadVirus.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.MadVirus.GA.Evaluable.MadVirusFeasibilityFunction;
import org.soyaga.examples.MadVirus.GA.Evaluable.MadVirusObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.ArrayList;
import java.util.Random;

@AllArgsConstructor
public class MadVirusInitializer extends GAInitializer {
    private final MadVirusObjectiveFunction objectiveFunction;
    private final MadVirusFeasibilityFunction feasibilityFunction;
    private final ArrayList<Integer> choices;
    private final int turns;
    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<Integer> genome = new ArrayListGenome<>();
        for(int turn=0; turn<this.turns;turn++){
            int color = this.choices.get(random.nextInt(this.choices.size()));
            while(turn >1 && color == genome.getGeneticInformation().get(turn-1)){
                color = this.choices.get(random.nextInt(this.choices.size()));
            }
            genome.add(color);
        }
        return  new Individual(genome,this.feasibilityFunction,this.objectiveFunction,10.);
    }
}
