package org.soyaga.examples.SlitherLink.ACO.StoppingPolicy;

import lombok.AllArgsConstructor;
import org.soyaga.aco.Colony;
import org.soyaga.aco.StoppingPolicy.StoppingCriteriaPolicy;

import java.util.Objects;

@AllArgsConstructor
public class TargetFeasibilityCriteriaPolicy implements StoppingCriteriaPolicy {
    /**
     * Integer with the maximum number of iterations to perform.
     */
    private Double targetFeasibility;
    /**
     * Colony to evaluate.
     */
    private Colony colony;

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
        if(this.colony.getBestSolution()==null) return true;
        return !Objects.equals(this.colony.getBestSolution().getFeasibilityValue(), this.targetFeasibility);
    }
}
