package com.ui.datadriven;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;

import java.io.File;
import java.util.List;
import com.ui.base.*;
import com.ui.pages.ApiTestingUIPage;
import com.ui.utilities.ExcelUtils;
import com.ui.utilities.ScreenshotUtilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class DataDrivenAPITest {
    
    // Instance variables for this test class only
    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        String reportPath = System.getProperty("user.dir") 
            + "/reports/datadriven_reports/APIExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("API Test Report");
        sparkReporter.config().setReportName("API Testing Automation Report");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Tester", "QA Team");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Browser", "Chrome");
    }
    
    @BeforeMethod
    public void setUp() {
        // Setup Chrome driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for speed
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void executeTestCases() {
        String excelPath = System.getProperty("user.dir") + "/src/test/resources/api.xlsx";  
        List<String[]> testData = ExcelUtils.getTestData(excelPath);

        for (int i = 0; i < testData.size(); i++) {
            String testCaseID = testData.get(i)[0].trim();
            String email = testData.get(i)[1].trim();

            runAPITest(testCaseID, email, i);
        }
    }

    private void runAPITest(String testCaseID, String email, int testCaseNumber) {
        test = extent.createTest("Test Case " + testCaseNumber + " - " + testCaseID);

        try {
            ApiTestingUIPage apiPage = new ApiTestingUIPage(driver);
            apiPage.navigateToApiTestingPage();
            test.log(Status.INFO, "Navigated to API Testing page");

            if (!email.isEmpty()) {
                apiPage.scrollToSubscriptionSection();
                apiPage.enterSubscriptionEmail(email);
                test.log(Status.INFO, "Entered email: " + email);
                apiPage.clickSubscribeButton();
                test.log(Status.INFO, "Clicked Subscribe button");
            }

            boolean successDisplayed = apiPage.getSubscriptionEmailFieldLocator() != null; // Use your actual verification method

            if (testCaseID.equals("TC_API_Testing_23") && email.equals("keerthanashetty0024@gmail.com")) {
                if (successDisplayed) throw new AssertionError("Expected failure for duplicate subscription");
            } else if (!email.isEmpty() && email.contains("@") && !email.contains("!")) {
                if (!successDisplayed) throw new AssertionError("Expected success, but subscription failed");
            } else if (email.isEmpty()) {
                if (successDisplayed) throw new AssertionError("Expected failure for empty email");
            } else {
                if (successDisplayed) throw new AssertionError("Expected failure, but subscription succeeded");
            }

            test.log(Status.PASS, "Test passed for: " + testCaseID);

        } catch (AssertionError ae) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "api_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Assertion failed: " + ae.getMessage());
            try {
                test.fail("Screenshot of failure", MediaEntityBuilder.createScreenCaptureFromPath(new File(screenshotPath).getAbsolutePath()).build());
            } catch (Exception e) {
                test.log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
            }

        } catch (Exception e) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "api_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Unexpected error: " + e.getMessage());
            try {
                test.fail("Screenshot of failure", MediaEntityBuilder.createScreenCaptureFromPath(new File(screenshotPath).getAbsolutePath()).build());
            } catch (Exception ex) {
                test.log(Status.WARNING, "Could not attach screenshot: " + ex.getMessage());
            }
        }
    }

    @AfterSuite
    public void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }
}