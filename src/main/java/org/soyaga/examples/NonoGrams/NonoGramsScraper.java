package org.soyaga.examples.NonoGrams;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.soyaga.examples.NonoGrams.MathModel.NonoGramsMMInitializer;
import org.soyaga.examples.NonoGrams.MathModel.NonoGramsMathModel;

import java.time.Duration;
import java.util.*;

public class NonoGramsScraper {

    public static void main(String[] args) {
        //Selenium driver path
        String seleniumPath = "src\\main\\resources\\edgedriver_win64\\msedgedriver.exe";

        //Dimensions
        Integer rows = null;
        Integer cols = null;


        //WebElements
        WebElement rowDel = null;
        WebElement cellDel = null;

        // Set path to your ChromeDriver executable
        System.setProperty("webdriver.edge.driver", seleniumPath);


        WebDriver driver = new EdgeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Waiter
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofMillis(4000));

            System.out.println("Loading nonogram: ...");
            // Wait/load, then navigate to the game
            Thread.sleep(2000);
            driver.get("https://www.nonograms.org/search?name=&colors=2&colors_min=1&colors_max=10&width_min=0&width_max=200&height_min=0&height_max=200&rating_min=9&rating_max=10&complexity_min=9&complexity_max=10&coloring_min=0&coloring_max=100&symmetry_min=0&symmetry_max=100&numgrid_min=0&numgrid_max=50&sort=0");
            longWait.until(ExpectedConditions.urlToBe("https://www.nonograms.org/search?name=&colors=2&colors_min=1&colors_max=10&width_min=0&width_max=200&height_min=0&height_max=200&rating_min=9&rating_max=10&complexity_min=9&complexity_max=10&coloring_min=0&coloring_max=100&symmetry_min=0&symmetry_max=100&numgrid_min=0&numgrid_max=50&sort=0"));
            System.out.println("Nonograms loaded.");

