package org.soyaga.examples.Minesweeper.MathModel;

import org.soyaga.examples.Minesweeper.Graph.Cell;
import org.soyaga.mm.mathematicalModelAlgorithm.ConstraintProgrammingSatisfiabilityModel;
import org.soyaga.mm.satisfiabilityCallBack.AllSolutionCallBack;

import java.util.*;

/**
 * Class that extends ConstraintProgrammingSatisfiabilityModel defines the problem objective, solver parameters,
 * SolutionCallBacks and Log for the solver callbacks.
 */
public class MinesweeperMathModel extends ConstraintProgrammingSatisfiabilityModel {
    private final ArrayList<Cell> problemCells;

    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param problemRules
     */
    public MinesweeperMathModel(String ID, ArrayList<Map.Entry<Integer, HashSet<Cell>>> problemRules, ArrayList<Cell> problemCells) {
        super(ID, new MinesweeperMMInitializer(problemRules, problemCells));
        this.addSolutionCAllBack(new AllSolutionCallBack());
        ((MinesweeperMMInitializer)this.getMMInitializer()).setModel(this);
        this.problemCells = problemCells;
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
    public HashMap<Cell, Double> getResult(Object... resultArgs) {
        HashMap<Cell, Double> certainty = new HashMap<>();
        this.problemCells.forEach(c->certainty.put(c,0.));
        for(AbstractMap.SimpleEntry<HashMap<String, ArrayList<?>>, Double> solutionEntry : this.getSolutionsCallBlack()){
            ArrayList<AbstractMap.SimpleEntry<?,Long>> valuesArray = (ArrayList<AbstractMap.SimpleEntry<?, Long>>) solutionEntry.getKey().get("CD_(c)");
            for(int i=0; i<valuesArray.size(); i++){
                Cell cell = this.problemCells.get(i);
                Double value = valuesArray.get(i).getValue().doubleValue() + certainty.get(cell);
                certainty.put(cell,value);
            }
        }
        int size = this.getSolutionsCallBlack().size();
        this.problemCells.forEach(c->certainty.put(c, certainty.get(c)/size));
        return certainty;
    }


    /**
     * Function that sets the objective function of the proto model. For linear Objectives in MP problems, we set the
     * objective coefficient directly in the variable Builder, for quadratic objectives we have to set the indexes
     * in the model builder proto.
     */
    @Override
    public void setObjectiveFunction(Object... mmVarArgs) {
    }

    /**
     * Function that allows the user to change solver default parameters.
     */
    @Override
    protected void initializeSolveParameters() {
        this.getSolveParameters().setMaxTimeInSeconds(30);
        this.getSolveParameters().setLogSearchProgress(true);
        this.getSolveParameters().setLogToStdout(true);
        this.getSolveParameters().setEnumerateAllSolutions(true);
    }

}
