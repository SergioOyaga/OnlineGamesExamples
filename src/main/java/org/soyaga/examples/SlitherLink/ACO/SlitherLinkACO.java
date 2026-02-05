package org.soyaga.examples.SlitherLink.ACO;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.AntColonyAlgorithm.StatsAntColonyAlgorithm;
import org.soyaga.aco.BuilderEvaluator.CircleBuilderEvaluator;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Evaluable.Feasibility.ConstraintBasedFeasibilityFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.ParallelConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.*;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.MaxGlobalBestFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.MinPercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.World;
import org.soyaga.examples.SlitherLink.ACO.Ant.SlitherLinkAnt;
import org.soyaga.examples.SlitherLink.ACO.Evaluable.DistanceObjectiveFunction;
import org.soyaga.examples.SlitherLink.ACO.Stats.ImageStat;
import org.soyaga.examples.SlitherLink.ACO.StoppingPolicy.TargetFeasibilityCriteriaPolicy;

import java.io.IOException;
import java.util.ArrayList;

public class SlitherLinkACO extends StatsAntColonyAlgorithm {
    public SlitherLinkACO(String ID, Colony colony, World world, int rows, int cols , ArrayList<Constraint> constraints, ImageStat stat) throws IOException {
        super(ID,
                world,                                                              //World
                colony,                                                             //Colony
                new TargetFeasibilityCriteriaPolicy(0., colony),            //Stopping criteria policy
                new ACOInitializer(
                        new SlitherLinkAnt(
                                new Solution(
                                    new DistanceObjectiveFunction(rows*cols),
                                    new ConstraintBasedFeasibilityFunction(constraints),
                                    1000.,
                                    rows*cols,
                                    new CircleBuilderEvaluator()          //Solution evaluator
                                    ),
                                new RandomProportionalEdgeSelector(                //Edge selector How the ant selects the edge to take.
                                        0.5,                                        //Double with the ants' Alpha (>0) parameter (importance of the edges pheromones against the edges "distances").
                                        0.5                                         //Double with the ants' Beta (>0) parameter (importance of the edges "distances" against the edges pheromones).
                                ),                                                 //
                                rows*cols*10.                                               //Double with the amount of pheromone each ant can deposit in its track (same order of the problem optimal fitness).
                        ),
                        rows*cols*1                                                         //Integer with the initial number of ants.
                ),                                                     //Initializer
                new ParallelConstructorPolicy(),                                    //Constructor
                new SimpleUpdatePheromonePolicy(                                    //UpdatePheromonePolicy, first evaporate then add:
                        new MaxGlobalBestFitnessProportionalAddPheromonePolicy(     //AddPheromonePolicy, prop to sol fitness:
                                1.                                                  //Double, max pheromones.
                        ),
                        new MinPercentageEvaporatePheromonePolicy(                  //EvaporatePheromonePolicy, percentage persistence:
                                0.99,                                                 //Double, persistence.
                                0.01                                                // Double, min pheromone.
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
                            add(stat);
                        }},
                        null,                                               //String, with the output path.
                        true,                                                       //Boolean, print in console.
                        false                                                       //Boolean, store in csv file.
                )
        );
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
        return new Object [] {"ACO_Optimal", this.getColony().getBestSolution().getEdgesUtilized()};
    }
}
