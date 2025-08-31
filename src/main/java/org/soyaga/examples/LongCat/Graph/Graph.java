package org.soyaga.examples.LongCat.Graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.HashSet;

@Getter @AllArgsConstructor
public class Graph {
    private final Node[][] nodes;
    private final int numberOfNodes;
    private final int rows;
    private final int cols;
    private final Node startingNode;

    public HashSet<Node> getAvailableNodes(Node node, HashSet<Node> visitedNodes) {
        HashSet<Node> connectedNodes = new HashSet<>(node.getConnectedNodes());
        connectedNodes.removeAll(visitedNodes);
        return connectedNodes;
    }

    public Node makeStraightMovement(Node orig, Node dest, HashSet<Node> visitedNodes){
        int origRow = orig.getRow();
        int origCol = orig.getCol();
        int destRow = dest.getRow();
        int destCol = dest.getCol();
        //North
        if(destRow < origRow){
            for(int row=origRow-1; row>=0; row--){
                Node nextNode = nodes[row][origCol];
                if(nextNode==null || visitedNodes.contains(nextNode)) return nodes[row+1][origCol];
                visitedNodes.add(nextNode);
            }
            return nodes[0][origCol];
        }
        //East
        else if(destCol > origCol){
            for(int col=origCol+1; col<cols; col++){
                Node nextNode = nodes[origRow][col];
                if(nextNode==null || visitedNodes.contains(nextNode)) return nodes[origRow][col-1];
                visitedNodes.add(nextNode);
            }
            return nodes[origRow][cols-1];
        }
        //South
        else if (destRow > origRow){
            for(int row=origRow+1; row<rows; row++){
                Node nextNode = nodes[row][origCol];
                if(nextNode==null || visitedNodes.contains(nextNode)) return nodes[row-1][origCol];
                visitedNodes.add(nextNode);
            }
            return nodes[rows-1][origCol];
        }
        //West
        else if (destCol < origCol){
            for(int col=origCol-1; col>=0; col--){
                Node nextNode = nodes[origRow][col];
                if(nextNode==null || visitedNodes.contains(nextNode)) return nodes[origRow][col+1];
                visitedNodes.add(nextNode);
            }
            return nodes[origRow][0];
        }
        return nodes[origRow][origCol];
    }

}
