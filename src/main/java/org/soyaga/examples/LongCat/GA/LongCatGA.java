package org.soyaga.examples.LongCat.GA;


import org.soyaga.examples.LongCat.GA.Evaluable.LongCatFeasibilityFunction;
import org.soyaga.examples.LongCat.GA.Evaluable.LongCatObjectiveFunction;
import org.soyaga.examples.LongCat.GA.Initializer.LongCatInitializer;
import org.soyaga.examples.LongCat.GA.StoppingPolicy.TargetFeasibilityCriteriaPolicy;
import org.soyaga.examples.LongCat.Graph.Graph;
import org.soyaga.examples.LongCat.Graph.Node;
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
import java.util.HashSet;

/**
 * Extends StatsGeneticAlgorithm and defines how we gather the results.
 */
public class LongCatGA extends StatsGeneticAlgorithm {
    private final Graph graph;
    /**
     * Constructor that matches its super constructor.
     *
     * @param ID String with the GA description.
     * @param graph Graph with the state of the board.
     */
    public LongCatGA(String ID, Graph graph) throws IOException {
        super(ID,
                10,
                new TargetFeasibilityCriteriaPolicy(0.),
                new FixedCrossoverPolicy(
                        0,
                        new TournamentSelection(),
                        new HeuristicCrossover()
                ),
                new ByLevelLevelProbMutPolicy(
                        new ArrayList<>(){{
                            add(
                                new ArrayList<>(){{
                                    add(null); //TODO: add
                                }});
                            }},
                        new ArrayList<>(){{add(0.);}},//TODO:add
                        false,
                        false
                ),
                new FixedElitismPolicy(1),
                new FixedNewbornPolicy(9),
                new LongCatInitializer(
                        new LongCatObjectiveFunction(),
                        new LongCatFeasibilityFunction(graph),
                        graph
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
        this.graph = graph;
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
        ArrayList<String> genomeContainer = (ArrayList<String>) this.population.getBestIndividual().getGenome().getGeneticInformation();
        ArrayList<String> solutionList = new ArrayList<>();
        Node currentNode = this.graph.getStartingNode();
        HashSet<Node> reachedNodes = new HashSet<>();
        reachedNodes.add(currentNode);
        for(String chromosome:genomeContainer){
            switch (chromosome){
                case"N":{
                    if(currentNode.getRow()>0) {
                        Node nextNode = graph.getNodes()[currentNode.getRow() - 1][currentNode.getCol()];
                        if (currentNode.getConnectedNodes().contains(nextNode) && !reachedNodes.contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                            solutionList.add(chromosome);
                        }
                    }
                    break;
                }
                case"E":{
                    if(currentNode.getCol()<graph.getCols()-1) {
                        Node nextNode = graph.getNodes()[currentNode.getRow()][currentNode.getCol() + 1];
                        if (currentNode.getConnectedNodes().contains(nextNode) && !reachedNodes.contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                            solutionList.add(chromosome);
                        }
                    }
                    break;
                }
                case"S":{
                    if(currentNode.getRow()< graph.getRows()-1) {
                        Node nextNode = graph.getNodes()[currentNode.getRow() + 1][currentNode.getCol()];
                        if (currentNode.getConnectedNodes().contains(nextNode)&& !reachedNodes.contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                            solutionList.add(chromosome);
                        }
                    }
                    break;
                }
                case"W":{
                    if(currentNode.getCol()>0) {
                        Node nextNode = graph.getNodes()[currentNode.getRow()][currentNode.getCol() - 1];
                        if (currentNode.getConnectedNodes().contains(nextNode)&& !reachedNodes.contains(nextNode)) {
                            currentNode = graph.makeStraightMovement(currentNode, nextNode, reachedNodes);
                            solutionList.add(chromosome);
                        }
                    }
                    break;
                }
            }
            if(graph.getNumberOfNodes()== reachedNodes.size()) break;
        }
        return new Object [] {"GA_Optimal",  solutionList};
    }
}
