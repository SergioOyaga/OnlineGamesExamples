package org.soyaga.examples.SlitherLink.ACO.Evaluable.Constraints;

import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.World;

import java.util.ArrayList;
import java.util.HashSet;

public class SlitherLinkConstraint implements Constraint {
    private final int value;
    private final HashSet<Edge> edges;

    /**
     * Constructor
     * @param value int with the exact number of edges that must be utilized.
     * @param edges HashSet of edges surrounding the number.
     */
    public SlitherLinkConstraint(int value, HashSet<Edge> edges) {
        this.value = value;
        this.edges = edges;
    }

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
        ArrayList<Object> solutionCopy = new ArrayList<>(solution.getEdgesUtilized());
        solutionCopy.removeIf(o->!this.edges.contains(o));
        return (double) Math.abs(this.value-solutionCopy.size());
    }
}
