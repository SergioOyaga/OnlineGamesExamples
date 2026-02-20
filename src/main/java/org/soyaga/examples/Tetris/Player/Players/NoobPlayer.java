package org.soyaga.examples.Tetris.Player.Players;

import lombok.Getter;
import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.Piece;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;
import org.soyaga.examples.Tetris.Player.Movement;
import org.soyaga.examples.Tetris.Player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoobPlayer implements Player {
    @Getter
    private BoardEvaluationFunction evaluationFunction;

    public NoobPlayer(BoardEvaluationFunction boardEvaluationFunction) {
        this.evaluationFunction = boardEvaluationFunction;
    }

    public Movement movePiece(Board board, ArrayList<Piece> pieces){
        Piece piece = pieces.get(0);
        HashMap<Movement,Double> movementOptions = this.evaluateBoardOptions(board, piece);
        Map.Entry<Movement, Double> maxEntry = movementOptions.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        Movement movement = maxEntry.getKey();
        board.moveDownPiece(piece.getNewPosition(movement.orientation(),movement.gap()));
        return movement;
    }

    @Override
    public Player newInstance() {
        return new NoobPlayer(this.evaluationFunction.newInstance());
    }
}
