package org.soyaga.examples.Zip.ACO.UpdatePheromonePolicy.AddPheromonePolicy;

import lombok.AllArgsConstructor;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.AddPheromonePolicy;
import org.soyaga.aco.world.World;

import java.util.ArrayList;

/**
 * This class defines a pheromone addition policy. The addition is carried out in a manner similar to
 * SolFitnessProportionalAddPheromonePolicy, but with a maximum limit on the amount of pheromone that an Edge
 * can hold.
 */
public class MaxSolFitnessProportionalGlobalBestAddPheromonePolicy implements AddPheromonePolicy {
    /**
     * Double with the maximum amount of pheromone that an Edge can have
     */
    private final Double maxPheromone;
    /**
     * ArrayList with the nodes visited by the historical best.
     */
    private ArrayList<Object> bestHistNodesVisited;
    /**
     * ArrayList with the edges visited by the historical best.
     */
    private ArrayList<Object> bestHistEdgesUtilized;
    /**
     * Double with the fitness of the historical best.
     */
    private Double bestHistFitness;

    /**
     * Constructor.
     * @param maxPheromone Double with the pheromone maximum.
     */
    public MaxSolFitnessProportionalGlobalBestAddPheromonePolicy(Double maxPheromone) {
        this.maxPheromone = maxPheromone;
    }

    /**
     * Function that performs the AddPheromonePolicy action over the PheromoneContainer.
     *
     * @param world      world that is going to update its PheromoneContainer.
     * @param colony     Colony that contains the ants with the solutions.
     * @param policyArgs VarArgs containing the additional information needed to apply the pheromone update policy.
     */
    @Override
    public void apply(World world, Colony colony, Object... policyArgs) {
        for(Ant ant: colony.getAnts()){
            Solution solution=ant.getSolution();
            Object node = solution.getNodesVisited().get(0);
            for(Object edge:solution.getEdgesUtilized()){
                world.getPheromoneContainer().setNextPheromone(node, edge,
                        this.computeAddition(solution.getFitnessValue(), world, node, edge, ant.getPheromoneQuantity()));
                node = world.getGraph().getNextNode(edge);
            }
        }
        Solution solution=colony.getBestSolution();
        if(this.bestHistFitness ==null || solution.getFitnessValue()<this.bestHistFitness){
            this.bestHistNodesVisited = new ArrayList<>(solution.getNodesVisited());
            this.bestHistEdgesUtilized = new ArrayList<>(solution.getEdgesUtilized());
            this.bestHistFitness = solution.getFitnessValue();
        }
        if(this.bestHistNodesVisited.size()==0) return;
        Object node = this.bestHistNodesVisited.get(0);
        for(Object edge:this.bestHistEdgesUtilized){
            world.getPheromoneContainer().setNextPheromone(node, edge,
                    this.computeAddition(this.bestHistFitness, world, node, edge, colony.getAnts().get(0).getPheromoneQuantity()));
            node = world.getGraph().getNextNode(edge);
        }
    }

    /**
     * Function that carries out the addition to the pheromone.
     * <ol>
     *     <li>The ant can deposit Q/sol.Fitness pheromone in each edge.</li>
     *     <li>The pheromone is limited to the maximum amount the Edge can have.</li>
     * </ol>
     *
     * @param fitness Double.
     * @param world world with the PheromoneContainer.
     * @param node Node from where the edge originates.
     * @param edge Edge to which pheromone is intended to be added.
     * @param pheromoneQuantity  Double with the amount of pheromone, this ant can deposit in all the path.
     * @return Double representing the new pheromone value.
     */
    private Double computeAddition(Double fitness, World world, Object node, Object edge,
                                   Double pheromoneQuantity){
        Double pheromone= world.getPheromoneContainer().getNextPheromone(node,edge);
        pheromone += pheromoneQuantity/ fitness;
        return Math.min(pheromone,this.maxPheromone);
    }
}
