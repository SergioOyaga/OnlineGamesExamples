package org.soyaga.examples.SlitherLink;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.Graph.GenericGraph;
import org.soyaga.examples.SlitherLink.ACO.Evaluable.Constraints.SlitherLinkConstraint;
import org.soyaga.examples.SlitherLink.ACO.Evaluable.Constraints.StartEndConstraint;
import org.soyaga.examples.SlitherLink.ACO.SlitherLinkACO;
import org.soyaga.examples.SlitherLink.ACO.Stats.ImageStat;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlitherLinkScraper {

    static double initialPheromone = 1.0;

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        EdgeDriver driver = new EdgeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading https://www.puzzle-loop.com/: ...");
            driver.get("https://www.puzzle-loop.com/?size=4"); // Hard
            //driver.get("https://www.puzzle-loop.com");  //Easy
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);
            Robot robot = new Robot();

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.manage().window().setSize(new Dimension(600, 800));
            driver.manage().window().setPosition(new Point(0,0));
            driver.navigate().refresh();
            longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("accept-choices"))).click();
            System.out.println("Refreshed.");

            for(int loop=0; loop<10; loop++){
                System.out.println("Scrolling...");
                try {
                    WebElement elementB = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnNew")));
                    js.executeScript(
                            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                            elementB
                    );
                    longWait.until(d -> {
                        JavascriptExecutor jse = (JavascriptExecutor) d;

                        Double scrollY = (Double) jse.executeScript("return window.scrollY;");
                        jse.executeScript("window.__lastScrollY = window.__lastScrollY || arguments[0];", scrollY);

                        Double last = (Double) jse.executeScript("return window.__lastScrollY;");
                        jse.executeScript("window.__lastScrollY = arguments[0];", scrollY);

                        return scrollY.equals(last);
                    });
                    Toolkit.getDefaultToolkit().sync();
                    System.out.println("Scrolled.");
                }
                catch (Exception ex){
                    System.out.println("Not scrolled.");
                }

                System.out.println("Retrieving grid size...");
                int cols = 0;
                int rows = 0;
                try {
                    WebElement label = longWait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(".puzzleInfo label")
                    ));
                    String text = label.getText();
                    Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        cols = Integer.parseInt(matcher.group(1));
                        rows = Integer.parseInt(matcher.group(2));
                    }
                    System.out.println("Grid size retrieved.");
                }
                catch (Exception ex){
                        System.out.println("Grid size not retrieved.");
                }

                System.out.println("Retrieving grid...");
                WebElement[][] gridElement = new WebElement[rows][cols];
                HashMap<List,Integer> gridNumbers = new HashMap<>();
                try {
                    List<WebElement> cells = driver.findElements(By.className("loop-task-cell"));
                    for(WebElement cell:cells){
                        int row = ((Long) js.executeScript(
                                "return arguments[0].row;", cell)).intValue();

                        int col = ((Long) js.executeScript(
                                "return arguments[0].col;", cell)).intValue();
                        gridElement[row][col] = cell;
                        String text = cell.getText();
                        if(text.isEmpty()) continue;
                        gridNumbers.put(List.of(row, col),Integer.valueOf(text));
                    }
                    System.out.println("Grid retrieved.");
                }
                catch (Exception ex){
                    System.out.println("Grid not retrieved.");
                }

                System.out.println("Computing World...");
                GenericWorld world = null;
                ArrayList<Constraint> constraints = new ArrayList<>();
                constraints.add(new StartEndConstraint());
                Node[][] nodes = new Node[rows+1][cols+1];
                HashMap<Edge,Object[]> edgeElementRelation=new HashMap<>();
                try {
                    world = computeWorld(rows, cols, gridNumbers,constraints, nodes, gridElement, edgeElementRelation);
                    System.out.println("World computed.");
                } catch (Exception ex) {
                    System.out.println("World not computed.");
                }

                System.out.println("Creating stat image...");
                ImageStat stat = new ImageStat(nodes,rows+1,cols+1, initialPheromone);
                System.out.println("Stat image created.");

                System.out.println("Building ACO Model...");
                Colony colony = new Colony();
                SlitherLinkACO model = new SlitherLinkACO("SlitherLink",colony, world, rows+1, cols+1,constraints,stat);
                System.out.println("ACO Model built.");

                System.out.println("Running ACO Model...");
                model.optimize();
                System.out.println("ACO Model run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<Edge> result = (ArrayList<Edge>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                if (resultText.startsWith("ACO_Optimal")) {
                    Thread.sleep(1000);
                    stat.close();
                    for(Edge edge:result){
                        Object[] elementDirection = edgeElementRelation.get(edge);
                        WebElement element = (WebElement) elementDirection[0];
                        String direction = (String) elementDirection[1];
                        Point p = getElementCenterOnScreen(driver,element,js);
                        Dimension d = element.getSize();
                        int x = p.x;
                        int y = p.y;
                        int ratio = 2;
                        switch (direction){
                            case "N":{
                                y-=d.getHeight()/ratio;
                                break;
                            }
                            case "E":{
                                x+=d.getWidth()/ratio;
                                break;
                            }
                            case "S":{
                                y+=d.getHeight()/ratio;
                                break;
                            }
                            case "W":{
                                x-=d.getWidth()/ratio;
                                break;
                            }
                        }
                        Toolkit.getDefaultToolkit().sync();
                        robot.mouseMove(x,y);
                        Thread.sleep(20);
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(20);
                    }
                    System.out.println("Optimal solution introduced.");
                    Thread.sleep(1000);
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Continuing game...");
                try {
                    longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnNew"))).click();
                    System.out.println("Game continued.");
                }
                catch (Exception ex){
                    System.out.println("Game not continued.");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

    /**
     * Static function that builds the world.
     * @return GenericWorld processed.
     */
    public static GenericWorld computeWorld(Integer rows, Integer cols, HashMap<List,Integer> gridNumbers,
                                            ArrayList<Constraint> constraints, Node[][] nodes, WebElement[][] gridElement,
                                            HashMap<Edge,Object[]> edgeElementRelation){
        GenericGraph graph = new GenericGraph(initialPheromone);
        for(int row = 0; row < rows+1; row++){
            for(int col = 0; col < cols+1; col++){
                Node node = new Node("("+row+", "+col+")");
                graph.addNode(node);
                nodes[row][col] = node;
            }
        }
        HashMap<List, Edge> constraintsInfoHelper = new HashMap<>();
        for(int row = 0; row < rows+1; row++){
            for(int col = 0; col < cols+1; col++){
                Node origin = nodes[row][col];
                HashSet<Edge> outputEdges = origin.getOutputEdges();
                if(row>0){
                    Node nodeNorth = nodes[row-1][col];
                    Edge edge = new Edge(origin,nodeNorth,1.,initialPheromone);
                    outputEdges.add(edge);
                    constraintsInfoHelper.put(List.of(row,col,row-1,col),edge);
                    if (col==cols) edgeElementRelation.put(edge, new Object[]{gridElement[row-1][col-1],"E"});
                    else edgeElementRelation.put(edge, new Object[]{gridElement[row-1][col],"W"});
                }
                if(row<rows){
                    Node nodeSouth = nodes[row+1][col];
                    Edge edge = new Edge(origin,nodeSouth,1.,initialPheromone);
                    outputEdges.add(edge);
                    constraintsInfoHelper.put(List.of(row,col,row+1,col),edge);
                    if (col==cols) edgeElementRelation.put(edge, new Object[]{gridElement[row][col-1],"E"});
                    else edgeElementRelation.put(edge, new Object[]{gridElement[row][col],"W"});
                }
                if(col>0){
                    Node nodeWest = nodes[row][col-1];
                    Edge edge = new Edge(origin,nodeWest,1.,initialPheromone);
                    outputEdges.add(edge);
                    constraintsInfoHelper.put(List.of(row,col,row,col-1),edge);
                    if (row==rows) edgeElementRelation.put(edge, new Object[]{gridElement[row-1][col-1],"S"});
                    else edgeElementRelation.put(edge, new Object[]{gridElement[row][col-1],"N"});
                }
                if(col<cols){
                    Node nodeEast = nodes[row][col+1];
                    Edge edge = new Edge(origin,nodeEast,1.,initialPheromone);
                    outputEdges.add(edge);
                    constraintsInfoHelper.put(List.of(row,col,row,col+1),edge);
                    if (row==rows) edgeElementRelation.put(edge, new Object[]{gridElement[row-1][col],"S"});
                    else edgeElementRelation.put(edge, new Object[]{gridElement[row][col],"N"});
                }
            }
        }

        for(Map.Entry<List,Integer> numberEntry: gridNumbers.entrySet()){
            int row = (int)numberEntry.getKey().get(0);
            int col = (int)numberEntry.getKey().get(1);
            int value = numberEntry.getValue();

            HashSet<Edge> edgesIncluded = new HashSet<>();
            Edge northWestToEast = constraintsInfoHelper.get(List.of(row, col, row, col + 1));
            Edge northEastToWest = constraintsInfoHelper.get(List.of(row, col + 1, row, col));

            Edge southWestToEast = constraintsInfoHelper.get(List.of(row + 1, col, row + 1, col + 1));
            Edge southEastToWest = constraintsInfoHelper.get(List.of(row + 1, col + 1, row + 1, col));

            Edge westNorthToSouth = constraintsInfoHelper.get(List.of(row, col, row + 1, col));
            Edge westSouthToNorth = constraintsInfoHelper.get(List.of(row + 1, col, row, col));

            Edge eastNorthToSouth = constraintsInfoHelper.get(List.of(row, col + 1, row + 1, col + 1));
            Edge eastSouthToNorth = constraintsInfoHelper.get(List.of(row + 1, col + 1, row, col + 1));

            if(value>0) {
                if (northWestToEast != null) edgesIncluded.add(northWestToEast);
                if (northEastToWest != null) edgesIncluded.add(northEastToWest);
                if (southWestToEast != null) edgesIncluded.add(southWestToEast);
                if (southEastToWest != null) edgesIncluded.add(southEastToWest);
                if (westNorthToSouth != null) edgesIncluded.add(westNorthToSouth);
                if (westSouthToNorth != null) edgesIncluded.add(westSouthToNorth);
                if (eastNorthToSouth != null) edgesIncluded.add(eastNorthToSouth);
                if (eastSouthToNorth != null) edgesIncluded.add(eastSouthToNorth);
            }
            else{
                northWestToEast.getOrigin().getOutputEdges().remove(northWestToEast);
                northEastToWest.getOrigin().getOutputEdges().remove(northEastToWest);
                southWestToEast.getOrigin().getOutputEdges().remove(southWestToEast);
                southEastToWest.getOrigin().getOutputEdges().remove(southEastToWest);
                westNorthToSouth.getOrigin().getOutputEdges().remove(westNorthToSouth);
                westSouthToNorth.getOrigin().getOutputEdges().remove(westSouthToNorth);
                eastNorthToSouth.getOrigin().getOutputEdges().remove(eastNorthToSouth);
                eastSouthToNorth.getOrigin().getOutputEdges().remove(eastSouthToNorth);
            }

            SlitherLinkConstraint constraint = new SlitherLinkConstraint(value,edgesIncluded);
            constraints.add(constraint);
        }
        return new GenericWorld(graph, graph);
    }

    private static Point getElementCenterOnScreen(WebDriver driver, WebElement element, JavascriptExecutor js) {
        // element center relative to VIEWPORT
        Map<String, Number> rect = (Map<String, Number>) js.executeScript(
                "const r = arguments[0].getBoundingClientRect();" +
                        "return { x: r.left + r.width, y: r.top };",
                element
        );

        double viewportX = rect.get("x").doubleValue();
        double viewportY = rect.get("y").doubleValue();

        // browser window position on screen
        Point windowPos = driver.manage().window().getPosition();

        // browser chrome offset (Edge/Chrome on Windows)
        Number chromeOffsetY = (Number) js.executeScript(
                "return window.outerHeight - window.innerHeight;"
        );

        int screenX = windowPos.getX() + (int) viewportX;
        int screenY = windowPos.getY() + chromeOffsetY.intValue() + (int) viewportY;

        return new Point(screenX, screenY);
    }

}
