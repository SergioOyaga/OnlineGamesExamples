package org.soyaga.examples.RushHour.GA.Initializer;

import lombok.AllArgsConstructor;
import org.soyaga.Initializer.GAInitializer;
import org.soyaga.examples.RushHour.Board.Board;
import org.soyaga.examples.RushHour.Board.Movement;
import org.soyaga.examples.RushHour.Board.Vehicle;
import org.soyaga.examples.RushHour.GA.Evaluable.RushHourObjectiveFunction;
import org.soyaga.ga.GeneticInformationContainer.Genome.ArrayListGenome;
import org.soyaga.ga.Individual;

import java.util.*;

@AllArgsConstructor
public class RushHourInitializer extends GAInitializer {
    private final RushHourObjectiveFunction objectiveFunction;
    private final LinkedHashMap<String, Object[]> vehiclesInfo;
    private final Integer maxMoves;

    /**
     * Abstract function that initializes an individual.
     */
    @Override
    public Individual initializeIndividual() {
        Random random = new Random();
        ArrayListGenome<Movement> genome = new ArrayListGenome<>();
        Board board = new Board(this.vehiclesInfo);
        for(int genIndex=0; genIndex<this.maxMoves; genIndex++) {
            ArrayList<Map.Entry<String,ArrayList<Map.Entry<String,Integer>>>> movementPool = new ArrayList<>();
            for(Map.Entry<String, Vehicle> vehicleEntry:board.getVehiclesById().entrySet()){
                String vehicleID = vehicleEntry.getKey();
                int nDistance = board.computeMovementDistance(vehicleID, "N");
                int eDistance = board.computeMovementDistance(vehicleID, "E");
                int sDistance = board.computeMovementDistance(vehicleID, "S");
                int wDistance = board.computeMovementDistance(vehicleID, "W");
                ArrayList<Map.Entry<String,Integer>> directions = new ArrayList<>();
                if (nDistance > 0) directions.add(new AbstractMap.SimpleEntry<>("N", nDistance));
                if (eDistance > 0) directions.add(new AbstractMap.SimpleEntry<>("E", eDistance));
                if (sDistance > 0) directions.add(new AbstractMap.SimpleEntry<>("S", sDistance));
                if (wDistance > 0) directions.add(new AbstractMap.SimpleEntry<>("W", wDistance));
                if(!directions.isEmpty()){
                    movementPool.add(new AbstractMap.SimpleEntry<>(vehicleID,directions));
                }
            }

            Map.Entry<String, ArrayList<Map.Entry<String, Integer>>> selectedVehicle = movementPool.get(random.nextInt(movementPool.size()));
            Map.Entry<String, Integer> selectedDirection = selectedVehicle.getValue().get(random.nextInt(selectedVehicle.getValue().size()));
            Integer distance = random.nextInt(selectedDirection.getValue())+1;
            Movement move = new Movement(selectedVehicle.getKey(),selectedDirection.getKey(),distance);
            genome.add(move);
            board.moveVehicle(move);
            if (board.solutionDistance()==0) break;
        }
        return  new Individual(genome,null,this.objectiveFunction,0.);
    }
}
