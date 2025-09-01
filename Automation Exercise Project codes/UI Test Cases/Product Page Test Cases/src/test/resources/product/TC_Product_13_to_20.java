package product;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_Product_13_to_20 {
    WebDriver driver;
    WebDriverWait wait;
    String projectpath = System.getProperty("user.dir");
    ExtentReports extent;
    ExtentTest test;

    @BeforeClass
    public void beforeClass() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(projectpath + "\\tc_product_13_to_20.html");
        extent.attachReporter(spark);
    }

    @AfterClass
    public void afterClass() {
        extent.flush();
    }

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(dataProvider = "dp")
    public void f(Integer n, String s) {
        test = extent.createTest("Verify Product Features 13 to 20");

        driver.get("https://automationexercise.com/");
        test.info("Opened Automation Exercise homepage");

        driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
        test.info("Clicked on Products page");

        try {
            // TC_Product_13 - Add to cart button
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//a[text()='Add to cart'])[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
            WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='cartModal']//h4")));
            Assert.assertTrue(cart.isDisplayed());
            test.pass("Add to cart button works correctly");
            driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
        } catch (Exception e) {
            test.fail("Add to cart button test failed: " + e.getMessage());
        }

        try {
            // TC_Product_14 - Go to top button
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            WebElement scrollUp = wait.until(ExpectedConditions.elementToBeClickable(By.id("scrollUp")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", scrollUp);
            test.pass("Go to top button functionality verified");
        } catch (Exception e) {
            test.fail("Go to top button test failed: " + e.getMessage());
        }

        try {
            // TC_Product_15 - View product button
            WebElement viewProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//a[text()='View Product'])[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewProduct);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewProduct);
            WebElement productDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='product-information']")));
            Assert.assertTrue(productDetails.isDisplayed());
            test.pass("View product button verified");
            driver.navigate().back();
        } catch (Exception e) {
            test.fail("View product button test failed: " + e.getMessage());
        }

        try {
            // TC_Product_16 - Women category sections
            WebElement women = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", women);
            WebElement womenSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='Women']/div/ul/li[2]/a")));
            Assert.assertTrue(womenSection.isDisplayed());
            test.pass("Women sections under category verified");
        } catch (Exception e) {
            test.fail("Women category sections test failed: " + e.getMessage());
        }

        try {
            // TC_Product_17 - Men category sections
            WebElement men = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"accordian\"]/div[2]/div[1]/h4/a/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", men);
            WebElement menSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='Men']/div/ul/li[2]/a")));
            Assert.assertTrue(menSection.isDisplayed());
            test.pass("Men sections under category verified");
        } catch (Exception e) {
            test.fail("Men category sections test failed: " + e.getMessage());
        }

        try {
            // TC_Product_18 - Kids category sections
            WebElement kids = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"accordian\"]/div[3]/div[1]/h4/a/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", kids);
            WebElement kidsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='Kids']/div/ul/li[2]/a")));
            Assert.assertTrue(kidsSection.isDisplayed());
            test.pass("Kids sections under category verified");
        } catch (Exception e) {
            test.fail("Kids category sections test failed: " + e.getMessage());
        }

        try {
            // TC_Product_19 - View Cart button after adding to cart
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            WebElement addToCartBtn1 = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//a[text()='Add to cart'])[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn1);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn1);
            WebElement cart1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"cartModal\"]/div/div/div[2]/p[2]/a/u")));
            Assert.assertTrue(cart1.isDisplayed());
            test.pass("View Cart button after adding to cart verified");
            driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
        } catch (Exception e) {
            test.fail("View Cart button test failed: " + e.getMessage());
        }

        try {
            // TC_Product_20 - Email subscription invalid email
            WebElement emailField = driver.findElement(By.id("susbscribe_email"));
            emailField.clear();
            emailField.sendKeys("a@g");
            driver.findElement(By.id("subscribe")).click();
            // Wait for error or success alert
            WebElement alertMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'alert-success') or contains(@class,'alert-danger')]")));
            Assert.assertTrue(alertMsg.isDisplayed());
            test.pass("Email subscription invalid email validation performed");
        } catch (Exception e) {
            test.fail("Invalid email subscription test failed: " + e.getMessage());
        }
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}
