package org.soyaga.examples.Tetris.Player.BoardEvaluationFunction;

import lombok.Getter;
import org.soyaga.examples.Tetris.Board.Board;

import java.io.Serializable;
import java.util.HashMap;

public class LinearBoardEvaluationFunction implements BoardEvaluationFunction, Serializable {
    @Getter
    private final HashMap<String, Double> weights;

    public LinearBoardEvaluationFunction() {
        super();
        this.weights = new HashMap<>();
        this.setWeights(0.,0.,0.,0.,0.,0.,
                0.,0.);
    }

    public LinearBoardEvaluationFunction newInstance(){
        return new LinearBoardEvaluationFunction();
    }

    public void setWeight(String key, Double value){
        this.weights.put(key, value);
    }

    private void setWeights(Double meanHeightW1, Double stdHeightW1, Double maxHeightW1, Double smoothnessHeightW1,
                            Double holeNumberW1, Double meanHoleDepthW1, Double stdHoleDepthW1, Double clearedLinesW1){

        this.weights.put("meanHeightW1", meanHeightW1);
        this.weights.put("stdHeightW1", stdHeightW1);
        this.weights.put("maxHeightW1", maxHeightW1);
        this.weights.put("smoothnessHeightW1", smoothnessHeightW1);
        this.weights.put("holeNumberW1", holeNumberW1);
        this.weights.put("meanHoleDepthW1", meanHoleDepthW1);
        this.weights.put("stdHoleDepthW1", stdHoleDepthW1);
        this.weights.put("clearedLinesW1", clearedLinesW1);
    }
    public double evaluateBoard(Board board){
        return this.weights.get("meanHeightW1") * board.getMeanHeight()
                + this.weights.get("stdHeightW1") * board.getStdHeight()
                + this.weights.get("maxHeightW1") * board.getMaxHeight()
                + this.weights.get("smoothnessHeightW1") * board.getSmoothnessHeight()
                + this.weights.get("holeNumberW1") * board.getHoleNumber()
                + this.weights.get("meanHoleDepthW1") * board.getMeanHoleDepth()
                + this.weights.get("stdHoleDepthW1") * board.getStdHoleDepth()
                + this.weights.get("clearedLinesW1") * board.getLastClearedLinesNumber();
    }
}
