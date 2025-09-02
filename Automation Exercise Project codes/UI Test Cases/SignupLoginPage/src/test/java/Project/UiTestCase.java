package Project;

import org.testng.annotations.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.time.Duration;

public class UiTestCase {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;
    SignupPage signupPage;
    String projectPath = System.getProperty("user.dir");

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        ExtentSparkReporter spark = new ExtentSparkReporter(projectPath + "\\UiReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        signupPage = new SignupPage(driver);
        driver.get("https://automationexercise.com/");
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
        extent.flush();
    }

    @Test
    public void navigationTest() {
        test = extent.createTest("Navigation Icons Test");
        signupPage.clickHome();
        signupPage.clickProducts();
        signupPage.clickCart();
        signupPage.clickTestCases();
        signupPage.clickApiTesting();
        signupPage.clickVideoTutorials();
        signupPage.clickContactUs();
        test.pass("Navigation icons clicked successfully");
    }

    @Test
    public void subscriptionTest() {
        test = extent.createTest("Subscription Test");
        signupPage.enterSubscriptionEmail("dummy@test.com");
        signupPage.clickSubscribe();
        test.pass("Subscription email entered successfully");
    }

    @Test
    public void scrollTest() {
        test = extent.createTest("Scroll Test");
        signupPage.scrollToTop();
        Assert.assertTrue(signupPage.isScrollUpButtonVisible(), "Scroll up button not visible!");
        test.pass("Scroll to top verified");
    }

    @Test
    public void loginSignupPageTest() {
        test = extent.createTest("Login/Signup Page Test");
        driver.get("https://automationexercise.com/login");
        Assert.assertTrue(signupPage.isLoginSectionVisible(), "Login section not visible");
        Assert.assertTrue(signupPage.isSignupSectionVisible(), "Signup section not visible");
        Assert.assertTrue(signupPage.isOrSeparatorVisible(), "OR separator not visible");
        test.pass("Login/Signup sections verified");
    }
}
