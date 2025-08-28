package org.soyaga.examples.MadVirus.GA.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.examples.MadVirus.Graph.Graph;
import org.soyaga.examples.MadVirus.Graph.Node;
import org.soyaga.ga.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;
import java.util.HashSet;

@AllArgsConstructor
public class MadVirusFeasibilityFunction implements FeasibilityFunction {
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
        ArrayList<Integer> genomeContainer = (ArrayList<Integer>) genome.getGeneticInformation();
        HashSet<Node> reachedNodes = new HashSet<>();
        reachedNodes.add(this.graph.getStartingNode());
        for(Integer chromosome:genomeContainer){
            HashSet<Node> toAddNodes = new HashSet<>();
            for(Node node:reachedNodes){
                toAddNodes.addAll(graph.getColorNodes(node,chromosome));
            }
            reachedNodes.addAll(toAddNodes);
            if(graph.getNodes().size()== reachedNodes.size()) return 0.;
        }
        return (double) (graph.getNodes().size()- reachedNodes.size());
    }
}
