package org.soyaga.examples.Tetris.Player;

import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.Piece;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public interface Player {
    BoardEvaluationFunction getEvaluationFunction();
    Movement movePiece(Board board, ArrayList<Piece> piece);
    Player newInstance();
    default HashMap<Movement,Double> evaluateBoardOptions(Board board, Piece piece){
        HashMap<Movement,Double> movementOptions = new HashMap<>();
        for(String orientation:piece.getOrientations()){
            int[] gaps = piece.getGaps(orientation);
            for (int gap = gaps[0]; gap<=gaps[1]; gap++){
                Movement movement = new Movement(orientation,gap);
                Board copy = board.copyBoard();
                copy.moveDownPiece(piece.getNewPosition(orientation,gap));
                movementOptions.put(movement,this.getEvaluationFunction().evaluateBoard(copy));
            }
        }
        return  movementOptions;
    }

}
