package org.soyaga.examples.LongCat;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.LongCat.GA.LongCatGA;
import org.soyaga.examples.LongCat.Graph.Graph;
import org.soyaga.examples.LongCat.Graph.Node;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class LongCatScraper {
    static int catColor = new Color(255,234,0).getRGB();
    static int frameColor = new Color(255,255,255).getRGB();
    static int frameSize = 14;
    static int wallColor;
    static int cellXSize;
    static int cellYSize;

    public static void removeSmallWhiteAreas(BufferedImage img, int minSize, Color replacement) {
        int width = img.getWidth();
        int height = img.getHeight();
        boolean[][] visited = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!visited[x][y] && img.getRGB(x, y)==frameColor) {
                    // Collect connected component of white pixels
                    ArrayList<int[]> component = new ArrayList<>();
                    floodFill(img, x, y, visited, component);

                    // If too small, replace
                    if (component.size() < minSize) {
                        for (int[] p : component) {
                            img.setRGB(p[0], p[1], replacement.getRGB());
                        }
                    }
                }
            }
        }
    }

    private static void floodFill(BufferedImage img, int startX, int startY,
                                  boolean[][] visited, ArrayList<int[]> component) {
        int width = img.getWidth();
        int height = img.getHeight();

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int x = p[0], y = p[1];
            component.add(p);

            // 4-directional neighbors (N,S,E,W) – you can extend to 8 if diagonal counts
            int[][] neighbors = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] d : neighbors) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height
                        && !visited[nx][ny] && img.getRGB(nx, ny)==frameColor) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    public static BufferedImage applyPatches(BufferedImage boardImage){
        wallColor = boardImage.getRGB(0,0);
        BufferedImage cropped = boardImage.getSubimage(boardImage.getWidth()/4,0,boardImage.getWidth()/2,boardImage.getHeight());
        removeSmallWhiteAreas(cropped, 100, new Color(wallColor));
        return cropped;
    }

    public static  BufferedImage cropBoard(BufferedImage boardImage){
        int minX = boardImage.getWidth(), minY = boardImage.getHeight();
        int maxX = 0, maxY = 0;
        for (int y = 0; y < boardImage.getHeight(); y++) {
            for (int x = 0; x < boardImage.getWidth(); x++) {
                if (boardImage.getRGB(x, y) == frameColor) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        int width = maxX-2*frameSize - minX + 1;
        int height = maxY-2*frameSize - minY + 1;
        return boardImage.getSubimage(minX+frameSize, minY+frameSize, width, height);
    }

    private static void replaceCatColors(BufferedImage cropped) {
        for (int y = 0; y < cropped.getHeight(); y++) {
            for (int x = 0; x < cropped.getWidth(); x++) {
                int color = cropped.getRGB(x, y);
                if(color==catColor){
                    for(int h=0;h<cellYSize-1; h++){
                        for(int w=0;w<cellXSize-1; w++){
                            if(x+w>=cropped.getWidth() || y+h>=cropped.getHeight()) continue;
                            cropped.setRGB(x+w,y+h, catColor);
                        }
                    }
                    return;
                }
            }
        }
    }

    public static BufferedImage processImage(BufferedImage boardImage){
        System.out.println("    Applying patches...");
        BufferedImage chopped = applyPatches(boardImage);
        System.out.println("    Patches applied.");
        System.out.println("    Cropping image...");
        BufferedImage cropped = cropBoard(chopped);
        System.out.println("    Image Crop.");

        System.out.println("    Computing size...");
        computeCellSize(cropped);
        System.out.println("    Size computed.");

        System.out.println("    Replacing cat colors...");
        replaceCatColors(cropped);
        System.out.println("    Cat colors replaced.");
        return cropped;
    }

    private static void computeCellSize(BufferedImage cropped) {
        int minX = cropped.getWidth();
        int maxX = 0;
        boolean rowMeasured = false;
        for (int y = 0; y < cropped.getHeight(); y++) {
            for (int x = 0; x < cropped.getWidth(); x++) {
                if (cropped.getRGB(x, y) == catColor) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    rowMeasured = true;
                }
            }
            if(rowMeasured) break;
        }
        cellXSize = 2+maxX-minX;

        int mixedColor1 = new Color(255,250,191).getRGB();
        int mixedColor2 = new Color(255,239,64).getRGB();
        int minY = cropped.getHeight();
        int maxY = 0;
        boolean colMeasured = false;
        for (int x = 0; x < cropped.getWidth(); x++) {
            for (int y = 0; y < cropped.getHeight(); y++) {
                if (cropped.getRGB(x, y) == catColor) {
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                    colMeasured = true;
                } else if (colMeasured && (cropped.getRGB(x, y) == frameColor || cropped.getRGB(x, y)== mixedColor1|| cropped.getRGB(x, y)== mixedColor2)) {
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                } else if (colMeasured) {
                    break;
                }
            }
            if(colMeasured) break;
        }
        cellYSize = 2+maxY-minY;
        cellXSize = Math.max(cellXSize, cellYSize);
        cellYSize = cellXSize;
    }

    public static double colorDistance(int color1, int color2) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        return Math.sqrt(
                Math.pow(r1 - r2, 2) +
                        Math.pow(g1 - g2, 2) +
                        Math.pow(b1 - b2, 2)
        );
    }

    private static Graph computeBoardState(BufferedImage cropped, int rows, int cols) {
        Node[][] boardState = new Node[rows][cols];
        Node startingNode = null;
        int numberOfNodes = 0;
        //Create Nodes
        for(int row=0; row<rows; row++){
            for(int col=0; col<cols; col++){
               int cellColor = cropped.getRGB((int) ((col+0.5) * cellXSize), (int) ((row+0.5)*cellYSize));
               if(colorDistance(cellColor,wallColor)<10) {
                   boardState[row][col] = null;
               }
               else if (colorDistance(cellColor,catColor)<10) {
                   startingNode = new Node(row,col);
                   boardState[row][col] = startingNode;
                   numberOfNodes++;
               }
               else  {
                   boardState[row][col] = new Node(row,col);
                   numberOfNodes++;
               }

            }
        }

        //Join nodes
        for(int row=0; row<rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node currentNode = boardState[row][col];
                if (currentNode!=null){
                    if(row>0) {
                        Node northNode = boardState[row-1][col];
                        if(northNode !=null) currentNode.addNode(northNode);
                    }
                    if(col<cols-1) {
                        Node eastNode = boardState[row][col+1];
                        if(eastNode !=null) currentNode.addNode(eastNode);
                    }
                    if(row<rows-1) {
                        Node southNode = boardState[row+1][col];
                        if(southNode !=null) currentNode.addNode(southNode);
                    }
                    if(col>0) {
                        Node westNode = boardState[row][col-1];
                        if(westNode !=null) currentNode.addNode(westNode);
                    }
                }
            }
        }
        return new Graph(boardState, numberOfNodes,rows,cols,startingNode);
    }

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();
        Actions actions = new Actions(driver);

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading https://poki.com/en/g/longcat: ...");
            driver.get("https://poki.com/en/g/longcat");
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));


            System.out.println("Refreshing in case it did not load...");
            driver.navigate().refresh();
            System.out.println("Refreshed.");

            System.out.println("Accepting cookies...");
            try {
                WebElement accentCookies = longWait.until(
                        ExpectedConditions.elementToBeClickable(By.id("accept-btn"))
                );
                accentCookies.click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception ex){
                System.out.println("No cookies detected");
            }

            System.out.println("Centering and clicking...");
            WebElement container =null;
            try {
                container = driver.findElement(By.id("game-player"));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                        container
                );
                Thread.sleep(2000);
                container.click();
                System.out.println("Centered and clicked.");
            }
            catch (Exception ex){
                System.out.println("Not centered and clicked.");
            }

            for(int loop=0; loop<50; loop++){
                System.out.println("Screenshot the Game...");
                BufferedImage image = null;
                try {
                    assert container != null;
                    Thread.sleep(4000);
                    File screenshot = container.getScreenshotAs(OutputType.FILE);
                    image = ImageIO.read(screenshot);
                    System.out.println("Game screenshot.");
                }
                catch (Exception e) {
                    System.out.println("Game not screenshot");
                }

                System.out.println("Processing image...");
                BufferedImage cropped = null;
                try{
                    cropped = processImage(image);
                    System.out.println("Image processed.");
                }
                catch(Exception e){
                    System.out.println("Image not processed.");
                }

                System.out.println("Computing cell states...");
                Integer rows, cols;
                Graph boardState = null;
                try{
                    rows = Math.toIntExact(Math.round(cropped.getHeight()*1./cellYSize));
                    cols =  Math.toIntExact(Math.round(cropped.getWidth()*1./cellXSize));
                    boardState = computeBoardState(cropped, rows, cols);
                    System.out.println("Cell states computed.");
                }
                catch(Exception e){
                    System.out.println("Cell states not computed.");
                }


                System.out.println("Building GAModel...");
                assert boardState != null;
                LongCatGA model = new LongCatGA("LongCatModel",boardState);
                System.out.println("GAModel built.");

                System.out.println("Running MathModel...");
                model.optimize();
                System.out.println("MathModel run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<String> result = (ArrayList<String>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                if (resultText.startsWith("GA_Optimal")) {
                    for (String movement : result) {
                        Thread.sleep(300+loop*10);
                        switch (movement) {
                            case "W":
                                actions.sendKeys(Keys.LEFT).perform();
                                break;
                            case "E":
                                actions.sendKeys(Keys.RIGHT).perform();
                                break;
                            case "N":
                                actions.sendKeys(Keys.UP).perform();
                                break;
                            case "S":
                                actions.sendKeys(Keys.DOWN).perform();
                                break;
                        }
                    }
                    System.out.println("Optimal solution introduced.");
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Continuing game...");
                Thread.sleep(2000);
                actions.sendKeys(Keys.ENTER).perform();
                Thread.sleep(2000);
                System.out.println("Game continued.");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

}
