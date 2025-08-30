package org.soyaga.examples.MadVirus;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.MadVirus.GA.MadVirusGA;
import org.soyaga.examples.MadVirus.Graph.Graph;
import org.soyaga.examples.MadVirus.Graph.Node;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.time.Duration;
import java.util.*;

public class MadVirusScraper {
    static int buttonsWidth = 90;
    static int turnsXStart = 247;
    static int turnsWidth = 63;
    static int turnsHeight = 27;
    static int gameXStart = 100;
    static int gameYStart = 30;

    static int bgRGB = new Color(51,51,51).getRGB();

    private static ArrayList<Integer> computeColCenters(BufferedImage cropped){
        boolean isBackground = true;
        int count = 0;
        int sum = 0;
        ArrayList<Integer> colCenters = new ArrayList<>();
        for (int col = 0; col < cropped.getWidth(); col++){
            if(cropped.getRGB(col, 0) == bgRGB){
                if(!isBackground){
                    colCenters.add(sum/count);
                    isBackground = true;
                    count = 0;
                    sum = 0;
                }
            }
            else{
                if(isBackground){
                    colCenters.add(sum/count);
                    isBackground = false;
                    count = 0;
                    sum = 0;
                }
            }
            count++;
            sum += col;
        }
        if (cropped.getWidth()-colCenters.get(colCenters.size()-1) > colCenters.get(2) - colCenters.get(1)) {
            colCenters.add(sum / count);
        }
        colCenters.remove(0);
        return colCenters;
    }

    private static ArrayList<Integer> computeRowCenters(BufferedImage cropped){
        boolean isBackground = true;
        int count = 0;
        int sum = 0;
        ArrayList<Integer> rowCenters = new ArrayList<>();
        for (int row = 0; row < cropped.getHeight(); row++){
            if(cropped.getRGB(0, row) == bgRGB){
                if(!isBackground){
                    rowCenters.add(sum/count);
                    isBackground = true;
                    count = 0;
                    sum = 0;
                }
            }
            else{
                if(isBackground){
                    rowCenters.add(sum/count);
                    isBackground = false;
                    count = 0;
                    sum = 0;
                }
            }
            count++;
            sum += row;
        }
        if (cropped.getHeight()-rowCenters.get(rowCenters.size()-1) > rowCenters.get(2) - rowCenters.get(1)) {
            rowCenters.add(sum / count);
        }
        rowCenters.remove(0);
        return rowCenters;
    }

