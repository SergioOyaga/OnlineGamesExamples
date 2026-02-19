package org.soyaga.examples.Tetris.Player.BoardEvaluationFunction;

import org.soyaga.examples.Tetris.Board.Board;

import java.util.HashMap;

public interface BoardEvaluationFunction {
    HashMap<String, Double> getWeights();
    double evaluateBoard(Board board);
    void setWeight(String key, Double value);

    BoardEvaluationFunction newInstance();
}
