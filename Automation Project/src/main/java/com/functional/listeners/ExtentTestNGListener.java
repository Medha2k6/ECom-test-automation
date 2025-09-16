package com.functional.listeners;

import com.aventstack.extentreports.*;
import com.functional.utilities.ExtentManager;
import org.openqa.selenium.*;
import org.testng.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.lang.reflect.Field;
import com.product.Base.BaseTest;

/**
 * TestNG Listener for ExtentReports
 * - Logs test status
 * - Captures screenshot on failure
 */
public class ExtentTestNGListener implements ITestListener, ISuiteListener, ITestNGListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    // CRITICAL: Removed the static WebDriver variable from here
    // public static WebDriver driver; 

    @Override
    public void onStart(ISuite suite) {
        String reportName = "ContactUsReport.html";
        extent = ExtentManager.getInstance(reportName);
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().log(Status.PASS, "✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testThread.get();
        test.log(Status.FAIL, "❌ Test Failed: " + result.getThrowable());

        try {
            // Get the instance of the test class that failed
            Object testClassInstance = result.getInstance();

            // Use Java Reflection to get the 'driver' field from the test instance
            Field driverField = testClassInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            WebDriver driver = (WebDriver) driverField.get(testClassInstance);

            if (driver != null) {
                try {
                    // Take screenshot
                    TakesScreenshot ts = (TakesScreenshot) driver;
                    byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

                    // Attach as Base64 so it’s embedded in the report
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    test.addScreenCaptureFromBase64String(base64, "Screenshot on Failure");

                    // Also save to file (optional)
                    String fileName = "screenshot_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
                    File dest = new File(System.getProperty("user.dir") + "/reports/screenshots/" + fileName);
                    dest.getParentFile().mkdirs();
                    Files.write(dest.toPath(), screenshot);

                } catch (IOException e) {
                    test.log(Status.WARNING, "⚠️ Failed to save screenshot: " + e.getMessage());
                }
            } else {
                 test.log(Status.WARNING, "⚠️ WebDriver instance was not available. Likely already quit.");
            }
        } catch (Exception e) {
            // This is for cases where reflection fails
            test.log(Status.WARNING, "⚠️ Failed to get WebDriver instance via reflection: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().log(Status.SKIP, "⏭️ Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        testThread.get().log(Status.WARNING, "⚠️ Test failed but within success percentage");
    }

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}

    public static ExtentTest getTest() {
        return testThread.get();
    }
}
