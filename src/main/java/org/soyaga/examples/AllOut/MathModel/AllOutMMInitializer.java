package org.soyaga.examples.AllOut.MathModel;


import com.google.ortools.linearsolver.MPConstraintProto;
import com.google.ortools.linearsolver.MPVariableProto;
import lombok.AllArgsConstructor;
import org.soyaga.Initializer.MMInitializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements MMInitializer and defines the base initializing techniques for a mathematical modeling
 * optimization.
 * It should contain all the information needed to initialize the mathematical problem.
 */
@AllArgsConstructor
public class AllOutMMInitializer extends MMInitializer {
    Boolean[][] imageGrid;
    Integer rows;
    Integer cols;

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("BA_(r,c)", createBoardActionVar());
        variables.put("BS_(r,c)", createBoardStateVar());
        variables.put("Aux_(r,c)", createBoardStateAuxVar());
    }

    /**
     * Creates the Board Action variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBoardActionVar(){
        ArrayList<ArrayList<MPVariableProto.Builder >> BA = new ArrayList<>(this.rows); //board
        for (int row=0; row<this.rows; row++){
            BA.add(row, new ArrayList<>(this.cols));
            for(int col=0; col< this.cols; col++) {
                BA.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("BA_(" + row + "," + col + ")")
                        .setLowerBound(0)
                        .setUpperBound(1)
                        .setIsInteger(true));
            }
        }
        return BA;
    }

    /**
     * Creates the Board State variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBoardStateVar(){
        ArrayList<ArrayList<MPVariableProto.Builder >> BA = new ArrayList<>(this.rows); //board
        for (int row=0; row<this.rows; row++){
            BA.add(row, new ArrayList<>(this.cols));
            for(int col=0; col< this.cols; col++) {
                BA.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("BS_(" + row + "," + col + ")")
                        .setLowerBound(0)
                        .setUpperBound(10)
                        .setIsInteger(true));
            }
        }
        return BA;
    }

    /**
     * Creates the Board State Aux variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBoardStateAuxVar(){
        ArrayList<ArrayList<MPVariableProto.Builder >> BA = new ArrayList<>(this.rows); //board
        for (int row=0; row<this.rows; row++){
            BA.add(row, new ArrayList<>(this.cols));
            for(int col=0; col< this.cols; col++) {
                BA.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("Aux_(" + row + "," + col + ")")
                        .setLowerBound(0)
                        .setUpperBound(5)
                        .setIsInteger(true));
            }
        }
        return BA;
    }


    /**
     * Function that computes the indexes of the variable from the indexes in the variable space and the indexing
     * created for the model space.
     * @param name String with the name of the variable in the variables' dict.
     * @param indexes Array with the indexes in the variable space.
     * @param variables HashMap with the arrays of variables by variable name.
     * @param indexByVariable HashMap with the variable and the index in the model space.
     * @return Integer with the index of the variable in the model space.
     */
    private Integer getVariableIndex(String name, Integer[] indexes,HashMap<String, ArrayList<?>> variables,
                                     HashMap<Object ,Integer> indexByVariable ){
        ArrayList<?> var = variables.get(name);
        for(int i = 0; i<indexes.length-1; i++){
            var = (ArrayList<?>) var.get(indexes[i]);
        }
        return indexByVariable.get(var.get(indexes[indexes.length-1]));
    }

    /**
     * Function that creates and adds all the constraints.
     * @param constraints HashMap{@literal <String, ArrayList<?>>} where to store the constraints.
     * @param variables           HashMap{@literal <String, ArrayList<?>>} with the variables.
     * @param varIndexesByBuilder HashMap{@literal <MPVariableProto.Builder, Integer>} with the variables by index.
     * @param mmVarArgs           VarArgs to create the constraints.
     */
    @Override
    public void createConstraints(HashMap<String, ArrayList<?>> constraints,HashMap<String, ArrayList<?>> variables, HashMap<Object, Integer> varIndexesByBuilder, Object[] mmVarArgs) {
        constraints.put("ForceEvenState", createEvenState(variables, varIndexesByBuilder));
        constraints.put("RelateActionWithState", createActionStateConstraints(variables, varIndexesByBuilder));
    }

    /**
     * Constraint that ensures the state is even. (all are off)
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createEvenState(HashMap<String, ArrayList<?>> variables,
                                                              HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<ArrayList<MPConstraintProto.Builder>> even = new ArrayList<>(this.rows);
        for (int row=0; row<rows; row++){
            ArrayList<MPConstraintProto.Builder> evenRow = new ArrayList<>(this.cols);
            even.add(evenRow);
            for(int col=0; col<cols; col++){
                MPConstraintProto.Builder evenRowCol = MPConstraintProto.newBuilder()
                        .setName("uniqueQueenInRow_("+row+","+col+")")
                        .setLowerBound(0)
                        .setUpperBound(0);
                evenRow.add(evenRowCol);
                Integer BS_r_c_index = getVariableIndex("BS_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                evenRowCol.addVarIndex(BS_r_c_index)
                        .addCoefficient(1.);
                Integer Aux_r_c_index = getVariableIndex("Aux_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                evenRowCol.addVarIndex(Aux_r_c_index)
                        .addCoefficient(-2.);
            }
        }
        return even;
    }

    /**
     * Constraint that ensures the board behavior.
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createActionStateConstraints(HashMap<String, ArrayList<?>> variables,
                                                           HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<ArrayList<MPConstraintProto.Builder>> action = new ArrayList<>(rows);
        for (int row=0; row<rows; row++) {
            ArrayList<MPConstraintProto.Builder> actionRow = new ArrayList<>(this.cols);
            action.add(actionRow);
            for (int col = 0; col < cols; col++) {
                int bound=0;
                if(this.imageGrid[row][col]) bound++;
                MPConstraintProto.Builder actionRowCol = MPConstraintProto.newBuilder()
                        .setName("actionRowCol(" + row + "," + col + ")")
                        .setLowerBound(bound)
                        .setUpperBound(bound);
                actionRow.add(actionRowCol);
                Integer BS_r_c_index = getVariableIndex("BS_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                actionRowCol.addVarIndex(BS_r_c_index)
                        .addCoefficient(-1.);
                Integer BA_r_c = getVariableIndex("BA_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                actionRowCol.addVarIndex(BA_r_c)
                        .addCoefficient(1.);
                if(row>0){
                    Integer BA_rmin_c = getVariableIndex("BA_(r,c)",
                            new Integer[]{row-1, col},
                            variables,
                            varIndexesByBuilder);
                    actionRowCol.addVarIndex(BA_rmin_c)
                            .addCoefficient(1.);
                }
                if(col>0){
                    Integer BA_r_cmin = getVariableIndex("BA_(r,c)",
                            new Integer[]{row, col-1},
                            variables,
                            varIndexesByBuilder);
                    actionRowCol.addVarIndex(BA_r_cmin)
                            .addCoefficient(1.);
                }
                if(row<rows-1){
                    Integer BA_rmax_c = getVariableIndex("BA_(r,c)",
                            new Integer[]{row+1, col},
                            variables,
                            varIndexesByBuilder);
                    actionRowCol.addVarIndex(BA_rmax_c)
                            .addCoefficient(1.);
                }
                if(col<cols-1){
                    Integer BA_r_cmax = getVariableIndex("BA_(r,c)",
                            new Integer[]{row, col+1},
                            variables,
                            varIndexesByBuilder);
                    actionRowCol.addVarIndex(BA_r_cmax)
                            .addCoefficient(1.);

                }
            }
        }
        return action;
    }
}
