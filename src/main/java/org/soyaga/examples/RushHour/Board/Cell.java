package org.soyaga.examples.RushHour.Board;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {
    private final int row, col;
    @Setter
    private Vehicle vehicle;
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

}
