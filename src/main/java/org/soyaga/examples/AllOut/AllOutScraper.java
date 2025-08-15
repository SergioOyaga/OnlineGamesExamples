package org.soyaga.examples.AllOut;

import nu.pattern.OpenCV;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.AllOut.GA.AllOutGA;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AllOutScraper {
    static int rows=5;
    static int cols=5;
    static int size=70;
    static int gapy = 75;
    static int gapx = 5;
    static int loops = 10;


    /**
     * Static function that process the image.
     * @param image BufferedImage to process.
     * @return BufferedImage processed.
     */
    public static BufferedImage processScreenshot(BufferedImage image){
        System.out.println("    Loading OpenCV...");
        OpenCV.loadLocally();
        System.out.println("    OpenCV loaded.");

        System.out.println("    Cropping...");
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat cv = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
        cv.put(0, 0, pixels);
        Mat processed = new Mat(cv, new Rect(gapx, gapy,cols*size,rows*size));
        System.out.println("    Cropped");

        System.out.println("    Converting to Grayscale...");
        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_BGR2GRAY);
        System.out.println("    Grayscale converted.");

        System.out.println("    Converting to Black&White...");
        Imgproc.threshold(processed, processed, 128, 255, Imgproc.THRESH_BINARY);
        System.out.println("    Black&White Converting.");

        BufferedImage imageCVProcessed = new BufferedImage(processed.cols(), processed.rows(), BufferedImage.TYPE_BYTE_GRAY);
        final byte[] targetPixels = ((DataBufferByte) imageCVProcessed.getRaster().getDataBuffer()).getData();
        processed.get(0, 0, targetPixels);
        return imageCVProcessed;
    }

    /**
     * Static function that computes the grid from the image
     * @param imageCVProcessed BufferedImage, processed.
     * @return Boolean[][] with the problem grid computed.
     */
    private static Boolean[][] computeGrid(BufferedImage imageCVProcessed) {
        Boolean[][] imageGrid = new Boolean[rows][cols];
        int off = Color.BLACK.getRGB();
        int on = Color.WHITE.getRGB();
        for(int row = 0; row<rows; row++){
            for(int col=0; col<cols;col++){
                int color = imageCVProcessed.getRGB(col*size+size/2, row*size+size/2);
                imageGrid[row][col] = color==on;
            }
        }
        return imageGrid;
    }

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();
        try {
            // Waiter
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));

            System.out.println("Loading https://www.mathsisfun.com/games/allout.html: ...");
            // Go to Sudoku page
            driver.get("https://www.mathsisfun.com/games/allout.html");
            System.out.println("Loaded.");

            System.out.println("Accepting cookies...");
            try {
                WebElement accentCookies = wait.until(
                        ExpectedConditions.elementToBeClickable(By.className("fc-button-label"))
                );
                accentCookies.click();
                System.out.println("Cookies accepted.");
            }
            catch (Exception ex){
                System.out.println("No cookies detected");
            }

            System.out.println("Switching to Iframe...");
            try {
                WebElement iframe = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector("iframe[title='JavaScript Animation']"))
                );
                driver.switchTo().frame(iframe);
                System.out.println("Iframe switched.");
            }
            catch (Exception ex){
                System.out.println("Iframe not found.");
            }

            for(int loop=0; loop<loops; loop++) {
                System.out.println("Screenshot the board...");
                BufferedImage image = null;
                try {
                    WebElement screenshotElement = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("main"))
                    );
                    File screenshot = screenshotElement.getScreenshotAs(OutputType.FILE);
                    image = ImageIO.read(screenshot);
                    System.out.println("Board screenshot.");
                }
                catch (Exception ex) {
                    System.out.println("Board not screenshot.");
                }

                System.out.println("Transforming image...");
                BufferedImage imageCVProcessed = null;
                try {
                    imageCVProcessed = processScreenshot(image);
                    System.out.println("Image transformed.");
                }
                catch (Exception e) {
                    System.out.println("Image not transformed");
                }

                System.out.println("Computing grid...");
                Boolean[][] imageGrid = null;
                try{
                    imageGrid = computeGrid(imageCVProcessed);
                    System.out.println("Grid computed.");
                }
                catch (Exception ex){
                    System.out.println("Grid not computed.");
                }

                System.out.println("Mapping buttons...");
                WebElement[][] elementGrid = new WebElement[rows][cols];
                try {
                    WebElement buttonsContainer = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("board"))
                    );
                    List<WebElement> childDivs = buttonsContainer.findElements(By.xpath("./div"));
                    System.out.println("Buttons mapped.");
                    int count = 0;
                    for (WebElement button: childDivs){
                        int r = count%rows;
                        int c = count/cols;
                        elementGrid[r][c] = button;
                        count++;
                    }
                }
                catch (Exception ex) {
                    System.out.println("Buttons not mapped.");
                }

                System.out.println("Building GA...");
                AllOutGA model = new AllOutGA("AllOutGA", rows, cols, imageGrid);
                System.out.println("GA built.");

                System.out.println("Running GA...");
                model.optimize();
                System.out.println("GA run.");

                System.out.println("Gathering result...");
                Object[] results = (Object[]) model.getResult();
                String resultText = (String) results[0];
                ArrayList<ArrayList<Boolean>> result = (ArrayList<ArrayList<Boolean>>) results[1];
                System.out.println("Result gathered.");

                System.out.println("Introducing solution...");
                if (resultText.startsWith("GA_Optimal")) {
                    for(int row=0; row<rows; row++){
                        for(int col=0; col<cols;col++){
                            if(result.get(row).get(col)) elementGrid[row][col].click();
                        }
                    }
                    System.out.println("Solution introduced.");
                }
                else {
                    System.out.println("Optimal solution not found.");
                }

                System.out.println("Clicking next...");
                WebElement acceptButton = wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("nextBtn"))
                );
                acceptButton.click();
                System.out.println("Next clicked .");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}