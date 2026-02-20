package org.soyaga.examples.Tetris.GA;

import lombok.Getter;
import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.*;
import org.soyaga.examples.Tetris.Player.Player;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;
import org.soyaga.ga.GeneticInformationContainer.Genome.HashMapGenome;
import org.soyaga.ga.Individual;

import java.util.ArrayList;
import java.util.Random;

public class TetrisIndividual extends Individual {
    private final static ArrayList<Piece> pieces = new ArrayList<>(){{
        add(new IPiece());
        add(new JPiece());
        add(new LPiece());
        add(new OPiece());
        add(new TPiece());
        add(new SPiece());
        add(new ZPiece());
    }};
    @Getter
    private final Player player;
    @Getter
    private final int maxIterations;

    /**
     * Constructor.
     *
     * @param genome              Genome of the individual. Has to be a Genome instance
     * @param penalization        Double with the penalization for infeasibility.
     */
    public TetrisIndividual(Genome<?> genome, Double penalization, int maxIterations, Player player) {
        super(genome, null, null, penalization);
        this.maxIterations = maxIterations;
        this.player = player.newInstance();
    }
    /**
     * This function evaluates the feasibility, objective function and fitness of the
     * individual.
     *
     * @param statArgs VarArgs Object that allows to keep/retain information from the evaluation to be used in the
     *                 decision-making. Ej.:
     * <ul>
     *      <li> MutationPolicies</li>
     *      <li> CrossoverPolicies</li>
     *      <li> NewbornPolicies</li>
     *      <li> StoppingCriteriaPolicies</li>
     *      <li><b>***</b></li>
     * </ul>
     */
    @Override
    public void evaluate(Object ... statArgs){
        HashMapGenome<String,Double> genome = (HashMapGenome<String, Double>) this.getGenome();
        this.setWeights(genome);
        double objectiveValue = 0;
        double feasibilityValue = 0;
        int avgNumber = 100;
        for (int i =0;i<avgNumber; i++){
            Board board = this.simulateGame();
            objectiveValue+=board.getScore();
            feasibilityValue+=board.isActive()?0:1;
        }
        this.setObjectiveFunctionValue(-objectiveValue/avgNumber);
        this.setFeasibilityValue(feasibilityValue/avgNumber);
        this.setFitnessValue(this.getFeasibilityValue()*this.getPenalization() + this.getObjectiveFunctionValue());
    }

    private Board simulateGame() {
        Board board = new Board();
        Random random = new Random();
        for(int i=0;i<this.maxIterations; i++){
            if(!board.isActive()) break;
            Piece selectedPiece = pieces.get(random.nextInt(pieces.size()));
            this.player.movePiece(board,new ArrayList<>(){{add(selectedPiece);}});
        }
        return board;
    }

    public void setWeights(HashMapGenome<String,Double> genome){
        for (String key: this.player.getEvaluationFunction().getWeights().keySet()) {
            this.player.getEvaluationFunction().setWeight(key, genome.getValue(key));
        }
    }
}
