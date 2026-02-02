package org.soyaga.examples.RushHour;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.RushHour.Board.Movement;
import org.soyaga.examples.RushHour.GA.RushHoursGA;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.Duration;
import java.util.*;

public class RushHourScraper {

    public static int cellSize = 0;

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        EdgeDriver driver = new EdgeDriver();
        Actions actions = new Actions(driver);

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading https://rushhourgame.thinkfun.com/: ...");
            driver.get("https://rushhourgame.thinkfun.com/");
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
            Thread.sleep(500);
            driver.navigate().refresh();
            System.out.println("Refreshed.");

            System.out.println("Retrieving cell size...");
            try {
                cellSize = driver.findElement(By.id("cell-0")).getSize().getWidth();
                System.out.println("Cell size retrieved.");
            }
            catch (Exception ex){
                    System.out.println("Cell size not retrieved");
            }

            for(int loop=0; loop<2; loop++){
                Thread.sleep(1000);
                System.out.println("Getting vehicle elements...");
                LinkedHashMap<String, WebElement> vehiclesById = new LinkedHashMap<>();
                try {
                    vehiclesById = findVehiclesElementsByID(driver);
                    System.out.println("Vehicle elements got.");
                }
                catch (Exception ex){
                    System.out.println("Vehicle elements not got.");
                }

                System.out.println("Getting level info...");
                LinkedHashMap<String, Object[]> vehiclesInfo = new LinkedHashMap<>();

                try {
                    vehiclesInfo= findVehiclesInfoByID(vehiclesById);
                    System.out.println("Level info got.");
                }
                catch (Exception ex){
                    System.out.println("Level info not got.");
                }

                System.out.println("Building GAModel...");
                assert !vehiclesInfo.isEmpty();
                RushHoursGA model = new RushHoursGA("RushHour", vehiclesInfo, 100);
                System.out.println("GAModel built.");

                System.out.println("Running GAModel...");
                model.optimize();
                System.out.println("GAModel run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<Movement> result = (ArrayList<Movement>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                if (resultText.startsWith("GA_Optimal")) {
                    result.remove(result.size()-1);
                    for(Movement movement:result){
                        WebElement vehicle = vehiclesById.get(movement.vehicleID);
                        dragSmooth(robot, driver, vehicle, movement.direction, movement.distance, cellSize);
                    }
                    dragSmooth2(robot, driver, vehiclesById.get("CAR-RED"), "E", 6, cellSize);
                    System.out.println("Optimal solution introduced.");
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Continuing game...");
                try {
                    actions.sendKeys("GA").perform();
                    WebElement nextButton = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[normalize-space()='Next']")));
                    nextButton.click();
                    Thread.sleep(2000);
                    WebElement nextLevelButton = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[normalize-space()='Next Level']")));
                    nextLevelButton.click();
                    WebElement nextButton2 = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[normalize-space()='Next']")));
                    nextButton2.click();
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

    private static LinkedHashMap<String, WebElement> findVehiclesElementsByID(EdgeDriver driver){
        LinkedHashMap<String, WebElement> vehiclesElementByID = new LinkedHashMap<>();
        for (WebElement vehicle : driver.findElements(By.cssSelector("div.car"))) {
            String imageStyle = vehicle.findElement(By.cssSelector("div.w-full")).getAttribute("style");
            String color = imageStyle
                    .replaceAll(".*media/", "")
                    .replaceAll("-vertical", "")
                    .replaceAll("\\..*", "");

            color = color.toUpperCase(); // optional

            vehiclesElementByID.put(color, vehicle);
        }
        return vehiclesElementByID;
    }

    private static LinkedHashMap<String, Object[]> findVehiclesInfoByID(LinkedHashMap<String, WebElement> vehicleElements){
        LinkedHashMap<String, Object[]> vehiclesInfoByID = new LinkedHashMap<>();
        for (Map.Entry<String,WebElement> vehicleEntry : vehicleElements.entrySet()) {
            WebElement vehicle = vehicleEntry.getValue();
            String style = vehicle.getAttribute("style");
            int translateX = Integer.parseInt(style
                    .replaceAll(".*translateX\\(", "")
                    .replaceAll("px\\).*", ""));
            int translateY = Integer.parseInt(style
                    .replaceAll(".*translateY\\(", "")
                    .replaceAll("px\\).*", ""));

            Dimension d = vehicle.getSize();
            int w = d.getWidth();
            int h = d.getHeight();

            cellSize = Math.min(w,h);

            int row = translateY / cellSize;
            int col = translateX / cellSize;

            boolean horizontal = w > h;
            int length = Math.round((horizontal ? w : h) / (float) cellSize);

            vehiclesInfoByID.put(vehicleEntry.getKey(), new Object[]{row,col,length,horizontal});
        }
        return vehiclesInfoByID;
    }

    private static Point getElementCenterOnScreen(WebDriver driver, WebElement element) {
        Point elementLocation = element.getLocation();
        Dimension elementSize = element.getSize();

        // Center of element in viewport
        int centerX = elementLocation.getX() + elementSize.getWidth() / 2;
        int centerY = elementLocation.getY() + elementSize.getHeight() / 2;

        // Browser window position on screen
        Point windowPos = ((EdgeDriver)driver).manage().window().getPosition();

        // Chrome / Edge window decorations offset
        int browserChromeOffsetY = 120; // typical on Windows
        int browserChromeOffsetX = 8;

        return new Point(
                windowPos.getX() + browserChromeOffsetX + centerX,
                windowPos.getY() + browserChromeOffsetY + centerY
        );
    }

    private static Point computeTarget(Point start, String direction, int distance, int cellSize) {
        int pixels = distance * cellSize;

        int x = start.getX();
        int y = start.getY();

        switch (direction) {
            case "N": y -= pixels; break;
            case "S": y += pixels; break;
            case "E": x += pixels; break;
            case "W": x -= pixels; break;
        }

        return new Point(x, y);
    }

    private static void dragSmooth(Robot robot, WebDriver driver, WebElement element, String direction, int distance, int cellSize) throws InterruptedException {
        Point from = getElementCenterOnScreen(driver, element);
        Point to = computeTarget(from,direction,distance,cellSize);
        adjustPoint(robot,from,to,driver, element, cellSize);
    }
    private static void dragSmooth2(Robot robot, WebDriver driver, WebElement element, String direction, int distance, int cellSize) throws InterruptedException {
        Point from = getElementCenterOnScreen(driver, element);
        Point to = computeTarget(from,direction,distance,cellSize);robot.mouseMove(from.getX(), from.getY());
        Thread.sleep(100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(100);
        int size = Math.max(Math.abs(to.getX() - from.getX()),Math.abs(to.getY() - from.getY()));
        int stepSize =5;
        int steps = size/stepSize;
        double dx = (to.getX() - from.getX()) / (double) steps;
        double dy = (to.getY() - from.getY()) / (double) steps;
        for (int i = 1; i <= steps+4; i++) {
            int x = (int) (from.getX() + dx * i);
            int y = (int) (from.getY() + dy * i);
            robot.mouseMove(x, y);
            Thread.sleep(2);
        }
        Thread.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(100);
    }

    private static void adjustPoint(Robot robot, Point from, Point to, WebDriver driver, WebElement element, int cellSize) throws InterruptedException {
        robot.mouseMove(from.getX(), from.getY());
        Thread.sleep(100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(100);
        int size = Math.max(Math.abs(to.getX() - from.getX()),Math.abs(to.getY() - from.getY()));
        int stepSize =5;
        int steps = size/stepSize;
        double dx = (to.getX() - from.getX()) / (double) steps;
        double dy = (to.getY() - from.getY()) / (double) steps;
        for (int i = 1; i <= steps+4; i++) {
            int x = (int) (from.getX() + dx * i);
            int y = (int) (from.getY() + dy * i);
            robot.mouseMove(x, y);
            Thread.sleep(2);
        }
        Thread.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(100);
        Point newFrom = getElementCenterOnScreen(driver, element);
        if(((Math.abs(newFrom.x - to.x)>cellSize/2) ||(Math.abs(newFrom.y- to.y)>cellSize/2))){
            adjustPoint(robot,newFrom,to,driver, element,cellSize);
        }
    }

}
