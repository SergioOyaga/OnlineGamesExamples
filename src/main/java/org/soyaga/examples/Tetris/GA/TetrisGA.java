package org.soyaga.examples.Tetris.GA;


import org.soyaga.examples.Tetris.GA.CrossoverPolicy.ParentCross.MapAvgCross;
import org.soyaga.examples.Tetris.GA.CrossoverPolicy.ParentCross.MapMixCross;
import org.soyaga.examples.Tetris.GA.CrossoverPolicy.TetrisCrossoverPolicy;
import org.soyaga.examples.Tetris.GA.Initializer.TetrisInitializer;
import org.soyaga.examples.Tetris.GA.Mutations.TetrisBrownianMovement;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.LinearBoardEvaluationFunction;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.LinearSoftBoardEvaluationFunction;
import org.soyaga.examples.Tetris.Player.BoardEvaluationFunction.TanhBoardEvaluationFunction;
import org.soyaga.ga.CrossoverPolicy.ParentSelection.BlockWheelSelection;
import org.soyaga.ga.ElitismPolicy.FixedElitismPolicy;
import org.soyaga.ga.GeneticAlgorithm.StatsGeneticAlgorithm;
import org.soyaga.ga.MutationPolicy.TreeLevelSingleProbMutPolicy;
import org.soyaga.ga.NewbornPolicy.FixedNewbornPolicy;
import org.soyaga.ga.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.ga.StatsRetrievalPolicy.Stat.*;
import org.soyaga.ga.StoppingPolicy.MaxIterationCriteriaPolicy;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class TetrisGA extends StatsGeneticAlgorithm {

    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     */
    public TetrisGA(String ID, int populationSize, int maxIterations) throws IOException {
        super(ID,
                populationSize,
                new MaxIterationCriteriaPolicy(maxIterations),
                new TetrisCrossoverPolicy(
                        (int)(populationSize* 0.8),
                        new BlockWheelSelection(),
                        new ArrayList<>(){{
                            add(new MapMixCross());
                            add(new MapAvgCross());
                        }}
                ),
                new TreeLevelSingleProbMutPolicy(
                         new ArrayList<>(){{
                           add(new ArrayList<>(){{
                               add(new TetrisBrownianMovement());
                           }});
                        }},
                        0.1,
                        false
                ),
                new FixedElitismPolicy((int)(populationSize*0.1)),
                new FixedNewbornPolicy((int)(populationSize*0.1)),
                new TetrisInitializer(
                        new TanhBoardEvaluationFunction()
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
                        "src/out/Tetris",                                     // Stat retrieval output path.
                        true,                                           // Print Stats in command line.
                        true                                            // Save Stats summary in a csv.
                )
        );
    }


    /**
     * Transform the genome of the best individual into a String representing its value.
     *
     * @return String.
     */
    @Override
    public Object[] getResult(Object... resultArgs) {
        return new Object [] {"GA_Optimal",  ((TetrisIndividual)this.population.getBestIndividual()).getPlayer()};
    }
}
