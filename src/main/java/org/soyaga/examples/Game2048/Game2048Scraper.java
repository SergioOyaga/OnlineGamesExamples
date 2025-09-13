package org.soyaga.examples.Game2048;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.Game2048.Board.Board;
import org.soyaga.examples.Game2048.GA.Game2048GA;

import java.time.Duration;
import java.util.*;

public class Game2048Scraper {

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";

        //Dimensions
        int rows = 4;
        int cols = 4;

        //WebElements
        WebElement tileContainer = null;

        //Controller
        boolean canContinue = true;

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();
        Actions actions = new Actions(driver);

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(2000));
            WebDriverWait sortWait = new WebDriverWait(driver, Duration.ofMillis(10));

            System.out.println("Loading 2048: ...");
            // Wait/load, then navigate to the game
            Thread.sleep(2000);
            driver.get("https://www.2048.org/");
            longWait.until(ExpectedConditions.urlToBe("https://www.2048.org/"));
            System.out.println("2048 loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the last tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.navigate().refresh();
            System.out.println("Refreshed.");

            System.out.println("Accepting cookies...");
            try {
                WebElement btn = longWait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Consent']"))
                );
                btn.click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception ex){
                System.out.println("Play not clicked");
            }

            System.out.println("Getting tile-container...");
            try {
                // Wait for the grid to appear
                tileContainer =sortWait.until(
                        ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[@class='tile-container']")
                        )
                );
            }
            catch (Exception e) {
                System.out.println("Tile-container not found.");
            }
            assert tileContainer!=null;

            System.out.println("Centering and clicking...");
            try {
                WebElement grid = driver.findElement(By.xpath("//div[@class='grid-container']"));
                // scroll so it’s centered in the viewport
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});",
                        grid);
                grid.click();
                System.out.println("Centered and clicked.");
            }
            catch (Exception ex){
                System.out.println("Not centered and clicked.");
            }


            System.out.println("Starting game loop...");
            for(int i =0;i<30;i++){
                actions.sendKeys(Keys.RIGHT).perform();
                Thread.sleep(100);
                actions.sendKeys(Keys.DOWN).perform();
                Thread.sleep(100);
                if(i%10==0)actions.sendKeys(Keys.LEFT).perform();
            }

            Integer[][] gridNumber = new Integer[rows][cols];
            System.out.println("Retrieving grid...");
            for(WebElement element:tileContainer.findElements(By.xpath("./div"))){
                String classAttr = element.getAttribute("class");
                assert classAttr != null;
                String[] classLocationSplit = classAttr.split("\\s+")[2].split("-");
                int row= Integer.parseInt(classLocationSplit[3])-1;
                int col = Integer.parseInt(classLocationSplit[2])-1;
                gridNumber[row][col]= Integer.parseInt(element.getText().trim());
            }
            System.out.println("Grid retrieved.");

            while(canContinue){
                System.out.println("Creating GA...");
                Game2048GA model = new Game2048GA("MainGA", gridNumber,100,2000);
                System.out.println("GA created.");

                System.out.println("Optimizing GA...");
                model.optimize();
                System.out.println("GA optimized.");

                System.out.println("Introducing solution...");
                HashMap<String,Double> result = model.getResult();
                Board board = new Board(gridNumber);
                HashMap<String,Double> defaultPlay = board.getMovementsProbabilities();
                String move = null;
                double cumulative = 0.;
                double selection = new Random().nextDouble();
                // Get entry with the maximum value
                Map.Entry<String, Double> maxEntry = Collections.max(
                        result.entrySet(),
                        Map.Entry.comparingByValue()
                );
                for(Map.Entry<String,Double> defaultEntry:defaultPlay.entrySet()){
                    cumulative+=defaultEntry.getValue();
                    if(cumulative>selection){
                        move = defaultEntry.getKey();
                        break;
                    }
                }
                if(maxEntry.getValue()>=defaultPlay.get(maxEntry.getKey())) {
                    move = maxEntry.getKey();
                }
                switch(Objects.requireNonNull(move)){
                    case "N":{
                        actions.sendKeys(Keys.UP).perform();
                        break;
                    }
                    case "E":{
                        actions.sendKeys(Keys.RIGHT).perform();
                        break;
                    }
                    case "S":{
                        actions.sendKeys(Keys.DOWN).perform();
                        break;
                    }
                    case "W":{
                        actions.sendKeys(Keys.LEFT).perform();
                        break;
                    }
                }
                System.out.println("Solution introduced.");

                Thread.sleep(200);
                gridNumber = new Integer[rows][cols];
                System.out.println("Retrieving grid...");
                for(WebElement element:tileContainer.findElements(By.xpath("./div"))){
                    String classAttr = element.getAttribute("class");
                    assert classAttr != null;
                    String[] classLocationSplit = classAttr.split("\\s+")[2].split("-");
                    int row= Integer.parseInt(classLocationSplit[3])-1;
                    int col = Integer.parseInt(classLocationSplit[2])-1;
                    int value = Integer.parseInt(element.getText().trim());
                    gridNumber[row][col]= value;
                    if(value ==2048) canContinue=false;
                }
                System.out.println("Grid retrieved.");

                System.out.println("Checking canContinue...");
                Board b = new Board(gridNumber);
                if(b.getAvailableMovements().isEmpty()) canContinue=false;
                System.out.println("CanContinue checked.");
            }
            Thread.sleep(4000);
            System.out.println("Game loop finalized.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}