package org.soyaga.examples.Maze.ACO.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.Maze.ACO.Elements.MazeNode;


@AllArgsConstructor
public class MazeObjectiveFunction implements ObjectiveFunction {
    private final MazeNode endNode;
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
        double minDistance = world.getGraph().getNodes().size();
        for(Object nodeObject: solution.getNodesVisited()){
            double distance = this.endNode.computeDistance((MazeNode) nodeObject);
            minDistance = minDistance>distance? distance: minDistance;
        }
        return minDistance;
    }
}
