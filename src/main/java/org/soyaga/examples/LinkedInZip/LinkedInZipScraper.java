package org.soyaga.examples.LinkedInZip;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Ant.SimpleMemoryAnt;
import org.soyaga.aco.BuilderEvaluator.AllNodesLineBuilderEvaluator;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.Graph.GenericGraph;
import org.soyaga.examples.LinkedInZip.ACO.Evaluable.ZipFeasibilityFunction;
import org.soyaga.examples.LinkedInZip.ACO.ZipACO;
import org.soyaga.examples.LinkedInZip.MathModel.ZipMathModel;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LinkedInZipScraper {

    public static void main(String[] args) {
        //Credentials for LinkedIn
        String linkedInUser ="Your_LinkedIn_mail@example.com";
        String linkedInPassword ="Your_LinkedIn_password";
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // dimensions
        int rows = 0;
        int cols = 0;

        //Grid of buttons
        WebElement[][] gridButtons = null;
        //Grid of numbers
        Integer[][] gridNumbers = null;
        //Boolean[][] with the north border
        Boolean[][] northConnection = null;
        //Boolean[][] with the east border
        Boolean[][] eastConnection = null;
        //Boolean[][] with the south border
        Boolean[][] southConnection = null;
        //Boolean[][] with the west border
        Boolean[][] westConnection = null;
        //HashMap with the buttons by node
        HashMap<Node, WebElement> buttonByNode= new HashMap<>();

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();

        try {
            // Waiter
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(100));

            // Go to LinkedIn login
            driver.get("https://www.linkedin.com/login");

            // Login
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(linkedInUser);
            password.sendKeys(linkedInPassword);
            WebElement loggingButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))
            );
            loggingButton.click();
            Thread.sleep(2000);
            // Wait/load, then navigate to the game
            driver.get("https://www.linkedin.com/games/zip/");
            wait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/games/zip/"));

            // search the grid
            try {
                // Wait for the grid to appear
                WebElement grid = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.trail-grid.grid-game-board.gil__grid")));

                //Compute the dimensions
                String style = grid.getAttribute("style"); // "--rows: N; --cols: N"
                Pattern pattern = Pattern.compile("--rows:\\s*(\\d+);\\s*--cols:\\s*(\\d+)");
                assert style != null;
                Matcher matcher = pattern.matcher(style);
                if (matcher.find()) {
                    rows = Integer.parseInt(matcher.group(1));
                    cols = Integer.parseInt(matcher.group(2));
                }

                // Get all buttons inside the grid

                List<WebElement> allButtons = grid.findElements(By.cssSelector(".trail-cell"));
                allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                // Parse into 2D list
                gridButtons = new WebElement[rows][cols];
                gridNumbers = new Integer[rows][cols];
                northConnection = new Boolean[rows][cols];
                eastConnection = new Boolean[rows][cols];
                southConnection = new Boolean[rows][cols];
                westConnection = new Boolean[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        northConnection[i][j] = true;
                        eastConnection[i][j] = true;
                        southConnection[i][j] = true;
                        westConnection[i][j] = true;
                    }
                }
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int index = i * cols + j;
                        WebElement cell = allButtons.get(index);
                        if (!cell.findElements(By.cssSelector(".trail-cell-content")).isEmpty()){
                            int cellNumber = Integer.parseInt(Objects.requireNonNull(cell.findElement(By.cssSelector(".trail-cell-content")).getAttribute("outerText")));
                            gridNumbers[i][j] = cellNumber;
                        }
                        List<WebElement> edgeDivs = cell.findElements(By.cssSelector("div.trail-cell-wall"));
                        for(WebElement edgeDiv:edgeDivs) {
                            if(Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--up")) {
                                northConnection[i][j] = false;
                                southConnection[i-1][j] = false;
                            }
                            else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--down")) {
                                northConnection[i+1][j] = false;
                                southConnection[i][j] = false;
                            }
                            else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--right")) {
                                eastConnection[i][j] = false;
                                westConnection[i][j+1] = false;
                            }
                            else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--left")) {
                                eastConnection[i][j-1] = false;
                                westConnection[i][j] = false;
                            }
                        }
                        gridButtons[i][j]= cell;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Grid not found.");
            }

            if(rows==0) {
                // Bypass the human test
                try {
                    WebElement checkButton = driver.findElement(By.xpath("//button[text()='Start Puzzle']"));
                    checkButton.click();
                } catch (Exception e) {
                    System.out.println("Checkpoint not detected.");
                }

                // click the start button
                try {
                    // First, switch to the correct iframe
                    WebElement iframe = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.game-launch-page__iframe"))
                    );
                    driver.switchTo().frame(iframe);

                    // Search the start button and click it
                    WebElement startButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.id("launch-footer-start-button"))
                    );
                    startButton.click();
                } catch (Exception e) {
                    System.out.println("Start button not found.");
                }
                driver.switchTo().defaultContent();

                // decline cookies
                try {
                    // Search the reject button and click it
                    WebElement rejectBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-control-name='ga-cookie.consent.deny.v4']"))
                    );
                    rejectBtn.click();
                } catch (Exception e) {
                    System.out.println("Cookies not found.");
                }

                // skip the tutorial
                try {
                    // First, switch to the correct iframe
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));

                    // Now locate your specific iframe
                    WebElement iframe = driver.findElement(By.cssSelector("iframe[title='games']"));

                    // Switch into the iframe
                    driver.switchTo().frame(iframe);

                    // Wait for the Dismiss button inside the iframe
                    WebElement dismissButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button.artdeco-modal__dismiss[aria-label='Dismiss']")
                    ));

                    // Click the button
                    dismissButton.click();
                } catch (Exception e) {
                    System.out.println("Tutorial not found.");
                }
                driver.switchTo().defaultContent();

                // switch to correct iframe
                try {
                    // Move to the correct iframe
                    WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("iframe[title='games']")
                    ));
                    driver.switchTo().frame(iframe);
                } catch (Exception e) {
                    System.out.println("Iframe not found.");
                }

                // search the grid
                try {
                    // Wait for the grid to appear
                    WebElement grid = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.trail-grid.grid-game-board.gil__grid")));

                    //Compute the dimensions
                    String style = grid.getAttribute("style"); // "--rows: N; --cols: N"
                    Pattern pattern = Pattern.compile("--rows:\\s*(\\d+);\\s*--cols:\\s*(\\d+)");
                    assert style != null;
                    Matcher matcher = pattern.matcher(style);
                    if (matcher.find()) {
                        rows = Integer.parseInt(matcher.group(1));
                        cols = Integer.parseInt(matcher.group(2));
                    }

                    // Get all buttons inside the grid

                    List<WebElement> allButtons = grid.findElements(By.cssSelector(".trail-cell"));
                    allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                    // Parse into 2D list
                    gridButtons = new WebElement[rows][cols];
                    gridNumbers = new Integer[rows][cols];
                    northConnection = new Boolean[rows][cols];
                    eastConnection = new Boolean[rows][cols];
                    southConnection = new Boolean[rows][cols];
                    westConnection = new Boolean[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            northConnection[i][j] = true;
                            eastConnection[i][j] = true;
                            southConnection[i][j] = true;
                            westConnection[i][j] = true;
                        }
                    }
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int index = i * cols + j;
                            WebElement cell = allButtons.get(index);
                            if (!cell.findElements(By.cssSelector(".trail-cell-content")).isEmpty()) {
                                int cellNumber = Integer.parseInt(Objects.requireNonNull(cell.findElement(By.cssSelector(".trail-cell-content")).getAttribute("outerText")));
                                gridNumbers[i][j] = cellNumber;
                            }
                            List<WebElement> edgeDivs = cell.findElements(By.cssSelector("div.trail-cell-wall"));
                            for (WebElement edgeDiv : edgeDivs) {
                                if(Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--up")) {
                                    northConnection[i][j] = false;
                                    southConnection[i-1][j] = false;
                                }
                                else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--down")) {
                                    northConnection[i+1][j] = false;
                                    southConnection[i][j] = false;
                                }
                                else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--right")) {
                                    eastConnection[i][j] = false;
                                    westConnection[i][j+1] = false;
                                }
                                else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--left")) {
                                    eastConnection[i][j-1] = false;
                                    westConnection[i][j] = false;
                                }
                            }
                            gridButtons[i][j] = cell;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Grid not found.");
                }
            }

            Node [][] nodeGrid = new Node[rows][cols];
            ArrayList<Node> priorityNodes = new ArrayList<>();
            HashMap<Node,Integer> priorityByNode = new HashMap<>();
            Node startNode = null;
            Node endNode = null;
            ArrayList<Node> nodesToVisit = new ArrayList<>();
            GenericGraph graph = new GenericGraph(1.);
            for(int row=0; row<rows; row++){
                for(int col=0; col<cols; col++){
                    WebElement button = gridButtons[row][col];
                    Node node = new Node("Node_("+row+","+col+")");
                    buttonByNode.put(node, button);
                    graph.addNode(node);
                    nodeGrid[row][col] = node;
                    nodesToVisit.add(node);
                    if (gridNumbers[row][col]!=null){
                        int priority = gridNumbers[row][col];
                        priorityNodes.add(null);
                        priorityByNode.put(node,priority);
                        startNode = startNode==null? node:priorityByNode.get(startNode)<priorityByNode.get(node)? startNode:node;
                        endNode = endNode==null? node:priorityByNode.get(endNode)>priorityByNode.get(node)? endNode:node;
                    }
                }
            }
            for(int row=0; row<rows; row++){
                for(int col=0; col<cols; col++){
                    Node node = nodeGrid[row][col];
                    if(row>0 && northConnection[row][col]) graph.initializeEdge(node,nodeGrid[row-1][col],1.);
                    if(col< cols-1 && eastConnection[row][col]) graph.initializeEdge(node,nodeGrid[row][col+1],1.);
                    if(row < rows-1 && southConnection[row][col]) graph.initializeEdge(node,nodeGrid[row+1][col],1.);
                    if(col >0 && westConnection[row][col]) graph.initializeEdge(node,nodeGrid[row][col-1],1.);
                    if(priorityByNode.containsKey(node))priorityNodes.set(priorityByNode.get(node)-1,node);
                }
            }
            GenericWorld world = new GenericWorld(graph, graph);

            Colony colony = new Colony();
            //Create optimization object
            ZipACO acoModel = new ZipACO("ZipACO",
                    colony,
                    world,
                    new ACOInitializer(
                            new SimpleMemoryAnt(
                                    new Solution(
                                            null,
                                            new ZipFeasibilityFunction(
                                                    rows,
                                                    cols,
                                                    priorityNodes,
                                                    priorityByNode
                                                    ),
                                            1.,
                                            rows*cols,
                                            new AllNodesLineBuilderEvaluator(           //Solution evaluator
                                                    startNode,                          //Start Node
                                                    endNode,                            //End Node
                                                    nodesToVisit                        //Nodes to visit (all nodes)
                                            )
                                    ),
                                    new RandomProportionalEdgeSelector(                //Edge selector How the ant selects the edge to take.
                                            1.5,                                        //Double with the ants' Alpha (>0) parameter (importance of the edges pheromones against the edges "distances").
                                            .5                                         //Double with the ants' Beta (>0) parameter (importance of the edges "distances" against the edges pheromones).
                                    ),                                                 //
                                    1.                                               //Double with the amount of pheromone each ant can deposit in its track (same order of the problem optimal fitness).
                            ),
                            rows*cols                                                         //Integer with the initial number of ants.
                    )
            );

            ZipMathModel mmModel = new ZipMathModel("ZipMM",rows, cols, gridNumbers,northConnection, eastConnection,
                    southConnection, westConnection);

            ExecutorService executor = Executors.newFixedThreadPool(2);
            CompletionService<Object> completionService = new ExecutorCompletionService<>(executor);

            Future<Object> future1 = completionService.submit(mmModel);
            Future<Object> future2 = completionService.submit(acoModel);

            Object results_object = completionService.take().get();

            if (!future1.isDone()) future1.cancel(true);
            if (!future2.isDone()) future2.cancel(true);

            Object[] results = (Object[])results_object;

            executor.shutdown();
            if("ACO".equals(results[0])){
                Solution result =  (Solution) results[1];
                for(Object object: result.getNodesVisited()){
                    Node node = (Node)object;
                    WebElement buttonToClick = buttonByNode.get(node);
                    buttonToClick.click();
                }
                System.out.println("ACO optimal introduced.");
            } else if ("MM".equals(results[0])) {
                HashMap<Integer, Integer[]> result = (HashMap<Integer, Integer[]>) results[1];
                for(int i =1; i<= result.size(); i++){
                    Integer[] rowCol = result.get(i);
                    WebElement buttonToClick = gridButtons[rowCol[0]][rowCol[1]];
                    buttonToClick.click();
                }
                System.out.println("MM optimal introduced.");
            }
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println("Error in execution.");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
