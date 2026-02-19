package org.soyaga.examples.Tetris.Board.Pieces;

import java.util.ArrayList;
import java.util.HashMap;

public interface Piece {
    String[] getOrientations();

    HashMap<String, ArrayList<int[]>>  getInitialPositionByOrientation();
    HashMap<String, int[]> getInitialGaps();

    default ArrayList<int[]> getNewPosition(String orientation,int gapMove){
        ArrayList<int[]> original = getInitialPositionByOrientation().get(orientation);
        ArrayList<int[]> copy = new ArrayList<>(4);
        for (int[] arr : original) {
            if (arr != null) {
                int[] arrClone = arr.clone();
                arrClone[1]+=gapMove;
                copy.add(arrClone);
            } else {
                copy.add(null);
            }
        }
        return copy;
    }

    default int[] getGaps(String orientation){
        return this.getInitialGaps().get(orientation);
    }
}
