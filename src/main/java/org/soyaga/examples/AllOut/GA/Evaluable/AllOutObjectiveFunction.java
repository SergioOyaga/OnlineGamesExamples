package org.soyaga.examples.AllOut.GA.Evaluable;

import org.soyaga.ga.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Chromosome.ArrayListChromosome;
import org.soyaga.ga.GeneticInformationContainer.Gen.GenericGen;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;

public class AllOutObjectiveFunction implements ObjectiveFunction {

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
        ArrayList<ArrayListChromosome<GenericGen<Boolean>>> genomeContainer = (ArrayList<ArrayListChromosome<GenericGen<Boolean>>>) genome.getGeneticInformation();
        double objective = 0.;
        for(ArrayListChromosome chromosomeRow:genomeContainer){
            ArrayList<GenericGen<Boolean>> genContainer = chromosomeRow.getGeneticInformation();
            for(GenericGen<Boolean> genCell:genContainer){
                if(genCell.getGeneticInformation())objective++;
            }
        }
        return objective;

    }
}
