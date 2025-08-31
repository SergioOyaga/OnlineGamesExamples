package org.soyaga.examples.LongCat.Graph;

import lombok.Getter;

import java.util.HashSet;

@Getter
public class Node {
    private final HashSet<Node> connectedNodes;
    private final int row,col;

    public Node( int row, int col) {
        this.connectedNodes = new HashSet<>();
        this.row = row;
        this.col = col;
    }

    public void addNode(Node node){
        this.connectedNodes.add(node);
    }
}