            // Get all window handles
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);

            // Switch to the new tab (assuming it's the last tab)
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println("Refreshing in case it did not load...");
            driver.navigate().refresh();
            System.out.println("Refreshed.");

            System.out.println("Selecting random nonogram...");
            try {
                WebElement list = driver.findElement(By.className("nonogram_list"));
                List<WebElement> nonogramElements = list.findElements(By.className("nonogram_img"));
                Random rand = new Random();
                int index = rand.nextInt(nonogramElements.size());
                WebElement randomElement = nonogramElements.get(index);
                randomElement.click();
                System.out.println("Nonogram selected.");
            }
            catch (Exception ex){
                System.out.println("Nonogram not selected");
            }

            System.out.println("Scaling image...");
            try {
                WebElement table = driver.findElement(By.id("nonogram_table"));
                Long tableWidth = (Long) js.executeScript(
                        "return arguments[0].offsetWidth;", table);
                Long tableHeight = (Long) js.executeScript(
                        "return arguments[0].offsetHeight;", table);
                Long windowWidth = (Long) js.executeScript("return window.innerWidth;");
                Long windowHeight = (Long) js.executeScript("return window.innerHeight;");
                assert windowWidth != null;
                assert tableWidth != null;
                double zoomW = windowWidth.doubleValue() / tableWidth.doubleValue();
                assert windowHeight != null;
                assert tableHeight != null;
                double zoomH = windowHeight.doubleValue() / tableHeight.doubleValue();
                double zoom = Math.min(zoomW, zoomH);
                String zoomPercent = String.format("%.0f%%", zoom * 100);
                js.executeScript("document.body.style.zoom=arguments[0];", zoomPercent);
                js.executeScript("arguments[0].scrollIntoView(true);", table);
                List<WebElement> tableRows = table.findElement(By.tagName("tbody")).findElements(By.xpath("./tr"));
                rowDel = tableRows.get(0);
                cellDel = tableRows.get(1).findElements(By.xpath("./td")).get(0);
                System.out.println("Image scaled.");
            }
            catch (Exception ex){
                System.out.println("Image not scaled.");
            }

            System.out.println("Retrieving size...");
            try {
                List<WebElement> elements = driver.findElements(
                        By.xpath("//td[starts-with(normalize-space(text()), 'Size: ')]")
                );
                WebElement element = elements.get(0);
                String [] size = element.getText().split(" ")[1].split("x");
                rows = Integer.parseInt(size[1].trim());
                cols = Integer.parseInt(size[0].trim());
                System.out.println("Size retrieved.");
            }
            catch (Exception ex){
                System.out.println("Size not Retrieved");
            }


            System.out.println("Computing colors...");
            HashMap<String,WebElement> colorElementMap = new HashMap<>();
            HashMap<String,Integer> colorNumberMap = new HashMap<>();
            HashMap<Integer,String> numberColorMap = new HashMap<>();
            int colorNumber = 0;
            try {
                WebElement colorTable = driver.findElement(By.className("nonogram_color_table"));
                WebElement tableBody = colorTable.findElement(By.tagName("tbody"));
                List<WebElement> tableRows = tableBody.findElements(By.tagName("tr"));
                for (WebElement row : tableRows) {
                    List<WebElement> rowCols = row.findElements(By.tagName("td"));
                    for (WebElement col : rowCols) {
                        String color = col.getCssValue("background-color");
                        colorElementMap.putIfAbsent(color, col);
                        if(!colorNumberMap.containsKey(color)){
                            colorNumber++;
                            colorNumberMap.put(color,colorNumber);
                            numberColorMap.put(colorNumber,color);
                        }
                    }
                }
                System.out.println("Colors computed.");
            }
            catch (Exception ex){
                System.out.println("Colors not computed.");
            }

            System.out.println("Retrieving board");
            assert rows!=null;
            assert cols!=null;
            WebElement[][] boardGrid = new WebElement[rows][cols];
            HashMap<Integer,HashMap<Integer,Object[]>> rowConstraints = new HashMap<>();// row, col(left to right) [color,number]
            HashMap<Integer,HashMap<Integer,Object[]>> colConstraints = new HashMap<>();// col, row(down to up) [color,number]
            try{
                System.out.println("    Retrieving cells...");
                WebElement contentTable = driver.findElement(By.className("nmtc")).findElement(By.xpath("//table")).findElement(By.tagName("tbody"));
                List<WebElement> cells = contentTable.findElements(By.xpath(".//td[starts-with(@id, 'nmf')]"));
                for(WebElement element:cells){
                    String [] position = Objects.requireNonNull(element.getAttribute("id")).replace("nmf", "").split("_");
                    boardGrid[Integer.parseInt(position[1].trim())][Integer.parseInt(position[0].trim())] = element;
                }
                System.out.println("    Cells retrieved.");

                System.out.println("    Retrieving row constraints...");
                WebElement rowConstraintsTable = driver.findElement(By.className("nmtt")).findElement(By.xpath("//table")).findElement(By.tagName("tbody"));
                List<WebElement> rowConstraintsList = rowConstraintsTable.findElements(By.xpath(".//td[starts-with(@id, 'nmh')]"));
                for(WebElement element:rowConstraintsList){
                    String [] position = Objects.requireNonNull(element.getAttribute("id")).replace("nmh", "").split("_");
                    int r = Integer.parseInt(position[1]);
                    int c = Integer.parseInt(position[0]);
                    String color = element.getCssValue("background-color");
                    int number = Integer.parseInt(element.getText().trim());
                    rowConstraints.computeIfAbsent(r, x->new HashMap<>()).put(c,new Object[]{color,number});
                }
                System.out.println("    Row constraints retrieved.");

                System.out.println("    Retrieving col constraints...");
                WebElement colConstraintsTable = driver.findElement(By.className("nmtl")).findElement(By.xpath("//table")).findElement(By.tagName("tbody"));
                List<WebElement> colConstraintsList = colConstraintsTable.findElements(By.xpath(".//td[starts-with(@id, 'nmv')]"));
                for(WebElement element:colConstraintsList){
                    String [] position = Objects.requireNonNull(element.getAttribute("id")).replace("nmv", "").split("_");
                    int r = Integer.parseInt(position[1]);
                    int c = Integer.parseInt(position[0]);
                    String color = element.getCssValue("background-color");
                    int number = Integer.parseInt(element.getText().trim());
                    colConstraints.computeIfAbsent(c, x->new HashMap<>()).put(r,new Object[]{color,number});
                }
                System.out.println("    Col constraints retrieved.");
            }
            catch (Exception ex){
                System.out.println("Board not retrieved.");
            }

            System.out.println("Hiding details...");
            try{
                js.executeScript("arguments[0].style.display='none'; arguments[1].style.display='none';", rowDel, cellDel);
                System.out.println("Details hidden.");
            }
            catch (Exception ex){
                System.out.println("Details not hidden.");
            }
            System.out.println("Creating MathModel SAT...");
            NonoGramsMMInitializer initializer = new NonoGramsMMInitializer(rows, cols, rowConstraints, colConstraints,
                    colorNumberMap);
            NonoGramsMathModel model = new NonoGramsMathModel("NonoSAT",initializer, rows,cols,numberColorMap);
            initializer.setModel(model);
            System.out.println("MathModel SAT created.");

            System.out.println("Computing CP...");
            model.optimize();
            System.out.println("CP computed.");

            System.out.println("Introducing solution...");
            HashMap<String,ArrayList<Integer[]>> result = model.getResult(); // HashMap<ColorString, Array<{row,col}>
            for(Map.Entry<String,ArrayList<Integer[]>> resultEntry:result.entrySet()){
                System.out.println("    Selecting color...");
                colorElementMap.get(resultEntry.getKey()).click();
                System.out.println("    Color selected.");
                System.out.println("    Clicking cells...");
                for(Integer[] rowCol:resultEntry.getValue()){
                    boardGrid[rowCol[0]][rowCol[1]].click();
                }
                System.out.println("    Cells clicked.");
            }
            System.out.println("Solution introduced.");

            Thread.sleep(10000);

            System.out.println("Clicking OK...");
            try{
                Alert alert = driver.switchTo().alert();
                longWait.until(ExpectedConditions.alertIsPresent());
                alert.accept();
                System.out.println("OK clicked.");
            }
            catch (Exception ex){
                System.out.println("OK not clicked.");
            }
            System.out.println("Unhiding back details...");
            try{
                js.executeScript("arguments[0].style.display=''; arguments[1].style.display='';", rowDel, cellDel);
                System.out.println("Details Unhidden.");
            }
            catch (Exception ex){
                System.out.println("Details not Unhidden.");
            }

            System.out.println("Scaling image...");
            try {
                WebElement table = driver.findElement(By.id("nonogram_table"));
                Long tableWidth = (Long) js.executeScript(
                        "return arguments[0].offsetWidth;", table);
                Long tableHeight = (Long) js.executeScript(
                        "return arguments[0].offsetHeight;", table);
                Long windowWidth = (Long) js.executeScript("return window.innerWidth;");
                Long windowHeight = (Long) js.executeScript("return window.innerHeight;");
                assert windowWidth != null;
                assert tableWidth != null;
                double zoomW = windowWidth.doubleValue() / tableWidth.doubleValue();
                assert windowHeight != null;
                assert tableHeight != null;
                double zoomH = windowHeight.doubleValue() / tableHeight.doubleValue();
                double zoom = Math.min(zoomW, zoomH);
                String zoomPercent = String.format("%.0f%%", zoom * 100);
                js.executeScript("document.body.style.zoom=arguments[0];", zoomPercent);
                js.executeScript("arguments[0].scrollIntoView(true);", table);
                System.out.println("Image scaled.");
            }
            catch (Exception ex){
                System.out.println("Image not scaled.");
            }
            Thread.sleep(5000);
            System.out.println("Game loop finalized.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}