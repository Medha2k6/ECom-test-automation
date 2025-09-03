package functional.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import functional.utils.ExtentReportManager;
import functional.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TestCasesPageDetailedExtentTestListener implements ITestListener {
    
    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for Test Cases Page Detailed tests
        testDescriptions.put("TC_TestCasesPage_01_RegisterUserLink", "Verify 'Register User' link opens correct detailed test steps");
        testDescriptions.put("TC_TestCasesPage_02_LoginCorrectCredentials", "Verify 'Login User with correct email and password' link");
        testDescriptions.put("TC_TestCasesPage_03_LoginIncorrectCredentials", "Verify 'Login User with incorrect email and password' link");
        testDescriptions.put("TC_TestCasesPage_04_LogoutUser", "Verify 'Logout User' link");
        testDescriptions.put("TC_TestCasesPage_05_RegisterExistingEmail", "Verify 'Register User with existing email' link");
        testDescriptions.put("TC_TestCasesPage_06_ContactUsForm", "Verify 'Contact Us Form' link");
        testDescriptions.put("TC_TestCasesPage_27_AllLinksOpenCorrectScenarios", "Verify every test case link opens the correct scenario steps");
        testDescriptions.put("TC_TestCasesPage_28_NoLinksAreBroken", "Verify that test case links are not broken (no 404/500 error)");
        testDescriptions.put("TC_TestCasesPage_29_VerticalScrollSupport", "Verify page supports vertical scroll if test case list is long");
        testDescriptions.put("TC_TestCasesPage_30_FeedbackMailtoLink", "Verify feedback mailto link opens mail client prompt");
        testDescriptions.put("TC_TestCasesPage_31_ValidEmailSubscription", "Verify email subscription with valid email");
        testDescriptions.put("TC_TestCasesPage_32_InvalidEmailSubscription", "Verify email subscription with invalid email");
        testDescriptions.put("TC_TestCasesPage_33_BlankEmailSubscription", "Verify email subscription with blank email");
        testDescriptions.put("TC_TestCasesPage_34_DuplicateEmailSubscription", "Verify email subscription with duplicated email");
        testDescriptions.put("TC_TestCasesPage_35_LongEmailSubscription", "Verify entering more than 100 characters in the email subscription field");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Test Cases Page Detailed test suite: " + context.getName());
        functional.utils.ExtentReportManager.initializeExtentReport("testcasespage_func_extentreport");
        System.out.println("ExtentReports initialized for Test Cases Page Detailed suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Test Cases Page Detailed test suite: " + context.getName());
        functional.utils.ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for Test Cases Page Detailed suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Test Cases Page Detailed test: " + testName);
        
        try {
            functional.utils.ExtentReportManager.createTest(testName, description);
            functional.utils.ExtentReportManager.assignCategory("Test Cases Page Detailed Tests");
            functional.utils.ExtentReportManager.assignAuthor("QA Team");
            functional.utils.ExtentReportManager.logInfo("Starting test: " + testName);
            System.out.println("Starting test: " + testName);
        } catch (Exception e) {
            System.err.println("Error creating test in ExtentReports: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            functional.utils.ExtentReportManager.logPass("Test Passed: " + testName);
            
            // Capture screenshot on pass with custom folder
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = functional.utils.ScreenshotUtils.captureScreenshotOnPass(driver, testName, "testcasespage");
                if (screenshotPath != null) {
                    functional.utils.ExtentReportManager.addScreenshot(screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error logging test success: " + e.getMessage());
        }
        
        System.out.println("PASS: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        
        try {
            functional.utils.ExtentReportManager.logFail("Test Failed: " + testName);
            functional.utils.ExtentReportManager.logFail("Error: " + errorMessage);
            
            // Capture screenshot on failure with custom folder
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = functional.utils.ScreenshotUtils.captureScreenshotOnFailure(driver, testName, "testcasespage");
                if (screenshotPath != null) {
                    functional.utils.ExtentReportManager.addScreenshot(screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error logging test failure: " + e.getMessage());
        }
        
        System.out.println("FAIL: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";
        
        try {
            String description = testDescriptions.getOrDefault(testName, "Test Cases Page Detailed test: " + testName);
            functional.utils.ExtentReportManager.createTest(testName, description);
            functional.utils.ExtentReportManager.logSkip("Test Skipped: " + testName);
            functional.utils.ExtentReportManager.logSkip("Reason: " + skipReason);
        } catch (Exception e) {
            System.err.println("Error logging test skip: " + e.getMessage());
        }
        
        System.out.println("SKIP: " + testName + " - " + skipReason);
    }
    
    private WebDriver getDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(testInstance);
        } catch (Exception e) {
            System.err.println("Could not get driver instance for screenshot: " + e.getMessage());
            return null;
        }
    }
}