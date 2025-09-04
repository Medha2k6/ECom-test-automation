package com.functional.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import com.functional.utilities.ExtentManager;
import com.functional.utilities.ScreenshotUtilities;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrationExtentTestListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for User Registration tests
        testDescriptions.put("TC_ECOM_Reg_001_ValidNameAndEmail", "Verify signup with valid name and unique valid email");
        testDescriptions.put("TC_ECOM_Reg_002_InvalidEmailFormat", "Verify signup with invalid email format");
        testDescriptions.put("TC_ECOM_Reg_003_InvalidNameFormat", "Verify signup with invalid name format");
        testDescriptions.put("TC_ECOM_Reg_004_BlankEmail", "Verify signup with blank email");
        testDescriptions.put("TC_ECOM_Reg_005_BlankName", "Verify signup with blank name");
        testDescriptions.put("TC_ECOM_Reg_006_BothFieldsBlank", "Verify signup with both fields blank");
        testDescriptions.put("TC_ECOM_Reg_007_SpecialCharactersInName", "Verify signup with special characters in name");
        testDescriptions.put("TC_ECOM_Reg_008_ValidSignupProcess", "Verify clicking Signup proceeds with valid Name & Email");
        testDescriptions.put("TC_ECOM_Reg_010_MrRadioButton", "Verify functionality of Mr radio button");
        testDescriptions.put("TC_ECOM_Reg_011_MrsRadioButton", "Verify functionality of Mrs radio button");
        testDescriptions.put("TC_ECOM_Reg_014_PasswordMinimumLength", "Verify password minimum length (6 chars) and format");
        testDescriptions.put("TC_ECOM_Reg_015_ValidPassword", "Verify accepting valid password");
        testDescriptions.put("TC_ECOM_Reg_016_DateOfBirthSelection", "Verify selecting valid day, month, year");
        testDescriptions.put("TC_ECOM_Reg_037_CreateAccountWithValidData", "Verify create account button functionality with valid credentials");
        testDescriptions.put("TC_ECOM_Reg_039_ValidEmailSubscription", "Verify email subscription with valid email");
        testDescriptions.put("TC_ECOM_Reg_040_InvalidEmailSubscription", "Verify email subscription with invalid email");
        testDescriptions.put("TC_ECOM_Reg_044_GuestCheckoutPrompt", "Ensure guest is prompted to login/register when clicking checkout");
        testDescriptions.put("TC_ECOM_Reg_047_EmptyCartCheckout", "Verify checkout is blocked if cart is empty");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting User Registration test suite: " + context.getName());
        extent = ExtentManager.getInstance("UserRegistrationReport.html");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing User Registration test suite: " + context.getName());
        if (extent != null) {
            extent.flush();
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "User Registration test: " + testName);
        
        ExtentTest test = extent.createTest(testName, description)
                .assignCategory("User Registration Tests")
                .assignAuthor("QA Team");
        testThread.set(test);

        test.info("Starting test: " + testName);
        System.out.println("Starting test: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = testThread.get();
        
        test.pass("Test Passed: " + testName);

        // Capture screenshot on pass
        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            String screenshotPath = ScreenshotUtilities.captureScreenshot(driver, testName + "_PASS");
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        }

        System.out.println("PASS: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        ExtentTest test = testThread.get();

        test.fail("Test Failed: " + testName);
        test.fail("Error: " + errorMessage);

        // Capture screenshot on failure
        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            String screenshotPath = ScreenshotUtilities.captureScreenshot(driver, testName + "_FAIL");
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        }

        System.out.println("FAIL: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";
        ExtentTest test = extent.createTest(testName, testDescriptions.getOrDefault(testName, testName));

        test.skip("Test Skipped: " + testName);
        test.skip("Reason: " + skipReason);

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
