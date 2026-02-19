package org.soyaga.examples.Tetris.Board.Pieces;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class IPiece implements Piece{
    @Getter
    private final String[] orientations ={"N","E"};
    @Getter
    private final HashMap<String, ArrayList<int[]>> initialPositionByOrientation = new HashMap<>(){{
        put("N", new ArrayList<>(){{
            add(new int[]{0, 3});
            add(new int[]{0, 4});
            add(new int[]{0, 5});
            add(new int[]{0, 6});
        }});
        put("E", new ArrayList<>(){{
            add(new int[]{0, 5});
            add(new int[]{1, 5});
            add(new int[]{2, 5});
            add(new int[]{3, 5});
        }});
    }};
    @Getter
    private final HashMap<String, int[]> initialGaps = new HashMap<>(){{
        put("N", new int[]{-3,3});
        put("E", new int[]{-5,4});
    }};

}
