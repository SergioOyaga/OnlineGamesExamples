package org.soyaga.examples.LinkedInZip.ACO;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.AntColonyAlgorithm.StatsAntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.SolutionConstructorPolicy.ParallelConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.*;
import org.soyaga.aco.StoppingPolicy.TargetFitnessCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.MinPercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.World;
import org.soyaga.examples.LinkedInZip.ACO.UpdatePheromonePolicy.AddPheromonePolicy.MaxSolFitnessProportionalGlobalBestAddPheromonePolicy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ZipACO extends StatsAntColonyAlgorithm implements Callable<Object> {
    public ZipACO(String ID, Colony colony, World world, ACOInitializer acoInitializer ) throws IOException {
        super(ID,
                world,                                                              //World
                colony,                                                             //Colony
                new TargetFitnessCriteriaPolicy(0., colony),            //Stopping criteria policy
                acoInitializer,                                                     //Initializer
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
                        20,                                                         //Integer, steps between measures.
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
        return new Object [] {"ACO", this.getColony().getBestSolution()};
    }
}
