package org.soyaga.examples.Tetris.GA.Mutations;

import org.soyaga.ga.GeneticInformationContainer.Genome.HashMapGenome;
import org.soyaga.ga.MutationPolicy.Mutations.Mutation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class TetrisBrownianMovement implements Mutation {
    private final double maxStepSize = 0.00001;
    /**
     * Function that applies the Mutations to a Genome, Chromosome or Gen.
     *
     * @param gaPart       Genome, Chromosome or Gen to mutate.
     * @param mutationArgs Undefined array of objects containing information needed to mutate the part. Ej.:
     *                     <ul>
     *                         <li>Constraints or Fitness information about genome, chromosomes and/or genes to apply Mutations in
     *                             specific scenarios. (Apply only on infeasible chromosomes, chromosomes with highest fitness deviations...)</li>
     *                     </ul>
     */
    @Override
    public void apply(Object gaPart, Object... mutationArgs) {
        if(gaPart instanceof HashMapGenome<?,?> gaMap){
            Random random = new Random();
            ArrayList<Map.Entry<?,?>> genome = new ArrayList<>(gaMap.getGeneticInformation());
            int numberOfMutations = random.nextInt(genome.size());
            for(int i =0; i<numberOfMutations;i++) {
                Map.Entry<?, Double> selectedGenEntry = (Map.Entry<?, Double>) genome.get(random.nextInt(genome.size()));
                double nextValue = selectedGenEntry.getValue() + random.nextDouble(-this.maxStepSize, +this.maxStepSize);
                selectedGenEntry.setValue(nextValue);
            }
        }
    }
}
