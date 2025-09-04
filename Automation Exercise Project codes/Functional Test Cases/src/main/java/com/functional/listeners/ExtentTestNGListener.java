package com.functional.listeners;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.functional.utilities.ExtentManager;

import org.testng.*;

import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentTestNGListener implements ITestListener {

    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private ExtentReports extent;
    public static WebDriver driver;          // set this from your @BeforeClass in tests

    // Called before any tests in the <test> are run.
    @Override
    public void onStart(ITestContext context) {
        String suiteName = context.getSuite().getName(); // e.g., "Cart Suite" or "Contact Suite"
        String safeSuiteName = sanitizeFileName(suiteName);
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport_" + safeSuiteName + ".html";
        extent = ExtentManager.createInstance(reportPath);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().fail(result.getThrowable());

        // attach screenshot if possible
        String path = captureScreenshot(result.getMethod().getMethodName());
        if (path != null) {
            testThread.get().addScreenCaptureFromPath(path);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().skip(result.getThrowable());
    }

    // Called after all tests in the <test> are run.
    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    // ---------- Helpers ----------

    private String captureScreenshot(String methodName) {
        if (driver == null) return null;

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotsDir = System.getProperty("user.dir") + "/test-output/screenshots/";
            Files.createDirectories(Paths.get(screenshotsDir));
            String fileName = sanitizeFileName(methodName) + "_" + time + ".png";
            Path dest = Paths.get(screenshotsDir + fileName);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (WebDriverException | IOException e) {
            ((Throwable) e).printStackTrace();
            return null;
        }
    }
    
    public static ExtentTest getTest() {
        return testThread.get();
    }

    private String sanitizeFileName(String s) {
        if (s == null) return "report";
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
