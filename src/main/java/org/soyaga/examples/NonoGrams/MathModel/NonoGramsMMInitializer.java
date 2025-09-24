package org.soyaga.examples.NonoGrams.MathModel;

import com.google.ortools.sat.ConstraintProto;
import com.google.ortools.sat.IntegerVariableProto;
import com.google.ortools.sat.LinearConstraintProto;
import lombok.Setter;
import org.soyaga.Initializer.MMInitializer;

import java.util.ArrayList;
import java.util.HashMap;

public class NonoGramsMMInitializer extends MMInitializer {
    private final int rows;
    private final int cols;
    private final HashMap<Integer,HashMap<Integer,Object[]>> rowConstraints;// row, col(left to right) [color,number]
    private final HashMap<Integer,HashMap<Integer,Object[]>> colConstraints; // col, row(down to up) [color,number]
    private final HashMap<String,Integer> colorNumberMap;
    @Setter
    private  NonoGramsMathModel model;

    public NonoGramsMMInitializer(int rows, int cols, HashMap<Integer, HashMap<Integer, Object[]>> rowConstraints,
                                  HashMap<Integer, HashMap<Integer, Object[]>> colConstraints, HashMap<String,Integer> colorNumberMap) {
        this.rows = rows;
        this.cols = cols;
        this.rowConstraints = rowConstraints;
        this.colConstraints = colConstraints;
        this.colorNumberMap = colorNumberMap;
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("RPS_(r,p)", createRowPieceStartVar());
        variables.put("RPE_(r,p)", createRowPieceEndVar());
        variables.put("CPS_(c,p)", createColPieceStartVar());
        variables.put("CPE_(c,p)", createColPieceEndVar());
        variables.put("RPX_(r,p,c)", createRowPieceXVar());
        variables.put("CPX_(c,p,r)", createColPieceXVar());
        variables.put("B_(r,c)", createBoardVar());
    }

