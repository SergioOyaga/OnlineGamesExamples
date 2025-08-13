package org.soyaga.examples.Maze.ACO.Elements;

import org.soyaga.aco.world.Graph.Elements.Node;

public class MazeNode extends Node {
    private final int x,y;

    /**
     * Constructor.
     *
     * @param ID Object with the node ID.
     */
    public MazeNode(Object ID, int x, int y) {
        super(ID);
        this.x = x;
        this.y = y;
    }

    /**
     * Fucntion that computes the Euclidean distance between two MAzeNodes
     * @param other MazeNode to compare
     * @return double with the cistance
     */
    public double computeDistance(MazeNode other){
        return Math.sqrt(Math.pow(this.x-other.x,2)+ Math.pow(this.y-other.y,2));
    }

    /**
     * Function that defines the move to perform
     * @param nextNode next node to move
     * @return String with the move
     */
    public String getMove(MazeNode nextNode) {
        if (this.x > nextNode.x) return "L";
        if (this.x < nextNode.x) return "R";
        if (this.y > nextNode.y) return "U";
        return "D";
    }
}
