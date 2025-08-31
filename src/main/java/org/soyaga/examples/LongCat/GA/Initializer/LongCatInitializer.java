package org.soyaga.examples.LongCat.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.LongCat.GA.Evaluable.LongCatFeasibilityFunction;
import org.soyaga.examples.LongCat.GA.Evaluable.LongCatObjectiveFunction;
import org.soyaga.examples.LongCat.Graph.Graph;
import org.soyaga.examples.LongCat.Graph.Node;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

@AllArgsConstructor
public class LongCatInitializer extends GAInitializer {
    private final LongCatObjectiveFunction objectiveFunction;
    private final LongCatFeasibilityFunction feasibilityFunction;
    private final Graph graph;

    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<String> genome = new ArrayListGenome<>();
        Node currentNode = this.graph.getStartingNode();
        HashSet<Node> visitedNodes = new HashSet<>();
        visitedNodes.add(currentNode);
        ArrayList<Node> availableNodes = new ArrayList<>(graph.getAvailableNodes(currentNode,visitedNodes));
        while(!availableNodes.isEmpty()){
            Node selectedNode = availableNodes.get(random.nextInt(availableNodes.size()));
            if(currentNode.getRow() > selectedNode.getRow()) genome.add("N");
            else if(currentNode.getCol() < selectedNode.getCol()) genome.add("E");
            else if(currentNode.getRow() < selectedNode.getRow()) genome.add("S");
            else if(currentNode.getCol() > selectedNode.getCol()) genome.add("W");
            currentNode = graph.makeStraightMovement(currentNode, selectedNode, visitedNodes);
            availableNodes = new ArrayList<>(graph.getAvailableNodes(currentNode,visitedNodes));
        }
        return  new Individual(genome,this.feasibilityFunction,this.objectiveFunction,10.);
    }
}
