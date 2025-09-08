package com.ui.listeners;

import org.testng.ITestListener;

import org.testng.ITestResult;
import org.testng.ITestContext;
import com.ui.utilities.ExtentReportManager;
import com.ui.utilities.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;

public class ExtentTestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting test suite: " + context.getName());
        ExtentReportManager.initializeExtentReport();
        System.out.println("ExtentReports initialized for suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = getTestDescription(result);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("Home Page Tests");
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
            ExtentReportManager.logPass("✅ Test Passed: " + testName);
            
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
        
        System.out.println("✅ PASS: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        
        try {
            ExtentReportManager.logFail("❌ Test Failed: " + testName);
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
        
        System.out.println("❌ FAIL: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";
        
        try {
            ExtentReportManager.createTest(testName, getTestDescription(result));
            ExtentReportManager.logSkip("⚠️ Test Skipped: " + testName);
            ExtentReportManager.logSkip("Reason: " + skipReason);
        } catch (Exception e) {
            System.err.println("Error logging test skip: " + e.getMessage());
        }
        
        System.out.println("⚠️ SKIP: " + testName + " - " + skipReason);
    }
    
    private String getTestDescription(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        
        // Create description based on method name
        switch (methodName) {
            case "TC_Home_01_Chrome_URL":
                return "Verify homepage URL loads correctly on Chrome browser";
            case "TC_Home_02_Edge_URL":
                return "Verify homepage URL loads correctly on Edge browser";
            case "TC_Home_03_Brave_URL":
                return "Verify homepage URL loads correctly on Brave/Firefox browser";
            case "TC_Home_04_HomeIcon":
                return "Verify Home icon functionality in navigation";
            case "TC_Home_05_ProductsIcon":
                return "Verify Products icon functionality in navigation";
            case "TC_Home_06_CartIcon":
                return "Verify Cart icon functionality in navigation";
            case "TC_Home_07_SignupLoginIcon":
                return "Verify Signup/Login icon functionality in navigation";
            case "TC_Home_08_TestCasesIcon":
                return "Verify Test Cases icon functionality in navigation";
            case "TC_Home_09_APITestingIcon":
                return "Verify API Testing icon functionality in navigation";
            case "TC_Home_10_VideoTutorialsIcon":
                return "Verify Video Tutorials icon functionality in navigation";
            case "TC_Home_11_ContactUsIcon":
                return "Verify Contact Us icon functionality in navigation";
            case "TC_Home_12_TestCasesButton":
                return "Verify Test Cases button functionality on homepage";
            case "TC_Home_13_APIsListButton":
                return "Verify APIs List button functionality on homepage";
            case "TC_Home_14_AutomationExerciseLogo":
                return "Verify Automation Exercise logo click functionality";
            case "TC_Home_15_EmailSubscriptionValid":
                return "Verify email subscription with valid email address";
            case "TC_Home_16_BrandNameFilter":
                return "Verify brand name filter functionality";
            case "TC_Home_17_CategorySectionExpand":
                return "Verify category section expansion functionality";
            case "TC_Home_18_AddToCart":
                return "Verify Add to Cart button functionality";
            case "TC_Home_19_ScrollToTop":
                return "Verify scroll to top button functionality";
            case "TC_Home_20_ViewProduct":
                return "Verify View Product button functionality";
            case "TC_Home_21_WomenCategorySection":
                return "Verify Women category section functionality";
            case "TC_Home_22_MenCategorySection":
                return "Verify Men category section functionality";
            case "TC_Home_23_KidsCategorySection":
                return "Verify Kids category section functionality";
            case "TC_Home_24_ViewCartAfterAddToCart":
                return "Verify View Cart functionality after adding product";
            case "TC_Home_25_InvalidEmailSubscription":
                return "Verify email subscription with invalid email address";
            case "TC_Home_26_ScrollBarFunctionality":
                return "Verify scroll bar functionality on homepage";
            case "TC_Home_27_Responsiveness":
                return "Verify homepage responsiveness on different screen sizes";
            case "TC_Home_28_CarouselSliders":
                return "Verify carousel sliders functionality on homepage";
            case "TC_Home_29_EmailWithoutAtSymbol":
                return "Verify email validation without @ symbol";
            default:
                return "Test case: " + methodName;
        }
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