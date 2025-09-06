package org.soyaga.examples.JellyDoods.GA.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.examples.JellyDoods.Board.Board;
import org.soyaga.ga.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;

@AllArgsConstructor
public class JellyDoodsFeasibilityFunction implements FeasibilityFunction {
    private final ArrayList<ArrayList<Integer>> level;
    private final Integer maxMoves;

    /**
     * This function computes the feasibility function of a Genome.
     *
     * @param genome          Genome Object to evaluate.
     * @param feasibilityArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the objective function.
     */
    @Override
    public Double evaluate(Genome<?> genome, Object... feasibilityArgs) {
        return evaluate(genome);
    }
    private Double evaluate(Genome<?> genome){
        ArrayList<String[]> genomeContainer = (ArrayList<String[]>) genome.getGeneticInformation();
        Board board = new Board(this.level);

        for(int i = 0;i<this.maxMoves; i++){
            if(genomeContainer.size()==i) break;
            String[] chromosome = genomeContainer.get(i);
            String pieceId = chromosome[0];
            String direction = chromosome[1];
            board.movePiece(board.getJelliesById().get(pieceId), direction);
            if(board.solutionDistance()==0) return 0.;
        }
        return (double) (board.solutionDistance());
    }
}
