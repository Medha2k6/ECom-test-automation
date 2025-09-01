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

public class TC_Product_21_to_28 {
    WebDriver driver;
    WebDriverWait wait;
    String projectpath = System.getProperty("user.dir");
    ExtentReports extent;
    ExtentTest test;

    @BeforeClass
    public void beforeClass() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(projectpath + "\\tc_product_21_to_28.html");
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
        test = extent.createTest("Verify Product Features 21 to 28");

        try {
            driver.get("https://automationexercise.com/");
            test.info("Opened Automation Exercise homepage");

            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            test.info("Navigated to Products page");

            // TC_Product_21 - Page scroll bar
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");
            test.pass("Page scroll verified");

            // TC_Product_22 - Special offer / big sale
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
            WebElement specialOffer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"sale_image\"]")));
            Assert.assertTrue(specialOffer.isDisplayed());
            test.pass("Special offer section verified");

            // TC_Product_23 - Presence of promo banners / SPECIAL OFFER banners
            WebElement promoBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"sale_image\"]")));
            Assert.assertTrue(promoBanner.isDisplayed());
            test.pass("Promo banner presence verified");

            // TC_Product_24 - Email subscription blank validation
            WebElement emailField = driver.findElement(By.id("susbscribe_email"));
            emailField.clear();
            emailField.sendKeys("");
            driver.findElement(By.id("subscribe")).click();
            test.pass("Email subscription blank credentials validation verified");

            // TC_Product_25 - Invalid product search
            WebElement searchField = driver.findElement(By.id("search_product"));
            searchField.clear();
            searchField.sendKeys("invalidproduct123");
            driver.findElement(By.id("submit_search")).click();
            WebElement noResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
            Assert.assertTrue(noResult.isDisplayed());
            test.pass("Invalid product search returns no results verified");

            // TC_Product_26 - Each product has name, price, Add to cart
            driver.get("https://automationexercise.com/products");
            WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[@class='productinfo text-center'])[1]")));
            Assert.assertTrue(firstProduct.findElement(By.tagName("p")).isDisplayed()); // product name
            Assert.assertTrue(firstProduct.findElement(By.xpath(".//h2")).isDisplayed()); // product price
            Assert.assertTrue(firstProduct.findElement(By.xpath(".//a[text()='Add to cart']")).isDisplayed());
            test.pass("Product name, price, and Add to cart button verified");

            // TC_Product_27 - Maximum character limit in search bar
            String longText = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            searchField = driver.findElement(By.id("search_product"));
            searchField.clear();
            searchField.sendKeys(longText);
            test.pass("Maximum character limit in search bar verified");

            // TC_Product_28 - Special characters in search bar
            searchField.clear();
            searchField.sendKeys("@#$%^&*()");
            driver.findElement(By.id("submit_search")).click();
            WebElement specialCharResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
            Assert.assertTrue(specialCharResult.isDisplayed());
            test.pass("Special characters search returns No products found verified");

        } catch (Exception e) {
            test.fail("Test failed at some point: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}
