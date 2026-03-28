package org.soyaga.examples.LinkedInPatches;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.LinkedInPatches.MathModel.LinkedInPatchesMMInitializer;
import org.soyaga.examples.LinkedInPatches.MathModel.LinkedInPatchesMathModel;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedInPatchesScraper {

    /**
     * Method to parse rgba string to Color with no alpha
      */
    public static Color parseRgbaToColor(String rgba) {
        Pattern pattern = Pattern.compile("rgba?\\((\\d+),\\s*(\\d+),\\s*(\\d+)(?:,\\s*([\\d.]+))?\\)");
        Matcher matcher = pattern.matcher(rgba);

        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            return new Color(r, g, b);
        } else {
            throw new IllegalArgumentException("Invalid RGBA format: " + rgba);
        }
    }

    public static void main(String[] args) {
        //Credentials for LinkedIn
        String linkedInUser ="Your_LinkedIn_mail@example.com";
        String linkedInPassword ="Your_LinkedIn_password";
        //String linkedInUser ="Your_LinkedIn_mail@example.com";
        //String linkedInPassword ="Your_LinkedIn_password";
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win32\\msedgedriver.exe";
        // dimensions
        int rows = 0;
        int cols = 0;

        //Grid of buttons
        WebElement[][] gridButtons = null;
        //Data
        LinkedHashMap<Color, Integer[]> pointByColor = new LinkedHashMap<>();
        LinkedHashMap<Color,Integer> numberByColor = new LinkedHashMap<>();
        HashSet<Color> squareShapes = new HashSet<>();
        HashSet<Color> verticalRectShapes = new HashSet<>();
        HashSet<Color> horizontalRectShapes = new HashSet<>();
        HashSet<Color> unknownShapes = new HashSet<>();

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver",seleniumPath);

        WebDriver driver = new EdgeDriver();
        Actions actions = new Actions(driver);

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            // Go to LinkedIn login
            System.out.println("Loading https://www.linkedin.com/: ...");
            driver.get("https://www.linkedin.com/login");
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(600, 800));
            driver.manage().window().setPosition(new Point(0,0));
            System.out.println("Refreshed.");
            
            // Login
            System.out.println("Logging-in...");
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(linkedInUser);
            password.sendKeys(linkedInPassword);
            longWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
            Thread.sleep(2000);
            System.out.println("Logged.");

            // Wait/load, then navigate to the game
            System.out.println("Loading game...");
            driver.get("https://www.linkedin.com/games/patches/");
            longWait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/games/patches/"));
            System.out.println("Game loaded.");

            // try to run directly the optimization
            try {
                // Wait for the grid to appear
                System.out.println("Retrieving Grid...");
                WebElement grid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='interactive-grid']")));

                //Compute the dimensions
                List<WebElement> allButtons = grid.findElements(By.cssSelector("[data-testid^='cell-']"));
                String cssVar = grid.getAttribute("style").split(" ")[1].replace(";", "").trim();
                try {
                    cols = Integer.parseInt(cssVar);
                } catch (NumberFormatException e) {
                    cols= 1;
                }
                rows = allButtons.size()/cols;

                // Get all buttons inside the grid
                allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                // Parse into 2D list
                gridButtons = new WebElement[rows][cols];
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        int index = r * cols + c;
                        WebElement cell = allButtons.get(index);
                        gridButtons[r][c]= cell;
                        List<WebElement> elements = cell.findElements(By.cssSelector("[data-shape^='PatchesShapeConstraint_']"));
                        if(elements.isEmpty())continue;
                        WebElement element = elements.get(0);
                        Color color = parseRgbaToColor(element.getCssValue("background-color"));
                        //Color
                        pointByColor.put(color,new Integer[]{r,c});
                        //Shape
                        String shape = element.getAttribute("data-shape").replace("PatchesShapeConstraint_","");
                        switch(shape){
                            case "UNKNOWN"->{
                                unknownShapes.add(color);
                                break;
                            }
                            case "HORIZONTAL_RECT"->{
                                horizontalRectShapes.add(color);
                                break;
                            }
                            case "VERTICAL_RECT"->{
                                verticalRectShapes.add(color);
                                break;
                            }
                            case "SQUARE"->{
                                squareShapes.add(color);
                                break;
                            }
                            default -> System.out.println("Shape not identify "+ shape);
                        }
                        //Number
                        WebElement parent = element.findElement(By.xpath(".."));
                        String number = parent.getText();
                        if(!number.isEmpty())numberByColor.put(color, Integer.valueOf(number));
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Grid not retrieved.");
            }

            // Create optimization object
            LinkedInPatchesMathModel mathModel = new LinkedInPatchesMathModel("PatchesMM",
                    new LinkedInPatchesMMInitializer(
                            rows,
                            cols,
                            pointByColor,
                            numberByColor,
                            squareShapes,
                            verticalRectShapes,
                            horizontalRectShapes,
                            unknownShapes
                    ),
                    pointByColor);
            mathModel.optimize();
            Object []results = mathModel.getResult();
            String resultText = (String)results[0];
            HashMap<Color,ArrayList<Integer[]>> result = (HashMap<Color,ArrayList<Integer[]>>) results[1];
            if(resultText.startsWith("CP_Optimal")){
                for(Map.Entry<Color,Integer[]> colorEntry: pointByColor.entrySet()){
                    Integer [] colorHintPoint = colorEntry.getValue();
                    WebElement colorHintElement = gridButtons[colorHintPoint[0]][colorHintPoint[1]];
                    ArrayList<Integer[]> colorShapeCoord = result.get(colorEntry.getKey());
                    Integer [] colorStartPoint = colorShapeCoord.get(0);
                    WebElement colorStartElement = gridButtons[colorStartPoint[0]][colorStartPoint[1]];
                    Integer [] colorEndPoint = colorShapeCoord.get(1);
                    WebElement colorEndElement = gridButtons[colorEndPoint[0]][colorEndPoint[1]];
                    actions.clickAndHold(colorHintElement)
                            .pause(Duration.ofMillis(30))
                            .moveToElement(colorStartElement)
                            .pause(Duration.ofMillis(30))
                            .moveToElement(colorEndElement)
                            .pause(Duration.ofMillis(30))
                            .release()
                            .build()
                            .perform();
                }
                System.out.println("CP solution introduced.");
            }
            else {
                System.out.println("CP solution not found.");
            }
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}