    private static Graph computeGraph(BufferedImage cropped) {
        Graph graph = new Graph();
        System.out.println("    Computing centers...");
        ArrayList<Integer> colCenters = computeColCenters(cropped);
        ArrayList<Integer> rowCenters = computeRowCenters(cropped);
        System.out.println("    Centers computed.");
        int rows = rowCenters.size();
        int cols = colCenters.size();
        Node[][] nodeGrid = new Node[rows][cols];
        Graphics2D g2d = (Graphics2D) cropped.getGraphics();

        for (int row = 0; row < rows; row++) {
            if(row%2==0){
                for (int col = 0; col < cols; col+=2) {
                    int color = cropped.getRGB(colCenters.get(col), rowCenters.get(row));
                    if (color!=bgRGB) {
                        Node node = new Node(color, row, col);
                        nodeGrid[row][col] = node;
                        graph.addNode(node);
                        if (color == 0xFFFFFFFF) {
                            graph.setStartingNode(node);
                        }
                        g2d.setColor(Color.BLACK);
                        g2d.drawRect(colCenters.get(col)-5, rowCenters.get(row)-5, 10,10);
                    }
                }
            }
            else{
                for (int col = 1; col < cols; col+=2) {
                    int color = cropped.getRGB(colCenters.get(col), rowCenters.get(row));
                    if (color!=bgRGB) {
                        Node node = new Node(color, row, col);
                        nodeGrid[row][col] = node;
                        graph.addNode(node);
                        if (color == 0xFFFFFFFF) {
                            graph.setStartingNode(node);
                        }
                        g2d.setColor(Color.WHITE);
                        g2d.drawRect(colCenters.get(col)-5, rowCenters.get(row)-5,10,10);
                    }
                }
            }
        }
        g2d.dispose();
        for (int row = 0; row < rows; row++) {
            if(row%2==0){
                for (int col = 0; col < cols; col+=2) {
                    Node orig= nodeGrid[row][col];
                    if(orig==null) continue;
                    //North
                    if(row>1){
                        Node northNode = nodeGrid[row-2][col];
                        if(northNode!=null)orig.addNode(northNode);
                    }
                    //North-East
                    if((row>0) && (col<cols-1)){
                        Node northEastNode = nodeGrid[row-1][col+1];
                        if(northEastNode!=null)orig.addNode(northEastNode);
                    }
                    //South-East
                    if((row<rows-1) && (col<cols-1)){
                        Node southEastNode = nodeGrid[row+1][col+1];
                        if(southEastNode!=null)orig.addNode(southEastNode);
                    }
                    //South
                    if(row<rows-2){
                        Node southNode = nodeGrid[row+2][col];
                        if(southNode!=null)orig.addNode(southNode);
                    }
                    //South-West
                    if((row<rows-1) && (col>0)){
                        Node southWestNode = nodeGrid[row+1][col-1];
                        if(southWestNode!=null)orig.addNode(southWestNode);
                    }
                    //North-West
                    if((row>0) && (col>0)){
                        Node northWestNode = nodeGrid[row-1][col-1];
                        if(northWestNode!=null)orig.addNode(northWestNode);
                    }
                }
            }
            else{
                for (int col = 1; col < cols; col+=2) {
                    Node orig= nodeGrid[row][col];
                    if(orig==null) continue;
                    //North
                    if(row>1){
                        Node northNode = nodeGrid[row-2][col];
                        if(northNode!=null)orig.addNode(northNode);
                    }
                    //North-East
                    if(col<cols-1){
                        Node northEastNode = nodeGrid[row-1][col+1];
                        if(northEastNode!=null)orig.addNode(northEastNode);
                    }
                    //South-East
                    if((row<rows-1) && (col<cols-1)){
                        Node southEastNode = nodeGrid[row+1][col+1];
                        if(southEastNode!=null)orig.addNode(southEastNode);
                    }
                    //South
                    if(row<rows-2){
                        Node southNode = nodeGrid[row+2][col];
                        if(southNode!=null)orig.addNode(southNode);
                    }
                    //South-West
                    if((row<rows-1)){
                        Node southWestNode = nodeGrid[row+1][col-1];
                        if(southWestNode!=null)orig.addNode(southWestNode);
                    }
                    //North-West
                    Node northWestNode = nodeGrid[row-1][col-1];
                    if(northWestNode!=null)orig.addNode(northWestNode);
                }
            }
        }
        return graph;
    }

