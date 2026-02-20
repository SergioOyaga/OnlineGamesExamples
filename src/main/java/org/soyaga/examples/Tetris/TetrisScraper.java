package org.soyaga.examples.Tetris;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.Tetris.Board.Board;
import org.soyaga.examples.Tetris.Board.Pieces.*;
import org.soyaga.examples.Tetris.Player.Movement;
import org.soyaga.examples.Tetris.Player.Players.NoobPlayer;

import java.awt.Color;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.soyaga.examples.Tetris.Utils.loadObject;

public class TetrisScraper {

    private static final HashMap<Color, Piece> piecesMap = new HashMap<>(){{
        put(new Color(-16743022),new IPiece());
        put(new Color(-16749826),new JPiece());
        put(new Color(-98560),new LPiece());
        put(new Color(-5886),new OPiece());
        put(new Color(-16197622),new SPiece());
        put(new Color(-1826549),new ZPiece());
        put(new Color(-5558580),new TPiece());
    }};

    private final static Color playColor = new Color(-12281293);
    private static Long pause =30L;

    private static int maxIter = 1000;

    public static void main(String[] args) throws AWTException {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();
        Actions actions = new Actions(driver);
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(6000));

            System.out.println("Loading https://play.tetris.com/: ...");
            driver.get("https://play.tetris.com/");
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.manage().window().setSize(new Dimension(600, 800));
            driver.manage().window().setPosition(new Point(0,0));
            Thread.sleep(1000);
            longWait.until(ExpectedConditions.elementToBeClickable(By.id("ccc-notify-reject"))).click();
            longWait.until(ExpectedConditions.elementToBeClickable(By.className("fc-cta-consent"))).click();
            System.out.println("Refreshed.");

