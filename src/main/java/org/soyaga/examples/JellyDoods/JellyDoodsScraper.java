package org.soyaga.examples.JellyDoods;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.JellyDoods.GA.JellyDoodsGA;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class JellyDoodsScraper {
    static final double cellXStart = -0.2243;
    static final double cellYStart = -0.384615;
    static final double cellXSize = 0.062702;
    static final double cellYSize = 0.111538;


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

            System.out.println("Loading https://www.mathplayground.com/logic_jelly_doods.html: ...");
            driver.get("https://www.mathplayground.com/logic_jelly_doods.html");
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.navigate().refresh();
            System.out.println("Refreshed.");

            System.out.println("Clicking play...");
            try {
                WebElement btn = longWait.until(
                        ExpectedConditions.elementToBeClickable(By.id("gameBtn"))
                );
                btn.click();
                System.out.println("Play clicked.");
            }
            catch (Exception ex){
                System.out.println("Play not clicked");
            }

            System.out.println("Switching to iFrame...");
            try {
                // Move to the correct iframe
                WebElement iframe = longWait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("gameFrame")
                ));
                driver.switchTo().frame(iframe);
                System.out.println("iFrame switched.");
            }
            catch (Exception e) {
                System.out.println("iFrame not switched.");
            }

            System.out.println("Centering and clicking...");
            WebElement container =null;
            try {
                container = driver.findElement(By.id("canvas"));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                        container
                );
                container.click();
                container.click();
                Thread.sleep(1000);
                actions.moveToElement(container, (int)(-0.208108*container.getSize().width),  (int)(-0.365384*container.getSize().height)).click().perform();System.out.println("Centered and clicked.");
            }
            catch (Exception ex){
                System.out.println("Not centered and clicked.");
            }

            System.out.println("Retrieving levels...");
            ArrayList<? extends ArrayList<?>> levels = null;
            ArrayList<? extends ArrayList<?>> moves = null;
            try{
                JavascriptExecutor js = (JavascriptExecutor) driver;
                ArrayList<LinkedHashMap<String,ArrayList<?>>> jsReturn = (ArrayList<LinkedHashMap<String, ArrayList<?>>>) js.executeScript("return JellyDoods.levels;");
                assert jsReturn != null;
                levels = jsReturn.stream().map(linked ->linked.get("objects")).collect(Collectors.toCollection(ArrayList::new));
                moves = jsReturn.stream().map(linked ->linked.get("moves")).collect(Collectors.toCollection(ArrayList::new));
                System.out.println("Levels retrieved.");
            }
            catch (Exception ex){
                System.out.println("Levels not retrieved.");
            }

            for(int loop=0; loop<50; loop++){

                System.out.println("Getting level info...");
                ArrayList<ArrayList<Integer>> level = new ArrayList<>();
                assert moves != null;
                int maxMoves = Math.toIntExact((Long) moves.get(loop).get(1));
                if(loop==0)maxMoves=2;
                for (ArrayList<Long> inner : (ArrayList<ArrayList<Long>>) levels.get(loop)) {
                    ArrayList<Integer> newInner = new ArrayList<>();
                    for (Long value : inner) {
                        if (value != null) {
                            newInner.add(value.intValue()); // convert Long -> Integer
                        } else {
                            newInner.add(null); // keep nulls if needed
                        }
                    }
                    level.add(newInner);
                }
                System.out.println("Level info get.");

                System.out.println("Building GAModel...");
                assert !level.isEmpty();
                JellyDoodsGA model = new JellyDoodsGA("JellyDoods", level, maxMoves);
                System.out.println("GAModel built.");

                System.out.println("Running MathModel...");
                model.optimize();
                System.out.println("MathModel run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<Object[]> result = (ArrayList<Object[]>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                Thread.sleep(1000);
                assert container != null;
                if (resultText.startsWith("GA_Optimal")) {
                    for(Object[] resultObject:result){
                        int[] cell= (int[]) resultObject[0];
                        String direction = (String) resultObject[1];
                        int yLocation = (int)((cellYStart + cellYSize*cell[0])*container.getSize().height);
                        int xLocation = (int)((cellXStart + cellXSize*cell[1])*container.getSize().width);
                        switch(direction){
                            case "N":{
                                actions.moveToElement(container, xLocation, yLocation)
                                        .clickAndHold()
                                        .moveByOffset(0,-(int)(cellYSize*container.getSize().height))
                                        .release()
                                        .perform();
                                break;
                            }
                            case "E":{
                                actions.moveToElement(container, xLocation, yLocation)
                                        .clickAndHold()
                                        .moveByOffset((int)(cellXSize*container.getSize().width),0)
                                        .release()
                                        .perform();
                                break;
                            }
                            case "S":{
                                actions.moveToElement(container, xLocation, yLocation)
                                        .clickAndHold()
                                        .moveByOffset(0,(int)(cellYSize*container.getSize().height))
                                        .release()
                                        .perform();
                                break;
                            }
                            case "W":{
                                actions.moveToElement(container, xLocation, yLocation)
                                        .clickAndHold()
                                        .moveByOffset(-(int)(cellXSize*container.getSize().width),0)
                                        .release()
                                        .perform();
                                break;
                            }
                        }
                        Thread.sleep(500);

                    }
                    System.out.println("Optimal solution introduced.");
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Continuing game...");
                assert container!=null;
                try {
                    Thread.sleep(700);
                    actions.moveToElement(container, (int)(0.148648*container.getSize().width), (int)(0.173076*container.getSize().height)).click().perform();
                    System.out.println("Game continued.");
                }
                catch (Exception ex){
                    System.out.println("Game not continued.");
                }

                System.out.println("Clicking...");
                try {
                    Thread.sleep(1000);
                    actions.moveToElement(container, (int)(0.170270*container.getSize().width), (int)(-0.163461*container.getSize().height)).click().perform();
                    Thread.sleep(500);
                    System.out.println("Clicked.");
                }
                catch (Exception ex){
                    System.out.println("Not clicked.");
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
}
