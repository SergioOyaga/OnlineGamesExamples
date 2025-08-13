package org.soyaga.examples.Maze;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.aco.Colony;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.GenericGraph;
import org.soyaga.examples.Maze.ACO.Elements.MazeNode;
import org.soyaga.examples.Maze.ACO.MazeACO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;

public class MazeScraper {
    static int rows=20;
    static int cols=35;
    static double size=20.5;
    static int gapy = 25;
    static int gapx = 11;
    static double initialPheromone = 1.0;
    static MazeNode startNode = null;
    static MazeNode endNode = null;

    /**
     * Static function that builds the world.
     * @return GenericWorld processed.
     */
    public static GenericWorld computeWorld(BufferedImage processedImage){
        GenericGraph graph = new GenericGraph(initialPheromone);
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                graph.addNode(new MazeNode("Node("+row+","+col+")", col, row));
            }
        }
        int startColor = new Color(255,215,0).getRGB();
        int endColor = new Color(0,0,255).getRGB();
        int wallColor = new Color(136,136,170).getRGB();
        int noWallColor = new Color(10,18,57).getRGB();
        for(int row = 0; row < rows; row++){
            double cellYStart = row*size;
            double cellYMid = cellYStart+size/2.;
            double cellYEnd = cellYStart+size;
            for(int col = 0; col < cols; col++){
                double cellXStart = col*size;
                double cellXMid = cellXStart+size/2.;
                double cellXEnd = cellXStart+size;
                MazeNode origin = (MazeNode) graph.getNode("Node(" + row  + "," + col + ")");
                //north
                if(row > 0 && processedImage.getRGB((int)cellXMid, (int)cellYStart) != wallColor) {
                    origin.addEdge(graph.getNode("Node(" + (row - 1) + "," + col + ")"), 1., initialPheromone);
                }
                //south
                if(row < rows-1 && processedImage.getRGB((int)cellXMid, (int)cellYEnd) != wallColor) {
                    origin.addEdge(graph.getNode("Node(" + (row + 1) + "," + col + ")"), 1., initialPheromone);
                }
                //west
                if(col > 0 && processedImage.getRGB((int)cellXStart,(int)cellYMid) != wallColor) {
                    origin.addEdge(graph.getNode("Node(" + row + "," + (col - 1) + ")"), 1., initialPheromone);
                }
                //east
                if(col < cols-1 && processedImage.getRGB((int)cellXEnd,(int)cellYMid) != wallColor) {
                    origin.addEdge(graph.getNode("Node(" + row + "," + (col + 1) + ")"), 1., initialPheromone);
                }
                //start node
                if(startNode == null && processedImage.getRGB((int)cellXMid, (int)cellYMid) == startColor){
                    startNode = origin;
                }
                else if(endNode == null && processedImage.getRGB((int)cellXMid, (int)cellYMid) == endColor){
                    endNode = origin;
                }
            }
        }
        return new GenericWorld(graph, graph);
    }

    /**
     * Static function that process the image.
     * @param image BufferedImage to process.
     * @return BufferedImage processed.
     */
    public static BufferedImage processScreenshot(BufferedImage image){
        System.out.println("    Cropping image...");
        return image.getSubimage(gapx, gapy, (int) (cols*size), (int) (rows*size));
    }

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();
        try {
            // Waiter
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));

            System.out.println("Loading https://www.mathsisfun.com/measure/mazes.html: ...");
            // Go to Sudoku page
            driver.get("https://www.mathsisfun.com/measure/mazes.html");
            System.out.println("Loaded.");

            System.out.println("Accepting cookies...");
            try {
                WebElement accentCookies = wait.until(
                        ExpectedConditions.elementToBeClickable(By.className("fc-button-label"))
                );
                accentCookies.click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception ex){
                System.out.println("No cookies detected");
            }

            System.out.println("Switching to Iframe...");
            try {
                WebElement iframe = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[title='JavaScript Animation']"))
                );
                driver.switchTo().frame(iframe);
                System.out.println("Iframe switched.");
            }
            catch (Exception ex){
                System.out.println("Iframe not found.");
            }

            for(int loop=0; loop<10; loop++) {
                System.out.println("Retrieving Maze...");
                try {
                    System.out.println("    Selecting difficulty...");
                    WebElement difficultyRadioButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.id("gameUI4"))
                    );
                    difficultyRadioButton.click();
                    System.out.println("    Difficulty selected.");

                    System.out.println("    Clicking ✔...");
                    WebElement acceptButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='✔']"))
                    );
                    acceptButton.click();
                    System.out.println("    ✔ clicked .");
                    System.out.println("Maze retrieved");
                } catch (Exception ex) {
                    System.out.println("Maze not retrieved.");
                }

                System.out.println("Screenshot the Maze...");
                BufferedImage image = null;
                try {
                    WebElement canvas = driver.findElement(By.id("canvas2"));
                    File screenshot = canvas.getScreenshotAs(OutputType.FILE);
                    image = ImageIO.read(screenshot);
                    System.out.println("Maze screenshot.");
                } catch (Exception e) {
                    System.out.println("Maze not screenshot");
                }
                //driver.switchTo().defaultContent();

                System.out.println("Transforming image...");
                BufferedImage imageCVProcessed = null;
                try {
                    imageCVProcessed = processScreenshot(image);
                    System.out.println("Image transformed.");
                } catch (Exception e) {
                    System.out.println("Image not transformed");
                }

                System.out.println("Computing World...");
                GenericWorld world = null;
                try {
                    world = computeWorld(imageCVProcessed);
                    System.out.println("World computed.");
                } catch (Exception ex) {
                    System.out.println("World not computed.");
                }

                System.out.println("Building ACO...");
                MazeACO model = new MazeACO("MazeACO",
                        new Colony(),
                        world,
                        startNode,
                        endNode,
                        rows,
                        cols
                );
                System.out.println("ACO built.");

                System.out.println("Running ACO...");
                model.optimize();
                System.out.println("ACO run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<MazeNode> result = (ArrayList<MazeNode>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Optimizing the result...");
                ArrayList<MazeNode> optimizedResult = new ArrayList<>();
                for (MazeNode node : result) {
                    if (optimizedResult.contains(node)) {
                        optimizedResult.subList(optimizedResult.indexOf(node), optimizedResult.size()).clear();
                    }
                    optimizedResult.add(node);
                }
                System.out.println("Result optimized.");

                System.out.println("Introducing solution...");
                Actions actions = new Actions(driver);
                if (resultText.startsWith("ACO_Optimal")) {
                    MazeNode previousNode = null;
                    for (MazeNode currentNode : optimizedResult) {
                        if (previousNode == null) {
                            previousNode = currentNode;
                            continue;
                        }
                        String move = previousNode.getMove(currentNode);
                        switch (move) {
                            case "L":
                                actions.sendKeys(Keys.LEFT).perform();
                                break;
                            case "R":
                                actions.sendKeys(Keys.RIGHT).perform();
                                break;
                            case "U":
                                actions.sendKeys(Keys.UP).perform();
                                break;
                            case "D":
                                actions.sendKeys(Keys.DOWN).perform();
                                break;
                        }
                        previousNode = currentNode;
                    }
                    System.out.println("Optimal solution introduced.");
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("    Clicking ✔...");
                WebElement acceptButton = wait.until(
                        ExpectedConditions.elementToBeClickable(By.className("togglebtn"))
                );
                acceptButton.click();
                System.out.println("    ✔ clicked .");

                System.out.println("Restarting.");
                try {
                    startNode = null;
                    endNode = null;
                    WebElement startPopupButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.id("restart"))
                    );
                    startPopupButton.click();
                    System.out.println("Restarted.");
                }
                catch (Exception ex){
                    System.out.println("No restarted.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}