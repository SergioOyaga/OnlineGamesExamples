package org.soyaga.examples.AllOut.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.AllOut.GA.Evaluable.AllOutFeasibilityFunction;
import org.soyaga.examples.AllOut.GA.Evaluable.AllOutObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Chromosome.ArrayListChromosome;
import org.soyaga.ga.GeneticInformationContainer.Gen.GenericGen;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.Random;

@AllArgsConstructor
public class AllOutInitializer extends GAInitializer {
    private final AllOutObjectiveFunction objectiveFunction;
    private final AllOutFeasibilityFunction feasibilityFunction;
    private final int rows, cols;
    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<ArrayListChromosome<GenericGen<Boolean>>> genome = new ArrayListGenome<>();
        for(int row=0; row<rows;row++){
            ArrayListChromosome<GenericGen<Boolean>> chromosome = new ArrayListChromosome<>();
            genome.add(chromosome);
            for(int col=0; col<cols;col++){
                GenericGen<Boolean> gen= new GenericGen<>();
                chromosome.add(gen);
                gen.setGeneticInformation(random.nextBoolean());
            }
        }
        return  new Individual(genome,this.feasibilityFunction,this.objectiveFunction,3.);
    }
}
