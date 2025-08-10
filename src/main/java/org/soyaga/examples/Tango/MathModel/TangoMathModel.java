package org.soyaga.examples.Tango.MathModel;

import com.google.ortools.linearsolver.MPModelRequest;
import com.google.ortools.linearsolver.MPVariableProtoOrBuilder;
import lombok.Getter;
import org.soyaga.mm.mathematicalModelAlgorithm.MathematicalProgrammingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that extends MathematicalProgrammingModel defines the problem objective function and the solver parameters.
 */
@Getter
public class TangoMathModel extends MathematicalProgrammingModel implements Runnable{
    /**
     * Constructor.
     * @param ID String with the MM id.
     * @param rowSize Integer that contains the number of rows.
     * @param colSize Integer that contains the number of columns.
     * @param gridTypes String[][] with the types "s", "m", "".
     * @param northTypes String[][] with the north border types "x", "=", "".
     * @param eastTypes String[][] with the east border types "x", "=", "".
     * @param southTypes String[][] with the south border types "x", "=", "".
     * @param westTypes String[][] with the west border types "x", "=", "".
     */
    public TangoMathModel(String ID, Integer rowSize, Integer colSize, String[][] gridTypes, String[][] northTypes,
                          String[][] eastTypes, String[][] southTypes, String[][] westTypes) {
        super(ID, new TangoMMInitializer(rowSize, colSize, gridTypes, northTypes, eastTypes, southTypes, westTypes));
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
        Object[] solution = new Object[2];
        String optimizationText = this.getResponse().getStatus()
                + "\nbest objective value:" + this.getResponse().getObjectiveValue()
                + "\nbest objective bound:" + this.getResponse().getBestObjectiveBound()
                + "\n" + this.getResponse().getSolveInfo();
        TangoMMInitializer initializer =  (TangoMMInitializer)this.getMMInitializer();
        Boolean[][] result = new Boolean[initializer.getRowSize()][initializer.getColSize()];
        int row = 0;
        for(ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>> rowVariableValues:
                (ArrayList<ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>>>)this.getVariableValues().get("GCT_(r,c)")){
            int col = 0;
             for(Map.Entry<MPVariableProtoOrBuilder, Double> variableEntry:rowVariableValues){
                 result[row][col]= variableEntry.getValue() >= 0.5;
                 col++;
             }
             row++;
        }
        solution[0] = optimizationText;
        solution[1] = result;
        return solution;
    }

    /**
     * Function that initializes the MPModelRequest.Builder.
     */
    @Override
    public void initializeModelRequest() {
        this.getProtoModelRequest()
                .setEnableInternalSolverOutput(true)
                .setSolverTimeLimitSeconds(600)
                .setSolverType(MPModelRequest.SolverType.SCIP_MIXED_INTEGER_PROGRAMMING);
    }

    /**
     * Function that sets the objective function of the proto model. For linear Objectives in MP problems, we set the
     * objective coefficient directly in the variable Builder, for quadratic objective functions we have to set the indexes
     * in the model builder proto.
     */
    @Override
    public void setObjectiveFunction(Object[] mmVarArgs) {
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            this.optimize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}