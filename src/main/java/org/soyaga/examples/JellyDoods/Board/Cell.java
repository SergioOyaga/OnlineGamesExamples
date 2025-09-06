package org.soyaga.examples.JellyDoods.Board;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Cell {
    private final int row, col;
    @Setter
    private Piece piece;
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

}
