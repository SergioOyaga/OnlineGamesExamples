package org.soyaga.examples.Bridges;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.Bridges.Graph.Bridge;
import org.soyaga.examples.Bridges.Graph.Rule;
import org.soyaga.examples.Bridges.MathModel.BridgesMathModel;
import org.soyaga.examples.Bridges.MathModel.CallBack.ConnectedSolutionCallBack;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class BridgesScraper {

    public static int cellSizePx = 18;
    public static int bridgePerpendicularShiftPx = 6;
    public static int bridgeParallelShiftPx = 9;

    public static void main(String[] args) throws AWTException {

        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        EdgeDriver driver = new EdgeDriver();
        Actions action = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading https://www.puzzle-bridges.com/: ...");
            driver.get("https://www.puzzle-bridges.com/?size=18"); // Hard
            //driver.get("https://www.puzzle-bridges.com/?size=17"); // medium
            //driver.get("https://www.puzzle-bridges.com/");  //Easy
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            try {
                driver.manage().window().setSize(new Dimension(600, 800));
                driver.manage().window().setPosition(new Point(0, 0));
                WebElement zoomElement = driver.findElement(By.id("zoomslider"));
                js.executeScript(
                        "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
                        zoomElement, 1.0
                );
                System.out.println("Refreshed.");
            }
            catch (Exception e) {
                System.out.println("Not Refreshed.");
            }

            System.out.println("Accepting cookies...");
            try{
                longWait.until(ExpectedConditions.elementToBeClickable(By.id("accept-choices"))).click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception e) {
                System.out.println("Cookies not accepted.");
            }

            System.out.println("Scrolling...");
            try {
                WebElement elementB = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoLink")));
                js.executeScript(
                        "arguments[0].scrollIntoView({block:'start', inline:'nearest'});",
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

            System.out.println("Retrieving grid properties...");
            ArrayList<Bridge> bridges = new ArrayList<>();
            ArrayList<Rule> valueRules = new ArrayList<>();
            HashMap<Bridge,HashSet<Bridge>> crossingRule = new HashMap<>();
            HashMap<Integer, HashMap<Integer,Rule>> ruleMap = new HashMap<>();
            try {

                WebElement nodesGrid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".board-tasks.selectable")));
                List<WebElement> nodesChildren = nodesGrid.findElements(By.className("bridges-task-cell"));
                for(WebElement child:nodesChildren){
                    int col = Integer.parseInt(child.getAttribute("col"));
                    int row = Integer.parseInt(child.getAttribute("row"));
                    int value = Integer.parseInt(child.getText());
                    Rule rule = new Rule(row,col,value,child);
                    valueRules.add(rule);
                    ruleMap.putIfAbsent(row, new HashMap<>());
                    ruleMap.get(row).putIfAbsent(col, rule);
                }

                WebElement bridgesGrid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".board-bridges")));
                ArrayList<Bridge> verticalBridges = new ArrayList<>();
                int id =0;
                List<WebElement> bridgesChildren = bridgesGrid.findElements(By.cssSelector(".bridges-bridge-down"));
                for(WebElement child:bridgesChildren){
                    double top    = Double.parseDouble(child.getCssValue("top").replace("px",""));
                    double left  = Double.parseDouble(child.getCssValue("left").replace("px",""));
                    double height= Double.parseDouble(child.getCssValue("height").replace("px",""));
                    int rowStart = (int) Math.round((top- bridgePerpendicularShiftPx)/cellSizePx);
                    int colStart = (int) Math.round((left- bridgeParallelShiftPx)/cellSizePx);
                    int rowEnd = (int) Math.round((top- bridgePerpendicularShiftPx +height)/cellSizePx);
                    int colEnd = (int) Math.round((left- bridgeParallelShiftPx)/cellSizePx);
                    Rule ruleStart = ruleMap.get(rowStart).get(colStart);
                    Rule ruleEnd = ruleMap.get(rowEnd).get(colEnd);
                    Bridge bridge = new Bridge(id, child, "down", ruleStart, ruleEnd);
                    ruleStart.bridges.add(bridge);
                    ruleEnd.bridges.add(bridge);
                    bridges.add(bridge);
                    verticalBridges.add(bridge);
                    id++;
                }

                bridgesChildren = bridgesGrid.findElements(By.cssSelector(".bridges-bridge-right"));
                ArrayList<Bridge> horizontalBridges = new ArrayList<>();
                for(WebElement child:bridgesChildren){
                    double top    = Double.parseDouble(child.getCssValue("top").replace("px",""));
                    double left  = Double.parseDouble(child.getCssValue("left").replace("px",""));
                    double width = Double.parseDouble(child.getCssValue("width").replace("px",""));
                    int rowStart = (int) Math.round((top- bridgeParallelShiftPx)/cellSizePx);
                    int colStart = (int) Math.round((left- bridgePerpendicularShiftPx)/cellSizePx);
                    int rowEnd = (int) Math.round((top-  bridgeParallelShiftPx)/cellSizePx);
                    int colEnd = (int) Math.round((left- bridgePerpendicularShiftPx +width)/cellSizePx);
                    Rule ruleStart = ruleMap.get(rowStart).get(colStart);
                    Rule ruleEnd = ruleMap.get(rowEnd).get(colEnd);
                    Bridge bridge = new Bridge(id, child, "right", ruleStart, ruleEnd);
                    ruleStart.bridges.add(bridge);
                    ruleEnd.bridges.add(bridge);
                    bridges.add(bridge);
                    horizontalBridges.add(bridge);
                    id++;
                }

                for(Bridge verticalBridge:verticalBridges){
                    for(Bridge horizontalBridge:horizontalBridges){
                        if(bridgesCross(verticalBridge,horizontalBridge)){
                            crossingRule.putIfAbsent(verticalBridge,new HashSet<>());
                            crossingRule.get(verticalBridge).add(horizontalBridge);
                        }
                    }
                }
                System.out.println("Grid properties retrieved.");
            }
            catch (Exception ex){
                    System.out.println("Grid properties not retrieved.");
            }

            System.out.println("Building CP Model...");
            BridgesMathModel model = new BridgesMathModel("BridgesSAT", valueRules,crossingRule, bridges);
            System.out.println("CP Model built.");

            System.out.println("Running CP Model...");
            model.optimize();
            System.out.println("CP Model run.");

            String results = model.getResult();

            if("CP_Optimal".equals(results)){
                HashMap<Bridge,Integer> result = ((ConnectedSolutionCallBack)model.getSolutionCallback()).getResultMap();
                for(Map.Entry<Bridge,Integer> entryMap:result.entrySet()){
                    Bridge bridge = entryMap.getKey();
                    String type = bridge.type();
                    WebElement closeNode = bridge.ruleFrom().element;
                    int xOffset = type.equals("right")? cellSizePx/2 + 1 : 0;
                    int yOffset = type.equals("down")? cellSizePx/2 + 1 : 0;
                    action.moveToElement(closeNode)
                            .moveByOffset(xOffset, yOffset);
                    for (int i=0; i<entryMap.getValue();i++){
                        action.click();
                    }
                    try {
                        action.perform();
                    }
                    catch (Exception ex){
                        System.out.println("Not actionable.");
                    }
                }
            }
            else {
                System.out.println("Solution not found.");
            }

            System.out.println("Scrolling...");
            try {
                WebElement elementB = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("ajaxResponse")));
                js.executeScript(
                        "arguments[0].scrollIntoView({block:'start', inline:'nearest'});",
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
            Thread.sleep(1000);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }
    public static boolean bridgesCross(Bridge b1, Bridge b2) {
        Rule a = b1.ruleFrom();
        Rule b = b1.ruleTo();
        Rule c = b2.ruleFrom();
        Rule d = b2.ruleTo();
        // ignore if they share an endpoint
        if (a.equals(c) || a.equals(d) || b.equals(c) || b.equals(d)) {
            return false;
        }
        return segmentsIntersect(a.row, a.col, b.row, b.col, c.row, c.col, d.row, d.col);
    }

    private static boolean segmentsIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int o1 = orientation(x1, y1, x2, y2, x3, y3);
        int o2 = orientation(x1, y1, x2, y2, x4, y4);
        int o3 = orientation(x3, y3, x4, y4, x1, y1);
        int o4 = orientation(x3, y3, x4, y4, x2, y2);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special collinear cases
        if (o1 == 0 && onSegment(x1, y1, x2, y2, x3, y3)) return true;
        if (o2 == 0 && onSegment(x1, y1, x2, y2, x4, y4)) return true;
        if (o3 == 0 && onSegment(x3, y3, x4, y4, x1, y1)) return true;
        if (o4 == 0 && onSegment(x3, y3, x4, y4, x2, y2)) return true;

        return false;
    }

    private static int orientation(int x1, int y1, int x2, int y2, int x3, int y3) {
        long val = (long)(y2 - y1) * (x3 - x2)
                - (long)(x2 - x1) * (y3 - y2);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    private static boolean onSegment(int x1, int y1, int x2, int y2, int x3, int y3) {
        return x3 >= Math.min(x1, x2) &&
                x3 <= Math.max(x1, x2) &&
                y3 >= Math.min(y1, y2) &&
                y3 <= Math.max(y1, y2);
    }

}