    public static  BufferedImage cropBoard(BufferedImage boardImage){
        int minX = boardImage.getWidth(), minY = boardImage.getHeight();
        int maxX = 0, maxY = 0;
        for (int y = 0; y < boardImage.getHeight(); y++) {
            for (int x = 0; x < boardImage.getWidth(); x++) {
                if (boardImage.getRGB(x, y) != bgRGB) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
       return boardImage.getSubimage(minX, minY, width, height);
    }

    public static Graph processBoard(BufferedImage boardImage){
        System.out.println("    Cropping...");
        BufferedImage cropped = cropBoard(boardImage);
        System.out.println("    Crop.");
        System.out.println("    Computing Graph...");
        Graph graph = computeGraph(cropped);
        System.out.println("    Graph computed.");
        return graph;
    }


    /**
     * Static function that process the image for the OCR. Uses opencv.
     * @param image BufferedImage to process.
     * @return BufferedImage processed.
     */
    public static Integer processTurns(BufferedImage image) throws TesseractException {
        BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster raster = image.getRaster().createWritableChild(0,0, turnsWidth, turnsHeight,0, 0, null);
        imageCopy.setData(raster);
        System.out.println("    Loading OpenCV...");
        OpenCV.loadLocally();
        System.out.println("    OpenCV loaded.");

        byte[] pixels = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();
        Mat cv = new Mat(imageCopy.getHeight(),imageCopy.getWidth(),CvType.CV_8UC3);
        cv.put(0, 0, pixels);
        Mat processed = new Mat();

        System.out.println("    Converting to Grayscale...");
        Imgproc.cvtColor(cv, processed, Imgproc.COLOR_BGR2GRAY);
        System.out.println("    Grayscale converted.");

        System.out.println("    Scaling...");
        Imgproc.resize(processed, processed, new Size(processed.width() * 2, processed.height() * 2));
        System.out.println("    Scaled.");

        System.out.println("    Converting to Black and White...");
        Imgproc.threshold(processed, processed, 128, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        System.out.println("    Black and White converted.");

        System.out.println("    Converting to BuffIm...");
        BufferedImage imageCVProcessed = new BufferedImage(processed.cols(), processed.rows(), BufferedImage.TYPE_BYTE_GRAY);
        final byte[] targetPixels = ((DataBufferByte) imageCVProcessed.getRaster().getDataBuffer()).getData();
        processed.get(0, 0, targetPixels);
        System.out.println("    BuffIm converted.");
        return OCRTurns(imageCVProcessed);
    }
    public static HashMap<Integer, int[]> processButtons(BufferedImage image){
        HashMap<Integer, int[]> colorLocation = new HashMap<>();

        HashMap<Integer, int[]> colorSumXSumYCount = new HashMap<>();
        System.out.println("    Replacing conflictive colors...");
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int currentColor = image.getRGB(x, y);
                // Extract RGB components
                int red   = (currentColor >> 16) & 0xFF;
                int green = (currentColor >> 8) & 0xFF;
                int blue  = (currentColor) & 0xFF;
                if(red == green && green == blue) image.setRGB(x, y, 0xFFFFFFFF);
                else{
                    colorSumXSumYCount.putIfAbsent(currentColor, new int[]{0, 0, 0});
                    colorSumXSumYCount.get(currentColor)[0]+=x;
                    colorSumXSumYCount.get(currentColor)[1]+=y;
                    colorSumXSumYCount.get(currentColor)[2]++;
                }
            }
        }
        for(Map.Entry<Integer, int[]> colorEntry:colorSumXSumYCount.entrySet()){
            if(colorEntry.getValue()[2]>100) {
                colorLocation.put(colorEntry.getKey(), new int[]{
                        colorEntry.getValue()[0] / colorEntry.getValue()[2],
                        colorEntry.getValue()[1] / colorEntry.getValue()[2]
                });
            }
        }
        return colorLocation;
    }

    /**
     * Static function that performs the OCR to the Sudoku image. It splits the image in a 9x9 grid and recognizes
     * each number. Use Tesseract (Tess4j) for OCR.
     * @param imageCVProcessed Buffered image of the howl Sudoku.
     * @return Integer[][] with the numbers detected.
     * @throws TesseractException exception.
     */
    public static Integer OCRTurns(BufferedImage imageCVProcessed) throws TesseractException {
        System.out.println("    Preparing Tesseract for OCR...");
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(new File("src\\main\\resources\\tesseract").getPath());
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(3);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_CHAR);
        tesseract.setVariable("tessedit_char_whitelist", "0123456789");
        tesseract.setVariable("debug_file", "NULL");
        tesseract.setVariable("tessedit_pageseg_mode", "7");
        System.out.println("    Tesseract prepared.");

        System.out.println("    Recognizing characters...");
        String result = tesseract.doOCR(imageCVProcessed);
        System.out.println("    Characters recognized.");
        return Integer.valueOf(result.trim());
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

            System.out.println("Loading https://www.mathsisfun.com/games/mad-virus.html: ...");
            driver.get("https://www.mathsisfun.com/games/mad-virus.html");
            System.out.println("Loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the second tab)
            driver.switchTo().window(tabs.get(tabs.size()-1));

            System.out.println("Accepting cookies...");
            try {
                WebElement accentCookies = longWait.until(
                        ExpectedConditions.elementToBeClickable(By.className("fc-button-label"))
                );
                accentCookies.click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception ex){
                System.out.println("No cookies detected");
            }

            System.out.println("Pressing play...");
            WebElement container =null;
            try {
                WebElement ruffle = driver.findElement(By.tagName("ruffle-player"));
                SearchContext shadowRoot = (SearchContext) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].shadowRoot", ruffle);
                assert shadowRoot != null;
                container = shadowRoot.findElement(By.id("container"));
                WebElement playButton = shadowRoot.findElement(By.id("play_button"));
                playButton.click();
                Thread.sleep(8000);
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                        container
                );
                actions.moveToElement(container, 0, 20).click().perform();
                container.click();
                System.out.println("Play pressed.");
            }
            catch (Exception ex){
                System.out.println("Play not pressed.");
            }

