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

public class TestCasesPageDetailedExtentTestListener implements ITestListener {

    private static final Map<String, String> testDescriptions = new HashMap<>();
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    static {
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
        System.out.println("Starting Test Suite: " + context.getName());
        extent = ExtentManager.getInstance("testcasespage_func_extentreport.html");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Test Suite: " + context.getName());
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Detailed Test: " + testName);

        ExtentTest extentTest = extent.createTest(testName, description)
                                      .assignCategory("Test Cases Page Detailed Tests")
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
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnPass(driver, testName, "testcasespage");
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
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testName, "testcasespage");
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
            System.err.println("Could not get driver instance: " + e.getMessage());
            return null;
        }
    }
}