            System.out.println("Switching to iframe..");
            try{
                // First, switch to the correct iframe
                WebElement iframe = longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("tetris-game-iframe")));
                driver.switchTo().frame(iframe);
                System.out.println("Switched");
            }
            catch (Exception ex){
                System.out.println("Not switched");
            }

            System.out.println("Loading NoobPlayer..");
            NoobPlayer noobPlayer = (NoobPlayer) loadObject("src/out/Tetris/player_Tanh_2.dat");
            System.out.println("NoobPlayer loaded");

            System.out.println("Creating Board...");
            Board board = new Board();
            System.out.println("Board Created");

            System.out.println("Selecting Config...");
            try {
                closeAdd(driver, actions);
                while (!robot.getPixelColor(270,360).equals(playColor)){
                    Thread.sleep(1000);
                }
                //Click settings
                robot.mouseMove(220,590);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //Disable mouse control
                Thread.sleep(200);
                robot.mouseMove(360,215);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //Minimize autorepeat delay??
                //Minimize autorepeat speed??

                //Disable animations
                robot.mouseMove(360,425);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //Disable ghost piece
                robot.mouseMove(360,450);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                //Click Done
                robot.mouseMove(300,590);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                System.out.println("Config selected");
            }
            catch (Exception ex){
                System.out.println("Config not selected");
            }

            System.out.println("Clicking play...");
            try {
                actions.moveToLocation(280,240).pause(1000).click().perform();
                System.out.println("Play clicked.");
            }
            catch (Exception ex){
                System.out.println("Play not clicked");
            }

            System.out.println("Retrieving first piece...");
            Thread.sleep(2850);
            Color pieceColor = robot.getPixelColor(440,270);
            Color nextPieceColor = robot.getPixelColor(440,320);
            Color nextSecondPieceColor = robot.getPixelColor(440,370);
            Piece piece = piecesMap.get(pieceColor);
            Piece firstPiece = piecesMap.get(pieceColor);
            ArrayList<Piece> pieces = new ArrayList<>(){{add(firstPiece);}};
            System.out.println("First piece retrieved");

            System.out.println("Solving first move..");
            System.out.println(board);
            Movement move = noobPlayer.movePiece(board,pieces);
            System.out.println("First move solved");

            System.out.println(board);
            int iter=1;
            while(board.isActive() && iter<maxIter){
                Thread.sleep(pause);
                boolean hasToClear = board.getLastClearedLinesNumber()>0;
                System.out.println("Retrieving next piece...");
                pieceColor = robot.getPixelColor(440,270);
                if(nextPieceColor!=pieceColor){
                    Thread.sleep(pause);
                    pieceColor = robot.getPixelColor(440,270);
                }
                nextPieceColor = robot.getPixelColor(440,320);
                nextSecondPieceColor = robot.getPixelColor(440,370);
                Piece nextPiece = piecesMap.get(pieceColor);
                Piece nextSecondPiece = piecesMap.get(nextPieceColor);
                Piece nextThirdPiece = piecesMap.get(nextSecondPieceColor);
                ArrayList<Piece> nextPieces = new ArrayList<>(){{
                    add(nextPiece);
                    add(nextSecondPiece);
                    add(nextThirdPiece);
                }};
                System.out.println("Next piece retrieved");

                System.out.println("Solving next move...");
                Movement nextMove = noobPlayer.movePiece(board,nextPieces);
                System.out.println("Next move solved");

                System.out.println("Introducing move...");
                System.out.println("ITeration: "+ iter+", "+piece.getClass().getSimpleName()+" --> "+ move);
                convertToActions(move, robot);
                if(hasToClear){
                    Thread.sleep(350 );
                }
                System.out.println(board);
                move = nextMove;
                piece = nextPiece;
                System.out.println("Move introduced");
                iter++;
            }
            System.out.println("Game ended by "+ (board.isActive()?"MAX ROUNDS":"GAME OVER"));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }

    private static void closeAdd(WebDriver driver, Actions action) throws InterruptedException {
        System.out.println("Searching add...");
         List<WebElement> adds = driver.findElements(By.className("adsbygoogle"));
         if(adds.isEmpty()){
             System.out.println("Add not found");
             return;
         }
         System.out.println("Closing add...");
         Thread.sleep(6000);
         action.moveToLocation(450,100).click().pause(1000).click().perform();
    }

    private static void convertToActions(Movement move, Actions actions) throws InterruptedException {
        String orientation = move.orientation();
        switch (orientation){
            case "E":{
                actions.sendKeys(Keys.ARROW_UP);
                break;
            }
            case "S":{
                actions.sendKeys(Keys.ARROW_UP).pause(pause)
                        .sendKeys(Keys.ARROW_UP);
                break;
            }
            case "W":{
                actions.sendKeys(Keys.ARROW_UP).pause(pause)
                        .sendKeys(Keys.ARROW_UP).pause(pause)
                        .sendKeys(Keys.ARROW_UP);
                break;
            }
            default:break;
        }
        actions.perform();
        Thread.sleep(300);
        int gap = move.gap();
        if(gap>0){
            while (gap>0){
                actions.sendKeys(Keys.ARROW_RIGHT).pause(pause);
                gap--;
            }
        }
        else {
            while (gap<0){
                actions.sendKeys(Keys.ARROW_LEFT).pause(pause);
                gap++;
            }
        }
        actions.perform();
        Thread.sleep(300);
        actions.pause(pause).sendKeys(Keys.SPACE).perform();

    }

    private static void convertToActions(Movement move, Robot robot) throws InterruptedException {
        Thread.sleep(2*pause);
        String orientation = move.orientation();
        switch (orientation){
            case "E":{
                robot.keyPress(KeyEvent.VK_UP);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_UP);
                Thread.sleep(pause);
                break;
            }
            case "S":{
                robot.keyPress(KeyEvent.VK_UP);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_UP);
                Thread.sleep(pause);
                robot.keyPress(KeyEvent.VK_UP);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_UP);
                Thread.sleep(pause);
                break;
            }
            case "W":{
                robot.keyPress(KeyEvent.VK_Z);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_Z);
                Thread.sleep(pause);
                break;
            }
            default:break;
        }
        Thread.sleep(2*pause);
        int gap = move.gap();
        if(gap>0){
            while (gap>0){
                robot.keyPress(KeyEvent.VK_RIGHT);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_RIGHT);
                Thread.sleep(pause);
                gap--;
            }
        }
        else {
            while (gap<0){
                robot.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(pause);
                robot.keyRelease(KeyEvent.VK_LEFT);
                Thread.sleep(pause);
                gap++;
            }
        }
        Thread.sleep(2*pause);
        robot.keyPress(KeyEvent.VK_SPACE);
        Thread.sleep(pause);
        robot.keyRelease(KeyEvent.VK_SPACE);
    }

    private static void convertToActions(Movement move, JavascriptExecutor  js, WebElement gameCanvas) throws InterruptedException {
        String orientation = move.orientation();
        String script="";
        switch (orientation){
            case "E":{
                script+="arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));";
                break;
            }
            case "S":{
                script+="arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));";
                break;
            }
            case "W":{
                script+="arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowUp', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowUp', bubbles:true}));";
                break;
            }
            default:break;
        }
        js.executeScript(script,gameCanvas);
        Thread.sleep(500);
        script ="";
        int gap = move.gap();
        if(gap>0){
            while (gap>0){
                script+="arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowRight', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowRight', bubbles:true}));";
                gap--;
            }
        }
        else {
            while (gap<0){
                script+="arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:'ArrowLeft', bubbles:true}));" +
                        "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:'ArrowLeft', bubbles:true}));";
                gap++;
            }
        }
        js.executeScript(script,gameCanvas);
        Thread.sleep(500);
        js.executeScript("arguments[0].dispatchEvent(new KeyboardEvent('keydown', {key:' ', bubbles:true}));" +
                "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {key:' ', bubbles:true}));",gameCanvas);
    }

    public static void showClickPoint(WebDriver driver, int x, int y) {
        String script =
                "var dot = document.createElement('div');" +
                        "dot.style.position='fixed';" +          // fixed = viewport coordinates
                        "dot.style.left='" + x + "px';" +
                        "dot.style.top='" + y + "px';" +
                        "dot.style.width='12px';" +
                        "dot.style.height='12px';" +
                        "dot.style.background='red';" +
                        "dot.style.borderRadius='50%';" +
                        "dot.style.zIndex='99999';" +
                        "dot.style.pointerEvents='none';" +
                        "document.body.appendChild(dot);" +
                        "setTimeout(function(){dot.remove();}, 2000);";

        ((JavascriptExecutor) driver).executeScript(script);
    }
}
