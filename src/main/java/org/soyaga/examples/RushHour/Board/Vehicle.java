package org.soyaga.examples.RushHour.Board;

import lombok.Getter;

import java.util.HashSet;


public class Vehicle {
    @Getter
    private final String ID;
    @Getter
    private final HashSet<Cell> currentCells;
    @Getter
    private final boolean horizontal;

    public Vehicle(String ID, boolean isHorizontal) {
        this.ID = ID;
        this.horizontal = isHorizontal;
        this.currentCells = new HashSet<>();
    }


    public void clearCells(){
        for(Cell cell:new HashSet<>(this.currentCells)){
            this.removeCell(cell);
        }
    }

    public void addCells(HashSet<Cell> cells) {
        for(Cell cell:cells){
            this.addCell(cell);
        }
    }

    public void addCell(Cell cell) {
        cell.setVehicle(this);
        this.currentCells.add(cell);
    }
    public void removeCell(Cell cell) {
        cell.setVehicle(null);
        this.currentCells.remove(cell);
    }
}
