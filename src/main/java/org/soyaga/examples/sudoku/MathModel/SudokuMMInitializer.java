package org.soyaga.examples.sudoku.MathModel;

import com.google.ortools.linearsolver.MPConstraintProto;
import com.google.ortools.linearsolver.MPVariableProto;
import org.soyaga.Initializer.MMInitializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SudokuMMInitializer extends MMInitializer {
    private final Integer[][] boardNumbers;

    /**
     * Constructor
     * @param boardNumbers Integer[][] board.
     */
    public SudokuMMInitializer(Integer[][] boardNumbers) {
        this.boardNumbers = boardNumbers;
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("S_(r,c)", createBoardVar());
        variables.put("DRCC_(r,c,c)", createByColDiscriminatorVar());
        variables.put("DCRR_(c,r,r)", createByRowDiscriminatorVar());
        variables.put("DRCS_(r,c,s)", createBySquareDiscriminatorVar());
    }


    /**
     * Creates the Board variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBoardVar(){
        ArrayList<ArrayList<MPVariableProto.Builder >> S = new ArrayList<>(this.boardNumbers.length); //board
        for (int row=0; row<this.boardNumbers.length; row++){
            S.add(row, new ArrayList<>(this.boardNumbers[row].length));
            for(int col=0; col< this.boardNumbers[row].length; col++) {
                S.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("S_(" + row + "," + col + ")")
                        .setLowerBound(1)
                        .setUpperBound(9)
                        .setIsInteger(true));
            }
        }
        return S;
    }

    /**
     * Creates the row difference discriminator variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createByColDiscriminatorVar() {
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.boardNumbers.length); //discriminator
        for (int row=0; row<this.boardNumbers.length; row++){
            D.add(row, new ArrayList<>(this.boardNumbers.length));
            for(int col=0; col< this.boardNumbers.length; col++) {
                D.get(row).add(col, new ArrayList<>(this.boardNumbers.length));
                for(int col2=col+1; col2< this.boardNumbers.length; col2++) {
                    D.get(row).get(col).add(MPVariableProto.newBuilder()
                            .setName("DRCC_(" + row + "," + col+ "," + col2 + ")")
                            .setLowerBound(0)
                            .setUpperBound(1)
                            .setIsInteger(true));
                }
            }
        }
        return D;
    }

    /**
     * Creates the col difference discriminator variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createByRowDiscriminatorVar() {
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.boardNumbers.length); //discriminator
        for (int col=0; col<this.boardNumbers.length; col++){
            D.add(col, new ArrayList<>(this.boardNumbers.length));
            for(int row=0; row< this.boardNumbers.length; row++) {
                D.get(col).add(row, new ArrayList<>(this.boardNumbers.length));
                for(int row2=row+1; row2< this.boardNumbers.length; row2++) {
                    D.get(col).get(row).add(MPVariableProto.newBuilder()
                            .setName("DCRR_(" + col+ "," + row  + "," + row2 + ")")
                            .setLowerBound(0)
                            .setUpperBound(1)
                            .setIsInteger(true));
                }
            }
        }
        return D;
    }

    /**
     * Creates the square difference discriminator variables.
     * @return ArrayList with the dimensions and the values of the variable.
     */
    private ArrayList<?> createBySquareDiscriminatorVar() {
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.boardNumbers.length); //discriminator
        for (int row = 0; row < this.boardNumbers.length; row++) {
            D.add(row, new ArrayList<>(this.boardNumbers.length));
            for (int col = 0; col < this.boardNumbers.length; col++) {
                D.get(row).add(col, new ArrayList<>(this.boardNumbers.length));
                int cellRowInBox = row % 3;
                int cellColInBox = col % 3;
                for (int cellRow = cellRowInBox; cellRow < 3; cellRow++) {
                    int startCol = (cellRow == cellRowInBox) ? cellColInBox + 1 : 0;
                    for (int cellCol = startCol; cellCol < 3; cellCol++) {
                        int boxIndex = cellRow * 3 + cellCol;
                        D.get(row).get(col).add(
                                MPVariableProto.newBuilder()
                                        .setName("DRCS_(" + row + "," + col + "," + boxIndex + ")")
                                        .setLowerBound(0)
                                        .setUpperBound(1)
                                        .setIsInteger(true)
                        );
                    }
                }
            }
        }
        return D;
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
        constraints.put("AllDifferentInRow", createAllDifferentInRowConstraints(variables, varIndexesByBuilder));
        constraints.put("AllDifferentInColum", createAllDifferentInColConstraints(variables, varIndexesByBuilder));
        constraints.put("AllDifferentInSquare", createAllDifferentInSquareConstraints(variables, varIndexesByBuilder));
        constraints.put("ForceAssignations", forceAssignationsConstraints(variables, varIndexesByBuilder));
    }


    /**
     * Constraints that ensures no two values are the same in the same row.
     *
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createAllDifferentInRowConstraints(HashMap<String, ArrayList<?>> variables,
                                                           HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<ArrayList<ArrayList<MPConstraintProto.Builder>>> allDifferentInRows = new ArrayList<>();
        for (int row=0; row<this.boardNumbers.length; row++){
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentRowCols = new ArrayList<>();
            allDifferentInRows.add(allDifferentRowCols);
            for(int col=0; col<this.boardNumbers.length; col++){
                ArrayList<MPConstraintProto.Builder> allDifferentRowColCols = new ArrayList<>();
                allDifferentRowCols.add(allDifferentRowColCols);
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                for (int col2 = col + 1; col2 < this.boardNumbers.length; col2++) {
                    MPConstraintProto.Builder differentValuesRowColCol2 = MPConstraintProto.newBuilder()
                            .setName("allDifferentInRowColCol_(" + row + "," + col + "," + col2 + ")")
                            .setLowerBound(1)
                            .setUpperBound(8);
                    allDifferentRowColCols.add(differentValuesRowColCol2);
                    Integer S_c_r_by_row_2 = getVariableIndex("S_(r,c)",
                            new Integer[]{row, col2},
                            variables,
                            varIndexesByBuilder);
                    Integer D_c_r_c = getVariableIndex("DRCC_(r,c,c)",
                            new Integer[]{row, col, col2-col - 1},
                            variables,
                            varIndexesByBuilder);
                    differentValuesRowColCol2.addVarIndex(S_c_r)
                            .addCoefficient(1.)
                            .addVarIndex(S_c_r_by_row_2)
                            .addCoefficient(-1.)
                            .addVarIndex(D_c_r_c)
                            .addCoefficient(+9.);
                }
            }
        }
        return allDifferentInRows;
    }

    /**
     * Constraints that ensures no two values are the same in the same column.
     *
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createAllDifferentInColConstraints(HashMap<String, ArrayList<?>> variables,
                                                            HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<ArrayList<ArrayList<MPConstraintProto.Builder>>> allDifferentInCols = new ArrayList<>();
        for (int col=0; col<this.boardNumbers.length; col++){
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentColRows = new ArrayList<>();
            allDifferentInCols.add(allDifferentColRows);
            for(int row=0; row<this.boardNumbers.length; row++){
                ArrayList<MPConstraintProto.Builder> allDifferentColRowRows = new ArrayList<>();
                allDifferentColRows.add(allDifferentColRowRows);
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row,col},
                        variables,
                        varIndexesByBuilder);
                for (int row2 = row + 1; row2 < this.boardNumbers.length; row2++) {
                    MPConstraintProto.Builder differentValuesColRowRow = MPConstraintProto.newBuilder()
                            .setName("allDifferentInColRowRow_(" + col + "," + row + "," + row2 + ")")
                            .setLowerBound(1)
                            .setUpperBound(8);
                    allDifferentColRowRows.add(differentValuesColRowRow);
                    Integer S_c_r_by_row_2 = getVariableIndex("S_(r,c)",
                            new Integer[]{row2, col},
                            variables,
                            varIndexesByBuilder);
                    Integer D_c_r_c = getVariableIndex("DCRR_(c,r,r)",
                            new Integer[]{col, row, row2-row - 1},
                            variables,
                            varIndexesByBuilder);
                    differentValuesColRowRow.addVarIndex(S_c_r)
                            .addCoefficient(1.)
                            .addVarIndex(S_c_r_by_row_2)
                            .addCoefficient(-1.)
                            .addVarIndex(D_c_r_c)
                            .addCoefficient(+9.);
                }
            }
        }
        return  allDifferentInCols;
    }

    /**
     * Constraints that ensures no two values are the same in the same square.
     *
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> createAllDifferentInSquareConstraints(HashMap<String, ArrayList<?>> variables,
                                                            HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<ArrayList<ArrayList<MPConstraintProto.Builder>>> allDifferentInSquares = new ArrayList<>();
        for (int row=0; row<this.boardNumbers.length; row++) {
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentRowCols = new ArrayList<>();
            allDifferentInSquares.add(allDifferentRowCols);
            for (int col = 0; col < this.boardNumbers.length; col++) {
                ArrayList<MPConstraintProto.Builder> allDifferentRowColBoxes = new ArrayList<>();
                allDifferentRowCols.add(allDifferentRowColBoxes);
                int boxStartRow = (row / 3) * 3;
                int boxStartCol = (col / 3) * 3;
                int cellRowInBox = row % 3;
                int cellColInBox = col % 3;
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                int i=0;
                for (int cellRow = cellRowInBox; cellRow < 3; cellRow++) {
                    int startCol = (cellRow == cellRowInBox) ? cellColInBox + 1 : 0;
                    for (int cellCol = startCol; cellCol < 3; cellCol++) {
                        int boxIndex = cellRow * 3 + cellCol;
                        int row2 = boxStartRow + cellRow;
                        int col2 = boxStartCol + cellCol;
                        MPConstraintProto.Builder differentValuesColRowBox = MPConstraintProto.newBuilder()
                                .setName("allDifferentInRowColBox_(" + row + "," + col + "," + boxIndex + ")")
                                .setLowerBound(1)
                                .setUpperBound(8);
                        allDifferentRowColBoxes.add(differentValuesColRowBox);
                        Integer S_c_r_box = getVariableIndex("S_(r,c)",
                                new Integer[]{row2, col2},
                                variables,
                                varIndexesByBuilder);
                        Integer D_r_c_s = getVariableIndex("DRCS_(r,c,s)",
                                new Integer[]{row, col, i},
                                variables,
                                varIndexesByBuilder);
                        differentValuesColRowBox.addVarIndex(S_c_r)
                                .addCoefficient(1.)
                                .addVarIndex(S_c_r_box)
                                .addCoefficient(-1.)
                                .addVarIndex(D_r_c_s)
                                .addCoefficient(+9.);
                        i++;
                    }
                }
            }
        }
        return  allDifferentInSquares;
    }

    /**
     * Constraints that ensures reference values in the problem are met.
     *
     * @param variables HashMap with the variables
     * @param varIndexesByBuilder HashMap with the index by variable.
     * @return ArrayList with the constraints.
     */
    private ArrayList<?> forceAssignationsConstraints(HashMap<String, ArrayList<?>> variables, HashMap<Object, Integer> varIndexesByBuilder) {
        ArrayList<MPConstraintProto.Builder> assignations = new ArrayList<>();
        for(int row = 0; row<this.boardNumbers.length; row++){
            for(int col=0; col<this.boardNumbers[row].length; col++){
                if(this.boardNumbers[row][col] != null){
                    MPConstraintProto.Builder forceAssignationRowCol = MPConstraintProto.newBuilder()
                            .setName("forceAssignation(" + row + "," + col + ")")
                            .setLowerBound(this.boardNumbers[row][col])
                            .setUpperBound(this.boardNumbers[row][col]);
                    assignations.add(forceAssignationRowCol);Integer S_c_r = getVariableIndex("S_(r,c)",
                            new Integer[]{row, col},
                            variables,
                            varIndexesByBuilder);
                    forceAssignationRowCol.addVarIndex(S_c_r)
                            .addCoefficient(1.);
                }
            }
        }
        return assignations;
    }
}
