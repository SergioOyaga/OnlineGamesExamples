package org.soyaga.examples.Minesweeper;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.Minesweeper.Graph.Cell;
import org.soyaga.examples.Minesweeper.MathModel.MinesweeperMathModel;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MinesweeperScraper {


    public static void main(String[] args) {

        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        EdgeDriver driver = new EdgeDriver();
        Actions action = new Actions(driver);

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading https://minesweeper.online/: ...");
            driver.get("https://minesweeper.online/start/3"); // Hard
            //driver.get("https://minesweeper.online/start/1");  //Easy
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.manage().window().setSize(new Dimension(600, 800));
            driver.manage().window().setPosition(new Point(0,0));
            WebElement zoomElement = driver.findElement(By.id("zoom_select"));
            Select select = new Select(zoomElement);
            select.selectByValue("16");
            System.out.println("Refreshed.");
            boolean hasToContinue = true;
            while(hasToContinue){
                System.out.println("Retrieving bomb number...");
                int bombNumber = 0;
                try{
                    int element1000 = findNumberOr0(driver,By.id("top_area_mines_1000"));
                    int element100 = findNumberOr0(driver,By.id("top_area_mines_100"));
                    int element10 = findNumberOr0(driver,By.id("top_area_mines_10"));
                    int element1 = findNumberOr0(driver,By.id("top_area_mines_1"));
                    bombNumber= element1000*1000+element100*100+element10*10+element1;
                    System.out.println("Bomb number retrieved.");
                }
                catch (Exception ex){
                    System.out.println("Bomb number not retrieved.");
                }

                System.out.println("Retrieving grid properties...");
                int cols = 0;
                int rows = 0;
                HashMap<Integer,HashMap<Integer,WebElement>> webMap = new HashMap<>();
                Cell[][] gridCells = null;
                ArrayList<WebElement> elementsToDecide = new ArrayList<>();
                try {
                    WebElement grid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("AreaBlock")));
                    List<WebElement> children = grid.findElements(By.xpath("./*[starts-with(@id, 'cell_')]"));
                    for(WebElement child:children){
                        int col = Integer.parseInt(child.getAttribute("data-x"));
                        int row = Integer.parseInt(child.getAttribute("data-y"));
                        if(cols<col) cols=col;
                        if(rows<row) rows=row;
                        webMap.computeIfAbsent(row,k->new HashMap<>()).put(col,child);
                        child.getAttribute("class");
                    }
                    rows++;
                    cols++;
                    gridCells = new Cell[rows][cols];

                    for(Map.Entry<Integer, HashMap<Integer,WebElement>> rowEntry:webMap.entrySet()){
                        int row= rowEntry.getKey();
                        for(Map.Entry<Integer, WebElement> colEntry:rowEntry.getValue().entrySet()){
                            int col = colEntry.getKey();
                            WebElement element = colEntry.getValue();
                            String text = colEntry.getValue().getAttribute("class");
                            if(text.contains("hdd_closed")) {
                                if(text.contains("hdd_flag")){
                                    gridCells[row][col] = new Cell(row,col,true,-1, element);
                                }
                                else {
                                    gridCells[row][col] = new Cell(row,col,false,null, element);
                                    elementsToDecide.add(element);
                                }
                            }
                            else {
                                int number = Arrays.stream(text.split("\\s+"))
                                        .filter(c -> c.startsWith("hdd_type"))
                                        .map(c -> c.replace("hdd_type", ""))
                                        .mapToInt(Integer::parseInt)
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException("size not found"));
                                gridCells[row][col] = new Cell(row, col, true, number,element);
                            }
                        }
                    }

                    System.out.println("Grid properties retrieved.");
                }
                catch (Exception ex){
                        System.out.println("Grid properties not retrieved.");
                }

                ArrayList<WebElement> leftClickElements = new ArrayList<>();
                ArrayList<WebElement> rightClickElements = new ArrayList<>();
                String resultText;
                if(bombNumber==0){
                    leftClickElements.addAll(elementsToDecide);
                    resultText = "No more Bombs to add";
                }
                else if (bombNumber==elementsToDecide.size()) {
                    resultText = "All Bombs to add";
                    rightClickElements.addAll(elementsToDecide);
                }
                else {
                    System.out.println("Computing Rules...");
                    ArrayList<Cell> problemCells = new ArrayList<>();
                    ArrayList<Map.Entry<Integer, HashSet<Cell>>> problemRules = new ArrayList<>();
                    try {
                        computeRules(gridCells, problemCells, problemRules);
                        System.out.println("Rules computed.");
                    }
                    catch (Exception ex) {
                        System.out.println("Rules not computed.");
                    }

                    if (problemCells.isEmpty()){
                        System.out.println("Selecting random start...");
                        leftClickElements.add(elementsToDecide.get(new Random().nextInt(elementsToDecide.size())));
                        resultText = "Uncertain";
                        System.out.println("Random start selected.");
                    }
                    else{
                        System.out.println("Building CP Model...");
                        MinesweeperMathModel model = new MinesweeperMathModel("MinesweeperSAT",problemRules, problemCells);
                        System.out.println("CP Model built.");

                        System.out.println("Running CP Model...");
                        model.optimize();
                        System.out.println("CP Model run.");

                        System.out.println("Gathering result...");
                        HashMap<Cell, Double> certainty = model.getResult();
                        rightClickElements = (ArrayList<WebElement>)certainty.entrySet().stream().filter(entry->entry.getValue()>=0.99).map(entry->entry.getKey().element()).collect(Collectors.toList());
                        leftClickElements = (ArrayList<WebElement>) certainty.entrySet().stream().filter(entry->entry.getValue()<=0.01).map(entry->entry.getKey().element()).collect(Collectors.toList());
                        System.out.println("Result gathered.");
                        if(rightClickElements.isEmpty() && leftClickElements.isEmpty()){
                            System.out.println("Selecting uncertain next...");
                            double statisticalBombUSed = certainty.values().stream().reduce(0.,Double::sum);
                            double bombProbability = (bombNumber-statisticalBombUSed)/(elementsToDecide.size()-problemCells.size());
                            HashMap<WebElement, Double> elementCertainty = new HashMap<>();
                            problemCells.forEach(c ->elementCertainty.put(c.element(), certainty.get(c)));
                            elementsToDecide.removeAll(elementCertainty.keySet());
                            elementsToDecide.forEach(el-> elementCertainty.put(el,bombProbability));
                            double minBombProb = elementCertainty.values().stream().min(Double::compare).get();
                            ArrayList<WebElement> listMinBombProb = (ArrayList<WebElement>) elementCertainty.entrySet().stream().filter(entry->entry.getValue()==minBombProb).map(Map.Entry::getKey).collect(Collectors.toList());
                            leftClickElements.add(listMinBombProb.get(new Random().nextInt(listMinBombProb.size())));
                            resultText = "Uncertain";
                            System.out.println("Uncertain next selected.");
                        }
                        else{
                            resultText = "CP_optimal";
                        }
                    }
                }

                System.out.println("Introducing solution...");
                if (resultText.equals("CP_optimal")) {
                    leftClickElements.forEach(el->action.click(el).perform());
                    rightClickElements.forEach(el->action.contextClick(el).perform());
                    System.out.println("Math Mod Optimal solution introduced.");
                }
                else if (resultText.equals("Uncertain")) {
                    leftClickElements.forEach(el->action.click(el).perform());
                    System.out.println("Uncertain solution introduced.");
                }
                else if (resultText.equals("No more Bombs to add")) {
                    leftClickElements.forEach(el->action.click(el).perform());
                    System.out.println("No more Bombs to add solution introduced.");
                }
                else if (resultText.equals("All Bombs to add")) {
                    rightClickElements.forEach(el->action.contextClick(el).perform());
                    System.out.println("All Bombs to add solution introduced.");
                }
                else {
                    System.out.println("No movement.");
                }

                System.out.println("Continuing game...");
                try {
                    Thread.sleep(200);
                    WebElement face = driver.findElement(By.id("top_area_face"));
                    String text = face.getAttribute("class");
                    String facetType = Arrays.stream(text.split("\\s+"))
                            .filter(c -> c.startsWith("hdd_top-area-face-"))
                            .map(c -> c.replace("hdd_top-area-face-", ""))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("face not found"));
                    if(facetType.equals("win")){
                        System.out.println("Game won!!!");
                        hasToContinue = false;

                    } else if (facetType.equals("lose")) {
                        System.out.println("Game lost!!!");
                        action.click(face).perform();
                        hasToContinue = true;
                        Thread.sleep(100);
                    } else if (facetType.equals("unpressed")) {
                        System.out.println("Game continued.");
                    }
                }
                catch (Exception ex){
                    System.out.println("Error while continuing.");
                }
            }
            Thread.sleep(1000);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

    private static void computeRules(Cell[][] gridCells, ArrayList<Cell> problemCells,
                                     ArrayList<Map.Entry<Integer, HashSet<Cell>>> problemRules) {
        HashSet<Cell> problemCellsSet= new HashSet<>();
        int rows = gridCells.length;
        for(int row=0; row< rows; row++){
            int cols = gridCells[row].length;
            for(int col=0; col< cols; col++){
                Cell ruleCell = gridCells[row][col];
                if(ruleCell.isDecided()){
                    if(ruleCell.value()>0){
                        int value = ruleCell.value();
                        HashSet<Cell> constraintCells= new HashSet<>();
                        if(row>0) {
                            if(col>0){
                                Cell northWestCell = gridCells[row - 1][col-1];
                                if (!northWestCell.isDecided()) {
                                    constraintCells.add(northWestCell);
                                }
                                else if(northWestCell.value()<0){
                                    value--;
                                }
                            }
                            Cell northCell = gridCells[row - 1][col];
                            if (!northCell.isDecided()) {
                                constraintCells.add(northCell);
                            }
                            else if(northCell.value()<0){
                                value--;
                            }
                            if(col<cols-1){
                                Cell northEastCell = gridCells[row - 1][col+1];
                                if (!northEastCell.isDecided()) {
                                    constraintCells.add(northEastCell);
                                }
                                else if(northEastCell.value()<0){
                                    value--;
                                }
                            }
                        }
                        if(col>0){
                            Cell westCell = gridCells[row][col-1];
                            if (!westCell.isDecided()) {
                                constraintCells.add(westCell);
                            }
                            else if(westCell.value()<0){
                                value--;
                            }
                        }
                        if(col<cols-1){
                            Cell EastCell = gridCells[row][col+1];
                            if (!EastCell.isDecided()) {
                                constraintCells.add(EastCell);
                            }
                            else if(EastCell.value()<0){
                                value--;
                            }
                        }
                        if(row<rows-1){
                            if(col>0){
                                Cell southWestCell = gridCells[row + 1][col-1];
                                if (!southWestCell.isDecided()) {
                                    constraintCells.add(southWestCell);
                                }
                                else if(southWestCell.value()<0){
                                    value--;
                                }
                            }
                            Cell southCell = gridCells[row + 1][col];
                            if (!southCell.isDecided()) {
                                constraintCells.add(southCell);
                            }
                            else if(southCell.value()<0){
                                value--;
                            }
                            if(col<cols-1){
                                Cell southEastCell = gridCells[row + 1][col+1];
                                if (!southEastCell.isDecided()) {
                                    constraintCells.add(southEastCell);
                                }
                                else if(southEastCell.value()<0){
                                    value--;
                                }
                            }
                        }
                        if(!constraintCells.isEmpty()){
                            problemRules.add(new AbstractMap.SimpleEntry<>(value, constraintCells));
                            problemCellsSet.addAll(constraintCells);
                        }
                    }
                }
            }
        }
        problemCells.addAll(problemCellsSet);
    }

    public static int findNumberOr0(WebDriver driver, By locator) {
        List<WebElement> elements = driver.findElements(locator);
        WebElement element = elements.isEmpty() ? null : elements.get(0);
        if(element==null) return 0;
        String text = element.getAttribute("class");
        return Arrays.stream(text.split("\\s+"))
                .filter(c -> c.startsWith("hdd_top-area-num"))
                .map(c -> c.replace("hdd_top-area-num", ""))
                .mapToInt(Integer::parseInt)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("element number not found"));
    }
}
