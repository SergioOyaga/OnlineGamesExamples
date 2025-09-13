package org.soyaga.examples.Game2048.Board;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {
    private final Integer [][] boardGrid;
    private int rows, cols;


    public Board(Integer[][] boardGrid) {
        this.boardGrid = deepCopy(boardGrid);
    }

    public HashMap<String, Double> getMovementsProbabilities(){
        HashMap<String, Double> movementsProbabilities = new HashMap<>();
        ArrayList<String> availableMovements = this.getAvailableMovements();
        Integer maxValue=0;
        for (Integer[] row : this.boardGrid) {
            for (Integer value : row) {
                if (value != null && value > maxValue) {
                    maxValue = value;
                }
            }
        }
        boolean cantMixLastRowEast= false;
        boolean cantMixLastRowWest= false;
        for(int col=0; col<this.cols;col++){
            if(col==0) {
                if (this.boardGrid[this.rows - 1][col] == null) cantMixLastRowEast = true;
            }
            else if(col==this.cols-1) {
                if (this.boardGrid[this.rows - 1][col] == null) cantMixLastRowWest = true;
            }
            else {
                if (this.boardGrid[this.rows - 1][col] == null) {
                    cantMixLastRowEast = true;
                    cantMixLastRowWest = true;
                }
            }
        }
        if(availableMovements.size()==1){
            movementsProbabilities.put(availableMovements.get(0), 1.);
        }
        else if(availableMovements.size()==2){
            if(availableMovements.contains("N")){
                availableMovements.remove("N");
                movementsProbabilities.put(availableMovements.get(0),1.);
            } else if (availableMovements.contains("W") && availableMovements.contains("E")) {
                if(cumulateEast()){
                    movementsProbabilities.put("E",0.9);
                    movementsProbabilities.put("W",0.1);
                }
                else{
                    movementsProbabilities.put("E",0.1);
                    movementsProbabilities.put("W",0.9);
                }
            } else if (availableMovements.contains("W")) {
                movementsProbabilities.put("W",0.1);
                movementsProbabilities.put("S",0.9);
            } else {
                movementsProbabilities.put("E",0.1);
                movementsProbabilities.put("S",0.9);
            }
        }
        else if(availableMovements.size()==3){
            if(availableMovements.contains("N")){
                if (availableMovements.contains("W") && availableMovements.contains("E")) {
                    if(cumulateEast()){
                        movementsProbabilities.put("E",0.9);
                        movementsProbabilities.put("W",0.1);
                    }
                    else{
                        movementsProbabilities.put("W",0.9);
                        movementsProbabilities.put("E",0.1);
                    }
                }
                else if (availableMovements.contains("W")) {
                    movementsProbabilities.put("W",0.1);
                    movementsProbabilities.put("S",0.9);
                }
                else {
                    movementsProbabilities.put("E",0.1);
                    movementsProbabilities.put("S",0.9);
                }
            } else {
                if(cumulateEast()){
                    movementsProbabilities.put("E",0.15);
                    movementsProbabilities.put("W",0.05);
                }
                else{
                    movementsProbabilities.put("W",0.15);
                    movementsProbabilities.put("E",0.05);
                }
                movementsProbabilities.put("S",0.8);
            }
        }
        else if(availableMovements.size()==4){
            if(cumulateEast()){
                movementsProbabilities.put("E",0.15);
                movementsProbabilities.put("W",0.05);
            }
            else{
                movementsProbabilities.put("W",0.15);
                movementsProbabilities.put("E",0.05);
            }
            movementsProbabilities.put("S",0.8);
        }
        if(movementsProbabilities.size()>=2){
            if(
                    (cantMixLastRowEast && maxValue.equals(this.boardGrid[this.rows-1][this.cols-1])) ||
                    (maxValue.equals(this.boardGrid[this.rows-1][this.cols-2]) && maxValue.equals(this.boardGrid[this.rows-1][this.cols-1]))
            ) {
                movementsProbabilities.remove("W");
                double total = 0.;
                for(Map.Entry<String,Double> entry : movementsProbabilities.entrySet()){
                    total+= entry.getValue();
                }
                for(Map.Entry<String,Double> entry : movementsProbabilities.entrySet()){
                    entry.setValue(entry.getValue()/total);
                }
            } else if (maxValue.equals(this.boardGrid[this.rows-1][this.cols-2]) && movementsProbabilities.containsKey("E")) {
                movementsProbabilities.clear();
                movementsProbabilities.put("E",1.);
            }
            if(
                    (cantMixLastRowWest && maxValue.equals(this.boardGrid[this.rows-1][0])) ||
                    (maxValue.equals(this.boardGrid[this.rows-1][1]) && maxValue.equals(this.boardGrid[this.rows-1][0]))
            ) {
                movementsProbabilities.remove("E");
                double total = 0.;
                for(Map.Entry<String,Double> entry : movementsProbabilities.entrySet()){
                    total+= entry.getValue();
                }
                for(Map.Entry<String,Double> entry : movementsProbabilities.entrySet()){
                    entry.setValue(entry.getValue()/total);
                }
            }else if (maxValue.equals(this.boardGrid[this.rows-1][1]) && movementsProbabilities.containsKey("W")) {
                movementsProbabilities.clear();
                movementsProbabilities.put("W",1.);
            }
        }
        return movementsProbabilities;
    }

    public ArrayList<String> getAvailableMovements(){
        ArrayList<String> availableMovements = new ArrayList<>();
        if(this.canMoveNorth()) availableMovements.add("N");
        if(this.canMoveEast()) availableMovements.add("E");
        if(this.canMoveSouth()) availableMovements.add("S");
        if(this.canMoveWest()) availableMovements.add("W");
        return availableMovements;
    }

    private boolean canMoveNorth() {
        for(int col=0;col<this.cols; col++){
            boolean hasNull=false;
            for(int row=0;row<this.rows; row++){
                Integer cellValue= this.boardGrid[row][col];
                if(hasNull && cellValue!=null) return true;
                else if (cellValue==null) hasNull=true;
                else if (row<this.rows-1 && cellValue.equals(this.boardGrid[row+1][col])) return true;

            }
        }
        return false;
    }

    private boolean canMoveEast() {
        for(int row=0;row<this.rows; row++){
            boolean hasNull=false;
            for(int col=this.cols-1;col>=0; col--){
                Integer cellValue= this.boardGrid[row][col];
                if(hasNull && cellValue!=null) return true;
                else if (cellValue==null) hasNull=true;
                else if (col >0 && cellValue.equals(this.boardGrid[row][col-1])) return true;
            }
        }
        return false;
    }

    private boolean canMoveSouth() {
        for(int col=0;col<this.cols; col++){
            boolean hasNull=false;
            for(int row=this.rows-1;row>=0; row--){
                Integer cellValue= this.boardGrid[row][col];
                if(hasNull && cellValue!=null) return true;
                else if (cellValue==null) hasNull=true;
                else if (row > 0 && cellValue.equals(this.boardGrid[row-1][col])) return true;
            }
        }
        return false;
    }

    private boolean canMoveWest() {
        for(int row=0;row<this.rows; row++){
            boolean hasNull=false;
            for(int col=0;col<this.cols; col++){
                Integer cellValue= this.boardGrid[row][col];
                if(hasNull && cellValue!=null) return true;
                else if (cellValue==null) hasNull=true;
                else if (col< this.cols-1 && cellValue.equals(this.boardGrid[row][col+1])) return true;
            }
        }
        return false;
    }


    public void move(String direction){
        switch (direction){
            case "N":{
                //Compact to the north
                this.compactToTheNorth();
                //Merge to the north
                this.mergeToTheNorth();
                //Compact to the north
                this.compactToTheNorth();
                break;
            }
            case "E":{
                //Compact to the east
                this.compactToTheEast();
                //Merge to the east
                this.mergeToTheEast();
                //Compact to the east
                this.compactToTheEast();
                break;
            }
            case "S":{
                //Compact to the south
                this.compactToTheSouth();
                //Merge to the south
                this.mergeToTheSouth();
                //Compact to the south
                this.compactToTheSouth();
                break;
            }
            case "W":{
                //Compact to the west
                this.compactToTheWest();
                //Merge to the west
                this.mergeToTheWest();
                //Compact to the west
                this.compactToTheWest();
                break;
            }
            default:{
                System.out.println("direction not determined");
            }
        }
        this.addNewNumber();
    }

    private void compactToTheNorth() {
        for(int col=0;col<this.cols; col++){
            for(int row=0; row<this.rows; row++){
                Integer currentValue= this.boardGrid[row][col];
                if(currentValue==null){
                    boolean superiorAllNull=true;
                    for(int rowReplacement = row+1; rowReplacement<this.rows;rowReplacement++){
                        if (this.boardGrid[rowReplacement][col] !=null) superiorAllNull=false;
                        this.boardGrid[rowReplacement-1][col] = this.boardGrid[rowReplacement][col];
                    }
                    this.boardGrid[this.rows-1][col] = null;
                    if(superiorAllNull) break;
                    else if(this.boardGrid[row][col]==null) row--;
                }
            }
        }
    }

    private void compactToTheEast() {
        for(int row=0; row<this.rows; row++){
            for(int col=this.cols-1;col>=0; col--){
                Integer currentValue= this.boardGrid[row][col];
                if(currentValue==null){
                    boolean superiorAllNull=true;
                    for(int colReplacement = col-1; colReplacement>=0; colReplacement--){
                        if (this.boardGrid[row][colReplacement] !=null) superiorAllNull=false;
                        this.boardGrid[row][colReplacement+1] = this.boardGrid[row][colReplacement];
                    }
                    this.boardGrid[row][0] = null;
                    if(superiorAllNull) break;
                    else if(this.boardGrid[row][col]==null) col++;
                }
            }
        }
    }

    private void compactToTheSouth() {
        for(int col=0;col<this.cols; col++){
            for(int row=this.rows-1; row>=0; row--){
                Integer currentValue= this.boardGrid[row][col];
                if(currentValue==null){
                    boolean superiorAllNull=true;
                    for(int rowReplacement = row-1; rowReplacement>=0;rowReplacement--){
                        if (this.boardGrid[rowReplacement][col] !=null) superiorAllNull=false;
                        this.boardGrid[rowReplacement+1][col] = this.boardGrid[rowReplacement][col];
                    }
                    this.boardGrid[0][col] = null;
                    if(superiorAllNull) break;
                    else if(this.boardGrid[row][col]==null) row++;
                }
            }
        }
    }

    private void compactToTheWest() {
        for(int row=0; row<this.rows; row++){
            for(int col=0;col<this.cols; col++){
                Integer currentValue= this.boardGrid[row][col];
                if(currentValue==null){
                    boolean superiorAllNull=true;
                    for(int colReplacement = col+1; colReplacement<this.cols;colReplacement++){
                        if (this.boardGrid[row][colReplacement] !=null) superiorAllNull=false;
                        this.boardGrid[row][colReplacement-1] = this.boardGrid[row][colReplacement];
                    }
                    this.boardGrid[row][this.cols-1] = null;
                    if(superiorAllNull) break;
                    else if(this.boardGrid[row][col]==null) col--;
                }
            }
        }
    }

    private void mergeToTheNorth() {
        for(int row=0; row<this.rows-1; row++){
            for(int col=0;col<this.cols; col++){
                Integer currentValue = this.boardGrid[row][col];
                Integer nextValue = this.boardGrid[row+1][col];
                if(currentValue!=null && currentValue.equals(nextValue)) {
                    this.boardGrid[row][col] = currentValue + nextValue;
                    this.boardGrid[row + 1][col] = null;
                }
            }
        }
    }

    private void mergeToTheEast() {
        for(int col=this.cols-1;col>0; col--){
            for(int row=0; row<this.rows; row++){
                Integer currentValue = this.boardGrid[row][col];
                Integer nextValue = this.boardGrid[row][col-1];
                if(currentValue!=null && currentValue.equals(nextValue)) {
                    this.boardGrid[row][col] = currentValue + nextValue;
                    this.boardGrid[row][col-1] = null;
                }
            }
        }
    }

    private void mergeToTheSouth() {
        for(int row=this.rows-1; row>0; row--){
            for(int col=0;col<this.cols; col++){
                Integer currentValue = this.boardGrid[row][col];
                Integer nextValue = this.boardGrid[row-1][col];
                if(currentValue!=null && currentValue.equals(nextValue)) {
                    this.boardGrid[row][col] = currentValue + nextValue;
                    this.boardGrid[row - 1][col] = null;
                }
            }
        }
    }

    private void mergeToTheWest() {
        for(int col=0;col<this.cols-1; col++){
            for(int row=0; row<this.rows; row++){
                Integer currentValue = this.boardGrid[row][col];
                Integer nextValue = this.boardGrid[row][col+1];
                if(currentValue!=null && currentValue.equals(nextValue)) {
                    this.boardGrid[row][col] = currentValue + nextValue;
                    this.boardGrid[row][col+1] = null;
                }
            }
        }
    }

    private void addNewNumber() {
        Random random = new Random();
        int value = random.nextDouble() < 0.9 ? 2 : 4;
        ArrayList<int[]> availableCells = new ArrayList<>();
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                if (this.boardGrid[row][col] == null) {
                    availableCells.add(new int[]{row, col});
                }
            }
        }
        if(!availableCells.isEmpty()){
            int[] selected = availableCells.get(random.nextInt(availableCells.size()));
            this.boardGrid[selected[0]][selected[1]]= value;
        }
    }

    private Integer[][] deepCopy(Integer[][] original) {
        if (original == null) {
            this.rows=0;
            this.cols=0;
            return null;
        }
        this.rows=original.length;
        this.cols=original[0].length;
        Integer[][] copy = new Integer[this.rows][];
        for (int i = 0; i < this.rows; i++) {
            copy[i] = original[i].clone(); // Cloning each sub-array
        }
        return copy;
    }

    public Integer[][] getBoardGrid() {
        return this.deepCopy(this.boardGrid);
    }

    public boolean cumulateEast(){
        boolean cumulateEast = true;
        for (int row=this.rows-1;row>0;row--){
            boolean isNorthBlocking = false;
            if(row%2==0){
                for(int col=0;col<this.cols-1;col++){
                    Integer value = this.boardGrid[row][col];
                    Integer nextValue = this.boardGrid[row][col+1];
                    if (value==null || nextValue==null || value.equals(nextValue)) return false;
                    Integer northValue= this.boardGrid[row-1][col];
                    if(northValue!=null && northValue>value) isNorthBlocking = true;
                }
            }
            else{
                for(int col=this.cols-1;col>0;col--){
                    Integer value = this.boardGrid[row][col];
                    Integer nextValue = this.boardGrid[row][col-1];
                    if (value==null || nextValue==null || value.equals(nextValue)) return true;
                    Integer northValue= this.boardGrid[row-1][col];
                    if(northValue!=null && northValue>value) isNorthBlocking = true;
                }
            }
            if(isNorthBlocking) cumulateEast = !cumulateEast;
            cumulateEast = !cumulateEast;
        }
        return !cumulateEast;
    }
}
