package org.soyaga.examples.LinkedInQueens.MathModel;


import com.google.ortools.linearsolver.MPConstraintProto;
import com.google.ortools.linearsolver.MPVariableProto;
import lombok.Setter;
import org.soyaga.Initializer.MMInitializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements MMInitializer and defines the base initializing techniques for a mathematical modeling
 * optimization.
 * It should contain all the information needed to initialize the mathematical problem.
 */
@Setter
public class QueensMMInitializer extends MMInitializer {
    private Color[][] boardColors;

    /**
     * Constructor
     * @param boardColors Color[][] board.
     */
    public QueensMMInitializer(Color[][] boardColors) {
        this.boardColors = boardColors;
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("B_(r,c)", createBoardVar() );
    }

    /**
     * Creates the Board variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBoardVar(){
        ArrayList<ArrayList<MPVariableProto.Builder >> B = new ArrayList<>(this.boardColors.length); //board
        for (int row=0; row<this.boardColors.length; row++){
            B.add(row, new ArrayList<>(this.boardColors[row].length));
            for(int col=0; col< this.boardColors[row].length; col++) {
                B.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("B_(" + row + "," + col + ")")
                        .setLowerBound(0)
                        .setUpperBound(1)
                        .setIsInteger(true));
            }
        }
        return B;
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
        constraints.put("UniqueQueenInRow", createUniqueQueenInRowConstraints(variables, varIndexesByBuilder));
        constraints.put("UniqueQueenInColumn", createUniqueQueenInColumnConstraints(variables, varIndexesByBuilder));
        constraints.put("UniqueQueenInDiagonal", createQueenInDiagonalConstraints(variables, varIndexesByBuilder));
        constraints.put("UniqueQueenInColor", createUniqueQueenInColorConstraints(variables, varIndexesByBuilder));
    }

    /**
     * Constraint that ensures only one queen is present per row.
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createUniqueQueenInRowConstraints(HashMap<String, ArrayList<?>> variables,
                                                              HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<MPConstraintProto.Builder> uniqueQueenInRow = new ArrayList<>(this.boardColors.length);
        for (int row=0; row<this.boardColors.length; row++){
            MPConstraintProto.Builder uniqueQueenInRowCol = MPConstraintProto.newBuilder()
                    .setName("uniqueQueenInRow_("+row+")")
                    .setLowerBound(1)
                    .setUpperBound(1);
            uniqueQueenInRow.add(row,uniqueQueenInRowCol);
            for(int col=0; col<this.boardColors[row].length; col++){
                Integer B_c_r_index = getVariableIndex("B_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);

                uniqueQueenInRowCol.addVarIndex(B_c_r_index)
                        .addCoefficient(1.);
            }
        }
        return uniqueQueenInRow;
    }

    /**
     * Constraint that ensures only one queen is present per column.
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createUniqueQueenInColumnConstraints(HashMap<String, ArrayList<?>> variables,
                                                           HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<MPConstraintProto.Builder> uniqueQueenInColumn = new ArrayList<>(this.boardColors[0].length);
        for(int col=0; col<this.boardColors[0].length; col++) {
            MPConstraintProto.Builder uniqueQueenInColumnColumn = MPConstraintProto.newBuilder()
                    .setName("uniqueQueenInColumn_(" + col + ")")
                    .setLowerBound(1)
                    .setUpperBound(1);
            uniqueQueenInColumn.add(col, uniqueQueenInColumnColumn);
        }
        for (int rowl=0; rowl<this.boardColors.length; rowl++){
            for(int col=0; col<this.boardColors[rowl].length; col++){
                MPConstraintProto.Builder uniqueQueenInColumnColumn = uniqueQueenInColumn.get(col);
                Integer B_c_r_index = getVariableIndex("B_(r,c)",
                        new Integer[]{rowl, col},
                        variables,
                        varIndexesByBuilder);

                uniqueQueenInColumnColumn.addVarIndex(B_c_r_index)
                        .addCoefficient(1.);
            }
        }
        return uniqueQueenInColumn;
    }


    /**
     * Constraint that ensures only one queen is present in the next diagonal.
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createQueenInDiagonalConstraints(HashMap<String, ArrayList<?>> variables,
                                                          HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<MPConstraintProto.Builder> queenInDiagonal = new ArrayList<>();
        for (int row=0; row<this.boardColors.length; row++){
            for(int col=0; col<this.boardColors[row].length; col++){
                MPConstraintProto.Builder queenInDiagonalRowCol= MPConstraintProto.newBuilder()
                        .setName("queenInDiagonal_(" + col + "," + row + ")")
                        .setLowerBound(0)
                        .setUpperBound(1);
                queenInDiagonal.add(queenInDiagonalRowCol);

                Integer B_c_r_index = getVariableIndex("B_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                queenInDiagonalRowCol.addVarIndex(B_c_r_index).addCoefficient(1.);
                if(row< this.boardColors.length-1){
                    if(col > 0) {
                        Integer B_c_plus_r_minus_index = getVariableIndex("B_(r,c)",
                                new Integer[]{row + 1, col-1},
                                variables,
                                varIndexesByBuilder);
                        queenInDiagonalRowCol.addVarIndex(B_c_plus_r_minus_index).addCoefficient(1.);
                    }

                    if(col < this.boardColors[row].length-1) {
                        Integer B_c_plus_r_plus_index = getVariableIndex("B_(r,c)",
                                new Integer[]{row + 1, col+1},
                                variables,
                                varIndexesByBuilder);
                        queenInDiagonalRowCol.addVarIndex(B_c_plus_r_plus_index).addCoefficient(1.);
                    }
                }
            }
        }
        return queenInDiagonal;
    }


    /**
     * Constraint that ensures only one queen is present per color.
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createUniqueQueenInColorConstraints(HashMap<String, ArrayList<?>> variables,
                                                             HashMap<Object, Integer> varIndexesByBuilder) {
        HashMap<Color, MPConstraintProto.Builder> uniqueQueenInColor = new HashMap<>();
        for (int row=0; row<this.boardColors.length; row++){
            for(int col=0; col<this.boardColors[row].length; col++){
                Color currentColor= this.boardColors[row][col];
                MPConstraintProto.Builder uniqueQueenInColorColor = uniqueQueenInColor.computeIfAbsent(currentColor,
                        k-> MPConstraintProto.newBuilder()
                                .setName("uniqueQueenInColor_(" + currentColor.toString() + ")")
                                .setLowerBound(1)
                                .setUpperBound(1));

                Integer B_c_r_index = getVariableIndex("B_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);

                uniqueQueenInColorColor.addVarIndex(B_c_r_index)
                        .addCoefficient(1.);
            }
        }
        return new ArrayList<>(uniqueQueenInColor.values());
    }

}
