package org.soyaga.examples.LongCat.GA.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.ga.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

@AllArgsConstructor
public class LongCatObjectiveFunction implements ObjectiveFunction {

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
        return (double) genome.getGeneticInformation().size();
    }
}
