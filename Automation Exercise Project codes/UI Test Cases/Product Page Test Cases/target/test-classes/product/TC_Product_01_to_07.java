package product;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_Product_01_to_07 {
    WebDriver driver;
    WebDriverWait wait;
    ExtentReports extent;
    ExtentTest test;
    String projectpath = System.getProperty("user.dir");

    @BeforeSuite
    public void setUpSuite() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(projectpath + "\\tc_product_01_to_07.html");
        extent.attachReporter(spark);
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(dataProvider = "dp")
    public void f(Integer n, String s) {
        test = extent.createTest("Verify the icons - Iteration " + n);

        // Home Page
        driver.get("https://automationexercise.com/");
        test.info("Navigated to Automation Exercise home page");

        // Home icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[1]/a")).click();
            WebElement msg = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg.isDisplayed());
            test.pass("The home page is displayed successfully");
        } catch (Exception e) {
            test.fail("The home page is NOT displayed: " + e.getMessage());
        }

        // Cart icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[3]/a")).click();
            WebElement msg1 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg1.isDisplayed());
            test.pass("The cart page is displayed successfully");
        } catch (Exception e) {
            test.fail("The cart page is NOT displayed: " + e.getMessage());
        }

        // Login/Signup page icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[4]/a")).click();
            WebElement msg2 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg2.isDisplayed());
            test.pass("The login/signup page is displayed successfully");
        } catch (Exception e) {
            test.fail("The login/signup page is NOT displayed: " + e.getMessage());
        }

        // Test cases page icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[5]/a")).click();
            WebElement msg3 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg3.isDisplayed());
            test.pass("The test cases page is displayed successfully");
        } catch (Exception e) {
            test.fail("The test cases page is NOT displayed: " + e.getMessage());
        }

        // API Testing page icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[6]/a")).click();
            WebElement msg4 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg4.isDisplayed());
            test.pass("The API testing page is displayed successfully");
        } catch (Exception e) {
            test.fail("The API testing page is NOT displayed: " + e.getMessage());
        }

        // Video Tutorials icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[7]/a")).click();
            WebElement videoPageElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            Assert.assertTrue(videoPageElement.isDisplayed());
            test.pass("The video tutorials page is displayed successfully");
        } catch (Exception e) {
            test.fail("The video tutorials page is NOT displayed: " + e.getMessage());
        }
        driver.navigate().back();

        // Contact Us icon
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[8]/a")).click();
            WebElement msg7 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(msg7.isDisplayed());
            test.pass("The contact us page is displayed successfully");
        } catch (Exception e) {
            test.fail("The contact us page is NOT displayed: " + e.getMessage());
        }

        
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { new Object[] { 1, "a" } };
    }
}
