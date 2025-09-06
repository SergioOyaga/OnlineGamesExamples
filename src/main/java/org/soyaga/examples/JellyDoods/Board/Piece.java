package org.soyaga.examples.JellyDoods.Board;

import lombok.Getter;

import java.util.HashSet;

@Getter
public class Piece {
    private final String id;
    private final HashSet<Cell> currentCells;
    private final int type;

    public Piece(String id, int type) {
        this.id = id;
        this.type = type;
        this.currentCells = new HashSet<>();
    }

    public HashSet<Piece> mergePieces(HashSet<Piece> mergingPieces){
        HashSet<Piece> mergedPieces = new HashSet<>();
        for(Piece piece:mergingPieces) {
            if (this.type == piece.type) {
                this.addCells(piece.currentCells);
                mergedPieces.add(piece);
            }
        }
        return mergedPieces;
    }

    public void addCells(HashSet<Cell> cells){
        for(Cell cell:cells){
            this.addCell(cell);
        }
    }

    public void clearCells(){
        for(Cell cell:new HashSet<>(this.currentCells)){
            this.removeCell(cell);
        }
    }

    public void addCell(Cell cell) {
        cell.setPiece(this);
        this.currentCells.add(cell);
    }
    public void removeCell(Cell cell) {
        cell.setPiece(null);
        this.currentCells.remove(cell);
    }
}
