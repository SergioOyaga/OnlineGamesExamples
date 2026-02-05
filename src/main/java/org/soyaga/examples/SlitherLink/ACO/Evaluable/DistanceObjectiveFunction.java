package org.soyaga.examples.SlitherLink.ACO.Evaluable;

import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;

public class DistanceObjectiveFunction implements ObjectiveFunction {
    private final int maxSize;

    /**
     * Constructor
     * @param maxSize int with the max route size.
     */
    public DistanceObjectiveFunction(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    /**
     * This function computes the value of the evaluable object.
     *
     * @param world         world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution      Solution object to evaluate.
     * @param evaluableArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... evaluableArgs) {
        return (double) (this.maxSize-solution.getEdgesUtilized().size());
    }
}
