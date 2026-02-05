package org.soyaga.examples.SlitherLink.ACO.Ant;

import lombok.Getter;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Ant.EdgeSelector.EdgeSelector;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;

import java.util.ArrayList;
import java.util.HashSet;

public class SlitherLinkAnt implements Ant {
    /**
     * ArrayList of Nodes containing the already visited ones (ants memory).
     */
    private ArrayList<Object> memory;
    /**
     * Solution object built by the ant.
     */
    @Getter
    private Solution solution;
    /**
     * EdgeSelection with the edge selection methodology.
     */
    @Getter
    private final EdgeSelector edgeSelector;
    /**
     * A double value indicating the quantity of pheromone that an ant can distribute across the entire path.
     */
    @Getter
    private final Double pheromoneQuantity;

    /**
     * Constructor.
     *
     * @param solution Solution Type this ant has to build incrementally.
     * @param edgeSelector EdgeSelector Type that this ant uses to chose its next edge
     * @param pheromoneQuantity Double with the quantity of pheromone that an ant can distribute across the entire path.
     */
    public SlitherLinkAnt(Solution solution, EdgeSelector edgeSelector,Double pheromoneQuantity) {
        this.solution = solution;
        this.edgeSelector = edgeSelector;
        this.pheromoneQuantity = pheromoneQuantity;
        this.memory=new ArrayList<>();
    }

    /**
     * Constructor that creates a new empty instance of an ant.
     *
     * @return Ant an ant with an empty memory, new Solution, the same EdgeSelector and the same pheromone quantity.
     */
    @Override
    public SlitherLinkAnt createNewInstance() {
        return new SlitherLinkAnt(this.solution.createNewInstance(),this.edgeSelector,this.pheromoneQuantity);
    }

    /**
     * Function that builds a solution using the ant information, the world information (Graph, pheromone, ...) and any
     * other argument.
     *
     * @param world     world Object containing the "Graph" and "Pheromone" information.
     * @param buildArgs VarArgs containing the additional information needed to build a solution.
     */
    public void buildSolution(World world, Object... buildArgs) {
        this.solution = this.solution.createNewInstance();
        this.solution.selectInitialNode(world,buildArgs);
        do{
            Object currentNode=this.solution.getCurrentNode();
            HashSet<Object> edges = world.getGraph().getOutputEdges(currentNode);
            this.removeVisitedNodes(world,edges);
            if(edges.isEmpty())break;
            Object nextEdge = this.edgeSelector.apply(world, currentNode, edges);
            this.solution.buildSolution(nextEdge,world.getGraph().getNextNode(nextEdge));
            this.memory.add(this.solution.getCurrentNode());
        }
        while (!this.solution.stopBuild());
    }

    /**
     * This function resets the ant. It is used in every iteration to clean the Solution and other objects that the ant
     * might have modified during the solution build process.
     *
     * @param resetArgs VarArgs containing the additional information needed to perform the reset.
     */
    @Override
    public void resetAnt(Object... resetArgs) {
        this.solution = this.solution.createNewInstance();
        this.memory = new ArrayList<>();
    }

    /**
     * This function removes edges whose target node has already been visited. If no edge leading to an unvisited node is found,
     * edges leading to the starting node are chosen. If no suitable edge is found even then, all available edges are considered viable.
     *
     * @param world The world in which to search for Nodes/Edges.
     * @param edges The edges to examine for visited target nodes.
     */
    private void removeVisitedNodes(World world, HashSet<Object> edges){
        HashSet<Object> aux = new HashSet<>(edges);
        for(Object edge :aux){
            if(this.memory.contains(world.getGraph().getNextNode(edge))) edges.remove(edge);
        }
    }

    /**
     * Function to verbose the optimization process.
     *
     * @return a string containing the Ant string representation.
     */
    @Override
    public String toString(){
        return memory.toString();
    }
}