            for(int loop=0; loop<10; loop++) {
                System.out.println("Screenshot the Game...");
                BufferedImage image = null;
                try {
                    assert container != null;
                    File screenshot = container.getScreenshotAs(OutputType.FILE);
                    image = ImageIO.read(screenshot);
                    System.out.println("Game screenshot.");
                }
                catch (Exception e) {
                    System.out.println("Game not screenshot");
                }

                System.out.println("Splitting the image...");
                BufferedImage buttonsImage=null;
                BufferedImage turnsImage=null;
                BufferedImage boardImage=null;
                try{
                    System.out.println("    Buttons Split.");
                    assert image != null;
                    buttonsImage = image.getSubimage(0,0, buttonsWidth, image.getHeight());
                    System.out.println("    Turns Split.");
                    turnsImage = image.getSubimage(turnsXStart,0, turnsWidth, turnsHeight);
                    System.out.println("    Board Split.");
                    boardImage = image.getSubimage(gameXStart, gameYStart, image.getWidth()-gameXStart, image.getHeight()-gameYStart);
                    System.out.println("Image split.");
                }
                catch (Exception ex){
                    System.out.println("Image not split.");
                }

                System.out.println("Processing images...");
                HashMap<Integer, int[]> colorLocation=null;
                Integer turns = null;
                Graph graph = null;
                try {
                    System.out.println("    Processing buttons...");
                    assert buttonsImage != null;
                    colorLocation = processButtons(buttonsImage);
                    System.out.println("    Buttons processed.");
                    System.out.println("    Processing turns...");
                    assert turnsImage != null;
                    turns = processTurns(turnsImage);
                    System.out.println("    Turns processed.");
                    System.out.println("    Processing board...");
                    graph = processBoard(boardImage);
                    System.out.println("    Board processed.");
                    System.out.println("Images processed.");
                }
                catch (Exception e) {
                    System.out.println("Images not processed.");
                }


                System.out.println("Building GAModel...");
                assert colorLocation != null;
                MadVirusGA model = new MadVirusGA("MadVirusModel", new ArrayList<>(colorLocation.keySet()), turns, graph);
                System.out.println("GAModel built.");

                System.out.println("Running MathModel...");
                model.optimize();
                System.out.println("MathModel run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<Integer> result = (ArrayList<Integer>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                Dimension size = container.getSize();
                int width = size.getWidth()/2;
                int height = size.getHeight()/2;
                if (resultText.startsWith("GA_Optimal")) {
                    for (Integer colorToClickValues: result) {
                        int xOff = colorLocation.get(colorToClickValues)[0];
                        int yOff = colorLocation.get(colorToClickValues)[1];
                        actions.moveToElement(container, xOff-width, yOff-height).click().perform();
                    }
                    System.out.println("Optimal solution introduced.");
                } else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Continuing game...");
                System.out.println("    Clicking...");
                Thread.sleep(1000);
                actions.moveToElement(container, 0, 20).click().perform();
                System.out.println("    Clicked.");
                System.out.println("    Selecting starting point...");
                File screenshot = container.getScreenshotAs(OutputType.FILE);
                image = ImageIO.read(screenshot);
                boardImage = image.getSubimage(gameXStart, gameYStart, image.getWidth()-gameXStart, image.getHeight()-gameYStart);

                Graphics2D g2d = (Graphics2D) boardImage.getGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(boardImage.getWidth()/2-100, boardImage.getHeight()/5, 200, 100);
                g2d.dispose();
                ArrayList<int[]> whitePixels = new ArrayList<>();
                int white = new Color(255,255,255).getRGB();
                for (int y = 0; y < boardImage.getHeight(); y++) {
                    for (int x = 0; x < boardImage.getWidth(); x++) {
                        int rgb = boardImage.getRGB(x, y);
                        if (rgb == white) {
                            whitePixels.add(new int[]{x, y});
                        }
                    }
                }
                Random rand = new Random();
                int[] pixel = whitePixels.get(rand.nextInt(0,whitePixels.size()));
                actions.moveToElement(container, -image.getWidth()/2+gameXStart+pixel[0], -image.getHeight()/2+gameYStart+pixel[1]).click().perform();
                System.out.println("    Starting point selected.");
                System.out.println("Game continued.");
                Thread.sleep(1000);
            }
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}