package Project;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTestCase {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;
    LoginPage loginPage;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("LoginReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.automationexercise.com");
        loginPage = new LoginPage(driver);
        loginPage.clickLoginLink();
    }

    @Test(priority = 1)
    public void verifyLoginWithExistingAccount() {
        test = extent.createTest("Verify login with existing account");
        loginPage.enterUsername("validemail@example.com");
        loginPage.enterPassword("validpassword");
        loginPage.clickLoginButton();

        if (loginPage.isDashboardDisplayed()) {
            test.pass("Login successful with existing account.");
        } else {
            test.fail("Login failed with existing account.");
        }
    }

    @Test(priority = 2)
    public void verifyLoginWithNewAccountDetails() {
        test = extent.createTest("Verify login with new account details");
        loginPage.enterUsername("newuser@example.com");
        loginPage.enterPassword("newpassword");
        loginPage.clickLoginButton();

        String error = loginPage.getErrorMessage();
        if (error != null) {
            test.pass("Error message displayed for new account as expected: " + error);
        } else {
            test.fail("Unexpected behavior for new account login.");
        }
    }

    @Test(priority = 3)
    public void verifyLoginWithWrongPassword() {
        test = extent.createTest("Verify login with wrong password");
        loginPage.enterUsername("validemail@example.com");
        loginPage.enterPassword("wrongpassword");
        loginPage.clickLoginButton();

        String error = loginPage.getErrorMessage();
        if (error != null) {
            test.pass("Error message displayed: " + error);
        } else {
            test.fail("No error shown for wrong password.");
        }
    }

    @Test(priority = 4)
    public void verifyLoginWithBlankPassword() {
        test = extent.createTest("Verify login with blank password");
        loginPage.enterUsername("validemail@example.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        String error = loginPage.getErrorMessage();
        if (error != null) {
            test.pass("Error shown for blank password: " + error);
        } else {
            test.fail("No error shown for blank password.");
        }
    }

    @Test(priority = 5)
    public void verifyLoginWithBlankEmail() {
        test = extent.createTest("Verify login with blank email");
        loginPage.enterUsername("");
        loginPage.enterPassword("somepassword");
        loginPage.clickLoginButton();

        String error = loginPage.getErrorMessage();
        if (error != null) {
            test.pass("Error shown for blank email: " + error);
        } else {
            test.fail("No error shown for blank email.");
        }
    }

    @Test(priority = 6)
    public void verifyLoginWithBothFieldsBlank() {
        test = extent.createTest("Verify login with both fields blank");
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        String error = loginPage.getErrorMessage();
        if (error != null) {
            test.pass("Error shown for both fields blank: " + error);
        } else {
            test.fail("No error shown for both fields blank.");
        }
    }

    @Test(priority = 7)
    public void verifyLoginButtonWorks() {
        test = extent.createTest("Verify login button works");
        if (loginPage.isLoginButtonDisplayed()) {
            test.pass("Login button is displayed and clickable.");
        } else {
            test.fail("Login button not found or not clickable.");
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
}
