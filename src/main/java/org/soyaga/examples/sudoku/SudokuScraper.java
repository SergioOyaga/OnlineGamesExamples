package org.soyaga.examples.sudoku;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.sudoku.MathModel.SudokuMathModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;

public class SudokuScraper {
    /**
     * Static attribute that is not really used, but it allows us to check when the OCR fails detecting a number.
     */
    public static BufferedImage[][] imageGrid = new BufferedImage[9][9];

    /**
     * Static function that process the image for the OCR. Uses opencv.
     * @param image BufferedImage to process.
     * @return BufferedImage processed.
     */
    public static BufferedImage processScreenshot(BufferedImage image){
        System.out.println("    Loading OpenCV...");
        OpenCV.loadLocally();
        System.out.println("    OpenCV loaded.");

        System.out.println("    Replacing conflictive colors...");
        HashSet<Integer> conflictiveColors = new HashSet<>();
        conflictiveColors.add(new Color(187,222,251).getRGB());
        conflictiveColors.add(new Color(195,215,234).getRGB());
        conflictiveColors.add(new Color(226,235,243).getRGB());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int currentColor = image.getRGB(x, y);
                if (conflictiveColors.contains(currentColor)) {
                    image.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        System.out.println("    Conflictive colors replaced.");
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat cv = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
        cv.put(0, 0, pixels);
        Mat processed = new Mat();

        System.out.println("    Scaling...");
        Imgproc.resize(cv, processed,new Size(500,500),0,0, Imgproc.INTER_LANCZOS4);
        System.out.println("    Scaled.");

        System.out.println("    Converting to Grayscale...");
        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_BGR2GRAY);
        System.out.println("    Grayscale converted.");

        System.out.println("    Applying Gaussian noise...");
        Imgproc.GaussianBlur(processed, processed, new Size(3, 3), 0, 0);
        System.out.println("    Gaussian noise applied.");

        // load to a buffered image
        BufferedImage imageCVProcessed = new BufferedImage(processed.cols(), processed.rows(), BufferedImage.TYPE_BYTE_GRAY);
        final byte[] targetPixels = ((DataBufferByte) imageCVProcessed.getRaster().getDataBuffer()).getData();
        processed.get(0, 0, targetPixels);
        return imageCVProcessed;
    }

    /**
     * Static function that performs the OCR to the sudoku image. It splits the image in a 9x9 grid and recognizes
     * each number. Use Tesseract (Tess4j) for OCR.
     * @param imageCVProcessed Buffered image of the howl sudoku.
     * @return Integer[][] with the numbers detected.
     * @throws TesseractException exception.
     */
    public static Integer[][] OCRSudoku(BufferedImage imageCVProcessed) throws TesseractException {
        // dimensions
        int rows = 9;
        int cols = 9;
        //gid of numbers
        Integer[][] numberGrid = new Integer[9][9];

        // Size of each tile
        int tileWidth = imageCVProcessed.getWidth() / cols;
        int tileHeight = imageCVProcessed.getHeight() / rows;
        System.out.println("    Preparing Tesseract for OCR...");
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(new File("src\\main\\resources\\tesseract").getPath());
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(3);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_CHAR);
        tesseract.setVariable("tessedit_char_whitelist", "123456789");
        tesseract.setVariable("debug_file", "NULL");
        System.out.println("    Tesseract prepared.");

        System.out.println("    Recognizing characters...");
        // Create sub-images
        for (int y = 0; y < rows; y++) {
            if(y%3==0) System.out.println("++===+===+===++===+===+===++===+===+===++");
            for (int x = 0; x < cols; x++) {
                int startX = x * tileWidth;
                int startY = y * tileHeight;
                BufferedImage subImage = imageCVProcessed.getSubimage(
                        (int) (startX+tileWidth*0.2),
                        (int) (startY+tileHeight*0.2),
                        (int) (tileWidth*0.6),
                        (int) (tileHeight*0.6)
                );
                imageGrid[y][x] = subImage;
                String result = tesseract.doOCR(subImage);
                result = result.replaceAll("\\D", "");
                if(!result.equals("")) {
                    numberGrid[y][x] = Integer.valueOf(result);
                }else{
                    result+=" ";
                }
                if(x%3==0){
                    System.out.print("||");
                }else {
                    System.out.print(" ");
                }
                System.out.print(" "+ result+" ");
            }
            System.out.println("||");
        }
        System.out.println("++===+===+===++===+===+===++===+===+===++");
        System.out.println("    Characters recognized.");
        return numberGrid;
    }

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";
        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);

        WebDriver driver = new EdgeDriver();

        try {
            // Waiter
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10));

            System.out.println("Loading https://sudoku.com/extreme/: ...");
            // Go to sudoku page
            driver.get("https://sudoku.com/extreme/");
            Thread.sleep(2000);
            System.out.println("Loaded.");

            System.out.println("Accepting cookies...");
            WebElement accentCoockies = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            accentCoockies.click();
            Thread.sleep(2000);
            System.out.println("Cookies accepted.");

            // Move pointer to remove popups
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ARROW_LEFT).perform();
            System.out.println("Sudoku loaded.");

            System.out.println("Screenshot the board...");
            Thread.sleep(1000);
            WebElement gameDiv = driver.findElement(By.id("game"));
            File screenshot = gameDiv.getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(screenshot);
            System.out.println("Board screenshot.");

            System.out.println("Transforming image for OCR...");
            BufferedImage imageCVProcessed = processScreenshot(image);
            System.out.println("Image transformed.");

            System.out.println("Doing OCR...");
            Integer[][] numberGrid = OCRSudoku(imageCVProcessed);
            System.out.println("OCR done.");

            System.out.println("Building MathModel...");
            SudokuMathModel model = new SudokuMathModel("SudokuModel", numberGrid);
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
            CharSequence seq = "\uE015\uE012\uE012\uE012\uE012\uE012\uE012\uE012\uE012";
            if (resultText.startsWith("MPSOLVER_OPTIMAL")) {
                for (ArrayList<Integer> rowValues : result) {
                    for (Integer value : rowValues) {
                        actions.sendKeys(value.toString() + "\uE014").perform();
                    }
                    actions.sendKeys(seq).perform();
                }
                System.out.println("Optimal solution introduced.");
            } else {
                System.out.println("Optimal solution not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}