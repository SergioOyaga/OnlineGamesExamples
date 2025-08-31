package org.soyaga.examples.LongCat.GA.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.examples.LongCat.Graph.Graph;
import org.soyaga.examples.LongCat.Graph.Node;
import org.soyaga.ga.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;
import java.util.HashSet;

@AllArgsConstructor
public class LongCatFeasibilityFunction implements FeasibilityFunction {
    private final Graph graph;

    /**
     * This function computes the feasibility function of a Genome.
     *
     * @param genome          Genome Object to evaluate.
     * @param feasibilityArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the objective function.
     */
    @Override
    public Double evaluate(Genome<?> genome, Object... feasibilityArgs) {
        return evaluate(genome);
    }
    private Double evaluate(Genome<?> genome){
        ArrayList<String> genomeContainer = (ArrayList<String>) genome.getGeneticInformation();
        Node currentNode = this.graph.getStartingNode();
        HashSet<Node> reachedNodes = new HashSet<>();
        reachedNodes.add(currentNode);
        for(String chromosome:genomeContainer){
            switch (chromosome){
                case"N":{
                    if(currentNode.getRow()>0) {
                        Node nextNode = graph.getNodes()[currentNode.getRow() - 1][currentNode.getCol()];
                        if(currentNode.getConnectedNodes().contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                        }
                    }
                    break;
                }
                case"E":{
                    if(currentNode.getCol()<graph.getCols()-1) {
                        Node nextNode = graph.getNodes()[currentNode.getRow()][currentNode.getCol() + 1];
                        if(currentNode.getConnectedNodes().contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                        }
                    }
                    break;
                }
                case"S":{
                    if(currentNode.getRow()< graph.getRows()-1) {
                        Node nextNode = graph.getNodes()[currentNode.getRow() + 1][currentNode.getCol()];
                        if(currentNode.getConnectedNodes().contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                        }
                    }
                    break;
                }
                case"W":{
                    if(currentNode.getCol()>0) {
                        Node nextNode = graph.getNodes()[currentNode.getRow()][currentNode.getCol() - 1];
                        if(currentNode.getConnectedNodes().contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                        }
                    }
                    break;
                }
            }
            if(graph.getNumberOfNodes()== reachedNodes.size()) return 0.;
        }
        return (double) (graph.getNumberOfNodes()-reachedNodes.size());
    }
}
