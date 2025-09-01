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

public class TC_Product_08_to_12 {
    WebDriver driver;
    WebDriverWait wait;
    ExtentReports extent;
    ExtentTest test;
    String projectpath = System.getProperty("user.dir");

    @BeforeSuite
    public void setUpSuite() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(projectpath + "\\tc_product_08_to_12.html");
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
        if (driver != null)
            driver.quit();
    }

    @Test(dataProvider = "dp")
    public void f(Integer n, String s) {
        test = extent.createTest("Verify Product Features 08 to 12 - Iteration " + n);
        try {
            driver.get("https://automationexercise.com/");
            test.info("Navigated to Automation Exercise home page");

            // Products icon
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            test.pass("Products icon clicked.");

            // TC_Product_08 - Automation Exercise logo leads to homepage
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img")).click();
            WebElement homepageLogo = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
            Assert.assertTrue(homepageLogo.isDisplayed());
            test.pass("Homepage logo navigates successfully to the homepage.");

            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();

            // TC_Product_09 - Email subscription with valid email
            try {
                WebElement emailField = driver.findElement(By.id("susbscribe_email"));
                emailField.sendKeys("testemail@example.com");
                driver.findElement(By.id("subscribe")).click();
                // Optionally, add a wait for success message
                test.pass("Email subscription tested with valid email.");
            } catch (Exception e) {
                test.fail("Error in email subscription with valid email: " + e.getMessage());
            }

            // TC_Product_10 - Brand filter
            try {
                driver.findElement(By.xpath("/html/body/section[2]/div/div/div[1]/div/div[3]/div/ul/li[1]/a")).click();
                WebElement msg = driver.findElement(By.xpath("/html/body/section/div/div[1]/ol/li[2]"));
                Assert.assertTrue(msg.isDisplayed());
                test.pass("Brand filter is functioning as expected.");
            } catch (Exception e) {
                test.fail("Brand filter check failed: " + e.getMessage());
            }

            // TC_Product_11 - Category section expand
            try {
                driver.findElement(By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a/span")).click();
                WebElement womenCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"Women\"]/div/ul/li[2]/a")));
                Assert.assertTrue(womenCategory.isDisplayed());
                test.pass("Sections under each category are displayed.");
            } catch (Exception e) {
                test.fail("Expanding category section failed: " + e.getMessage());
            }

            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();

            // TC_Product_12 - Search bar
            try {
                WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"search_product\"]"));
                searchBar.sendKeys("Dress");
                driver.findElement(By.xpath("//*[@id=\"submit_search\"]/i")).click();
                WebElement search =
                        driver.findElement(By.xpath("/html/body/section[2]/div/div/div[2]/div/div[3]/div/div[1]/div[2]/div"));
                Assert.assertTrue(search.isDisplayed());
                test.pass("Search bar functionality verified.");
            } catch (Exception e) {
                test.fail("Search bar test failed: " + e.getMessage());
            }
        } catch (Exception e) {
            test.fail("Test failed due to an unexpected error: " + e.getMessage());
        }
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { new Object[] { 1, "a" } };
    }
}
