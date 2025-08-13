package org.soyaga.examples.LinkedInZip.MathModel;

import com.google.ortools.linearsolver.MPModelRequest;
import com.google.ortools.linearsolver.MPVariableProtoOrBuilder;
import lombok.Getter;
import org.soyaga.mm.mathematicalModelAlgorithm.MathematicalProgrammingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Class that extends MathematicalProgrammingModel defines the problem objective function and the solver parameters.
 */
@Getter
public class ZipMathModel extends MathematicalProgrammingModel implements Callable<Object> {


    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param rowSize Integer
     * @param colSize Integer
     * @param gridNumbers Integer[][]
     * @param northConnection Boolean[][]
     * @param eastConnection Boolean[][]
     * @param southConnection Boolean[][]
     * @param westConnection Boolean[][]
     */
    public ZipMathModel(String ID, Integer rowSize, Integer colSize, Integer[][] gridNumbers, Boolean[][] northConnection,
                        Boolean[][] eastConnection, Boolean[][] southConnection, Boolean[][] westConnection) {
        super(ID, new ZipMMInitializer(rowSize, colSize, gridNumbers, northConnection, eastConnection, southConnection, westConnection));
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
        HashMap<Integer, Integer[]> result = new HashMap<>();
        int row = 0;
        for(ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>> rowVariableValues:
                (ArrayList<ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>>>)this.getVariableValues().get("GCV_(r,c)")){
            int col = 0;
             for(Map.Entry<MPVariableProtoOrBuilder, Double> variableEntry:rowVariableValues){
                 result.put(Math.toIntExact(Math.round(variableEntry.getValue())), new Integer[]{row,col});
                 col++;
             }
             row++;
        }
        return new Object[]{"MM", result};
    }

    /**
     * Function that initializes the MPModelRequest.Builder.
     */
    @Override
    public void initializeModelRequest() {
        this.getProtoModelRequest()
                .setEnableInternalSolverOutput(true)
                .setSolverTimeLimitSeconds(60)
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
    public Object call() {
        try {
            this.optimize();
            return this.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}