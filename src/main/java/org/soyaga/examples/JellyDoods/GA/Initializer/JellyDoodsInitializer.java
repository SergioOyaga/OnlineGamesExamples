package org.soyaga.examples.JellyDoods.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.JellyDoods.Board.Board;
import org.soyaga.examples.JellyDoods.GA.Evaluable.JellyDoodsFeasibilityFunction;
import org.soyaga.examples.JellyDoods.GA.Evaluable.JellyDoodsObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.ArrayList;
import java.util.Random;

@AllArgsConstructor
public class JellyDoodsInitializer extends GAInitializer {
    private final JellyDoodsObjectiveFunction objectiveFunction;
    private final JellyDoodsFeasibilityFunction feasibilityFunction;
    private final ArrayList<ArrayList<Integer>> level;
    private final Integer maxMoves;

    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<String[]> genome = new ArrayListGenome<>();
        Board board = new Board(this.level);
        int realLoops=0;
        for(int genIndex=0; genIndex<this.maxMoves; genIndex++) {
            if(realLoops++>100) break;
            ArrayList<String> piecesIds = new ArrayList<>(board.getJelliesById().keySet());
            String pieceId = piecesIds.get(random.nextInt(piecesIds.size()));
            int nDistance = board.computeMovementDistance(board.getJelliesById().get(pieceId), "N");
            int eDistance = board.computeMovementDistance(board.getJelliesById().get(pieceId), "E");
            int sDistance = board.computeMovementDistance(board.getJelliesById().get(pieceId), "S");
            int wDistance = board.computeMovementDistance(board.getJelliesById().get(pieceId), "W");
            ArrayList<String> directions = new ArrayList<>();
            if (nDistance > 0) directions.add("N");
            if (eDistance > 0) directions.add("E");
            if (sDistance > 0) directions.add("S");
            if (wDistance > 0) directions.add("W");
            if(directions.isEmpty()) {
                genIndex--;
                continue;
            }
            String direction = directions.get(random.nextInt(directions.size()));
            String[] move = {pieceId, direction};
            genome.add(move);
            board.movePiece(board.getJelliesById().get(pieceId), direction);
            if (board.solutionDistance()==0) break;
        }
        return  new Individual(genome,this.feasibilityFunction,this.objectiveFunction,10.);
    }
}
