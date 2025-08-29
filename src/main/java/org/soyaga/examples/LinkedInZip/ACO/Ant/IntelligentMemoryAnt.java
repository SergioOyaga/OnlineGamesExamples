package org.soyaga.examples.LinkedInZip.ACO.Ant;

import lombok.Getter;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Ant.EdgeSelector.EdgeSelector;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * An intelligent ant with memory. This implies that the ant considers its past movements to construct the solution.
 * In each building step, the ant examines the available edges to determine if any of them lead to an unvisited
 * destination node.
 */
public class IntelligentMemoryAnt implements Ant {
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
     * HashMap with the priority by node.
     */
    private final HashMap<Node, Integer> priorityByNode;

    /**
     * Constructor.
     *
     * @param solution Solution Type this ant has to build incrementally.
     * @param edgeSelector EdgeSelector Type that this ant uses to chose its next edge
     * @param pheromoneQuantity Double with the quantity of pheromone that an ant can distribute across the entire path.
     */
    public IntelligentMemoryAnt(Solution solution, EdgeSelector edgeSelector, Double pheromoneQuantity, HashMap<Node, Integer> priorityByNode) {
        this.solution = solution;
        this.edgeSelector = edgeSelector;
        this.pheromoneQuantity = pheromoneQuantity;
        this.memory=new ArrayList<>();
        this.priorityByNode = priorityByNode;
    }

    /**
     * Constructor that creates a new empty instance of an ant.
     *
     * @return Ant an ant with an empty memory, new Solution, the same EdgeSelector and the same pheromone quantity.
     */
    @Override
    public IntelligentMemoryAnt createNewInstance() {
        return new IntelligentMemoryAnt(this.solution.createNewInstance(),this.edgeSelector,this.pheromoneQuantity, this.priorityByNode);
    }

    /**
     * Function that builds a solution using the ant information, the world information (Graph, pheromone, ...) and any
     * other argument.
     *
     * @param world     world Object containing the "Graph" and "Pheromone" information.
     * @param buildArgs VarArgs containing the additional information needed to build a solution.
     */
    @Override
    public void buildSolution(World world, Object... buildArgs) {
        this.solution = this.solution.createNewInstance();
        this.solution.selectInitialNode(world,buildArgs);
        this.memory.add(this.solution.getCurrentNode());
        do{
            Object currentNode=this.solution.getCurrentNode();
            HashSet<Object> edges = world.getGraph().getOutputEdges(currentNode);
            this.removeVisitedNodes(world,edges);
            this.selectGoodEdges(world, edges);
            Object nextEdge = this.edgeSelector.apply(world, currentNode, edges);
            this.solution.buildSolution(nextEdge,world.getGraph().getNextNode(nextEdge));
            this.memory.add(this.solution.getCurrentNode());
        }
        while (!this.solution.stopBuild());
    }

    /**
     * Function that selects the available edges in an intelligent way
     * @param world World with the nodes and edges
     * @param edges current selection of edges.
     */
    private void selectGoodEdges(World world, HashSet<Object> edges) {
        if (edges.size()>1){
            HashSet<Object> aux = new HashSet<>(edges);
            HashSet<Integer> prios = new HashSet<>();
            for(Object node:this.memory){
                prios.add(this.priorityByNode.getOrDefault(node, 0));
            }
            ArrayList<Integer> sortedprios = new ArrayList<>(prios);
            sortedprios.sort(Integer::compareTo);
            Integer latestGoodPrio = extract(sortedprios);
            ArrayList<Object> mustBeEdges = new ArrayList<>();
            for(Object edge :aux){
                Object nextNode = world.getGraph().getNextNode(edge);
                Integer nextPrio = this.priorityByNode.get(nextNode);
                //Remove if next node is not following the priority
                if((nextPrio!=null) && (latestGoodPrio+1 != nextPrio)) {
                    edges.remove(edge);
                    continue;
                }
                HashSet nextOutputEdges = new HashSet(world.getGraph().getOutputEdges(nextNode));
                this.removeVisitedNodes(world,nextOutputEdges);
                //Check if next node only have 1 exit (must be selected)
                if(nextOutputEdges.size()<=1) {
                    mustBeEdges.add(edge);
                    continue;
                }
                HashSet nextOutputEdgesCopy = new HashSet<>(nextOutputEdges);
                for(Object innerEdge:nextOutputEdgesCopy){
                    Object nextInnerNode = world.getGraph().getNextNode(innerEdge);
                    Integer nextInnerPrio = this.priorityByNode.get(nextInnerNode);
                    //Remove if next node is not following the priority
                    if((nextInnerPrio!=null) && (latestGoodPrio+1 != nextInnerPrio)) {
                        nextOutputEdges.remove(innerEdge);
                    }
                }
                if (nextOutputEdges.size()==0){
                    edges.remove(edge);
                }
            }
            if(mustBeEdges.size()>0) {
                edges.clear();
                edges.addAll(mustBeEdges);
            }
            if(edges.size()==0) edges.addAll(aux);
        }

    }

    public static Integer extract(ArrayList<Integer> sortedArr) {
        if (sortedArr == null || sortedArr.isEmpty()) {
            return null; // or throw exception
        }

        for (int i = 1; i < sortedArr.size(); i++) {
            // check if sequence breaks
            if (sortedArr.get(i) != sortedArr.get(i - 1) + 1) {
                return sortedArr.get(i - 1);
            }
        }

        // if no break found, return last element
        return sortedArr.get(sortedArr.size() - 1);
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
     * This function eliminates edges whose target node has already been visited. If no edge leading to an unvisited node is found,
     * all available edges are regarded as viable options.
     *
     * @param world The world in which to search for Nodes/Edges.
     * @param edges The edges to examine for visited target nodes.
     */
    private void removeVisitedNodes(World world, HashSet<Object> edges){
        HashSet<Object> aux = new HashSet<>(edges);
        for(Object edge :aux){
            if(this.memory.contains(world.getGraph().getNextNode(edge))) edges.remove(edge);
        }
        if(edges.size()==0) edges.addAll(aux);
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
