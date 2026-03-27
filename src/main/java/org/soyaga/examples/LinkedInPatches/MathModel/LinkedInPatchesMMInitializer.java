package org.soyaga.examples.LinkedInPatches.MathModel;

import com.google.ortools.sat.ConstraintProto;
import com.google.ortools.sat.IntegerVariableProto;
import com.google.ortools.sat.LinearConstraintProto;
import lombok.Setter;
import org.soyaga.Initializer.MMInitializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class LinkedInPatchesMMInitializer extends MMInitializer {
    private final int rows;
    private final int cols;
    private final ArrayList<Color> colors;
    private final LinkedHashMap<Color,Integer> numberByColor;
    private final LinkedHashMap<Color, Integer[]> pointByColor;
    private final HashSet<Color> squareShapes;
    private final HashSet<Color> verticalRectShapes;
    private final HashSet<Color> horizontalRectShapes;
    private final HashSet<Color> unknownShapes;

    @Setter
    private LinkedInPatchesMathModel model;

    public LinkedInPatchesMMInitializer(int rows, int cols, LinkedHashMap<Color, Integer[]> pointByColor,
                                        LinkedHashMap<Color, Integer> numberByColor, HashSet<Color> squareShapes,
                                         HashSet<Color> verticalRectShapes, HashSet<Color> horizontalRectShapes,
                                        HashSet<Color> unknownShapes) {
        this.rows = rows;
        this.cols = cols;
        this.pointByColor = pointByColor;
        this.numberByColor = numberByColor;
        this.squareShapes = squareShapes;
        this.verticalRectShapes = verticalRectShapes;
        this.horizontalRectShapes = horizontalRectShapes;
        this.unknownShapes = unknownShapes;
        this.colors = new ArrayList<>(pointByColor.keySet());
    }

    /**
     * Function that creates and adds all the variables.
     *
     * @param variables HashMap{@literal <String, ArrayList<?>>} where to store the variables.
     * @param mmVarArgs VarArgs to create the variables.
     */
    @Override
    public void createVariables(HashMap<String, ArrayList<?>> variables, Object[] mmVarArgs) {
        variables.put("ClB_(cl,r,c)", createColorBoardVar());
        variables.put("ClRS_(cl,r)", createColorRowStartVar());
        variables.put("ClRE_(cl,r)", createColorRowEndVar());
        variables.put("ClCS_(cl,c)", createColorColStartVar());
        variables.put("ClCE_(cl,c)", createColorColEndVar());
        variables.put("ClH_(cl)", createColorHeightVar());
        variables.put("ClW_(cl)", createColorWidthVar());
    }

    private ArrayList<?> createColorBoardVar(){
        ArrayList<ArrayList<ArrayList<IntegerVariableProto.Builder>>> ClB = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<ArrayList<IntegerVariableProto.Builder>> ClB_cl = new ArrayList<>();
            ClB.add(ClB_cl);
            for (int r = 0; r < this.rows; r++) {
                ArrayList<IntegerVariableProto.Builder> ClB_cl_r = new ArrayList<>();
                ClB_cl.add(ClB_cl_r);
                for (int c = 0; c < this.cols; c++) {
                    ClB_cl_r.add(IntegerVariableProto.newBuilder()
                            .setName("ClB_(" + clIndex + "," + r + "," + c + ")")
                            .addDomain(0)
                            .addDomain(1)
                    );
                }
            }
            clIndex++;
        }
        return ClB;
    }

    private ArrayList<?> createColorRowStartVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> ClRS = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<IntegerVariableProto.Builder> ClRS_cl = new ArrayList<>();
            ClRS.add(ClRS_cl);
            for (int r = 0; r < this.rows; r++) {
                ClRS_cl.add(IntegerVariableProto.newBuilder()
                            .setName("ClRS_(" + clIndex + "," + r + ")")
                            .addDomain(0)
                            .addDomain(1)
                    );
            }
            clIndex++;
        }
        return ClRS;
    }

    private ArrayList<?> createColorRowEndVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> ClRE = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<IntegerVariableProto.Builder> ClRE_cl = new ArrayList<>();
            ClRE.add(ClRE_cl);
            for (int r = 0; r < this.rows; r++) {
                ClRE_cl.add(IntegerVariableProto.newBuilder()
                        .setName("ClRE_(" + clIndex + "," + r + ")")
                        .addDomain(0)
                        .addDomain(1)
                );
            }
            clIndex++;
        }
        return ClRE;
    }

    private ArrayList<?> createColorColStartVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> ClCS = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<IntegerVariableProto.Builder> ClCS_cl = new ArrayList<>();
            ClCS.add(ClCS_cl);
            for (int c = 0; c < this.cols; c++) {
                ClCS_cl.add(IntegerVariableProto.newBuilder()
                        .setName("ClCS_(" + clIndex + "," + c + ")")
                        .addDomain(0)
                        .addDomain(1)
                );
            }
            clIndex++;
        }
        return ClCS;
    }

    private ArrayList<?> createColorColEndVar(){
        ArrayList<ArrayList<IntegerVariableProto.Builder>> ClCE = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<IntegerVariableProto.Builder> ClCE_cl = new ArrayList<>();
            ClCE.add(ClCE_cl);
            for (int c = 0; c < this.cols; c++) {
                ClCE_cl.add(IntegerVariableProto.newBuilder()
                        .setName("ClCE_(" + clIndex + "," + c + ")")
                        .addDomain(0)
                        .addDomain(1)
                );
            }
            clIndex++;
        }
        return ClCE;
    }

    private ArrayList<?> createColorHeightVar(){
        ArrayList<IntegerVariableProto.Builder> ClH = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ClH.add(IntegerVariableProto.newBuilder()
                    .setName("ClH_(" + clIndex +")")
                    .addDomain(0)
                    .addDomain(this.rows-1)
            );
            clIndex++;
        }
        return ClH;
    }

    private ArrayList<?> createColorWidthVar(){
        ArrayList<IntegerVariableProto.Builder> ClW = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ClW.add(IntegerVariableProto.newBuilder()
                    .setName("ClH_(" + clIndex +")")
                    .addDomain(0)
                    .addDomain(this.cols-1)
            );
            clIndex++;
        }
        return ClW;
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
        constraints.put("ColorHintPos", createColorHintPosConstraint());
        constraints.put("BoardUniqueColor", createBoardUniqueColorConstraint());
        constraints.put("ColorSize", createColorSizeConstraint());
        constraints.put("ColorRowStartUnique", createColorRowStartUniqueConstraint());
        constraints.put("ColorRowEndUnique", createColorRowEndUniqueConstraint());
        constraints.put("RowStartEndPrecedence", createRowStartEndPrecedenceConstraint());
        constraints.put("ColorColStartLimit", createColorColStartUniqueConstraint());
        constraints.put("ColorColEndLimit", createColorColEndUniqueConstraint());
        constraints.put("CowStartEndPrecedence", createColStartEndPrecedenceConstraint());
        constraints.put("ColorBoardRangeRelation", createColorBoardRangeRelationConstraint());
        constraints.put("ColorHeightComputation", createColorHeightComputationConstraint());
        constraints.put("ColorWidthComputation", createColorWidthComputationConstraint());
        constraints.put("ColorSquareRule", createColorSquareRuleConstraint());
        constraints.put("VerticalRectangleRule", createColorVerticalRectangleRuleConstraint());
        constraints.put("HorizontalRectangleRule", createColorHorizontalRectangleRuleConstraint());

    }

    private ArrayList<?> createColorHintPosConstraint() {
        ArrayList<ConstraintProto.Builder> colorHintPos = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            Integer colorRowHint = this.pointByColor.get(color)[0];
            Integer colorColHint = this.pointByColor.get(color)[1];
            Integer ClB_cl_r_c = this.model.getVariableIndex("ClB_(cl,r,c)",new Integer[]{clIndex,colorRowHint,colorColHint});

            LinearConstraintProto.Builder colorHintPos_cl = LinearConstraintProto.newBuilder()
                    .addDomain(1)
                    .addDomain(1)
                    .addVars(ClB_cl_r_c)
                    .addCoeffs(1);
            ConstraintProto.Builder colorHintPosConstraint = ConstraintProto.newBuilder()
                    .setName("colorHintPos_("+clIndex+")")
                    .setLinear(colorHintPos_cl);
            colorHintPos.add(colorHintPosConstraint);
            clIndex++;
        }
        return colorHintPos;
    }

    private ArrayList<?> createBoardUniqueColorConstraint() {
        ArrayList<ArrayList<ConstraintProto.Builder>> boardUniqueColor = new ArrayList<>();
        for(int r=0; r< this.rows; r++){
            ArrayList<ConstraintProto.Builder> boardUniqueColor_r = new ArrayList<>();
            boardUniqueColor.add(boardUniqueColor_r);
            for(int c=0; c<this.cols; c++){
                LinearConstraintProto.Builder boardUniqueColor_r_c = LinearConstraintProto.newBuilder()
                        .addDomain(1)
                        .addDomain(1);
                int clIndex = 0;
                for (Color color:this.colors) {
                    Integer ClB_cl_r_c = this.model.getVariableIndex("ClB_(cl,r,c)",new Integer[]{clIndex,r,c});
                    boardUniqueColor_r_c.addVars(ClB_cl_r_c).addCoeffs(1);
                    clIndex++;
                }
                ConstraintProto.Builder boardUniqueColorConstraint = ConstraintProto.newBuilder()
                        .setName("boardUniqueColor_(" + r + "," + c + ")")
                        .setLinear(boardUniqueColor_r_c);
                boardUniqueColor_r.add(boardUniqueColorConstraint);
            }
        }
        return boardUniqueColor;
    }

    private ArrayList<?> createColorSizeConstraint() {
        ArrayList<ConstraintProto.Builder> colorSize = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            if (this.numberByColor.containsKey(color)) {
                LinearConstraintProto.Builder colorSize_cl = LinearConstraintProto.newBuilder()
                        .addDomain(this.numberByColor.get(color))
                        .addDomain(this.numberByColor.get(color));
                for (int r=0;r<this.rows;r++) {
                    for (int c=0;c<this.cols;c++) {
                        Integer ClB_cl_r_c = this.model.getVariableIndex("ClB_(cl,r,c)",new Integer[]{clIndex, r,c});
                        colorSize_cl.addVars(ClB_cl_r_c)
                                .addCoeffs(1);
                    }
                }
                ConstraintProto.Builder colorSizeConstraint = ConstraintProto.newBuilder()
                        .setName("colorSize_("+clIndex+")")
                        .setLinear(colorSize_cl);
                colorSize.add(colorSizeConstraint);
            }
            clIndex++;
        }
        return colorSize;
    }

    private ArrayList<?> createColorRowStartUniqueConstraint() {
        ArrayList<ConstraintProto.Builder> colorRowStartUnique = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder colorRowStartUnique_cl = LinearConstraintProto.newBuilder()
                    .addDomain(1)
                    .addDomain(1);
            for (int r=0;r<this.rows;r++) {
                Integer ClRS_cl_r = this.model.getVariableIndex("ClRS_(cl,r)",new Integer[]{clIndex, r});
                colorRowStartUnique_cl.addVars(ClRS_cl_r)
                        .addCoeffs(1);
            }
            ConstraintProto.Builder colorRowStartUniqueConstraint = ConstraintProto.newBuilder()
                    .setName("colorRowStartUnique_("+clIndex+")")
                    .setLinear(colorRowStartUnique_cl);
            colorRowStartUnique.add(colorRowStartUniqueConstraint);
            clIndex++;
        }
        return colorRowStartUnique;
    }

    private ArrayList<?> createColorRowEndUniqueConstraint() {
        ArrayList<ConstraintProto.Builder> colorRowEndUnique = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder colorRowEndUnique_cl = LinearConstraintProto.newBuilder()
                    .addDomain(1)
                    .addDomain(1);
            for (int r=0;r<this.rows;r++) {
                Integer ClRE_cl_r = this.model.getVariableIndex("ClRE_(cl,r)",new Integer[]{clIndex, r});
                colorRowEndUnique_cl.addVars(ClRE_cl_r)
                        .addCoeffs(1);
            }
            ConstraintProto.Builder colorRowEndUniqueConstraint = ConstraintProto.newBuilder()
                    .setName("colorRowEndUnique_("+clIndex+")")
                    .setLinear(colorRowEndUnique_cl);
            colorRowEndUnique.add(colorRowEndUniqueConstraint);
            clIndex++;
        }
        return colorRowEndUnique;
    }

    private ArrayList<?> createRowStartEndPrecedenceConstraint() {
        ArrayList<ConstraintProto.Builder> rowStartEndPrecedence = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder rowStartEndPrecedence_cl = LinearConstraintProto.newBuilder()
                    .addDomain(0)
                    .addDomain(this.rows);
            for (int r=0;r<this.rows;r++) {
                Integer ClRE_cl_r = this.model.getVariableIndex("ClRE_(cl,r)",new Integer[]{clIndex, r});
                Integer ClRS_cl_r = this.model.getVariableIndex("ClRS_(cl,r)",new Integer[]{clIndex, r});
                rowStartEndPrecedence_cl
                        .addVars(ClRE_cl_r)
                        .addCoeffs(r)
                        .addVars(ClRS_cl_r)
                        .addCoeffs(-r);
            }
            ConstraintProto.Builder rowStartEndPrecedenceConstraint = ConstraintProto.newBuilder()
                    .setName("rowStartEndPrecedence_("+clIndex+")")
                    .setLinear(rowStartEndPrecedence_cl);
            rowStartEndPrecedence.add(rowStartEndPrecedenceConstraint);
            clIndex++;
        }
        return rowStartEndPrecedence;
    }

    private ArrayList<?> createColorColStartUniqueConstraint() {
        ArrayList<ConstraintProto.Builder> colorColStartUnique = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder colorColStartUnique_cl = LinearConstraintProto.newBuilder()
                    .addDomain(1)
                    .addDomain(1);
            for (int c=0;c<this.cols;c++) {
                Integer ClCS_cl_r = this.model.getVariableIndex("ClCS_(cl,c)",new Integer[]{clIndex, c});
                colorColStartUnique_cl.addVars(ClCS_cl_r)
                        .addCoeffs(1);
            }
            ConstraintProto.Builder colorColStartUniqueConstraint = ConstraintProto.newBuilder()
                    .setName("colorColStartUnique_("+clIndex+")")
                    .setLinear(colorColStartUnique_cl);
            colorColStartUnique.add(colorColStartUniqueConstraint);
            clIndex++;
        }
        return colorColStartUnique;
    }

    private ArrayList<?> createColorColEndUniqueConstraint() {
        ArrayList<ConstraintProto.Builder> colorColEndUnique = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder colorColEndUnique_cl = LinearConstraintProto.newBuilder()
                    .addDomain(1)
                    .addDomain(1);
            for (int c=0;c<this.cols;c++) {
                Integer ClCE_cl_r = this.model.getVariableIndex("ClCE_(cl,c)",new Integer[]{clIndex, c});
                colorColEndUnique_cl.addVars(ClCE_cl_r)
                        .addCoeffs(1);
            }
            ConstraintProto.Builder colorColEndUniqueConstraint = ConstraintProto.newBuilder()
                    .setName("colorColEndUnique_("+clIndex+")")
                    .setLinear(colorColEndUnique_cl);
            colorColEndUnique.add(colorColEndUniqueConstraint);
            clIndex++;
        }
        return colorColEndUnique;
    }

    private ArrayList<?> createColStartEndPrecedenceConstraint() {
        ArrayList<ConstraintProto.Builder> colStartEndPrecedence = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            LinearConstraintProto.Builder colStartEndPrecedence_cl = LinearConstraintProto.newBuilder()
                    .addDomain(0)
                    .addDomain(this.cols);
            for (int c=0;c<this.cols;c++) {
                Integer ClCE_cl_r = this.model.getVariableIndex("ClCE_(cl,c)",new Integer[]{clIndex, c});
                Integer ClCS_cl_r = this.model.getVariableIndex("ClCS_(cl,c)",new Integer[]{clIndex, c});
                colStartEndPrecedence_cl
                        .addVars(ClCE_cl_r)
                        .addCoeffs(c)
                        .addVars(ClCS_cl_r)
                        .addCoeffs(-c);
            }
            ConstraintProto.Builder colStartEndPrecedenceConstraint = ConstraintProto.newBuilder()
                    .setName("colStartEndPrecedence_("+clIndex+")")
                    .setLinear(colStartEndPrecedence_cl);
            colStartEndPrecedence.add(colStartEndPrecedenceConstraint);
            clIndex++;
        }
        return colStartEndPrecedence;
    }

    private ArrayList<?> createColorBoardRangeRelationConstraint() {
        ArrayList<ArrayList<ArrayList<ConstraintProto.Builder>>> colorBoardRangeRelation = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            ArrayList<ArrayList<ConstraintProto.Builder>> colorBoardRangeRelation_cl = new ArrayList<>();
            colorBoardRangeRelation.add(colorBoardRangeRelation_cl);
            for (int r=0;r<this.rows;r++) {
                ArrayList<ConstraintProto.Builder> colorBoardRangeRelation_cl_r = new ArrayList<>();
                colorBoardRangeRelation_cl.add(colorBoardRangeRelation_cl_r);
                for (int c=0;c<this.cols;c++) {
                    LinearConstraintProto.Builder colorBoardRangeRelation_cl_r_c = LinearConstraintProto.newBuilder()
                            .addDomain(0)
                            .addDomain(1);
                    Integer ClB_cl_r_c = this.model.getVariableIndex("ClB_(cl,r,c)",new Integer[]{clIndex, r, c});
                    colorBoardRangeRelation_cl_r_c.addVars(ClB_cl_r_c).addCoeffs(-2);
                    for (int r_i=0;r_i<=r;r_i++) {
                        Integer ClRS_cl_ri = this.model.getVariableIndex("ClRS_(cl,r)",new Integer[]{clIndex, r_i});
                        colorBoardRangeRelation_cl_r_c.addVars(ClRS_cl_ri).addCoeffs(1);
                    }
                    for (int c_i=0;c_i<=c;c_i++) {
                        Integer ClCS_cl_ci = this.model.getVariableIndex("ClCS_(cl,c)",new Integer[]{clIndex, c_i});
                        colorBoardRangeRelation_cl_r_c.addVars(ClCS_cl_ci).addCoeffs(1);
                    }
                    if(r>0){
                        for (int r_i=0;r_i<r;r_i++) {
                            Integer ClRE_cl_ri = this.model.getVariableIndex("ClRE_(cl,r)",new Integer[]{clIndex, r_i});
                            colorBoardRangeRelation_cl_r_c.addVars(ClRE_cl_ri).addCoeffs(-1);
                        }
                    }
                    if(c>0){
                        for (int c_i=0;c_i<c;c_i++) {
                            Integer ClCE_cl_ci = this.model.getVariableIndex("ClCE_(cl,c)",new Integer[]{clIndex, c_i});
                            colorBoardRangeRelation_cl_r_c.addVars(ClCE_cl_ci).addCoeffs(-1);
                        }
                    }
                    ConstraintProto.Builder colorBoardRangeRelationConstraint = ConstraintProto.newBuilder()
                            .setName("colorBoardRangeRelation_("+clIndex + "," + r + "," + c +")")
                            .setLinear(colorBoardRangeRelation_cl_r_c);
                    colorBoardRangeRelation_cl_r.add(colorBoardRangeRelationConstraint);
                }
            }
            clIndex++;
        }
        return colorBoardRangeRelation;
    }

    private ArrayList<?> createColorHeightComputationConstraint() {
        ArrayList<ConstraintProto.Builder> colorHeightComputation = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            Integer ClH_cl = this.model.getVariableIndex("ClH_(cl)",new Integer[]{clIndex});

            LinearConstraintProto.Builder colorHeightComputation_cl = LinearConstraintProto.newBuilder()
                    .addDomain(0)
                    .addDomain(0)
                    .addVars(ClH_cl)
                    .addCoeffs(1);;
            for (int r=0;r<this.rows;r++) {
                Integer ClRS_cl_r = this.model.getVariableIndex("ClRS_(cl,r)",new Integer[]{clIndex, r});
                Integer ClRE_cl_r = this.model.getVariableIndex("ClRE_(cl,r)",new Integer[]{clIndex, r});
                colorHeightComputation_cl
                        .addVars(ClRS_cl_r).addCoeffs(r)
                        .addVars(ClRE_cl_r).addCoeffs(-r);
            }
            ConstraintProto.Builder colorHeightComputationConstraint = ConstraintProto.newBuilder()
                    .setName("colorHeightComputation_("+clIndex+")")
                    .setLinear(colorHeightComputation_cl);
            colorHeightComputation.add(colorHeightComputationConstraint);
            clIndex++;
        }
        return colorHeightComputation;
    }

    private ArrayList<?> createColorWidthComputationConstraint() {
        ArrayList<ConstraintProto.Builder> colorWidthComputation = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            Integer ClW_cl = this.model.getVariableIndex("ClW_(cl)",new Integer[]{clIndex});

            LinearConstraintProto.Builder colorWidthComputation_cl = LinearConstraintProto.newBuilder()
                    .addDomain(0)
                    .addDomain(0)
                    .addVars(ClW_cl)
                    .addCoeffs(1);;
            for (int c=0;c<this.cols;c++) {
                Integer ClCS_cl_c = this.model.getVariableIndex("ClCS_(cl,c)",new Integer[]{clIndex, c});
                Integer ClCE_cl_c = this.model.getVariableIndex("ClCE_(cl,c)",new Integer[]{clIndex, c});
                colorWidthComputation_cl
                        .addVars(ClCS_cl_c).addCoeffs(c)
                        .addVars(ClCE_cl_c).addCoeffs(-c);
            }
            ConstraintProto.Builder colorWidthComputationConstraint = ConstraintProto.newBuilder()
                    .setName("colorWidthComputation_("+clIndex+")")
                    .setLinear(colorWidthComputation_cl);
            colorWidthComputation.add(colorWidthComputationConstraint);
            clIndex++;
        }
        return colorWidthComputation;
    }

    private ArrayList<?> createColorSquareRuleConstraint() {
        ArrayList<ConstraintProto.Builder> colorSquareRule = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            if(this.squareShapes.contains(color)) {
                Integer ClH_cl = this.model.getVariableIndex("ClH_(cl)", new Integer[]{clIndex});
                Integer ClW_cl = this.model.getVariableIndex("ClW_(cl)", new Integer[]{clIndex});

                LinearConstraintProto.Builder colorSquareRule_cl = LinearConstraintProto.newBuilder()
                        .addDomain(0)
                        .addDomain(0)
                        .addVars(ClH_cl)
                        .addCoeffs(1)
                        .addVars(ClW_cl)
                        .addCoeffs(-1);
                ConstraintProto.Builder colorSquareRuleConstraint = ConstraintProto.newBuilder()
                        .setName("colorSquareRule_(" + clIndex + ")")
                        .setLinear(colorSquareRule_cl);
                colorSquareRule.add(colorSquareRuleConstraint);
            }
            clIndex++;
        }
        return colorSquareRule;
    }

    private ArrayList<?> createColorVerticalRectangleRuleConstraint() {
        ArrayList<ConstraintProto.Builder> colorVerticalRectangleRule = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            if(this.verticalRectShapes.contains(color)) {
                Integer ClH_cl = this.model.getVariableIndex("ClH_(cl)", new Integer[]{clIndex});
                Integer ClW_cl = this.model.getVariableIndex("ClW_(cl)", new Integer[]{clIndex});

                LinearConstraintProto.Builder colorVerticalRectangleRule_cl = LinearConstraintProto.newBuilder()
                        .addDomain(0)
                        .addDomain(this.rows)
                        .addVars(ClH_cl)
                        .addCoeffs(1)
                        .addVars(ClW_cl)
                        .addCoeffs(-1);
                ConstraintProto.Builder colorVerticalRectangleRuleConstraint = ConstraintProto.newBuilder()
                        .setName("colorVerticalRectangleRule_(" + clIndex + ")")
                        .setLinear(colorVerticalRectangleRule_cl);
                colorVerticalRectangleRule.add(colorVerticalRectangleRuleConstraint);
            }
            clIndex++;
        }
        return colorVerticalRectangleRule;
    }

    private ArrayList<?> createColorHorizontalRectangleRuleConstraint() {
        ArrayList<ConstraintProto.Builder> colorHorizontalRectangleRule = new ArrayList<>();
        int clIndex = 0;
        for (Color color:this.colors) {
            if(this.horizontalRectShapes.contains(color)) {
                Integer ClH_cl = this.model.getVariableIndex("ClH_(cl)", new Integer[]{clIndex});
                Integer ClW_cl = this.model.getVariableIndex("ClW_(cl)", new Integer[]{clIndex});

                LinearConstraintProto.Builder colorHorizontalRectangleRule_cl = LinearConstraintProto.newBuilder()
                        .addDomain(0)
                        .addDomain(this.cols)
                        .addVars(ClW_cl)
                        .addCoeffs(1)
                        .addVars(ClH_cl)
                        .addCoeffs(-1);
                ConstraintProto.Builder colorHorizontalRectangleRuleConstraint = ConstraintProto.newBuilder()
                        .setName("colorHorizontalRectangleRule_(" + clIndex + ")")
                        .setLinear(colorHorizontalRectangleRule_cl);
                colorHorizontalRectangleRule.add(colorHorizontalRectangleRuleConstraint);
            }
            clIndex++;
        }
        return colorHorizontalRectangleRule;
    }
}