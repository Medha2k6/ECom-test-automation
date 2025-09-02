package proj11.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import proj11.utils.ExtentReportManager;
import proj11.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TestCasesExtentTestListener implements ITestListener {
    
    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for Test Cases page
        testDescriptions.put("TC_Test_Cases_01_PageLoadsSuccessfully", "Verify Test Cases page loads successfully");
        testDescriptions.put("TC_Test_Cases_02_AllTestCaseLinksPresent", "Verify presence of all listed test case links");
        testDescriptions.put("TC_Test_Cases_03_TestCasesSeparatedByRows", "Verify each test case name/link is visually separated as a row");
        testDescriptions.put("TC_Test_Cases_04_LinksStyledAsClickable", "Verify test case links are styled as clickable");
        testDescriptions.put("TC_Test_Cases_05_ClickingLinkOpensDetails", "Verify clicking a test case link opens the test step details");
        testDescriptions.put("TC_Test_Cases_06_FeedbackSectionPresent", "Verify 'Feedback for Us' section is present below test cases");
        testDescriptions.put("TC_Test_Cases_07_URLsVisibleAndWorking", "Verify URL under each test case is visible and working");
        testDescriptions.put("TC_Test_Cases_08_FeedbackEmailOpensMailApp", "Verify clicking feedback email opens mail application prompt");
        testDescriptions.put("TC_Test_Cases_09_ValidEmailSubscription", "Verify email subscription field accepts valid email addresses");
        testDescriptions.put("TC_Test_Cases_10_InvalidEmailSubscription", "Verify email subscription field rejects invalid email addresses");
        testDescriptions.put("TC_Test_Cases_11_ScrollToTopArrow", "Verify orange arrow button scrolls the page to the top");
        testDescriptions.put("TC_Test_Cases_12_BlankEmailRejection", "Verify email subscription field rejects blank credentials");
        testDescriptions.put("TC_Test_Cases_13_LogoNavigatesToHome", "Verify clicking Automation Exercise logo navigates to Home");
        testDescriptions.put("TC_Test_Cases_14_HomeLinkNavigation", "Verify 'Home' link navigates to Home page");
        testDescriptions.put("TC_Test_Cases_15_ProductsLinkNavigation", "Verify 'Products' link navigates to Products page");
        testDescriptions.put("TC_Test_Cases_16_CartLinkNavigation", "Verify 'Cart' link navigates to Cart page");
        testDescriptions.put("TC_Test_Cases_17_SignUpLoginNavigation", "Verify 'Sign Up/Login' link navigates to SignUp/Login page");
        testDescriptions.put("TC_Test_Cases_18_TestCasesLinkNavigation", "Verify 'Test Cases' link navigates to Test Cases page");
        testDescriptions.put("TC_Test_Cases_19_APITestingNavigation", "Verify 'API Testing' link navigates to API Testing page");
        testDescriptions.put("TC_Test_Cases_20_VideoTutorialsNavigation", "Verify 'Video Tutorials' link navigates to Video Tutorials page");
        testDescriptions.put("TC_Test_Cases_21_ContactUsNavigation", "Verify 'Contact Us' link navigates to Contact Us page");
        testDescriptions.put("TC_Test_Cases_22_ScrollBarFunctionality", "Verify appearance and function of the page scroll bar");
        testDescriptions.put("TC_Test_Cases_23_PageResponsiveness", "Verify responsiveness of Test Cases page on different screen sizes");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Test Cases test suite: " + context.getName());
        ExtentReportManager.initializeExtentReport();
        System.out.println("ExtentReports initialized for Test Cases suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Test Cases test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for Test Cases suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Test Cases page test: " + testName);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("Test Cases Page Tests");
            ExtentReportManager.assignAuthor("QA Team");
            ExtentReportManager.logInfo("Starting test: " + testName);
            System.out.println("Starting test: " + testName);
        } catch (Exception e) {
            System.err.println("Error creating test in ExtentReports: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            ExtentReportManager.logPass("Test Passed: " + testName);
            
            // Capture screenshot on pass (optional)
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnPass(driver, testName);
                if (screenshotPath != null) {
                    ExtentReportManager.addScreenshot(screenshotPath);
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
            ExtentReportManager.logFail("Test Failed: " + testName);
            ExtentReportManager.logFail("Error: " + errorMessage);
            
            // Capture screenshot on failure
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnFailure(driver, testName);
                if (screenshotPath != null) {
                    ExtentReportManager.addScreenshot(screenshotPath);
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
            String description = testDescriptions.getOrDefault(testName, "Test Cases page test: " + testName);
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.logSkip("Test Skipped: " + testName);
            ExtentReportManager.logSkip("Reason: " + skipReason);
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