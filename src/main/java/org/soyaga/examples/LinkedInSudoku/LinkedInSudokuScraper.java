package org.soyaga.examples.LinkedInSudoku;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.LinkedInSudoku.MathModel.SudokuMathModel;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedInSudokuScraper {

    public static void main(String[] args) {
        //Credentials for LinkedIn
        String linkedInUser ="Your_LinkedIn_mail@example.com";
        String linkedInPassword ="Your_LinkedIn_password";
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // dimensions
        int rows = 0;
        int cols = 0;
        int rowGroup = 3;
        int colGroup = 3;

        //Grid of buttons
        WebElement[][] gridButtons = null;
        Integer[][] gridNumbers = null;
        HashMap<String, WebElement> inputMap = new HashMap<>();

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(2000));
            WebDriverWait sortWait = new WebDriverWait(driver, Duration.ofMillis(10));

            // Go to LinkedIn login
            driver.get("https://www.linkedin.com/login");

            // Login
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(linkedInUser);
            password.sendKeys(linkedInPassword);
            WebElement loggingButton = longWait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))
            );
            loggingButton.click();

            System.out.println("Loading sudoku: ...");
            // Wait/load, then navigate to the game
            Thread.sleep(2000);
            driver.get("https://www.linkedin.com/games/mini-sudoku/");
            longWait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/games/mini-sudoku/"));
            System.out.println("Loaded.");


            // try to run directly the optimization
            try {
                // Wait for the grid to appear
                WebElement grid =sortWait.until(
                        ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[contains(@class,'sudoku-grid') and contains(@class,'grid-game-board')]")
                        )
                );

                //Compute the dimensions
                String style = grid.getAttribute("style"); // "--rows: N; --cols: N"
                Pattern pattern = Pattern.compile("--rows:\\s*(\\d+);\\s*--cols:\\s*(\\d+)");
                assert style != null;
                Matcher matcher = pattern.matcher(style);
                if (matcher.find()) {
                    rows = Integer.parseInt(matcher.group(1));
                    cols = Integer.parseInt(matcher.group(2));
                }
                String grouping = grid.getAttribute("class");
                Pattern patternRows = Pattern.compile("-rows--(\\d+)");
                assert grouping != null;
                Matcher matcherRows = patternRows.matcher(grouping);
                if (matcherRows.find()) {
                    rowGroup = Integer.parseInt(matcherRows.group(1));
                }
                Pattern patternCols = Pattern.compile("-cols--\\s*(\\d+)");
                Matcher matcherCols = patternCols.matcher(grouping);
                if (matcherCols.find()) {
                    colGroup = Integer.parseInt(matcherCols.group(1));
                }

                List<WebElement> allInputButtons = driver.findElements(By.cssSelector("button.sudoku-input-button"));
                for (WebElement inputButton : allInputButtons) {
                    inputMap.put(inputButton.getText().trim(), inputButton);
                }
                // Get all buttons inside the grid
                List<WebElement> allButtons = grid.findElements(By.cssSelector(".sudoku-cell"));
                allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                // Parse into 2D list
                gridButtons = new WebElement[rows][cols];
                gridNumbers = new Integer[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int index = i * cols + j;
                        WebElement cell = allButtons.get(index);
                        gridButtons[i][j]= cell;
                        String value = cell.getText().trim();
                        if(!value.isEmpty()) {
                            gridNumbers[i][j] = Integer.valueOf(value);
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Grid not found.");
            }
            if (rows==0) {
                // Bypass the human test
                try {
                    WebElement checkButton = sortWait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Start Puzzle']"))
                    );
                    checkButton.click();
                }
                catch (Exception e) {
                    System.out.println("Checkpoint not detected.");
                }

                // click the start button
                try {
                    // First, switch to the correct iframe
                    WebElement iframe = longWait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.game-launch-page__iframe"))
                    );
                    driver.switchTo().frame(iframe);

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

                // search the grid
                try {
                    // Wait for the grid to appear
                    WebElement grid = driver.findElement(
                            By.xpath("//div[contains(@class,'sudoku-grid') and contains(@class,'grid-game-board')]")
                    );

                    //Compute the dimensions
                    String style = grid.getAttribute("style"); // "--rows: N; --cols: N"
                    Pattern pattern = Pattern.compile("--rows:\\s*(\\d+);\\s*--cols:\\s*(\\d+)");
                    assert style != null;
                    Matcher matcher = pattern.matcher(style);
                    if (matcher.find()) {
                        rows = Integer.parseInt(matcher.group(1));
                        cols = Integer.parseInt(matcher.group(2));
                    }
                    String grouping = grid.getAttribute("class");
                    Pattern patternRows = Pattern.compile("-rows--(\\d+)");
                    assert grouping != null;
                    Matcher matcherRows = patternRows.matcher(grouping);
                    if (matcherRows.find()) {
                        rowGroup = Integer.parseInt(matcherRows.group(1));
                    }
                    Pattern patternCols = Pattern.compile("-cols--\\s*(\\d+)");
                    Matcher matcherCols = patternCols.matcher(grouping);
                    if (matcherCols.find()) {
                        colGroup = Integer.parseInt(matcherCols.group(1));
                    }

                    List<WebElement> allInputButtons = driver.findElements(By.cssSelector("button.sudoku-input-button"));
                    for (WebElement inputButton : allInputButtons) {
                        inputMap.put(inputButton.getText().trim(), inputButton);
                    }
                    // Get all buttons inside the grid
                    List<WebElement> allButtons = grid.findElements(By.cssSelector(".sudoku-cell"));
                    allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                    // Parse into 2D list
                    gridButtons = new WebElement[rows][cols];
                    gridNumbers = new Integer[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int index = i * cols + j;
                            WebElement cell = allButtons.get(index);
                            gridButtons[i][j]= cell;
                            String value = cell.getText().trim();
                            if(!value.isEmpty()) {
                                gridNumbers[i][j] = Integer.valueOf(value);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Grid not found.");
                }
            }


            System.out.println("Building MathModel...");
            SudokuMathModel model = new SudokuMathModel("SudokuModel", gridNumbers,rows, cols, rowGroup, colGroup);
            System.out.println("MathModel built.");

            System.out.println("Running MathModel...");
            model.run();
            System.out.println("MathModel run.");

            System.out.println("Gathering result...");
            Object[] results = (Object[]) model.getResult();
            String resultText = (String) results[0];
            ArrayList<ArrayList<Integer>> result = (ArrayList<ArrayList<Integer>>) results[1];
            System.out.println("Result gathered.");

            System.out.println("Introducing solution...");
            assert gridButtons != null;
            if (resultText.startsWith("MPSOLVER_OPTIMAL")) {
                for(int row = 0;row< rows; row++){
                    for(int col = 0; col< cols; col++){
                        if(gridNumbers[row][col]==null){
                            gridButtons[row][col].click();
                            inputMap.get(result.get(row).get(col).toString()).click();
                        }
                    }
                }
                System.out.println("Optimal solution introduced.");
            } else {
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