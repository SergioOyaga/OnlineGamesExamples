package org.soyaga.examples.RushHour.Board;


import lombok.Getter;
import org.soyaga.examples.JellyDoods.Board.Piece;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {
    private final int rows;
    private final int cols;
    @Getter
    private final Cell[][] boardGrid;
    @Getter
    private final LinkedHashMap<String, Vehicle> vehiclesById;
    private final Vehicle redVehicle;


    public Board(LinkedHashMap<String, Object[]> vehiclePropertiesById) {
        this.rows = 6;
        this.cols = 6;
        this.boardGrid = new Cell[this.rows][this.cols];
        this.vehiclesById = new LinkedHashMap<>();
        this.computeBoard();
        computeVehicles(vehiclePropertiesById);
        this.redVehicle = this.vehiclesById.get("CAR-RED");
    }


    private void computeBoard(){
        for(int row=0;row<this.rows;row++){
            for(int col=0;col<this.cols;col++){
                this.boardGrid[row][col] = new Cell(row, col);
            }
        }
    }

    private void computeVehicles(LinkedHashMap<String, Object[]> vehiclePropertiesById) {
        for(Map.Entry<String, Object[]> vehicleEntry:vehiclePropertiesById.entrySet()){
            String vehicleId =vehicleEntry.getKey();
            int rowStart = (int) vehicleEntry.getValue()[0];
            int colStart = (int) vehicleEntry.getValue()[1];
            int length = (int) vehicleEntry.getValue()[2];
            boolean isHorizontal = (boolean) vehicleEntry.getValue()[3];
            Vehicle vehicle = new Vehicle(vehicleId, isHorizontal);
            if(isHorizontal){
                for(int col=colStart; col<colStart+length;col++){
                    vehicle.addCell(this.boardGrid[rowStart][col]);
                }
            }
            else {
                for(int row=rowStart; row<rowStart+length;row++){
                    vehicle.addCell(this.boardGrid[row][colStart]);
                }
            }
            this.vehiclesById.put(vehicleId,vehicle);
        }
    }

    public void moveVehicle(Movement move){
        if (move==null) return;
        Vehicle vehicle = this.vehiclesById.get(move.vehicleID);
        //Compute to be cells
        HashSet<Cell> tobeCells = computeToBeCells(vehicle, move.direction, move.distance);
        //Clear and set new piece cells
        vehicle.clearCells();
        vehicle.addCells(tobeCells);
    }

    public int computeMovementDistance(String vehicleID, String direction){
        Vehicle vehicle = this.vehiclesById.get(vehicleID);
        int movementDistance=this.rows;
        if(vehicle.isHorizontal()){
            switch (direction){
                case "N", "S":{
                    movementDistance = 0;
                    break;
                }
                case "E":{
                    for(Cell pieceCell: vehicle.getCurrentCells()){
                        for(int col= pieceCell.getCol()+1; col<this.cols;col++){
                            if (this.boardGrid[pieceCell.getRow()][col].getVehicle()!=null) {
                                if (!vehicle.equals(this.boardGrid[pieceCell.getRow()][col].getVehicle())) {
                                    movementDistance = Math.min(movementDistance, col - pieceCell.getCol() - 1);
                                }
                                break;
                            }
                        }
                        movementDistance = Math.min(movementDistance,this.cols-pieceCell.getCol()-1);
                    }
                    break;
                }
                case "W":{
                    for(Cell pieceCell: vehicle.getCurrentCells()){
                        for(int col= pieceCell.getCol()-1; col>=0;col--){
                            if (this.boardGrid[pieceCell.getRow()][col].getVehicle()!=null) {
                                if (!vehicle.equals(this.boardGrid[pieceCell.getRow()][col].getVehicle())) {
                                    movementDistance = Math.min(movementDistance, pieceCell.getCol() - col - 1);
                                }
                                break;
                            }
                        }
                        movementDistance = Math.min(movementDistance,pieceCell.getCol());
                    }
                    break;
                }
                default:{
                    System.out.println("Not recognized movement");
                }
            }
        }
        else{
            switch (direction){
                case "N":{
                    for(Cell pieceCell: vehicle.getCurrentCells()){
                        for(int row= pieceCell.getRow()-1;row>=0;row--){
                            if (this.boardGrid[row][pieceCell.getCol()].getVehicle()!=null) {
                                if (!vehicle.equals(this.boardGrid[row][pieceCell.getCol()].getVehicle())) {
                                    movementDistance = Math.min(movementDistance, pieceCell.getRow() - row - 1);
                                }
                                break;
                            }
                        }
                        movementDistance = Math.min(movementDistance,pieceCell.getRow());
                    }
                    break;
                }
                case "E", "W":{
                    movementDistance = 0;
                    break;
                }
                case "S":{
                    for(Cell pieceCell: vehicle.getCurrentCells()){
                        for(int row= pieceCell.getRow()+1;row<this.rows;row++){
                            if (this.boardGrid[row][pieceCell.getCol()].getVehicle()!=null) {
                                if (!vehicle.equals(this.boardGrid[row][pieceCell.getCol()].getVehicle())) {
                                    movementDistance = Math.min(movementDistance, row - pieceCell.getRow() - 1);
                                }
                                break;
                            }
                        }
                        movementDistance = Math.min(movementDistance,this.rows-pieceCell.getRow()-1);
                    }
                    break;
                }
                default:{
                    System.out.println("Not recognized movement");
                }
            }
        }
        return movementDistance;
    }

    private HashSet<Cell> computeToBeCells(Vehicle vehicle, String direction, int movementDistance){
        HashSet<Cell> tobeCells = new HashSet<>();
        for(Cell currentCell: vehicle.getCurrentCells()){
            int nextCellRow = currentCell.getRow();
            int nextCellCol = currentCell.getCol();
            switch (direction){
                case "N":{
                    nextCellRow-=movementDistance;
                    break;
                }
                case "E":{
                    nextCellCol+=movementDistance;
                    break;
                }
                case "S":{
                    nextCellRow+=movementDistance;
                    break;
                }
                case "W":{
                    nextCellCol-=movementDistance;
                    break;
                }
                default:{
                    System.out.println("Not recognized movement");
                }
            }
            tobeCells.add(this.boardGrid[nextCellRow][nextCellCol]);
        }
        return tobeCells;
    }

    public int solutionDistance() {
        return this.cols-1-this.redVehicle.getCurrentCells().stream().map(Cell::getCol).max(Integer::compareTo).get();
    }
}