    private ArrayList<?> createRowPieceStartVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> RPS = new ArrayList<>();  
        for (int r=0;r<this.rows;r++){
            ArrayList<IntegerVariableProto.Builder> RPS_r = new ArrayList<>();
            RPS.add(RPS_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                RPS_r.add(IntegerVariableProto.newBuilder()
                        .setName("RPS_(" + r+","+p+")")
                        .addDomain(0)
                        .addDomain(this.cols-1)
                );
            }
        }
        return RPS;
    }

    private ArrayList<?> createRowPieceEndVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> RPE = new ArrayList<>();  
        for (int r=0;r<this.rows;r++){
            ArrayList<IntegerVariableProto.Builder> RPE_r = new ArrayList<>();
            RPE.add(RPE_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                RPE_r.add(IntegerVariableProto.newBuilder()
                        .setName("RPE_(" + r+","+p+")")
                        .addDomain(0)
                        .addDomain(this.cols-1)
                );
            }
        }
        return RPE;
    }

    private ArrayList<?> createColPieceStartVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> CPS = new ArrayList<>();  
        for (int c=0;c<this.cols;c++){
            ArrayList<IntegerVariableProto.Builder> CPS_r = new ArrayList<>();
            CPS.add(CPS_r);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                CPS_r.add(IntegerVariableProto.newBuilder()
                        .setName("CPS_(" + c+","+p+")")
                        .addDomain(0)
                        .addDomain(this.rows-1)
                );
            }
        }
        return CPS;
    }

    private ArrayList<?> createColPieceEndVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> CPE = new ArrayList<>();  
        for (int c=0;c<this.cols;c++){
            ArrayList<IntegerVariableProto.Builder> CPE_r = new ArrayList<>();
            CPE.add(CPE_r);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                CPE_r.add(IntegerVariableProto.newBuilder()
                        .setName("CPE_(" + c+","+p+")")
                        .addDomain(0)
                        .addDomain(this.rows-1)
                );
            }
        }
        return CPE;
    }

    private ArrayList<?> createRowPieceXVar(){
        ArrayList<ArrayList<ArrayList<IntegerVariableProto.Builder>>> RPX = new ArrayList<>();  
        for (int r=0;r<this.rows;r++){
            ArrayList<ArrayList<IntegerVariableProto.Builder>> RPX_r = new ArrayList<>();
            RPX.add(RPX_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                ArrayList<IntegerVariableProto.Builder> RPX_r_p = new ArrayList<>();
                RPX_r.add(RPX_r_p);
                for(int c=0; c<this.cols;c++){
                    RPX_r_p.add(IntegerVariableProto.newBuilder()
                            .setName("RPX_(" + r+","+p+","+c+")")
                            .addDomain(0)
                            .addDomain(1)
                    );
                }
            }
        }
        return RPX;
    }

    private ArrayList<?> createColPieceXVar(){
        ArrayList<ArrayList<ArrayList<IntegerVariableProto.Builder>>> CPX = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ArrayList<IntegerVariableProto.Builder>> CPX_c = new ArrayList<>();
            CPX.add(CPX_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                ArrayList<IntegerVariableProto.Builder> CPX_c_p = new ArrayList<>();
                CPX_c.add(CPX_c_p);
                for(int r=0; r<this.rows;r++){
                    CPX_c_p.add(IntegerVariableProto.newBuilder()
                            .setName("CPX_(" + c+","+p+","+r+")")
                            .addDomain(0)
                            .addDomain(1)
                    );
                }
            }
        }
        return CPX;
    }

    private ArrayList<?> createBoardVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> B = new ArrayList<>();
        int colorNumber = this.colorNumberMap.size();
        for (int r=0;r<this.rows;r++){
            ArrayList<IntegerVariableProto.Builder> B_r = new ArrayList<>();
            B.add(B_r);
            for(int c=0; c<this.cols;c++){
                B_r.add(IntegerVariableProto.newBuilder()
                        .setName("B_(" + r+","+c+")")
                        .addDomain(0)
                        .addDomain(colorNumber)
                );
            }

        }
        return B;
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
        constraints.put("RowPieceSize",createRowPieceSizeConstraint());
        constraints.put("ColPieceSize",createColPieceSizeConstraint());
        constraints.put("RowPieceStartXlink",createRowPieceStartXLinkConstraint());
        constraints.put("RowPieceEndXlink",createRowPieceEndXLinkConstraint());
        constraints.put("ColPieceStartXlink",createColPieceStartXLinkConstraint());
        constraints.put("ColPieceEndXlink",createColPieceEndXLinkConstraint());
        constraints.put("RowPieceXSize",createRowPieceXSizeConstraint());
        constraints.put("ColPieceXSize",createColPieceXSizeConstraint());
        constraints.put("RowPieceToBoard",createRowPieceToBoardConstraint());
        constraints.put("ColPieceToBoard",createColPieceToBoardConstraint());
        constraints.put("RowPieceOrder",createRowPieceOrderConstraint());
        constraints.put("ColPieceOrder",createColPieceOrderConstraint());
    }

    private ArrayList<?> createRowPieceSizeConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceSize = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ConstraintProto.Builder> rowPieceSize_r = new ArrayList<>();
            rowPieceSize.add(rowPieceSize_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                Integer pieceSize = (Integer) rowPieces.get(p)[1];
                Integer RPE_r_p = this.model.getVariableIndex("RPE_(r,p)",new Integer[]{r,p});
                Integer RPS_r_p = this.model.getVariableIndex("RPS_(r,p)",new Integer[]{r,p});
                LinearConstraintProto.Builder linearSize = LinearConstraintProto.newBuilder()
                        .addDomain(pieceSize-1)
                        .addDomain(pieceSize-1)
                        .addVars(RPE_r_p)
                        .addCoeffs(1)
                        .addVars(RPS_r_p)
                        .addCoeffs(-1);
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("rowPieceSize_("+r+","+p+")")
                        .setLinear(linearSize);
                rowPieceSize_r.add(pieceSizeConstraint);
            }
        }
        return rowPieceSize;
    }

    private ArrayList<?> createColPieceSizeConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> colPieceSize = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ConstraintProto.Builder> colPieceSize_c = new ArrayList<>();
            colPieceSize.add(colPieceSize_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                Integer pieceSize = (Integer) colPieces.get(p)[1];
                Integer CPE_c_p = this.model.getVariableIndex("CPE_(c,p)",new Integer[]{c,p});
                Integer CPS_c_p = this.model.getVariableIndex("CPS_(c,p)",new Integer[]{c,p});
                LinearConstraintProto.Builder linearSize = LinearConstraintProto.newBuilder()
                        .addDomain(pieceSize-1)
                        .addDomain(pieceSize-1)
                        .addVars(CPE_c_p)
                        .addCoeffs(1)
                        .addVars(CPS_c_p)
                        .addCoeffs(-1);
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("colPieceSize_("+c+","+p+")")
                        .setLinear(linearSize);
                colPieceSize_c.add(pieceSizeConstraint);
            }
        }
        return colPieceSize;
    }

    private ArrayList<?> createRowPieceStartXLinkConstraint() {
        ArrayList<ArrayList<ArrayList<ConstraintProto.Builder>>> rowPieceStartXLink = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceStartXLink_r = new ArrayList<>();
            rowPieceStartXLink.add(rowPieceStartXLink_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                ArrayList<ConstraintProto.Builder> rowPieceStartXLink_r_p = new ArrayList<>();
                rowPieceStartXLink_r.add(rowPieceStartXLink_r_p);
                Integer RPS_r_p = this.model.getVariableIndex("RPS_(r,p)",new Integer[]{r,p});
                for(int c=0;c<this.cols; c++){
                    Integer RPX_r_p_c = this.model.getVariableIndex("RPX_(r,p,c)",new Integer[]{r,p,c});
                    LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                            .addDomain(0)
                            .addDomain(c+this.cols)
                            .addVars(RPS_r_p)
                            .addCoeffs(1)
                            .addVars(RPX_r_p_c)
                            .addCoeffs(this.cols);
                    ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                            .setName("rowPieceStartXLink_("+r+","+p+","+c+")")
                            .setLinear(linearRelation);
                    rowPieceStartXLink_r_p.add(pieceSizeConstraint);
                }
            }
        }
        return rowPieceStartXLink;
    }

    private ArrayList<?> createRowPieceEndXLinkConstraint() {
        ArrayList<ArrayList<ArrayList<ConstraintProto.Builder>>> rowPieceEndXLink = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceEndXLink_r = new ArrayList<>();
            rowPieceEndXLink.add(rowPieceEndXLink_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                ArrayList<ConstraintProto.Builder> rowPieceEndXLink_r_p = new ArrayList<>();
                rowPieceEndXLink_r.add(rowPieceEndXLink_r_p);
                Integer RPE_r_p = this.model.getVariableIndex("RPE_(r,p)",new Integer[]{r,p});
                for(int c=0;c<this.cols; c++){
                    Integer RPX_r_p_c = this.model.getVariableIndex("RPX_(r,p,c)",new Integer[]{r,p,c});
                    LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                            .addDomain(c-this.cols)
                            .addDomain(this.cols)
                            .addVars(RPE_r_p)
                            .addCoeffs(1)
                            .addVars(RPX_r_p_c)
                            .addCoeffs(-this.cols);
                    ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                            .setName("rowPieceEndXLink_("+r+","+p+","+c+")")
                            .setLinear(linearRelation);
                    rowPieceEndXLink_r_p.add(pieceSizeConstraint);
                }
            }
        }
        return rowPieceEndXLink;
    }

    private ArrayList<?> createColPieceStartXLinkConstraint() {
        ArrayList<ArrayList<ArrayList<ConstraintProto.Builder>>> colPieceStartXLink = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ArrayList<ConstraintProto.Builder>> colPieceStartXLink_c = new ArrayList<>();
            colPieceStartXLink.add(colPieceStartXLink_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                ArrayList<ConstraintProto.Builder> colPieceStartXLink_c_p = new ArrayList<>();
                colPieceStartXLink_c.add(colPieceStartXLink_c_p);
                Integer CPS_c_p = this.model.getVariableIndex("CPS_(c,p)",new Integer[]{c,p});
                for(int r=0;r<this.rows; r++){
                    Integer CPX_c_p_r = this.model.getVariableIndex("CPX_(c,p,r)",new Integer[]{c,p,r});
                    LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                            .addDomain(0)
                            .addDomain(r+this.rows)
                            .addVars(CPS_c_p)
                            .addCoeffs(1)
                            .addVars(CPX_c_p_r)
                            .addCoeffs(this.rows);
                    ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                            .setName("colPieceStartXLink_("+c+","+p+","+r+")")
                            .setLinear(linearRelation);
                    colPieceStartXLink_c_p.add(pieceSizeConstraint);
                }
            }
        }
        return colPieceStartXLink;
    }


    private ArrayList<?> createColPieceEndXLinkConstraint() {
        ArrayList<ArrayList<ArrayList<ConstraintProto.Builder>>> colPieceEndXLink = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ArrayList<ConstraintProto.Builder>> colPieceEndXLink_c = new ArrayList<>();
            colPieceEndXLink.add(colPieceEndXLink_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                ArrayList<ConstraintProto.Builder> colPieceEndXLink_c_p = new ArrayList<>();
                colPieceEndXLink_c.add(colPieceEndXLink_c_p);
                Integer CPE_c_p = this.model.getVariableIndex("CPE_(c,p)",new Integer[]{c,p});
                for(int r=0;r<this.rows; r++){
                    Integer CPX_c_p_r = this.model.getVariableIndex("CPX_(c,p,r)",new Integer[]{c,p,r});
                    LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                            .addDomain(r-this.rows)
                            .addDomain(this.rows)
                            .addVars(CPE_c_p)
                            .addCoeffs(1)
                            .addVars(CPX_c_p_r)
                            .addCoeffs(-this.rows);
                    ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                            .setName("colPieceEndXLink_("+c+","+p+","+r+")")
                            .setLinear(linearRelation);
                    colPieceEndXLink_c_p.add(pieceSizeConstraint);
                }
            }
        }
        return colPieceEndXLink;
    }

    private ArrayList<?> createRowPieceXSizeConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceXSize = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ConstraintProto.Builder> rowPieceXSize_r = new ArrayList<>();
            rowPieceXSize.add(rowPieceXSize_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size();p++){
                Integer pieceSize = (Integer) rowPieces.get(p)[1];
                LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                        .addDomain(pieceSize)
                        .addDomain(pieceSize);
                for(int c=0;c<this.cols; c++){
                    Integer RPX_r_p_c = this.model.getVariableIndex("RPX_(r,p,c)",new Integer[]{r,p,c});
                    linearRelation.addVars(RPX_r_p_c)
                            .addCoeffs(1);
                }
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("rowPieceXSize_("+r+","+p+")")
                        .setLinear(linearRelation);
                rowPieceXSize_r.add(pieceSizeConstraint);
            }
        }
        return rowPieceXSize;
    }

    private ArrayList<?> createColPieceXSizeConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> colPieceXSize = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ConstraintProto.Builder> colPieceXSize_c = new ArrayList<>();
            colPieceXSize.add(colPieceXSize_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size();p++){
                Integer pieceSize = (Integer) colPieces.get(p)[1];
                LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                        .addDomain(pieceSize)
                        .addDomain(pieceSize);
                for(int r=0;r<this.rows; r++){
                    Integer CPX_c_p_r = this.model.getVariableIndex("CPX_(c,p,r)",new Integer[]{c,p,r});
                    linearRelation.addVars(CPX_c_p_r)
                            .addCoeffs(1);
                }
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("colPieceXSize_("+c+","+p+")")
                        .setLinear(linearRelation);
                colPieceXSize_c.add(pieceSizeConstraint);
            }
        }
        return colPieceXSize;
    }

    private ArrayList<?> createRowPieceToBoardConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceToBoard = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ConstraintProto.Builder> rowPieceToBoard_r = new ArrayList<>();
            rowPieceToBoard.add(rowPieceToBoard_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int c=0;c<this.cols; c++){
                LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                        .addDomain(0)
                        .addDomain(0);
                Integer B_r_c = this.model.getVariableIndex("B_(r,c)",new Integer[]{r,c});
                linearRelation.addVars(B_r_c).addCoeffs(1);
                for(int p=0;p<rowPieces.size();p++){
                    Integer pieceColor = this.colorNumberMap.get((String) rowPieces.get(p)[0]);
                    Integer RPX_r_p_c = this.model.getVariableIndex("RPX_(r,p,c)",new Integer[]{r,p,c});
                    linearRelation.addVars(RPX_r_p_c).addCoeffs(-pieceColor);
                }
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("rowPieceToBoard_("+r+","+c+")")
                        .setLinear(linearRelation);
                rowPieceToBoard_r.add(pieceSizeConstraint);
            }
        }
        return rowPieceToBoard;
    }

    private ArrayList<?> createColPieceToBoardConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> colPieceToBoard = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ConstraintProto.Builder> colPieceToBoard_c = new ArrayList<>();
            colPieceToBoard.add(colPieceToBoard_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int r=0;r<this.rows; r++){
                LinearConstraintProto.Builder linearRelation = LinearConstraintProto.newBuilder()
                        .addDomain(0)
                        .addDomain(0);
                Integer B_r_c = this.model.getVariableIndex("B_(r,c)",new Integer[]{r,c});
                linearRelation.addVars(B_r_c).addCoeffs(1);
                for(int p=0;p<colPieces.size();p++){
                    Integer pieceColor = this.colorNumberMap.get((String) colPieces.get(p)[0]);
                    Integer CPX_c_p_r = this.model.getVariableIndex("CPX_(c,p,r)",new Integer[]{c,p,r});
                    linearRelation.addVars(CPX_c_p_r).addCoeffs(-pieceColor);
                }
                ConstraintProto.Builder pieceSizeConstraint = ConstraintProto.newBuilder()
                        .setName("colPieceToBoard_("+r+","+c+")")
                        .setLinear(linearRelation);
                colPieceToBoard_c.add(pieceSizeConstraint);
            }
        }
        return colPieceToBoard;
    }

    private ArrayList<?> createRowPieceOrderConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> rowPieceOrder = new ArrayList<>();
        for (int r=0;r<this.rows;r++){
            ArrayList<ConstraintProto.Builder> rowPieceOrder_r = new ArrayList<>();
            rowPieceOrder.add(rowPieceOrder_r);
            HashMap<Integer,Object[]> rowPieces = this.rowConstraints.get(r);
            for(int p=0;p<rowPieces.size()-1;p++){
                int colorP1 = this.colorNumberMap.get((String) rowPieces.get(p)[0]);
                int colorP2 = this.colorNumberMap.get((String) rowPieces.get(p+1)[0]);
                int space = colorP1==colorP2?2:1;
                Integer RPE_r_p1 = this.model.getVariableIndex("RPE_(r,p)",new Integer[]{r,p});
                Integer RPS_r_p2 = this.model.getVariableIndex("RPS_(r,p)",new Integer[]{r,p+1});
                LinearConstraintProto.Builder linearOrder = LinearConstraintProto.newBuilder()
                        .addDomain(space)
                        .addDomain(this.cols)
                        .addVars(RPS_r_p2)
                        .addCoeffs(1)
                        .addVars(RPE_r_p1)
                        .addCoeffs(-1);
                ConstraintProto.Builder pieceOrderConstraint = ConstraintProto.newBuilder()
                        .setName("rowPieceOrder_("+r+","+p+")")
                        .setLinear(linearOrder);
                rowPieceOrder_r.add(pieceOrderConstraint);
            }
        }
        return rowPieceOrder;
    }

    private ArrayList<?> createColPieceOrderConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> colPieceOrder = new ArrayList<>();
        for (int c=0;c<this.cols;c++){
            ArrayList<ConstraintProto.Builder> colPieceOrder_c = new ArrayList<>();
            colPieceOrder.add(colPieceOrder_c);
            HashMap<Integer,Object[]> colPieces = this.colConstraints.get(c);
            for(int p=0;p<colPieces.size()-1;p++){
                int colorP1 = this.colorNumberMap.get((String) colPieces.get(p)[0]);
                int colorP2 = this.colorNumberMap.get((String) colPieces.get(p+1)[0]);
                int space = colorP1==colorP2?2:1;
                Integer CPE_c_p1 = this.model.getVariableIndex("CPE_(c,p)",new Integer[]{c,p});
                Integer CPS_c_p2 = this.model.getVariableIndex("CPS_(c,p)",new Integer[]{c,p+1});
                LinearConstraintProto.Builder linearOrder = LinearConstraintProto.newBuilder()
                        .addDomain(space)
                        .addDomain(this.rows)
                        .addVars(CPS_c_p2)
                        .addCoeffs(1)
                        .addVars(CPE_c_p1)
                        .addCoeffs(-1);
                ConstraintProto.Builder pieceOrderConstraint = ConstraintProto.newBuilder()
                        .setName("colPieceOrder_("+c+","+p+")")
                        .setLinear(linearOrder);
                colPieceOrder_c.add(pieceOrderConstraint);
            }
        }
        return colPieceOrder;
    }
}
