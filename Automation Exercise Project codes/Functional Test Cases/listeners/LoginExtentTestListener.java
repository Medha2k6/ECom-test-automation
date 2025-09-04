package com.functional.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import com.functional.utilities.ExtentManager;
import com.functional.utilities.ScreenshotUtilities;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class LoginExtentTestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static final Map<String, String> testDescriptions = new HashMap<>();

    static {
        testDescriptions.put("TC_ECOM_Login_001_ValidCredentials", "Verify login with valid credentials");
        testDescriptions.put("TC_ECOM_Login_002_InvalidCredentials", "Verify login with invalid credentials");
        testDescriptions.put("TC_ECOM_Login_003_BlankEmailField", "Verify login with blank email field");
        testDescriptions.put("TC_ECOM_Login_004_BlankPasswordField", "Verify login with blank password field");
        testDescriptions.put("TC_ECOM_Login_005_BothFieldsBlank", "Verify login with both fields blank");
        testDescriptions.put("TC_ECOM_Login_006_PasswordFieldMasked", "Verify password field masks input");
        testDescriptions.put("TC_ECOM_Login_007_ValidEmailWrongPassword", "Verify login with valid email and wrong password");
        testDescriptions.put("TC_ECOM_Login_008_InvalidEmailRightPassword", "Verify login with invalid email and right password");
        testDescriptions.put("TC_ECOM_Login_009_ErrorMessageDisappears", "Verify error message disappears after correcting input");
        testDescriptions.put("TC_ECOM_Login_010_EmailFormatValidation", "Verify email field accepts only valid format");
        testDescriptions.put("TC_ECOM_Login_011_MaximumLengthValidation", "Verify maximum email and password length validation");
        testDescriptions.put("TC_ECOM_Login_012_MixedCaseEmail", "Verify email field accepts both lower and uppercase");
        testDescriptions.put("TC_ECOM_Login_013_RedirectToHomePage", "Verify login button redirects to home page after valid credentials");
        testDescriptions.put("TC_ECOM_Login_014_InvalidCredentialsError", "Verify login button functionality after invalid credentials");
        testDescriptions.put("TC_ECOM_Login_017_PasswordCaseSensitivity", "Verify case sensitivity of password field");
        testDescriptions.put("TC_ECOM_Login_018_BrowserBackAfterLogout", "Verify browser back button doesn't return to authenticated pages after logout");
        testDescriptions.put("TC_ECOM_Login_019_RedirectToLastVisitedPage", "Verify user is redirected to last visited page after login");
        testDescriptions.put("TC_ECOM_Login_020_SessionPersistence", "Verify account stays logged in after browser restart");
        testDescriptions.put("TC_ECOM_Login_021_SuccessfulLogout", "Verify successful logout operation");
        testDescriptions.put("TC_ECOM_Login_022_LogoutButtonNotVisibleWhenLoggedOut", "Verify logout button is not visible when logged out");
        testDescriptions.put("TC_ECOM_Login_023_LogoutButtonMultipleClicks", "Verify logout button responds to multiple rapid clicks");
        testDescriptions.put("TC_ECOM_Login_024_LoginPromptAfterLogout", "Verify user is prompted to login after logout");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Login test suite: " + context.getName());
        extent = ExtentManager.getInstance("login_func_extentreport.html");
        System.out.println("ExtentReports initialized for Login suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Login test suite: " + context.getName());
        if (extent != null) {
            extent.flush();
        }
        System.out.println("ExtentReports flushed for Login suite: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Login test: " + testName);

        ExtentTest extentTest = extent.createTest(testName, description)
                .assignCategory("Login Functionality Tests")
                .assignAuthor("QA Team");

        test.set(extentTest);
        test.get().info("Starting test: " + testName);

        System.out.println("Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        test.get().pass("Test Passed: " + testName);

        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnPass(driver, testName, "login");
            if (screenshotPath != null) {
                test.get().addScreenCaptureFromPath(screenshotPath);
            }
        }
        System.out.println("PASS: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";

        test.get().fail("Test Failed: " + testName);
        test.get().fail("Error: " + errorMessage);

        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testName, "login");
            if (screenshotPath != null) {
                test.get().addScreenCaptureFromPath(screenshotPath);
            }
        }
        System.out.println("FAIL: " + testName + " - " + errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";

        test.set(extent.createTest(testName, testDescriptions.getOrDefault(testName, testName)));
        test.get().skip("Test Skipped: " + testName);
        test.get().skip("Reason: " + skipReason);

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
