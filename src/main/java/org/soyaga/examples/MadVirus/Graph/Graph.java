package org.soyaga.examples.MadVirus.Graph;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.stream.Collectors;
@Getter
public class Graph {
    private final HashSet<Node> nodes;
    @Setter
    private Node startingNode;

    public Graph() {
        this.nodes = new HashSet<>();
    }

    public HashSet<Node> getColorNodes(Node node, Integer color) {
        HashSet<Node> connectedNodes = new HashSet<>();
        this.computeColorConnectedNodes(node, color,connectedNodes);
        return connectedNodes;
    }

    private void computeColorConnectedNodes(Node node, Integer color, HashSet<Node> currentNodes){
        HashSet<Node> connectedNodes = node.getConnectedNodes().stream().filter(n-> n.getColor()==color).collect(Collectors.toCollection(HashSet::new));
        connectedNodes.removeAll(currentNodes);
        currentNodes.addAll(connectedNodes);
        for(Node innerNode:connectedNodes){
            this.computeColorConnectedNodes(innerNode, color,currentNodes);
        }
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

}
