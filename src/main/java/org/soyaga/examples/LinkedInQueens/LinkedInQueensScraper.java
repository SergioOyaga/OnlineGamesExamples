package org.soyaga.examples.LinkedInQueens;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.LinkedInQueens.MathModel.QueensMathModel;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedInQueensScraper {

    /**
     * Method to parse rgba string to Color
      */
    public static Color parseRgbaToColor(String rgba) {
        Pattern pattern = Pattern.compile("rgba?\\((\\d+),\\s*(\\d+),\\s*(\\d+)(?:,\\s*([\\d.]+))?\\)");
        Matcher matcher = pattern.matcher(rgba);

        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            float a = matcher.group(4) != null ? Float.parseFloat(matcher.group(4)) : 1f;
            return new Color(r, g, b, Math.round(a * 255)); // Alpha in 0–255
        } else {
            throw new IllegalArgumentException("Invalid RGBA format: " + rgba);
        }
    }

    public static void main(String[] args) {
        //Credentials for LinkedIn
        String linkedInUser ="Your_LinkedIn_mail@example.com";
        String linkedInPassword ="Your_LinkedIn_password";
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win32\\msedgedriver.exe";
        // dimensions
        int rows = 0;
        int cols = 0;

        //Grid of buttons
        WebElement[][] gridButtons = null;
        //Grid of colors
        Color[][] gridColors= null;

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver",seleniumPath);

        WebDriver driver = new EdgeDriver();

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
            driver.get("https://www.linkedin.com/games/queens/");
            longWait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/games/queens/"));
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
                gridColors = new Color[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int index = i * cols + j;
                        WebElement cell = allButtons.get(index);
                        gridButtons[i][j]= cell;
                        Color color = parseRgbaToColor(cell.getCssValue("background-color"));
                        gridColors[i][j] = color;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Grid not retrieved.");
            }
            if (rows==0) {
                // Bypass the human test
                try {
                    System.out.println("Detecting checkpoint...");
                    WebElement checkButton = driver.findElement(By.xpath("//button[text()='Start Puzzle']"));
                    checkButton.click();
                }
                catch (Exception e) {
                    System.out.println("Checkpoint not detected.");
                }

                // click the start button
                try {
                    // First, switch to the correct iframe
                    System.out.println("Switching to iframe...");
                    WebElement iframe = longWait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.game-launch-page__iframe"))
                    );
                    driver.switchTo().frame(iframe);
                    System.out.println("Iframe switched.");

                    // Search the start button and click it
                    WebElement startButton = longWait.until(
                            ExpectedConditions.elementToBeClickable(By.id("launch-footer-start-button"))
                    );
                    startButton.click();
                }
                catch (Exception e) {
                    System.out.println("Start button not found.");
                }
                driver.switchTo().defaultContent();

                // decline cookies
                try {
                    // Search the reject button and click it
                    WebElement rejectBtn = longWait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-control-name='ga-cookie.consent.deny.v4']"))
                    );
                    rejectBtn.click();
                }
                catch (Exception e) {
                    System.out.println("Cookies not found.");
                }

                // skip the tutorial
                try {
                    // First, switch to the correct iframe
                    longWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));

                    // Now locate your specific iframe
                    WebElement iframe = driver.findElement(By.cssSelector("iframe[title='games']"));

                    // Switch into the iframe
                    driver.switchTo().frame(iframe);

                    // Wait for the Dismiss button inside the iframe
                    WebElement dismissButton = longWait.until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button.artdeco-modal__dismiss[aria-label='Dismiss']")
                    ));

                    // Click the button
                    dismissButton.click();
                }
                catch (Exception e) {
                    System.out.println("Tutorial not found.");
                }
                driver.switchTo().defaultContent();

                // switch to correct iframe
                try {
                    // Move to the correct iframe
                    WebElement iframe = longWait.until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("iframe[title='games']")
                    ));
                    driver.switchTo().frame(iframe);
                }
                catch (Exception e) {
                    System.out.println("Iframe not found.");
                }

                try {
                    // Wait for the grid to appear
                    WebElement grid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("queens-grid")));

                    //Compute the dimensions
                    List<WebElement> allButtons = grid.findElements(By.cssSelector("div[role='button']"));
                    allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));

                    String style = grid.getAttribute("style"); // "--rows: N; --cols: N"
                    Pattern pattern = Pattern.compile("--rows:\\s*(\\d+);\\s*--cols:\\s*(\\d+)");
                    Matcher matcher = pattern.matcher(style);
                    if (matcher.find()) {
                        rows = Integer.parseInt(matcher.group(1));
                        cols = Integer.parseInt(matcher.group(2));
                    }
                    rows = allButtons.size()/cols;

                    // Get all buttons inside the grid
                    allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                    // Parse into 2D list
                    gridButtons = new WebElement[rows][cols];
                    gridColors = new Color[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int index = i * cols + j;
                            WebElement cell = allButtons.get(index);
                            gridButtons[i][j]= cell;
                            Color color = parseRgbaToColor(cell.getCssValue("background-color"));
                            gridColors[i][j] = color;
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Grid not retrieved.");
                }
            }

            // Create optimization object
            QueensMathModel mathModel = new QueensMathModel("QueensMM", gridColors);
            mathModel.run();
            Object [] results =  (Object []) mathModel.getResult();
            String resultText = (String)results[0];
            ArrayList<Integer> result = (ArrayList<Integer>) results[1];
            if(resultText.startsWith("MPSOLVER_OPTIMAL")){
                for(int i = 0;i< rows; i++){
                    WebElement buttonToClick = gridButtons[i][result.get(i)];
                    buttonToClick.click();
                    buttonToClick.click();
                }
                System.out.println("Optimal introduced.");
            }
            else {
                System.out.println("Optimal solution not found.");
            }
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
