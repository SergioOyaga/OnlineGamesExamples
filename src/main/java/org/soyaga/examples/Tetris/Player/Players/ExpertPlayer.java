package org.soyaga.examples.Tetris.Player.Players;

import lombok.Getter;
import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.Piece;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.BoardEvaluationFunction;
import org.soyaga.examples.Tetris.Player.Movement;
import org.soyaga.examples.Tetris.Player.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpertPlayer implements Player {
    @Getter
    private BoardEvaluationFunction evaluationFunction;

    public ExpertPlayer(BoardEvaluationFunction boardEvaluationFunction) {
        this.evaluationFunction = boardEvaluationFunction;
    }

    public Movement movePiece(Board board, ArrayList<Piece> pieces){
        Piece firstPiece = pieces.get(0);
        Piece secondPiece = pieces.get(1);
        Piece thirdPiece = pieces.get(2);
        HashMap<Movement,Double> firstMovementOptions = this.evaluateBoardOptions(board, firstPiece);
        Map.Entry<Movement, Double> maxEntry= null;
        for(Map.Entry<Movement, Double> firstMovementEntry:firstMovementOptions.entrySet()){
            Movement firstMovement = firstMovementEntry.getKey();
            Board copy = board.copyBoard();
            copy.moveDownPiece(firstPiece.getNewPosition(firstMovement.orientation(), firstMovement.gap()));
            HashMap<Movement,Double> secondMovementOptions = this.evaluateBoardOptions(copy, secondPiece);
            for(Map.Entry<Movement, Double> secondMovementEntry:secondMovementOptions.entrySet()){
                Movement secondMovement = secondMovementEntry.getKey();
                Board copy2 = copy.copyBoard();
                copy2.moveDownPiece(secondPiece.getNewPosition(secondMovement.orientation(), secondMovement.gap()));
                HashMap<Movement,Double> thirdMovementOptions = this.evaluateBoardOptions(copy2, thirdPiece);
                Map.Entry<Movement, Double> thirdMoveMaxEntry = thirdMovementOptions.entrySet().stream().max(Map.Entry.comparingByValue()).get();
                if((maxEntry==null) || (maxEntry.getValue()<thirdMoveMaxEntry.getValue())){
                    maxEntry=new AbstractMap.SimpleEntry<>(firstMovement,thirdMoveMaxEntry.getValue());
                }
            }
        }
        Movement movement= maxEntry.getKey();
        board.moveDownPiece(firstPiece.getNewPosition(movement.orientation(),movement.gap()));
        return movement;
    }

    @Override
    public Player newInstance() {
        return new NoobPlayer(this.evaluationFunction.newInstance());
    }
}
