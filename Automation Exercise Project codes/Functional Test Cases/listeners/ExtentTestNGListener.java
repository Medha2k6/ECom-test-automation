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

/**
 * TestNG Listener for ExtentReports
 * - Logs test status
 * - Captures screenshot on failure
 */
public class ExtentTestNGListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    public static WebDriver driver; // Set by your test classes

    @Override
    public void onStart(ISuite suite) {
        String reportName = "ContactUsReport" + ".html";
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

        if (driver != null) {
            try {
                // Take screenshot
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

                // Attach as Base64 so it’s embedded in the report
                String base64 = Base64.getEncoder().encodeToString(screenshot);
                test.addScreenCaptureFromBase64String(base64, "Screenshot on Failure");

                // Also save to file (optional)
                String fileName = "screenshot_" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
                File dest = new File(System.getProperty("user.dir") + "/reports/screenshots/" + fileName);
                dest.getParentFile().mkdirs();
                Files.write(dest.toPath(), screenshot);

            } catch (IOException e) {
                test.log(Status.WARNING, "⚠️ Failed to save screenshot: " + e.getMessage());
            }
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
    public void onStart(ITestContext context) {
        // Nothing needed here
    }

    @Override
    public void onFinish(ITestContext context) {
        // Nothing needed here
    }
}
