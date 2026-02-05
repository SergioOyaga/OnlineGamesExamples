package org.soyaga.examples.SlitherLink.ACO.Evaluable.Constraints;

import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;

public class StartEndConstraint implements Constraint {
    /**
     * This function computes the feasibility value of a solution.
     *
     * @param world          world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution       Solution object to evaluate.
     * @param constraintArgs VarArgs containing the additional information needed to evaluate the constraint.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... constraintArgs) {
        Object startNode = solution.getNodesVisited().get(0);
        Object endNode = solution.getNodesVisited().get(solution.getNodesVisited().size()-1);
        return startNode==endNode?0.:100.;
    }
}
