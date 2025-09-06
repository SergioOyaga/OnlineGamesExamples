package org.soyaga.examples.JellyDoods.GA.StoppingPolicy;

import org.soyaga.ga.StoppingPolicy.StoppingCriteriaPolicy;

import java.util.Objects;

/**
 * This class defines a StoppingCriteriaPolicy based on whether the fitness value is a specific value.
 */
public class TargetFeasibilityCriteriaPolicy implements StoppingCriteriaPolicy {
    /**
     * Integer with the maximum number of iterations to perform.
     */
    private final Double targetFeasibility;

    public TargetFeasibilityCriteriaPolicy(Double targetFeasibility) {
        this.targetFeasibility = targetFeasibility;
    }

    /**
     * Function that checks if the AntColonyAlgorithm has to continue optimizing or not.
     *
     * @param stopArgs VarArgs containing the additional information needed to apply the stopping criteria policy.
     * <ul>
     *      <li>stopArgs[0] = currentIteration</li>
     *      <li><b>***</b></li>
     * </ul>
     * @return Boolean, <b><i> true </i></b> if it has to continue, <b><i> false </i></b>
     * if not.
     */
    @Override
    public Boolean hasToContinue(Object ... stopArgs){
        return !Objects.equals(stopArgs[0], this.targetFeasibility);
    }
}
