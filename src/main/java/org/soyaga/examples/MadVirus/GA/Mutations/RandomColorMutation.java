package org.soyaga.examples.MadVirus.GA.Mutations;

import lombok.AllArgsConstructor;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.MutationPolicy.Mutations.Mutation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mutation that perform a negation of a BooleanGen. Negates different random BooleanGen values swapNumber times.
 */
@AllArgsConstructor
public class RandomColorMutation implements Mutation {
    private final ArrayList<Integer> choices;

    /**
     * This function apply the negation of a boolean Gen type.
     * @param gen Gen to mutate.
     */
    @Override
    public void apply(Object gen, Object... mutationArgs) {
        ArrayList<Integer> genomeInformation =((ArrayListGenome<Integer>)gen).getGeneticInformation();
        Random random = new Random();
        genomeInformation.set(random.nextInt(genomeInformation.size()),this.choices.get(random.nextInt(this.choices.size())));
    }
}
