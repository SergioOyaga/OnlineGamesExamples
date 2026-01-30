package org.soyaga.examples.RushHour.GA;


import org.soyaga.examples.RushHour.Board.Board;
import org.soyaga.examples.RushHour.Board.Cell;
import org.soyaga.examples.RushHour.Board.Movement;
import org.soyaga.examples.RushHour.Board.Vehicle;
import org.soyaga.examples.RushHour.GA.Evaluable.RushHourObjectiveFunction;
import org.soyaga.examples.RushHour.GA.Initializer.RushHourInitializer;
import org.soyaga.ga.GeneticAlgorithm.StatsGeneticAlgorithm;
import org.soyaga.ga.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.ga.StatsRetrievalPolicy.Stat.*;
import org.soyaga.ga.StoppingPolicy.TargetFitnessCriteriaPolicy;

import java.io.IOException;
import java.util.*;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class RushHoursGA extends StatsGeneticAlgorithm {
    private final LinkedHashMap<String, Object[]> vehiclesInfo;

    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     * @param vehiclesInfo LinkedHashMap&lt;String, Object[];&gt;
     */
    public RushHoursGA(String ID, LinkedHashMap<String, Object[]> vehiclesInfo, Integer maxMoves) throws IOException {
        super(ID,
                1000,
                null,
                null,
                null,
                null,
                null,
                new RushHourInitializer(
                        new RushHourObjectiveFunction(vehiclesInfo),
                        vehiclesInfo,
                        maxMoves
                ),
                new NIterationsStatsRetrievalPolicy(
                        1,
                        new ArrayList<>(){{                         // Array of stats.
                            add(new CurrentMinFitnessStat(4));      // Min Fitness Stat.
                            add(new CurrentMaxFitnessStat(4));      // Max Fitness Stat.
                            add(new HistoricalMinFitnessStat(4));   // Hist Min Fitness Stat.
                            add(new HistoricalMaxFitnessStat(4));   // Hist Max Fitness Stat.
                            add(new MeanSdStat(4));                 // Fitness Mean and Standard Dev Stat.
                            add(new PercentileStat(4,               // Interpolated Percentile Fitness Stat.
                                    new ArrayList<>(){{                             // Array of percentiles.
                                        add(0);add(25);add(50);add(75);add(100);        // Percentiles values.
                                    }}));
                            add(new StepGradientStat(4));           // Step Gradient Stat.
                            add(new TimeGradientStat(4));           // Time Gradient Stat.
                            add(new ElapsedTimeStat(4));            // Elapsed Time Stat.
                        }},
                        null,                                     // Stat retrieval output path.
                        true,                                           // Print Stats in command line.
                        false                                            // Save Stats summary in a csv.
                )
        );
        this.vehiclesInfo = vehiclesInfo;
        this.stoppingCriteriaPolicy = new TargetFitnessCriteriaPolicy(0., this.population);
    }



    /**
     * Function that performs the typical optimization for a genetic algorithm.
     * <ol>
     *     <li>Initialize population.</li>
     *     <li>Evaluate population.</li>
     *     <li>Measure stats.</li>
     *     <li>while (evaluate stopping condition):</li>
     *     <ol type="I">
     *         <li>Cross population &rarr; new population.</li>
     *         <li>Mutate new population.</li>
     *         <li>Apply elitism.</li>
     *         <li>Apply newborns.</li>
     *         <li>Reassign population to new population.</li>
     *         <li>Evaluate population.</li>
     *         <li>Measure stats.</li>
     *     </ol>
     *     <li>Close stats.</li>
     * </ol>
     */
    @Override
    public void optimize() throws IOException {
        Integer generation=0;

        this.statsRetrievalPolicy.printHeaderInConsole();
        this.statsRetrievalPolicy.printHeaderInCSV();
        this.gaInitializer.initialize(this);
        this.population.evaluate();
        this.statsRetrievalPolicy.apply(this.population,generation);
        while (this.stoppingCriteriaPolicy.hasToContinue(generation)){
            this.population.clear();
            this.gaInitializer.initialize(this);
            this.population.evaluate();
            generation++;
            this.statsRetrievalPolicy.apply(this.population,generation);
        }
        this.statsRetrievalPolicy.closeWriter();
    }


    /**
     * Transform the genome of the best individual into a String representing its value.
     *
     * @return String.
     */
    @Override
    public Object getResult(Object... resultArgs) {
        ArrayList<Movement> genomeContainer = (ArrayList<Movement>) this.population.getBestIndividual().getGenome().getGeneticInformation();
        genomeContainer = this.optimizeSolution(genomeContainer);
        ArrayList<Movement> solutionList = new ArrayList<>();
        Board board = new Board(this.vehiclesInfo);
        for (Movement move: genomeContainer){
            board.moveVehicle(move);
            solutionList.add(move);
            if(board.solutionDistance()==0)break;
        }
        return new Object [] {"GA_Optimal",  solutionList};
    }

    private ArrayList<Movement> optimizeSolution(ArrayList<Movement> genomeContainer) {
        ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>(genomeContainer);
        int lastSize = genomeContainer.size();
        int currentSize =lastSize;
        do {
            optimizedGenomeContainer = groupMoves(optimizedGenomeContainer);
            optimizedGenomeContainer = removeLoops(optimizedGenomeContainer);
            optimizedGenomeContainer = removeUnnecessaryMoves(optimizedGenomeContainer);
            optimizedGenomeContainer = mergeMoves(optimizedGenomeContainer);
            lastSize=currentSize;
            currentSize=optimizedGenomeContainer.size();
        }while(lastSize>currentSize);
        return optimizedGenomeContainer;
    }


    private String[][] createState(Board board) {
        Cell[][] grid = board.getBoardGrid();
        String[][] state = new String[grid.length][grid[0].length];
        for(int row=0; row<grid.length; row++){
            for(int col=0; col<grid[row].length; col++){
                Vehicle vehicle = grid[row][col].getVehicle();
                state[row][col] = vehicle==null? null: vehicle.getID();
            }
        }
        return state;
    }

    private ArrayList<Movement> groupMoves(ArrayList<Movement> genomeContainer) {
        ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>();
        for(Movement move:genomeContainer){
            if(optimizedGenomeContainer.isEmpty()) {
                optimizedGenomeContainer.add(move);
                continue;
            }
            Movement lastOptimizedMove = optimizedGenomeContainer.get(optimizedGenomeContainer.size()-1);
            if(move.vehicleID.equals(lastOptimizedMove.vehicleID)){
                optimizedGenomeContainer.remove(lastOptimizedMove);
                if(move.direction.equals(lastOptimizedMove.direction)){
                    Integer resultingMovement = lastOptimizedMove.distance + move.distance;
                    optimizedGenomeContainer.add(new Movement(move.vehicleID,move.direction,resultingMovement));
                }
                else{
                    int resultingMovement = lastOptimizedMove.distance- move.distance;
                    if(resultingMovement>0){
                        optimizedGenomeContainer.add(new Movement(move.vehicleID,lastOptimizedMove.direction,resultingMovement));
                    }
                    else if(resultingMovement<0){
                        String newDirection = lastOptimizedMove.direction;
                        switch (newDirection){
                            case "N": {
                                newDirection="S";
                                break;
                            }
                            case "E": {
                                newDirection="W";
                                break;
                            }
                            case "S": {
                                newDirection="N";
                                break;
                            }
                            case "W": {
                                newDirection="E";
                                break;
                            }
                            default: {
                                System.out.println("Not supported direction swift detected.");
                                break;
                            }
                        }
                        optimizedGenomeContainer.add(new Movement(move.vehicleID,newDirection,-resultingMovement));
                    }
                }
            }
            else{
                optimizedGenomeContainer.add(move);
            }
        }
        return optimizedGenomeContainer;
    }

    private ArrayList<Movement> removeLoops(ArrayList<Movement> genomeContainer){
        ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>();
        ArrayList<String[][]> states = new ArrayList<>();
        Board board = new Board(this.vehiclesInfo);
        states.add(this.createState(board));
        for(Movement chromosome:genomeContainer) {
            board.moveVehicle(chromosome);
            String[][] state = this.createState(board);
            int indexFrom = 0;
            boolean contains = false;
            for (String[][] el : states) {
                if (Arrays.deepEquals(el, state)) {
                    contains = true;
                    break;
                }
                indexFrom++;
            }
            optimizedGenomeContainer.add(chromosome);
            if (contains) {
                optimizedGenomeContainer.subList(indexFrom, optimizedGenomeContainer.size()).clear();
                states.subList(indexFrom, states.size()).clear();
            }
            states.add(state);
            if (board.solutionDistance() == 0) break;
        }
        return optimizedGenomeContainer;
    }

    private ArrayList<Movement> removeUnnecessaryMoves(ArrayList<Movement> genomeContainer) {
        ArrayList<Movement> toRemoveList= new ArrayList<>();
        for(Movement movement:genomeContainer){
            ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>(genomeContainer);
            optimizedGenomeContainer.removeAll(toRemoveList);
            optimizedGenomeContainer.remove(movement);
            Board board = new Board(this.vehiclesInfo);
            for(Movement optimizedMovement:optimizedGenomeContainer){
                int maxDistance = board.computeMovementDistance(optimizedMovement.vehicleID, optimizedMovement.direction);
                if(maxDistance<optimizedMovement.distance) break;
                board.moveVehicle(optimizedMovement);
                if(board.solutionDistance() == 0){
                    toRemoveList.add(movement);
                    break;
                }
            }
        }
        ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>(genomeContainer);
        optimizedGenomeContainer.removeAll(toRemoveList);
        return optimizedGenomeContainer;
    }


    private ArrayList<Movement> mergeMoves(ArrayList<Movement> genomeContainer) {
        ArrayList<Movement> optimizedGenomeContainer = new ArrayList<>(genomeContainer);
        int movement1Index=0;
        Iterator<Movement> iterator1 = optimizedGenomeContainer.iterator();
        while (iterator1.hasNext()){
            Movement movement1 = iterator1.next();
            int movement2Index=0;
            Iterator<Movement> iterator2 = optimizedGenomeContainer.iterator();
            while (iterator2.hasNext()){
                Movement movement2 = iterator2.next();
                if(movement1==movement2)continue;
                if(movement1.vehicleID.equals(movement2.vehicleID)){
                    Movement temptativeMovement = new Movement(movement1.vehicleID, movement1.direction, movement1.distance);
                    if(movement1.direction.equals(movement2.direction)){
                        temptativeMovement.distance+= movement2.distance;
                    } else if (movement1.distance> movement2.distance) {
                        temptativeMovement.distance-= movement2.distance;
                    }else{
                        temptativeMovement.direction = movement2.direction;
                        temptativeMovement.distance-= movement2.distance;
                        temptativeMovement.distance*=-1;
                    }
                    Board board = new Board(this.vehiclesInfo);
                    ArrayList<Movement> optimizedTentativeGenomeContainer = new ArrayList<>(optimizedGenomeContainer);
                    optimizedTentativeGenomeContainer.set(movement1Index,temptativeMovement);
                    optimizedTentativeGenomeContainer.remove(movement2Index);
                    for(Movement movement:optimizedTentativeGenomeContainer){
                        int maxDistance = board.computeMovementDistance(movement.vehicleID, movement.direction);
                        if(maxDistance<movement.distance) break;
                        board.moveVehicle(movement);
                        if(board.solutionDistance()==0){
                            optimizedGenomeContainer=optimizedTentativeGenomeContainer;
                            movement1Index--;
                            break;
                        }
                    }
                }
                movement2Index++;
            }
            movement1Index++;
        }
        return optimizedGenomeContainer;
    }
}
