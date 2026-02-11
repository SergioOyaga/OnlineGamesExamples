package org.soyaga.examples.Bridges.MathModel;

import com.google.ortools.sat.ConstraintProto;
import com.google.ortools.sat.IntegerVariableProto;
import com.google.ortools.sat.LinearConstraintProto;
import lombok.Setter;
import org.soyaga.Initializer.MMInitializer;
import org.soyaga.examples.Bridges.Graph.Bridge;
import org.soyaga.examples.Bridges.Graph.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BridgesMMInitializer extends MMInitializer {
    private final ArrayList<Rule> constraints; // [Rule]
    private final HashMap<Bridge, HashSet<Bridge>> crossingConstraint;
    private final ArrayList<Bridge> problemBridges;
    @Setter
    private BridgesMathModel model;

    public BridgesMMInitializer(ArrayList<Rule> problemRules, ArrayList<Bridge> problemBridges, HashMap<Bridge, HashSet<Bridge>> crossingRule) {
        this.constraints = problemRules;
        this.problemBridges = problemBridges;
        this.crossingConstraint = crossingRule;
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("BN_(b)", createBridgeNumberVar());
        variables.put("BU_(b)", createBridgeUtilizedVar());
    }

    private ArrayList<?> createBridgeNumberVar(){
        ArrayList<IntegerVariableProto.Builder> BN = new ArrayList<>();
        for (Bridge bridge:this.problemBridges){
            BN.add(IntegerVariableProto.newBuilder()
                    .setName("BN_("+bridge.id()+")")
                    .addDomain(0)
                    .addDomain(2)
            );
        }
        return BN;
    }
    private ArrayList<?> createBridgeUtilizedVar(){
        ArrayList<IntegerVariableProto.Builder> BU = new ArrayList<>();
        for (Bridge bridge:this.problemBridges){
            BU.add(IntegerVariableProto.newBuilder()
                    .setName("BU_("+bridge.id()+")")
                    .addDomain(0)
                    .addDomain(1)
            );
        }
        return BU;
    }
    
    /**
     * Function that creates and adds all the Constraints.
     *
     * @param constraints         HashMap{@literal <String, ArrayList<?>>} to Stores the Constraints
     * @param variables           HashMap{@literal <String, ArrayList<?>>} with the variables .
     * @param varIndexesByBuilder HashMap{@literal <MPVariableProto.Builder, Integer>} with the variables by index.
     * @param mmVarArgs           VarArgs to create the Constraints.
     */
    @Override
    public void createConstraints(HashMap<String, ArrayList<?>> constraints, HashMap<String, ArrayList<?>> variables,
                                  HashMap<Object, Integer> varIndexesByBuilder, Object[] mmVarArgs) {
        constraints.put("RelateBNandBU", createRelationConstraint());
        constraints.put("Value", createValueConstraint());
        constraints.put("Crossing", createCrossingConstraint());
    }

    private ArrayList<?> createRelationConstraint() {
        ArrayList<ConstraintProto.Builder> relations = new ArrayList<>();
        int bridgeIndex =0;
        for(Bridge bridge:this.problemBridges){
            LinearConstraintProto.Builder relationBuilder = LinearConstraintProto.newBuilder()
                    .addDomain(0)
                    .addDomain(1);

            Integer BU = this.model.getVariableIndex("BU_(b)", new Integer[]{bridgeIndex});
            relationBuilder.addVars(BU).addCoeffs(2);
            Integer BN = this.model.getVariableIndex("BN_(b)", new Integer[]{bridgeIndex});
            relationBuilder.addVars(BN).addCoeffs(-1);

            ConstraintProto.Builder valueConstraint = ConstraintProto.newBuilder()
                    .setName("relation_(" + bridgeIndex + ")")
                    .setLinear(relationBuilder);
            relations.add(valueConstraint);
            bridgeIndex++;
        }
        return relations;
    }

    private ArrayList<?> createValueConstraint() {
        ArrayList<ConstraintProto.Builder> values = new ArrayList<>();
        for (Rule rule: this.constraints) {
            int value = rule.value;
            HashSet<Bridge> cellsInRule = rule.bridges;
            LinearConstraintProto.Builder ruleBuilder = LinearConstraintProto.newBuilder()
                    .addDomain(value)
                    .addDomain(value);
            int bridgeIndex = 0;
            for (Bridge bridge : this.problemBridges) {
                if (cellsInRule.contains(bridge)) {
                    Integer BN = this.model.getVariableIndex("BN_(b)", new Integer[]{bridgeIndex});
                    ruleBuilder.addVars(BN).addCoeffs(1);
                }
                bridgeIndex++;
            }
            ConstraintProto.Builder valueConstraint = ConstraintProto.newBuilder()
                    .setName("Rule_(" + value + ")")
                    .setLinear(ruleBuilder);
            values.add(valueConstraint);
        }
        return values;
    }

    private ArrayList<?> createCrossingConstraint() {
        ArrayList<ConstraintProto.Builder> cross = new ArrayList<>();
        int verticalIndex = 0;
        for(Bridge verticalBridge:this.problemBridges){
            Integer BU_vertical = this.model.getVariableIndex("BU_(b)", new Integer[]{verticalIndex});
            HashSet<Bridge> horizontalBridges = crossingConstraint.getOrDefault(verticalBridge, null);
            if(horizontalBridges != null){
                int horizontalIndex = 0;
                for(Bridge horizontalBridge:this.problemBridges){
                    if(horizontalBridges.contains(horizontalBridge)){
                        LinearConstraintProto.Builder crossBuilder = LinearConstraintProto.newBuilder()
                                .addDomain(0)
                                .addDomain(1);

                        Integer BU_horizontal = this.model.getVariableIndex("BU_(b)", new Integer[]{horizontalIndex});
                        crossBuilder.addVars(BU_vertical).addCoeffs(1);
                        crossBuilder.addVars(BU_horizontal).addCoeffs(1);

                        ConstraintProto.Builder crossConstraint = ConstraintProto.newBuilder()
                                .setName("cross_(" + verticalBridge.id() + "," + horizontalBridge.id() + ")")
                                .setLinear(crossBuilder);
                        cross.add(crossConstraint);
                    }
                    horizontalIndex++;
                }
            }
            verticalIndex++;
        }
        return cross;
    }
}
