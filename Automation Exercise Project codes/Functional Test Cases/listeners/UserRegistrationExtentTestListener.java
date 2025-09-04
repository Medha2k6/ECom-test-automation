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

public class UserRegistrationExtentTestListener implements ITestListener {
    
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
        ExtentReportManager.initializeExtentReport();
        System.out.println("ExtentReports initialized for User Registration suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing User Registration test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for User Registration suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "User Registration test: " + testName);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("User Registration Tests");
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
            
            // Capture screenshot on pass with custom folder
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnPass(driver, testName, "userregistration");
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
            
            // Capture screenshot on failure with custom folder
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnFailure(driver, testName, "userregistration");
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
            String description = testDescriptions.getOrDefault(testName, "User Registration test: " + testName);
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