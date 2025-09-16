package com.ui.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import com.ui.utilities.ExtentReportManager;
import com.ui.utilities.ScreenshotUtilities;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SignupLoginExtentTestListener implements ITestListener {
    
    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for Signup/Login page
        testDescriptions.put("TC_Signup_Login_01_NewUserSignupValidEmail", "To verify and validate the functionality of new user signup with valid email id");
        testDescriptions.put("TC_Signup_Login_02_NewUserSignupInvalidEmailFormat", "To verify and validate the functionality of new user signup with invalid email id(format)");
        testDescriptions.put("TC_Signup_Login_03_ExistingUserSignupValidEmail", "To verify and validate the functionality of existing user signup with valid email id");
        testDescriptions.put("TC_Signup_Login_04_SignupBlankEmailField", "To verify and validate signup attempt with blank email field");
        testDescriptions.put("TC_Signup_Login_05_SignupBlankNameField", "To verify and validate signup attempt with blank name field");
        testDescriptions.put("TC_Signup_Login_06_SignupBlankNameAndEmailFields", "To verify and validate signup attempt with blank name field and blank email id field");
        testDescriptions.put("TC_Signup_Login_07_SignupButtonFunctionality", "To verify and validate the functionality of sign up button");
        testDescriptions.put("TC_Signup_Login_08_LoginExistingAccountDetails", "To verify and validate the functionality of login with existing account details");
        testDescriptions.put("TC_Signup_Login_09_LoginNewAccountDetails", "To verify and validate the functionality of login function with new account details");
        testDescriptions.put("TC_Signup_Login_10_LoginWrongPassword", "To verify and validate the functionality of login with existing account details but wrong password");
        testDescriptions.put("TC_Signup_Login_11_LoginBlankPasswordField", "To verify and validate login attempt with blank password field");
        testDescriptions.put("TC_Signup_Login_12_LoginBlankEmailField", "To verify and validate login attempt with blank email field");
        testDescriptions.put("TC_Signup_Login_13_LoginBlankEmailAndPasswordFields", "To verify and validate login attempt with blank email field and blank password field");
        testDescriptions.put("TC_Signup_Login_14_LoginButtonFunctionality", "To verify and validate the functionality of login button");
        testDescriptions.put("TC_Signup_Login_15_HomeIconFunctionality", "Verify and validate the functionality of Home icon on the home page");
        testDescriptions.put("TC_Signup_Login_16_ProductsIconFunctionality", "Verify and validate the functionality of Products icon on the home page");
        testDescriptions.put("TC_Signup_Login_17_CartIconFunctionality", "Verify and validate the functionality of Cart icon on the home page");
        testDescriptions.put("TC_Signup_Login_18_TestCasesIconFunctionality", "Verify and validate the functionality of Test Cases icon on the home page");
        testDescriptions.put("TC_Signup_Login_19_APITestingIconFunctionality", "Verify and validate the functionality of API Testing icon on the home page");
        testDescriptions.put("TC_Signup_Login_20_VideoTutorialsIconFunctionality", "Verify and validate the functionality of Video Tutorials icon on the home page");
        testDescriptions.put("TC_Signup_Login_21_ContactUsIconFunctionality", "Verify and validate the functionality of Contact Us icon on the home page");
        testDescriptions.put("TC_Signup_Login_22_EmailSubscriptionValidEmail", "To verify and validate the email subscription functionality with valid email id");
        testDescriptions.put("TC_Signup_Login_23_EmailSubscriptionInvalidEmail", "To verify and validate that the email subscription field accepts invalid email addresses");
        testDescriptions.put("TC_Signup_Login_24_EmailSubscriptionBlankField", "To verify and validate that the email subscription field doesnt accept blank field");
        testDescriptions.put("TC_Signup_Login_25_PasswordFieldMasked", "To verify and validate that the password field is masked");
        testDescriptions.put("TC_Signup_Login_26_GoToTopButtonFunctionality", "To verify and validate the functionality of go to the top of the page button shown as '^' at the right bottom");
        testDescriptions.put("TC_Signup_Login_27_AutomationExerciseLogoFunctionality", "Verify and validate the functionality of the automation exercise on the website leads to homepage");
        testDescriptions.put("TC_Signup_Login_28_PageScrollBarFunctionality", "To verify and validate the appearance and function of the page scroll bar");
        testDescriptions.put("TC_Signup_Login_29_LoginSignupPageLoads", "To verify and validate that the Login/SignUp page loads successfully");
        testDescriptions.put("TC_Signup_Login_30_LoginAccountSectionPresence", "To verify and validate the presence of 'Login to your account' section");
        testDescriptions.put("TC_Signup_Login_31_NewUserSignupSectionPresence", "To verify and validate the presence of 'New User Signup!' section");
        testDescriptions.put("TC_Signup_Login_32_ORSeparatorPresence", "To verify and validate the presence of 'OR' separator/logo between login and signup");
        testDescriptions.put("TC_Signup_Login_33_LoginInvalidPasswordLength", "To verify and validate the Login functionality with valid email id and invalid password length");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Signup/Login test suite: " + context.getName());
        ExtentReportManager.initializeExtentReport("signupTestSuite");
        System.out.println("ExtentReports initialized for Signup/Login suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Signup/Login test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for Signup/Login suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Signup/Login page test: " + testName);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("Signup/Login Page Tests");
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
                String screenshotPath = ScreenshotUtilities.captureScreenshotOnPass(driver, testName);
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
                String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testName);
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
            String description = testDescriptions.getOrDefault(testName, "Signup/Login page test: " + testName);
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