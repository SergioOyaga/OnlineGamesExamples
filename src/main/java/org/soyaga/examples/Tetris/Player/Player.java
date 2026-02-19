package org.soyaga.examples.Tetris.Player;

import lombok.Getter;
import lombok.Setter;
import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.Piece;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    @Getter @Setter
    private BoardEvaluationFunction evaluationFunction;

    private HashMap<Movement,Double> evaluateBoardOptions(Board board, Piece piece){
        HashMap<Movement,Double> movementOptions = new HashMap<>();
        for(String orientation:piece.getOrientations()){
            int[] gaps = piece.getGaps(orientation);
            for (int gap = gaps[0]; gap<=gaps[1]; gap++){
                Movement movement = new Movement(orientation,gap);
                Board copy = board.copyBoard();
                copy.moveDownPiece(piece.getNewPosition(orientation,gap));
                movementOptions.put(movement,this.evaluationFunction.evaluateBoard(copy));
            }
        }
        return  movementOptions;
    }

    public Movement movePiece(Board board, Piece piece){
        HashMap<Movement,Double> movementOptions = this.evaluateBoardOptions(board, piece);
        Map.Entry<Movement, Double> maxEntry = movementOptions.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        Movement movement = maxEntry.getKey();
        board.moveDownPiece(piece.getNewPosition(movement.orientation(),movement.gap()));
        return movement;
    }
}
