package org.soyaga.examples.Tetris.Board.Pieces;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class OPiece implements Piece{
    @Getter
    private final String[] orientations ={"N"};
    @Getter
    private final HashMap<String, ArrayList<int[]>> initialPositionByOrientation = new HashMap<>(){{
        put("N", new ArrayList<>(){{
            add(new int[]{0, 4});
            add(new int[]{0, 5});
            add(new int[]{1, 4});
            add(new int[]{1, 5});
        }});
    }};
    @Getter
    private final HashMap<String, int[]> initialGaps = new HashMap<>(){{
        put("N", new int[]{-4,4});
    }};

}
