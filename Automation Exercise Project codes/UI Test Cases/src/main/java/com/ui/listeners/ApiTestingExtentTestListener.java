package com.ui.listeners;

import org.testng.ITestListener;

import org.testng.ITestResult;
import org.testng.ITestContext;
import com.ui.utilities.ExtentReportManager;
import com.ui.utilities.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ApiTestingExtentTestListener implements ITestListener {
    
    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for API Testing page
        testDescriptions.put("TC_API_Testing_01_NavigateToAPITesting", "Verify user can navigate to API testing page from home page");
        testDescriptions.put("TC_API_Testing_02_LogoRedirect", "Verify clicking the logo redirects to the homepage");
        testDescriptions.put("TC_API_Testing_03_HomeIconRedirect", "Verify Home icon redirects to the homepage successfully");
        testDescriptions.put("TC_API_Testing_04_ProductsIconRedirect", "Verify Products icon redirects to the product listings page");
        testDescriptions.put("TC_API_Testing_05_CartIconRedirect", "Verify Cart icon redirects to the cart page properly");
        testDescriptions.put("TC_API_Testing_06_SignupLoginIcon", "Verify Signup/login icon correctly redirects to login page");
        testDescriptions.put("TC_API_Testing_07_TestCasesIconRedirect", "Verify Test Cases icon redirects to the Test Cases listing page");
        testDescriptions.put("TC_API_Testing_08_APITestingIcon", "Verify API Testing icon redirects to the API list page");
        testDescriptions.put("TC_API_Testing_09_VideoTutorialsIcon", "Verify Video Tutorials icon opens the correct video tutorial resources");
        testDescriptions.put("TC_API_Testing_10_ContactUsIcon", "Verify Contact Us icon opens the contact form page successfully");
        testDescriptions.put("TC_API_Testing_11_SubscriptionSection", "Verify the subscription section is visible and functional");
        testDescriptions.put("TC_API_Testing_12_APISectionExpands", "Verify each API section expands when clicked");
        testDescriptions.put("TC_API_Testing_13_APISectionCollapses", "Verify expanded API section collapses when clicked again");
        testDescriptions.put("TC_API_Testing_14_ScrollFunctionality", "Verify scroll functionality to view all APIs");
        testDescriptions.put("TC_API_Testing_15_APIURLsClickable", "Verify that API URLs are clickable and working");
        testDescriptions.put("TC_API_Testing_16_ConsistentDesign", "Verify consistent design across all API blocks");
        testDescriptions.put("TC_API_Testing_17_RightClickNewTab", "Verify API section titles support 'Open in new tab' on right click");
        testDescriptions.put("TC_API_Testing_18_ValidEmailSubscription", "Validate successful subscription with a valid email");
        testDescriptions.put("TC_API_Testing_19_EmailWithoutAtSymbol", "Validate email field with missing '@' character");
        testDescriptions.put("TC_API_Testing_20_SpecialCharactersInEmail", "Validate email with special characters in domain part");
        testDescriptions.put("TC_API_Testing_21_LongEmail", "Validate email field with more than 100 characters");
        testDescriptions.put("TC_API_Testing_22_EmptyEmailField", "Validate behavior when email field is left empty");
        testDescriptions.put("TC_API_Testing_23_DuplicateEmailSubscription", "Validate repeated email subscription (duplicate check)");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting API Testing test suite: " + context.getName());
        ExtentReportManager.initializeExtentReport();
        System.out.println("ExtentReports initialized for API Testing suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing API Testing test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for API Testing suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "API Testing page test: " + testName);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("API Testing Page Tests");
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
            String description = testDescriptions.getOrDefault(testName, "API Testing page test: " + testName);
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