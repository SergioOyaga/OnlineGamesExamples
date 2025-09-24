package org.soyaga.examples.NonoGrams.MathModel;

import org.soyaga.Initializer.MMInitializer;
import org.soyaga.mm.mathematicalModelAlgorithm.ConstraintProgrammingSatisfiabilityModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that extends ConstraintProgrammingSatisfiabilityModel defines the problem objective, solver parameters,
 * SolutionCallBacks and Log for the solver callbacks.
 */
public class NonoGramsMathModel extends ConstraintProgrammingSatisfiabilityModel {

    private final int rows,cols;
    private final HashMap<Integer,String> numberColorMap;
    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param mmInitializer MMInitializer with the MathModelInfo.
     */
    public NonoGramsMathModel(String ID, MMInitializer mmInitializer, int rows, int cols,HashMap<Integer,String> numberColorMap) {
        super(ID, mmInitializer);
        this.rows=rows;
        this.cols=cols;
        this.numberColorMap = numberColorMap;
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
    public HashMap<String,ArrayList<Integer[]>> getResult(Object... resultArgs) {
        ArrayList<ArrayList<Map.Entry<?,Long>>> board = (ArrayList<ArrayList<Map.Entry<?,Long>>>) this.getVariableValues().get("B_(r,c)");
        HashMap<String,ArrayList<Integer[]>> result = new HashMap<>();
        for(int r=0; r<this.rows;r++){
            for(int c=0; c<this.cols;c++){
                String color = this.numberColorMap.get(board.get(r).get(c).getValue().intValue());
                if(color==null)continue;
                result.putIfAbsent(color,new ArrayList<>());
                result.get(color).add(new Integer[]{r,c});
            }
        }
        return result;
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
        this.getSolveParameters().setMaxTimeInSeconds(600);
        this.getSolveParameters().setLogSearchProgress(true);
        this.getSolveParameters().setLogToStdout(true);
    }

}
