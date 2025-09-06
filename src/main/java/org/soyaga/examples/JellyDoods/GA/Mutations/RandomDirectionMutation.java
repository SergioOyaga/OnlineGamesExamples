package org.soyaga.examples.JellyDoods.GA.Mutations;

import lombok.AllArgsConstructor;
import org.soyaga.examples.JellyDoods.Board.Board;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.MutationPolicy.Mutations.Mutation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mutation that perform a random genome change of direction.
 */
@AllArgsConstructor
public class RandomDirectionMutation implements Mutation {

    private final ArrayList<ArrayList<Integer>> level;
    private final Integer maxMoves;

    /**
     * This function apply the negation of a boolean Gen type.
     * @param gen Gen to mutate.
     */
    @Override
    public void apply(Object gen, Object... mutationArgs) {
        ArrayList<String[]> genomeInformation =((ArrayListGenome<String[]>)gen).getGeneticInformation();
        Random random = new Random();
        if(genomeInformation.isEmpty()) return;
        int chromosomeIndex = random.nextInt(genomeInformation.size());
        Board board = new Board(this.level);
        for(int i=0; i<chromosomeIndex; i++){
            String[] move = genomeInformation.get(i);
            String pieceId= move[0];
            String direction = move[1];
            board.movePiece(board.getJelliesById().get(pieceId),direction);
        }
        int size = genomeInformation.size()-chromosomeIndex;
        for(int i=0; i< size; i++){
            genomeInformation.remove(chromosomeIndex);
        }
        int realLoops=chromosomeIndex;
        for(int genIndex=chromosomeIndex; genIndex<this.maxMoves; genIndex++) {
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
            genomeInformation.add(move);
            board.movePiece(board.getJelliesById().get(pieceId), direction);
            if (board.solutionDistance()==0) break;
        }
    }
}
