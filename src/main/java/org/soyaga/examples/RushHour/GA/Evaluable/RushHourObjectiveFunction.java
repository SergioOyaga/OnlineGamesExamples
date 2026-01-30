package org.soyaga.examples.RushHour.GA.Evaluable;

import lombok.AllArgsConstructor;
import org.soyaga.examples.RushHour.Board.Board;
import org.soyaga.examples.RushHour.Board.Movement;
import org.soyaga.ga.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.Genome;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@AllArgsConstructor
public class RushHourObjectiveFunction implements ObjectiveFunction {
    private final LinkedHashMap<String, Object[]> vehiclesInfo;

    /**
     * This function computes the objective function of a Genome.
     *
     * @param genome        Genome Object to evaluate.
     * @param objectiveArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the objective function.
     */
    @Override
    public Double evaluate(Genome<?> genome, Object... objectiveArgs) {
        return evaluate(genome);
    }
    private Double evaluate(Genome<?> genome){
        ArrayList<Movement> genomeContainer = (ArrayList<Movement>) genome.getGeneticInformation();
        Board board = new Board(this.vehiclesInfo);
        for(Movement move : genomeContainer){
            board.moveVehicle(move);
            if(board.solutionDistance()==0) return 0.;
        }
        return (double) (board.solutionDistance());
    }
}
