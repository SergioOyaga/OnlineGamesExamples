package org.soyaga.examples.LinkedInTango;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.LinkedInTango.MathModel.TangoMathModel;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class LinkedInTangoScraper {

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
        //Grid of types
        String[][] gridTypes = null;
        //String[][] with the north border types "x", "=", ""
        String[][] northTypes = null;
        //String[][] with the east border types "x", "=", ""
        String[][] eastTypes = null;
        //String[][] with the south border types "x", "=", ""
        String[][] southTypes = null;
        //String[][] with the west border types "x", "=", ""
        String[][] westTypes = null;

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

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
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(linkedInUser);
            password.sendKeys(linkedInPassword);
            longWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
            Thread.sleep(2000);
            System.out.println("Logged.");

            // Wait/load, then navigate to the game
            System.out.println("Loading game...");
            driver.get("https://www.linkedin.com/games/tango/");
            longWait.until(ExpectedConditions.urlToBe("https://www.linkedin.com/games/tango/"));
            System.out.println("Game loaded.");

            // search the grid
            try {
                // Wait for the grid to appear
                System.out.println("Retrieving Grid...");
                WebElement grid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='interactive-grid']")));

                //Compute the dimensions
                List<WebElement> allButtons = grid.findElements(By.cssSelector("[data-testid^='cell-']"));

                allButtons = allButtons.stream()
                        .filter(e -> e.getAttribute("data-testid").matches("cell-\\d+"))
                        .collect(Collectors.toList());
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
                gridTypes = new String[rows][cols];
                northTypes = new String[rows][cols];
                eastTypes = new String[rows][cols];
                southTypes = new String[rows][cols];
                westTypes = new String[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int index = i * cols + j;
                        WebElement cell = allButtons.get(index);
                        List<WebElement> cellElements= cell.findElements(By.cssSelector("svg[aria-label]"));
                        for(WebElement cellElement: cellElements){
                            String type = Objects.requireNonNull(cellElement.getAttribute("data-testid")).trim();
                            switch (type){
                                case "cell-zero"->{
                                    gridTypes[i][j] = "s";
                                    break;
                                }
                                case "cell-one"->{
                                    gridTypes[i][j] = "m";
                                    break;
                                }
                                case "cell-empty"->{
                                    gridTypes[i][j] = "";
                                    break;
                                }
                                case "edge-cross"->{
                                    WebElement parent = cellElement.findElement(By.xpath(".."));
                                    Rectangle parentRect = parent.getRect();
                                    WebElement grandParent = parent.findElement(By.xpath(".."));
                                    Rectangle grandParentRect = grandParent.getRect();
                                    int dx = parentRect.getX() + parentRect.getWidth() / 2 - grandParentRect.getX() - grandParentRect.getWidth() / 2 ;
                                    int dy = parentRect.getY() + parentRect.getHeight() / 2 - grandParentRect.getY() - grandParentRect.getHeight() / 2 ;

                                    if (Math.abs(dx) > Math.abs(dy)) {
                                        if (dx >= 0) eastTypes[i][j] = "x";
                                        else westTypes[i][j] = "x";
                                    } else {
                                        if (dy >= 0) southTypes[i][j] = "x";
                                        else northTypes[i][j] = "x";
                                    }
                                    break;
                                }
                                case "edge-equal"->{
                                    WebElement parent = cellElement.findElement(By.xpath(".."));
                                    Rectangle parentRect = parent.getRect();
                                    WebElement grandParent = parent.findElement(By.xpath(".."));
                                    Rectangle grandParentRect = grandParent.getRect();
                                    int dx = parentRect.getX() + parentRect.getWidth() / 2 - grandParentRect.getX() - grandParentRect.getWidth() / 2 ;
                                    int dy = parentRect.getY() + parentRect.getHeight() / 2 - grandParentRect.getY() - grandParentRect.getHeight() / 2 ;

                                    if (Math.abs(dx) > Math.abs(dy)) {
                                        if (dx >= 0) eastTypes[i][j] = "=";
                                        else westTypes[i][j] = "=";
                                    } else {
                                        if (dy >= 0) southTypes[i][j] = "=";
                                        else northTypes[i][j] = "=";
                                    }
                                    break;
                                }
                                default -> System.out.println("Not in cell options");
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
                    WebElement iframe = longWait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe.game-launch-page__iframe"))
                    );
                    driver.switchTo().frame(iframe);

                    // Search the start button and click it
                    WebElement startButton = longWait.until(
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
                    WebElement rejectBtn = longWait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-control-name='ga-cookie.consent.deny.v4']"))
                    );
                    rejectBtn.click();
                } catch (Exception e) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
                    System.out.println("Iframe not found.");
                }

                // search the grid
                try {
                    // Wait for the grid to appear
                    WebElement grid = longWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.lotka-grid.gil__grid")));

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
                    List<WebElement> allButtons = grid.findElements(By.cssSelector("div[role='button']"));
                    allButtons.sort(Comparator.comparingInt(a -> Integer.parseInt(Objects.requireNonNull(a.getAttribute("data-cell-idx")))));
                    // Parse into 2D list
                    gridButtons = new WebElement[rows][cols];
                    gridTypes = new String[rows][cols];
                    northTypes = new String[rows][cols];
                    eastTypes = new String[rows][cols];
                    southTypes = new String[rows][cols];
                    westTypes = new String[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int index = i * cols + j;
                            WebElement cell = allButtons.get(index);

                            String type = Objects.requireNonNull(cell.findElement(By.cssSelector("svg[aria-label]")).getAttribute("aria-label")).trim();
                            type = ("Sun".equals(type) | "Sol".equals(type)) ? "s" : ("Moon".equals(type) | "Luna".equals(type)) ? "m" : "";
                            List<WebElement> edgeDivs = cell.findElements(By.cssSelector("div.lotka-cell-edge"));
                            switch (type) {
                                case "s", "m", "" -> {
                                    gridTypes[i][j] = type;
                                    for (WebElement edgeDiv : edgeDivs) {
                                        String edgeLabel = Objects.requireNonNull(edgeDiv.findElement(By.cssSelector("svg[aria-label]"))
                                                .getAttribute("aria-label")).trim();
                                        edgeLabel = ("Equal".equals(edgeLabel) | "Igual".equals(edgeLabel)) ? "=" : "Cross".equals(edgeLabel) ? "x" : "";
                                        if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--up")) {
                                            northTypes[i][j] = edgeLabel;
                                        } else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--right")) {
                                            eastTypes[i][j] = edgeLabel;
                                        } else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--down")) {
                                            southTypes[i][j] = edgeLabel;
                                        } else if (Objects.requireNonNull(edgeDiv.getAttribute("class")).contains("--left")) {
                                            westTypes[i][j] = edgeLabel;
                                        }
                                    }
                                }
                                default -> System.out.println("Cell type not found");
                            }
                            gridButtons[i][j] = cell;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Grid not found.");
                }
            }

            // Create optimization object
            TangoMathModel mathModel = new TangoMathModel("QueensMM", rows, cols, gridTypes, northTypes, eastTypes, southTypes, westTypes);
            mathModel.run();
            Object [] results =  (Object []) mathModel.getResult();
            String resultText = (String)results[0];
            Boolean[][] result = (Boolean[][]) results[1];
            if(resultText.startsWith("MPSOLVER_OPTIMAL")){
                for(int row = 0;row< rows; row++){
                    for(int col = 0; col< cols; col++){
                        assert gridButtons != null;
                        if(!gridTypes[row][col].isEmpty()) continue;
                        WebElement buttonToClick = gridButtons[row][col];
                        buttonToClick.click();
                        if(!result[row][col])buttonToClick.click();
                    }
                }
                System.out.println("Optimal introduced.");
            }
            else {
                System.out.println("Optimal solution not found.");
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
