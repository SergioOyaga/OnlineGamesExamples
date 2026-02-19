package org.soyaga.examples.Tetris.Player.BoardEvaluationFunction;

import lombok.Getter;
import org.soyaga.examples.Tetris.Board.Board;

import java.io.Serializable;
import java.util.HashMap;

public class TanhBoardEvaluationFunction implements BoardEvaluationFunction, Serializable {
    @Getter
    private final HashMap<String, Double> weights;

    public TanhBoardEvaluationFunction() {
        super();
        this.weights = new HashMap<>();
        this.setWeights(0.,0.,0.,0.,0.,0.,
                0.,0.,0.,0.,0.,
                0.,0.,0.,0.,0.);
    }

    public TanhBoardEvaluationFunction newInstance(){
        return new TanhBoardEvaluationFunction();
    }

    public void setWeight(String key, Double value){
        this.weights.put(key, value);
    }

    private void setWeights(Double meanHeightW1, Double meanHeightW2,
                           Double stdHeightW1, Double stdHeightW2,
                           Double maxHeightW1, Double maxHeightW2,
                           Double smoothnessHeightW1, Double smoothnessHeightW2,
                           Double holeNumberW1, Double holeNumberW2,
                           Double meanHoleDepthW1, Double meanHoleDepthW2,
                           Double stdHoleDepthW1, Double stdHoleDepthW2,
                           Double clearedLinesW1, Double clearedLinesW2){

        this.weights.put("meanHeightW1", meanHeightW1); this.weights.put("meanHeightW2", meanHeightW2);
        this.weights.put("stdHeightW1", stdHeightW1); this.weights.put("stdHeightW2", stdHeightW2);
        this.weights.put("maxHeightW1", maxHeightW1); this.weights.put("maxHeightW2", maxHeightW2);
        this.weights.put("smoothnessHeightW1", smoothnessHeightW1); this.weights.put("smoothnessHeightW2", smoothnessHeightW2);
        this.weights.put("holeNumberW1", holeNumberW1); this.weights.put("holeNumberW2", holeNumberW2);
        this.weights.put("meanHoleDepthW1", meanHoleDepthW1); this.weights.put("meanHoleDepthW2", meanHoleDepthW2);
        this.weights.put("stdHoleDepthW1", stdHoleDepthW1); this.weights.put("stdHoleDepthW2", stdHoleDepthW2);
        this.weights.put("clearedLinesW1", clearedLinesW1); this.weights.put("clearedLinesW2", clearedLinesW2);
    }
    public double evaluateBoard(Board board){
        return this.weights.get("meanHeightW1")*Math.tanh(this.weights.get("meanHeightW2") * board.getMeanHeight())
                + this.weights.get("stdHeightW1")*Math.tanh(this.weights.get("stdHeightW2") * board.getStdHeight())
                + this.weights.get("maxHeightW1")*Math.tanh(this.weights.get("maxHeightW2") * board.getMaxHeight())
                + this.weights.get("smoothnessHeightW1")*Math.tanh(this.weights.get("smoothnessHeightW2") * board.getSmoothnessHeight())
                + this.weights.get("holeNumberW1")*Math.tanh(this.weights.get("holeNumberW2") * board.getHoleNumber())
                + this.weights.get("meanHoleDepthW1")*Math.tanh(this.weights.get("meanHoleDepthW2") * board.getMeanHoleDepth())
                + this.weights.get("stdHoleDepthW1")*Math.tanh(this.weights.get("stdHoleDepthW2") * board.getStdHoleDepth())
                + this.weights.get("clearedLinesW1")*Math.tanh(this.weights.get("clearedLinesW2") * board.getLastClearedLinesNumber());
    }
}
