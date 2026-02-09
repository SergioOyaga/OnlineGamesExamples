package org.soyaga.examples.Minesweeper.MathModel;

import com.google.ortools.sat.ConstraintProto;
import com.google.ortools.sat.IntegerVariableProto;
import com.google.ortools.sat.LinearConstraintProto;
import lombok.Setter;
import org.soyaga.Initializer.MMInitializer;
import org.soyaga.examples.Minesweeper.Graph.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MinesweeperMMInitializer extends MMInitializer {
    private final ArrayList<Map.Entry<Integer, HashSet<Cell>>> constraints; // val, [Cell]
    private final ArrayList<Cell> cells;
    @Setter
    private MinesweeperMathModel model;

    public MinesweeperMMInitializer(ArrayList<Map.Entry<Integer, HashSet<Cell>>> constraints, ArrayList<Cell> cells) {
        this.constraints = constraints;
        this.cells = cells;
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("CD_(c)", createCellDecisionVar());
    }

    private ArrayList<?> createCellDecisionVar(){
        ArrayList<IntegerVariableProto.Builder> CD = new ArrayList<>();
        for (Cell cell:this.cells){
            CD.add(IntegerVariableProto.newBuilder()
                    .setName("CD_("+cell.row()+","+cell.coll()+")")
                    .addDomain(0)
                    .addDomain(1)
            );
        }
        return CD;
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
        constraints.put("Value",createValueConstraint());
    }

    private ArrayList<?> createValueConstraint() {
        ArrayList<ConstraintProto.Builder> values = new ArrayList<>();
        for (Map.Entry<Integer, HashSet<Cell>> ruleEntry: this.constraints) {
            Integer value = ruleEntry.getKey();
            HashSet<Cell> cellsInRule = ruleEntry.getValue();
            LinearConstraintProto.Builder rule = LinearConstraintProto.newBuilder()
                    .addDomain(value)
                    .addDomain(value);
            int cellIndex = 0;
            for (Cell cell : this.cells) {
                if (cellsInRule.contains(cell)) {
                    Integer CD = this.model.getVariableIndex("CD_(c)", new Integer[]{cellIndex});
                    rule.addVars(CD).addCoeffs(1);
                }
                cellIndex++;
            }
            ConstraintProto.Builder valueConstraint = ConstraintProto.newBuilder()
                    .setName("Value_(" + value + ")")
                    .setLinear(rule);
            values.add(valueConstraint);
        }
        return values;
    }
}
