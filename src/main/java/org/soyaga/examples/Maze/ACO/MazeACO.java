package org.soyaga.examples.Maze.ACO;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Ant.SimpleMemoryAnt;
import org.soyaga.aco.AntColonyAlgorithm.StatsAntColonyAlgorithm;
import org.soyaga.aco.BuilderEvaluator.LineBuilderEvaluator;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.ParallelConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.*;
import org.soyaga.aco.StoppingPolicy.TargetFitnessCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.MinPercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.World;
import org.soyaga.examples.LinkedInZip.ACO.UpdatePheromonePolicy.AddPheromonePolicy.MaxSolFitnessProportionalGlobalBestAddPheromonePolicy;
import org.soyaga.examples.Maze.ACO.Elements.MazeNode;
import org.soyaga.examples.Maze.ACO.Evaluable.MazeObjectiveFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MazeACO extends StatsAntColonyAlgorithm implements Callable<Object> {
    public MazeACO(String ID, Colony colony, World world, MazeNode startNode, MazeNode endNode, int rows, int cols ) throws IOException {
        super(ID,
                world,                                                              //World
                colony,                                                             //Colony
                new TargetFitnessCriteriaPolicy(0., colony),            //Stopping criteria policy
                new ACOInitializer(
                        new SimpleMemoryAnt(
                                new Solution(
                                        new MazeObjectiveFunction(endNode),
                                        null,
                                        1.,
                                        700,
                                        new LineBuilderEvaluator(           //Solution evaluator
                                                startNode,                          //Start MazeNode
                                                endNode                            //End MazeNode
                                        )
                                ),
                                new RandomProportionalEdgeSelector(                //Edge selector How the ant selects the edge to take.
                                        1.5,                                        //Double with the ants' Alpha (>0) parameter (importance of the edges pheromones against the edges "distances").
                                        .5                                         //Double with the ants' Beta (>0) parameter (importance of the edges "distances" against the edges pheromones).
                                ),                                                 //
                                1.                                               //Double with the amount of pheromone each ant can deposit in its track (same order of the problem optimal fitness).
                        ),
                        rows*cols                                                         //Integer with the initial number of ants.
                ),                                                     //Initializer
                new ParallelConstructorPolicy(),                                    //Constructor
                new SimpleUpdatePheromonePolicy(                                    //UpdatePheromonePolicy, first evaporate then add:
                        new MaxSolFitnessProportionalGlobalBestAddPheromonePolicy(     //AddPheromonePolicy, prop to sol fitness:
                                1.                                                  //Double, max pheromones.
                        ),
                        new MinPercentageEvaporatePheromonePolicy(                  //EvaporatePheromonePolicy, percentage persistence:
                                0.6,                                                 //Double, persistence.
                                0.1                                                // Double, min pheromone.
                        )
                ),
                new NIterationsStatsRetrievalPolicy(                                //Stats retrieval policy, every n iterations:
                        1,                                                         //Integer, steps between measures.
                        new ArrayList<>(){{                                         //Array Of Stats
                            add(new CurrentMinFitnessStat(2));        //Min Fitness Stat.
                            add(new CurrentMaxFitnessStat(2));        //Max Fitness Stat.
                            add(new HistoricalMinFitnessStat(2));     //Hist Min Fitness Stat.
                            add(new MeanSdFitnessStat(2));            //Fitness Mean and Standard Dev Stat.
                            add(new MeanSdPheromoneStat(2));          //Fitness Mean and Standard Dev Stat.
                        }},
                        null,                                               //String, with the output path.
                        true,                                                       //Boolean, print in console.
                        false                                                       //Boolean, store in csv file.
                )
        );
    }

    /**
     * Runs this operation.
     */
    @Override
    public Object call() {
        try {
            this.optimize();
            return this.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that returns from an optimized solution the actual result in the
     * form that is convenient for the problem.
     *
     * @param resultArgs VarArgs containing the additional information needed to get the results.
     * @return Object containing the result of the optimization. Ej.:
     * <ul>
     *     <li>Best <b><i>Individual</i></b></li>
     *     <li>Set of best <b><i>Individuals</i></b></li>
     *     <li>Any format suitable for our problem <b><i>Object</i></b></li>
     * </ul>
     */
    @Override
    public Object getResult(Object... resultArgs) {
        return new Object [] {"ACO_Optimal", this.getColony().getBestSolution().getNodesVisited()};
    }
}
