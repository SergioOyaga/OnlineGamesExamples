package org.soyaga.examples.Bridges.MathModel.CallBack;

import com.google.ortools.sat.CpObjectiveProto;
import lombok.Getter;
import lombok.Setter;
import org.soyaga.examples.Bridges.Graph.Bridge;
import org.soyaga.examples.Bridges.Graph.Rule;
import org.soyaga.mm.satisfiabilityCallBack.SolutionCallBack;

import java.util.*;

/**
 * Class that extends SolutionCallBack and retrieve N solutions found by the SAT solver. This means that the solver
 * might not find the optimal solution when an objective function is passed to the solver.
 */
public class ConnectedSolutionCallBack extends SolutionCallBack {
    private final ArrayList<Bridge> problemBridges;
    private final ArrayList<Rule> problemRules;

    /**
     * HashMap with the variables by ID.
     * Ej.:
     * <ul>
     *     <li> "TS_t" --> ArrayList{@literal <MPVariableProto.Builder>}: with the start times variables.</li>
     *     <li> "TE_t" --> ArrayList{@literal <MPVariableProto.Builder>}: with the end times variables.</li>
     * </ul>
     *
     */
    @Getter @Setter
    private HashMap<String, ArrayList<?>> variables;
    /**
     * HashMap with the variable index by variable builder.
     * Ej.:
     * <ul>
     *     <li>MPVariableProto.Builder --> Integer</li>
     * </ul>
     */
    @Getter @Setter
    private HashMap<Object, Integer> varIndexesByBuilder;
    /**
     * ArrayList with the solutions found by the solver. In the array we find an Entry with the hashmap of the solution
     * as key and the objective value (if exists) as value.
     */
    @Getter @Setter
    private ArrayList<AbstractMap.SimpleEntry<HashMap<String, ArrayList<?>>, Double>> solutionsCallBacks;
    /**
     * CpObjectiveProto.Builder with the objective function in the case that de CP problem requires one.
     */
    @Getter @Setter
    private CpObjectiveProto.Builder objectiveProto;
    @Getter
    private HashMap<Bridge,Integer> resultMap;

    /**
     * Constructor
     */
    public ConnectedSolutionCallBack(ArrayList<Bridge> problemBridges,ArrayList<Rule> problemRules) {
        this.problemBridges = problemBridges;
        this.problemRules = problemRules;
    }

    /**
     * Callback method to override. It will be called at each new solution.
     */
    @Override
    public void onSolutionCallback() {
        HashMap<Bridge,Integer> resultMap = new HashMap<>();
        HashMap<String,ArrayList<?>> solution = this.retrieveSolution();
        ArrayList<Map.Entry<?,Long>> bn = (ArrayList<Map.Entry<?,Long>>) solution.get("BN_(b)");
        int index = 0;
        for(Bridge bridge:this.problemBridges){
            resultMap.put(bridge,bn.get(index).getValue().intValue());
            index++;
        }
        int numberOfLoops = this.countLoops(resultMap);
        this.solutionsCallBacks.add(
                new AbstractMap.SimpleEntry<>(
                        this.retrieveSolution(),
                        numberOfLoops*1.
                )
        );

        if(numberOfLoops == 1) {
            stopSearch();
            this.resultMap = resultMap;
            System.out.println("SolutionFound");
        }
    }

    private int countLoops(HashMap<Bridge, Integer> resultMap) {
        HashSet<Rule> visited = new HashSet<>();
        int loops = 0;
        for (Rule rule : this.problemRules) {
            if (!visited.contains(rule)) {
                dfs(rule, resultMap, visited);
                loops++;
            }
        }
        return loops;
    }
    private static void dfs(Rule current, Map<Bridge, Integer> resultMap, Set<Rule> visited) {
        visited.add(current);
        for (Bridge bridge : current.bridges) {
            Integer value = resultMap.get(bridge);
            if (value != null && value > 0) {
                Rule neighbor = getOtherRule(bridge, current);
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, resultMap, visited);
                }
            }
        }
    }

    private static Rule getOtherRule(Bridge bridge, Rule current) {
        if (bridge.ruleFrom().equals(current)) {
            return bridge.ruleTo();
        } else {
            return bridge.ruleFrom();
        }
    }
}
