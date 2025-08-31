package org.soyaga.examples.AllOut.GA;


import org.soyaga.examples.AllOut.GA.Evaluable.AllOutFeasibilityFunction;
import org.soyaga.examples.AllOut.GA.Evaluable.AllOutObjectiveFunction;
import org.soyaga.examples.AllOut.GA.Initializer.AllOutInitializer;
import org.soyaga.examples.AllOut.GA.StoppingPolicy.TargetFeasibilityCriteriaPolicy;
import org.soyaga.ga.CrossoverPolicy.FixedCrossoverPolicy;
import org.soyaga.ga.CrossoverPolicy.ParentCross.TwoPointCrossover;
import org.soyaga.ga.CrossoverPolicy.ParentSelection.TournamentSelection;
import org.soyaga.ga.ElitismPolicy.FixedElitismPolicy;
import org.soyaga.ga.GeneticAlgorithm.StatsGeneticAlgorithm;
import org.soyaga.ga.GeneticInformationContainer.Chromosome.ArrayListChromosome;
import org.soyaga.ga.GeneticInformationContainer.Gen.GenericGen;
import org.soyaga.ga.MutationPolicy.ByLevelLevelProbMutPolicy;
import org.soyaga.ga.MutationPolicy.Mutations.BoolGenDenialMutation;
import org.soyaga.ga.MutationPolicy.Mutations.GenericSwapMutation;
import org.soyaga.ga.NewbornPolicy.FixedNewbornPolicy;
import org.soyaga.ga.Population;
import org.soyaga.ga.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.ga.StatsRetrievalPolicy.Stat.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class AllOutGA extends StatsGeneticAlgorithm implements Callable<Object> {
    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     * @param cols Integer with the number of cols.
     * @param rows Integer with the number of rows.
     */
    public AllOutGA(String ID, Integer rows, Integer cols, Boolean[][] imageGrid) throws IOException {
        super(ID,
                rows*cols ,
                new TargetFeasibilityCriteriaPolicy(0.),
                new FixedCrossoverPolicy(
                        rows*cols*5/10,
                        new TournamentSelection(),
                        new TwoPointCrossover()
                ),
                new ByLevelLevelProbMutPolicy(
                        new ArrayList<>(){{
                            add(new ArrayList<>());
                            add(
                                new ArrayList<>());
                            add(
                                new ArrayList<>(){{
                                    add(new BoolGenDenialMutation());
                                }});
                            }},
                        new ArrayList<>(){{add(0.);add(0.); add(0.05);}},
                        false,
                        false
                ),
                new FixedElitismPolicy(0),
                new FixedNewbornPolicy((rows*cols)*5/10),
                new AllOutInitializer(
                        new AllOutObjectiveFunction(),
                        new AllOutFeasibilityFunction(imageGrid, rows, cols),
                        rows,
                        cols
                ),
                new NIterationsStatsRetrievalPolicy(
                        10000,
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
        ArrayList<ArrayListChromosome<GenericGen<Boolean>>> solutionArray = (ArrayList<ArrayListChromosome<GenericGen<Boolean>>>) this.population.getBestIndividual().getGenome().getGeneticInformation();
        ArrayList<ArrayList<Boolean>> solution = new ArrayList<>();
        for(ArrayListChromosome<GenericGen<Boolean>> chromosome:solutionArray){
            ArrayList<Boolean> rowSolution = new ArrayList<>();
            solution.add(rowSolution);
            for(GenericGen<Boolean> gen: chromosome.getGeneticInformation()){
                rowSolution.add(gen.getGeneticInformation());
            }
        }
        return new Object [] {"GA_Optimal", solution};
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
}
