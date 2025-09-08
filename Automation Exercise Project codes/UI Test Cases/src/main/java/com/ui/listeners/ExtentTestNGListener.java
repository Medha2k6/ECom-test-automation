package com.ui.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestNG Listener to integrate ExtentReports with Selenium.
 * Creates separate ExtentReports per test class/module.
 */
public class ExtentTestNGListener implements ITestListener {

    public static WebDriver driver;

    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        // ✅ Use test context name (usually class name) for unique report file
        String reportName = context.getName() + "-extent-report.html";

        ExtentSparkReporter reporter = new ExtentSparkReporter("reports" + File.separator + reportName);
        reporter.config().setDocumentTitle("UI Automation Report");
        reporter.config().setReportName("Automation Test Results - " + context.getName());

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        // Add system/environment info if needed
        extent.setSystemInfo("Test Suite", context.getName());
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        if (driver != null) {
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File src = ts.getScreenshotAs(OutputType.FILE);

                String screenshotDir = "screenshots" + File.separator + result.getTestClass().getName();
                Files.createDirectories(Paths.get(screenshotDir));

                String screenshotPath = screenshotDir + File.separator + result.getMethod().getMethodName() + ".png";
                Files.copy(src.toPath(), Paths.get(screenshotPath));

                test.get().addScreenCaptureFromPath(screenshotPath);
            } catch (IOException e) {
                test.get().warning("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}
