package org.soyaga.examples.AllOut.MathModel;

import com.google.ortools.linearsolver.MPModelRequest;
import com.google.ortools.linearsolver.MPVariableProtoOrBuilder;
import lombok.Getter;
import org.soyaga.examples.LinkedInQueens.MathModel.QueensMMInitializer;
import org.soyaga.mm.mathematicalModelAlgorithm.MathematicalProgrammingModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Class that extends MathematicalProgrammingModel defines the problem objective function and the solver parameters.
 */
@Getter
public class AllOutMathModel extends MathematicalProgrammingModel implements Callable<Object> {

    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param imageGrid Boolean[][] with the current grid state.
     * @param rows Integer with the row number.
     * @param cols Integer with the col number
     */
    public AllOutMathModel(String ID, Boolean[][] imageGrid, Integer rows, Integer cols) {
        super(ID, new AllOutMMInitializer(imageGrid, rows, cols));
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
        ArrayList<ArrayList<Boolean>> result = new ArrayList<>();
        int row=0;
        for(ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>> rowResult:
                (ArrayList<ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>>>)this.getVariableValues().get("BA_(r,c)")){
            int col = 0;
            ArrayList<Boolean> rowResultBool = new ArrayList<>();
            result.add(rowResultBool);
             for(Map.Entry<MPVariableProtoOrBuilder, Double> variableEntry:rowResult){
                 if(variableEntry.getValue()==1.)rowResultBool.add(true);
                 else rowResultBool.add(false);
                 col++;
             }
             row++;
        }
        solution[0] = "MM_Optimal";
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
    public Object call() {
        try {
            this.optimize();
            return this.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}