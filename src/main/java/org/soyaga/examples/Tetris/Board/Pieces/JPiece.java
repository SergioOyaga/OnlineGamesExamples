package org.soyaga.examples.Tetris.Board.Pieces;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class JPiece implements Piece{
    @Getter
    private final String[] orientations ={"N","E", "S", "W"};
    @Getter
    private final HashMap<String, ArrayList<int[]>> initialPositionByOrientation = new HashMap<>(){{
        put("N", new ArrayList<>(){{
            add(new int[]{0, 3});
            add(new int[]{1, 3});
            add(new int[]{1, 4});
            add(new int[]{1, 5});
        }});
        put("E", new ArrayList<>(){{
            add(new int[]{0, 4});
            add(new int[]{0, 5});
            add(new int[]{1, 4});
            add(new int[]{2, 4});
        }});
        put("S", new ArrayList<>(){{
            add(new int[]{1, 3});
            add(new int[]{1, 4});
            add(new int[]{1, 5});
            add(new int[]{2, 5});
        }});
        put("W", new ArrayList<>(){{
            add(new int[]{0, 4});
            add(new int[]{1, 4});
            add(new int[]{2, 3});
            add(new int[]{2, 4});
        }});
    }};
    @Getter
    private final HashMap<String, int[]> initialGaps = new HashMap<>(){{
        put("N", new int[]{-3,4});
        put("E", new int[]{-4,4});
        put("S", new int[]{-3,4});
        put("W", new int[]{-3,5});
    }};
}
