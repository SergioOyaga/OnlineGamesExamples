package org.soyaga.examples.JellyDoods.GA;


import org.soyaga.examples.JellyDoods.Board.Board;
import org.soyaga.examples.JellyDoods.Board.Cell;
import org.soyaga.examples.JellyDoods.Board.Piece;
import org.soyaga.examples.JellyDoods.GA.Evaluable.JellyDoodsFeasibilityFunction;
import org.soyaga.examples.JellyDoods.GA.Evaluable.JellyDoodsObjectiveFunction;
import org.soyaga.examples.JellyDoods.GA.Initializer.JellyDoodsInitializer;
import org.soyaga.examples.JellyDoods.GA.Mutations.RandomDirectionMutation;
import org.soyaga.examples.JellyDoods.GA.StoppingPolicy.TargetFeasibilityCriteriaPolicy;
import org.soyaga.ga.CrossoverPolicy.FixedCrossoverPolicy;
import org.soyaga.ga.CrossoverPolicy.ParentCross.HeuristicCrossover;
import org.soyaga.ga.CrossoverPolicy.ParentSelection.TournamentSelection;
import org.soyaga.ga.ElitismPolicy.FixedElitismPolicy;
import org.soyaga.ga.GeneticAlgorithm.StatsGeneticAlgorithm;
import org.soyaga.ga.MutationPolicy.ByLevelLevelProbMutPolicy;
import org.soyaga.ga.NewbornPolicy.FixedNewbornPolicy;
import org.soyaga.ga.Population;
import org.soyaga.ga.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.ga.StatsRetrievalPolicy.Stat.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class JellyDoodsGA extends StatsGeneticAlgorithm {
    private final ArrayList<ArrayList<Integer>> level;
    private final Integer maxMoves;

    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     * @param level ArrayList&lt;ArrayList&lt;Integer&gt;&gt;
     */
    public JellyDoodsGA(String ID, ArrayList<ArrayList<Integer>> level, Integer maxMoves) throws IOException {
        super(ID,
                10,
                new TargetFeasibilityCriteriaPolicy(0.),
                new FixedCrossoverPolicy(
                        4,
                        new TournamentSelection(),
                        new HeuristicCrossover()
                ),
                new ByLevelLevelProbMutPolicy(
                        new ArrayList<>(){{
                            add(
                                new ArrayList<>(){{
                                    add(new RandomDirectionMutation(level, maxMoves));
                                }});
                            }},
                        new ArrayList<>(){{add(0.1);}},
                        false,
                        false
                ),
                new FixedElitismPolicy(1),
                new FixedNewbornPolicy(5),
                new JellyDoodsInitializer(
                        new JellyDoodsObjectiveFunction(),
                        new JellyDoodsFeasibilityFunction(level, maxMoves),
                        level,
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
        this.level = level;
        this.maxMoves = maxMoves;
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
        while (this.stoppingCriteriaPolicy.hasToContinue(this.population.getBestIndividual().getFeasibilityValue())){
            Population newPopulation=this.crossoverPolicy.apply(this.population);
            this.mutationPolicy.apply(newPopulation);
            newPopulation.add(this.elitismPolicy.apply(this.population));
            newPopulation.add(this.newbornPolicy.apply(this.gaInitializer));
            this.population = newPopulation;
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
        ArrayList<String[]> genomeContainer = (ArrayList<String[]>) this.population.getBestIndividual().getGenome().getGeneticInformation();
        genomeContainer = this.optimizeSolution(genomeContainer);
        ArrayList<Object[]> solutionList = new ArrayList<>(); //ArrayList([[row,col],"move"])
        Board board = new Board(this.level);
        for(int i = 0;i<this.maxMoves; i++){
            String[] chromosome = genomeContainer.get(i);
            String pieceId= chromosome[0];
            Piece piece = board.getJelliesById().get(pieceId);
            if(piece==null) continue;
            String move = chromosome[1];
            switch (move){
                case"N":{
                    if(board.computeMovementDistance(piece,"N")==0) continue;
                    Cell cell = piece.getCurrentCells().iterator().next();
                    int[] cellIndexes= {cell.getRow(), cell.getCol()};
                    solutionList.add(new Object[]{cellIndexes,"N"});
                    board.movePiece(piece,"N");
                    break;
                }
                case"E":{
                    if(board.computeMovementDistance(piece,"E")==0) continue;
                    Cell cell = piece.getCurrentCells().iterator().next();
                    int[] cellIndexes= {cell.getRow(), cell.getCol()};
                    solutionList.add(new Object[]{cellIndexes,"E"});
                    board.movePiece(piece,"E");
                    break;
                }
                case"S":{
                    if(board.computeMovementDistance(piece,"S")==0) continue;
                    Cell cell = piece.getCurrentCells().iterator().next();
                    int[] cellIndexes= {cell.getRow(), cell.getCol()};
                    solutionList.add(new Object[]{cellIndexes,"S"});
                    board.movePiece(piece,"S");
                    break;
                }
                case"W":{
                    if(board.computeMovementDistance(piece,"W")==0) continue;
                    Cell cell = piece.getCurrentCells().iterator().next();
                    int[] cellIndexes= {cell.getRow(), cell.getCol()};
                    solutionList.add(new Object[]{cellIndexes,"W"});
                    board.movePiece(piece,"W");
                    break;
                }
            }
            if(board.solutionDistance()==0)break;
        }
        return new Object [] {"GA_Optimal",  solutionList};
    }

    private ArrayList<String[]> optimizeSolution(ArrayList<String[]> genomeContainer) {
        ArrayList<String[]> optimizedGenomeContainer = removeLoops(genomeContainer);
        optimizedGenomeContainer = removeUnnecessaryMoves(optimizedGenomeContainer);
        return optimizedGenomeContainer;
    }

    private String[][] createState(Board board) {
        Cell[][] grid = board.getBoardGrid();
        String[][] state = new String[grid.length][grid[0].length];
        for(int row=0; row<grid.length; row++){
            for(int col=0; col<grid[row].length; col++){
                Piece piece = grid[row][col].getPiece();
                state[row][col] = piece==null? null: piece.getId();
            }
        }
        return state;
    }

    private ArrayList<String[]> removeLoops(ArrayList<String[]> genomeContainer){
        ArrayList<String[]> optimizedGenomeContainer = new ArrayList<>();
        ArrayList<String[][]> states = new ArrayList<>();
        Board board = new Board(this.level);
        states.add(this.createState(board));
        for(int i = 0;i<this.maxMoves; i++) {
            String[] chromosome = genomeContainer.get(i);
            String pieceId = chromosome[0];
            Piece piece = board.getJelliesById().get(pieceId);
            if (piece == null) continue;
            String move = chromosome[1];
            switch (move) {
                case "N": {
                    if (board.computeMovementDistance(piece, "N") == 0) continue;
                    board.movePiece(piece, "N");
                    break;
                }
                case "E": {
                    if (board.computeMovementDistance(piece, "E") == 0) continue;
                    board.movePiece(piece, "E");
                    break;
                }
                case "S": {
                    if (board.computeMovementDistance(piece, "S") == 0) continue;
                    board.movePiece(piece, "S");
                    break;
                }
                case "W": {
                    if (board.computeMovementDistance(piece, "W") == 0) continue;
                    board.movePiece(piece, "W");
                    break;
                }
            }
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
            if (contains) {
                optimizedGenomeContainer.subList(indexFrom, optimizedGenomeContainer.size()).clear();
            }
            optimizedGenomeContainer.add(chromosome);
            states.add(state);
            if (board.solutionDistance() == 0) break;
        }
        return optimizedGenomeContainer;
    }

    private ArrayList<String[]> removeUnnecessaryMoves(ArrayList<String[]> genomeContainer) {
        ArrayList<String[]> optimizedGenomeContainer = new ArrayList<>(genomeContainer);
        int indexToRemove = 0;
        for(int i = 0;i<this.maxMoves; i++) {
            Board board = new Board(this.level);
            ArrayList<String[]> optimizedGenomeContainerClon = new ArrayList<>(optimizedGenomeContainer);
            if(indexToRemove>=optimizedGenomeContainerClon.size())break;
            optimizedGenomeContainerClon.remove(indexToRemove);
            for(int j = 0;j<this.maxMoves; j++) {
                if(j>=optimizedGenomeContainerClon.size())break;
                String[] chromosome = optimizedGenomeContainerClon.get(j);
                String pieceId = chromosome[0];
                Piece piece = board.getJelliesById().get(pieceId);
                if (piece == null) continue;
                String move = chromosome[1];
                switch (move) {
                    case "N": {
                        if (board.computeMovementDistance(piece, "N") == 0) continue;
                        board.movePiece(piece, "N");
                        break;
                    }
                    case "E": {
                        if (board.computeMovementDistance(piece, "E") == 0) continue;
                        board.movePiece(piece, "E");
                        break;
                    }
                    case "S": {
                        if (board.computeMovementDistance(piece, "S") == 0) continue;
                        board.movePiece(piece, "S");
                        break;
                    }
                    case "W": {
                        if (board.computeMovementDistance(piece, "W") == 0) continue;
                        board.movePiece(piece, "W");
                        break;
                    }
                }
                if (board.solutionDistance() == 0) break;
            }
            if(board.solutionDistance() == 0) {
                optimizedGenomeContainer = optimizedGenomeContainerClon;
                indexToRemove--;
            }
            indexToRemove++;
        }
        return optimizedGenomeContainer;
    }
}
