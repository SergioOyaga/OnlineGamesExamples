package org.soyaga.examples.LinkedInPatches.MathModel;

import org.soyaga.mm.mathematicalModelAlgorithm.ConstraintProgrammingSatisfiabilityModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that extends ConstraintProgrammingSatisfiabilityModel defines the problem objective, solver parameters,
 * SolutionCallBacks and Log for the solver callbacks.
 */
public class LinkedInPatchesMathModel extends ConstraintProgrammingSatisfiabilityModel {
    private final LinkedHashMap<Color, Integer[]> pointsByColor;
    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param mmInitializer MMInitializer with the MathModelInfo.
     */
    public LinkedInPatchesMathModel(String ID, LinkedInPatchesMMInitializer mmInitializer, LinkedHashMap<Color, Integer[]> pointsByColor) {
        super(ID, mmInitializer);
        this.pointsByColor = pointsByColor;
        mmInitializer.setModel(this);
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
    public Object[] getResult(Object... resultArgs) {
        Object[] solution = new Object[2];
        ArrayList<ArrayList<Map.Entry<?,Long>>> ClRS = (ArrayList<ArrayList<Map.Entry<?,Long>>>) this.getVariableValues().get("ClRS_(cl,r)");
        ArrayList<ArrayList<Map.Entry<?,Long>>> ClRE = (ArrayList<ArrayList<Map.Entry<?,Long>>>) this.getVariableValues().get("ClRE_(cl,r)");
        ArrayList<ArrayList<Map.Entry<?,Long>>> ClCS = (ArrayList<ArrayList<Map.Entry<?,Long>>>) this.getVariableValues().get("ClCS_(cl,c)");
        ArrayList<ArrayList<Map.Entry<?,Long>>> ClCE = (ArrayList<ArrayList<Map.Entry<?,Long>>>) this.getVariableValues().get("ClCE_(cl,c)");
        HashMap<Color,ArrayList<Integer[]>> result = new HashMap<>();
        int colorIndex=0;
        for(Color color: this.pointsByColor.keySet()){
            result.putIfAbsent(color,new ArrayList<>());
            ArrayList<Map.Entry<?,Long>> ClRS_cl= ClRS.get(colorIndex);
            ArrayList<Map.Entry<?,Long>> ClRE_cl= ClRE.get(colorIndex);
            ArrayList<Map.Entry<?,Long>> ClCS_cl= ClCS.get(colorIndex);
            ArrayList<Map.Entry<?,Long>> ClCE_cl= ClCE.get(colorIndex);
            int rStart = findFirstIndex(ClRS_cl);
            int rEnd   = findFirstIndex(ClRE_cl);
            int cStart = findFirstIndex(ClCS_cl);
            int cEnd   = findFirstIndex(ClCE_cl);

            Integer [] start = new Integer[]{rStart,cStart};
            Integer [] end = new Integer[]{rEnd,cEnd};
            result.get(color).add(start);
            result.get(color).add(end);
            colorIndex++;
        }
        solution[0] = "CP_Optimal";
        solution[1] = result;
        return solution;
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

    private int findFirstIndex(ArrayList<Map.Entry<?,Long>> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() == 1) {
                return i;
            }
        }
        return 0;
    }
}
