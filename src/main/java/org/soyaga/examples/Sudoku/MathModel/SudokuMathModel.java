package org.soyaga.examples.Sudoku.MathModel;

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
public class SudokuMathModel extends MathematicalProgrammingModel implements Runnable{

    /**
     * Constructor.
     *
     * @param ID            String with the ID of the model.
     * @param boardNumbers Integer[][] board.
     */
    public SudokuMathModel(String ID, Integer[][] boardNumbers) {
        super(ID, new SudokuMMInitializer(boardNumbers));
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
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for(ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>> rowVariableValues:
                (ArrayList<ArrayList<Map.Entry<MPVariableProtoOrBuilder, Double>>>)this.getVariableValues().get("S_(r,c)")){
            ArrayList<Integer> sudokuRow = new ArrayList<>();
            for(Map.Entry<MPVariableProtoOrBuilder, Double> variableEntry:rowVariableValues){
                sudokuRow.add(variableEntry.getValue().intValue());
            }
            result.add(sudokuRow);
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
