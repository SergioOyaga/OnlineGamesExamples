package org.soyaga.examples.LinkedInSudoku.MathModel;

import com.google.ortools.linearsolver.MPConstraintProto;
import com.google.ortools.linearsolver.MPVariableProto;
import org.soyaga.Initializer.MMInitializer;

import java.util.ArrayList;
import java.util.HashMap;

public class SudokuMMInitializer extends MMInitializer {
    private final Integer[][] boardNumbers;
    private final int rows;
    private final int cols;
    private final int rowGroup;
    private final int colGroup;

    /**
     * Constructor
     * @param boardNumbers Integer[][] board.
     */
    public SudokuMMInitializer(Integer[][] boardNumbers, int rows, int cols, int rowGroup, int colGroup) {
        this.boardNumbers = boardNumbers;
        this.rows = rows;
        this.cols = cols;
        this.rowGroup = rowGroup;
        this.colGroup = colGroup;
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
        ArrayList<ArrayList<MPVariableProto.Builder >> S = new ArrayList<>(this.rows); //board
        for (int row=0; row<this.rows; row++){
            S.add(row, new ArrayList<>(this.cols));
            for(int col=0; col< this.cols; col++) {
                S.get(row).add(col, MPVariableProto.newBuilder()
                        .setName("S_(" + row + "," + col + ")")
                        .setLowerBound(1)
                        .setUpperBound(this.rows)
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
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.rows); //discriminator
        for (int row=0; row<this.rows; row++){
            D.add(row, new ArrayList<>(this.cols));
            for(int col=0; col< this.cols; col++) {
                D.get(row).add(col, new ArrayList<>(this.cols));
                for(int col2=col+1; col2< this.cols; col2++) {
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
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.cols); //discriminator
        for (int col=0; col<this.cols; col++){
            D.add(col, new ArrayList<>(this.rows));
            for(int row=0; row< this.rows; row++) {
                D.get(col).add(row, new ArrayList<>(this.rows));
                for(int row2=row+1; row2< this.rows; row2++) {
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
        ArrayList<ArrayList<ArrayList<MPVariableProto.Builder>>> D = new ArrayList<>(this.rows); //discriminator
        for (int row = 0; row < this.rows; row++) {
            D.add(row, new ArrayList<>(this.cols));
            for (int col = 0; col < this.cols; col++) {
                D.get(row).add(col, new ArrayList<>(this.rows));
                int cellRowInBox = row % this.rowGroup;
                int cellColInBox = col % this.colGroup;
                for (int cellRow = cellRowInBox; cellRow < this.rowGroup; cellRow++) {
                    int startCol = (cellRow == cellRowInBox) ? cellColInBox + 1 : 0;
                    for (int cellCol = startCol; cellCol < this.colGroup; cellCol++) {
                        int boxIndex = cellRow * this.rowGroup + cellCol;
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
        for (int row=0; row<this.rows; row++){
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentRowCols = new ArrayList<>();
            allDifferentInRows.add(allDifferentRowCols);
            for(int col=0; col<this.cols; col++){
                ArrayList<MPConstraintProto.Builder> allDifferentRowColCols = new ArrayList<>();
                allDifferentRowCols.add(allDifferentRowColCols);
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                for (int col2 = col + 1; col2 < this.cols; col2++) {
                    MPConstraintProto.Builder differentValuesRowColCol2 = MPConstraintProto.newBuilder()
                            .setName("allDifferentInRowColCol_(" + row + "," + col + "," + col2 + ")")
                            .setLowerBound(1)
                            .setUpperBound(this.cols-1);
                    allDifferentRowColCols.add(differentValuesRowColCol2);
                    Integer S_c_r_by_row_2 = getVariableIndex("S_(r,c)",
                            new Integer[]{row, col2},
                            variables,
                            varIndexesByBuilder);
                    Integer D_r_c_c = getVariableIndex("DRCC_(r,c,c)",
                            new Integer[]{row, col, col2-col - 1},
                            variables,
                            varIndexesByBuilder);
                    differentValuesRowColCol2.addVarIndex(S_c_r)
                            .addCoefficient(1.)
                            .addVarIndex(S_c_r_by_row_2)
                            .addCoefficient(-1.)
                            .addVarIndex(D_r_c_c)
                            .addCoefficient(this.cols);
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
        for (int col=0; col<this.cols; col++){
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentColRows = new ArrayList<>();
            allDifferentInCols.add(allDifferentColRows);
            for(int row=0; row<this.rows; row++){
                ArrayList<MPConstraintProto.Builder> allDifferentColRowRows = new ArrayList<>();
                allDifferentColRows.add(allDifferentColRowRows);
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row,col},
                        variables,
                        varIndexesByBuilder);
                for (int row2 = row + 1; row2 < this.rows; row2++) {
                    MPConstraintProto.Builder differentValuesColRowRow = MPConstraintProto.newBuilder()
                            .setName("allDifferentInColRowRow_(" + col + "," + row + "," + row2 + ")")
                            .setLowerBound(1)
                            .setUpperBound(this.rows-1);
                    allDifferentColRowRows.add(differentValuesColRowRow);
                    Integer S_c_r_by_row_2 = getVariableIndex("S_(r,c)",
                            new Integer[]{row2, col},
                            variables,
                            varIndexesByBuilder);
                    Integer D_c_r_r = getVariableIndex("DCRR_(c,r,r)",
                            new Integer[]{col, row, row2-row - 1},
                            variables,
                            varIndexesByBuilder);
                    differentValuesColRowRow.addVarIndex(S_c_r)
                            .addCoefficient(1.)
                            .addVarIndex(S_c_r_by_row_2)
                            .addCoefficient(-1.)
                            .addVarIndex(D_c_r_r)
                            .addCoefficient(this.rows);
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
        for (int row=0; row<this.rows; row++) {
            ArrayList<ArrayList<MPConstraintProto.Builder>> allDifferentRowCols = new ArrayList<>();
            allDifferentInSquares.add(allDifferentRowCols);
            for (int col = 0; col < this.cols; col++) {
                ArrayList<MPConstraintProto.Builder> allDifferentRowColBoxes = new ArrayList<>();
                allDifferentRowCols.add(allDifferentRowColBoxes);
                int boxStartRow = (row / this.rowGroup) * this.rowGroup;
                int boxStartCol = (col / this.colGroup) * this.colGroup;
                int cellRowInBox = row % this.rowGroup;
                int cellColInBox = col % this.colGroup;
                Integer S_c_r = getVariableIndex("S_(r,c)",
                        new Integer[]{row, col},
                        variables,
                        varIndexesByBuilder);
                int i=0;
                for (int cellRow = cellRowInBox; cellRow < this.rowGroup; cellRow++) {
                    int startCol = (cellRow == cellRowInBox) ? cellColInBox + 1 : 0;
                    for (int cellCol = startCol; cellCol < this.colGroup; cellCol++) {
                        int boxIndex = cellRow * this.colGroup + cellCol;
                        int row2 = boxStartRow + cellRow;
                        int col2 = boxStartCol + cellCol;
                        MPConstraintProto.Builder differentValuesColRowBox = MPConstraintProto.newBuilder()
                                .setName("allDifferentInRowColBox_(" + row + "," + col + "," + boxIndex + ")")
                                .setLowerBound(1)
                                .setUpperBound(rows-1);
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
                                .addCoefficient(this.rows);
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
        for(int row = 0; row<rows; row++){
            for(int col=0; col<cols; col++){
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
