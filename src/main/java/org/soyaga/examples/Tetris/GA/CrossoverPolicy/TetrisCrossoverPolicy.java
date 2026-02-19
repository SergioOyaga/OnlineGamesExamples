package org.soyaga.examples.Tetris.GA.CrossoverPolicy;

import org.soyaga.ga.CrossoverPolicy.CrossoverPolicy;
import org.soyaga.ga.CrossoverPolicy.ParentCross.Crossover;
import org.soyaga.ga.CrossoverPolicy.ParentSelection.Selection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class to perform a Fixed Mutation Policy:
 * <ul>
 *     <li>Fixed number of crossed individuals to produce (every iteration the crossover produces the same number of
 *     new Individuals).</li>
 *     <li>Fixed Selection Methodology (The parents are selected using the same methodology always. Ej.:
 *     TournamentSelection.</li>
 *     <li>Fixed Crossover Methodology (The parents are crossed using the same methodology always. Ej.:
 *    TwoPointCrossover.</li>
 * </ul>
 */
public class TetrisCrossoverPolicy implements CrossoverPolicy {
    /**
     * Number of crossed individuals to produce.
     */
    private final Integer CrossoverNumber;
    /**
     * Selection Object.
     */
    private final Selection selectionMethodology;
    /**
     * Crossover Object.
     */
    private final ArrayList<Crossover> crossoverMethodologyList;

    /**
     * Constructor.
     *
     * @param crossoverNumber Integer with the number of crossed Individuals to produce each iteration.
     * @param selectionMethodology Selection Object with the selection procedure.
     * @param crossoverMethodologyList CrossoverList Object with the crossover procedure list.
     */
    public TetrisCrossoverPolicy(Integer crossoverNumber, Selection selectionMethodology, ArrayList<Crossover> crossoverMethodologyList) {
        CrossoverNumber = crossoverNumber;
        this.selectionMethodology = selectionMethodology;
        this.crossoverMethodologyList = crossoverMethodologyList;
    }

    /**
     * Function that returns the number of individuals to produce.
     *
     * @param policyArgs VarArgs containing the additional information needed to compute the crossover number.
     *                   (Not used)
     * @return Integer with the number of Individuals to produce.
     */
    @Override
    public Integer getCrossoverNumber(Object... policyArgs) {
        return this.CrossoverNumber;
    }

    /**
     * Function that returns the Selection procedure.
     *
     * @param policyArgs VarArgs containing the additional information needed to obtain the Selection. (Not used)
     * @return Selection Object containing the parent selection procedure.
     */
    @Override
    public Selection getParentSelectionMethodology(Object... policyArgs) {
        return this.selectionMethodology;
    }

    /**
     * Function that returns the Crossover procedure.
     *
     * @param policyArgs VarArgs containing the additional information needed to obtain the Crossover. (Not used)
     * @return Crossover Object containing the parent crossover procedure.
     */
    @Override
    public Crossover getParentCrossMethodology(Object... policyArgs) {
        Random random = new Random();
        return this.crossoverMethodologyList.get(random.nextInt(this.crossoverMethodologyList.size()));
    }
}
