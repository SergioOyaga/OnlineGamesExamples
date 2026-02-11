package org.soyaga.examples.Bridges.MathModel;

import org.soyaga.examples.Bridges.Graph.Bridge;
import org.soyaga.examples.Bridges.Graph.Rule;
import org.soyaga.examples.Bridges.MathModel.CallBack.ConnectedSolutionCallBack;
import org.soyaga.mm.mathematicalModelAlgorithm.ConstraintProgrammingSatisfiabilityModel;

import java.util.*;

/**
 * Class that extends ConstraintProgrammingSatisfiabilityModel defines the problem objective, solver parameters,
 * SolutionCallBacks and Log for the solver callbacks.
 */
public class BridgesMathModel extends ConstraintProgrammingSatisfiabilityModel {
    private final ArrayList<Bridge> problemBridges;

    /**
     * Constructor.
     *
     * @param ID           String with the ID of the model.
     * @param crossingRule
     */
    public BridgesMathModel(String ID, ArrayList<Rule> problemRules, HashMap<Bridge, HashSet<Bridge>> crossingRule, ArrayList<Bridge> problemBridges) {
        super(ID, new BridgesMMInitializer(problemRules, problemBridges,crossingRule));
        ((BridgesMMInitializer)this.getMMInitializer()).setModel(this);
        this.setSolutionCallback(new ConnectedSolutionCallBack(problemBridges,problemRules));
        this.problemBridges = problemBridges;
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
    public String getResult(Object... resultArgs) {
        return "CP_Optimal";
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
        this.getSolveParameters().setMaxTimeInSeconds(120);
        this.getSolveParameters().setLogSearchProgress(true);
        this.getSolveParameters().setLogToStdout(true);
        this.getSolveParameters().setEnumerateAllSolutions(true);
    }

}
