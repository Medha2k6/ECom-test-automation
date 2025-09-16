package com.ui.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static WebDriver storedDriver;

    private static String REPORT_FOLDER;
    private static String REPORT_FILE;
    private static String SCREENSHOT_FOLDER;

    public static synchronized void initializeExtentReport(String suiteName) {
        if (extentReports == null) {
            try {
                String projectDir = System.getProperty("user.dir");
                REPORT_FOLDER = projectDir + File.separator + "reports" + File.separator + "testsuite_reports";
                SCREENSHOT_FOLDER = REPORT_FOLDER + File.separator + "screenshots";
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                REPORT_FILE = REPORT_FOLDER + File.separator + suiteName + "_" + timestamp + "_ExtentReport.html";
                new File(REPORT_FOLDER).mkdirs();
                new File(SCREENSHOT_FOLDER).mkdirs();

                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_FILE);
                sparkReporter.config().setDocumentTitle("Automation Exercise Test Report");
                sparkReporter.config().setReportName(suiteName + " Test Results");
                sparkReporter.config().setTheme(Theme.STANDARD);
                sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");

                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);

                extentReports.setSystemInfo("Application", "Automation Exercise Website");
                extentReports.setSystemInfo("Application URL", "https://automationexercise.com/");
                extentReports.setSystemInfo("Test Environment", "QA");
                extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("OS", System.getProperty("os.name"));
                extentReports.setSystemInfo("Browser", "Multi-browser (Chrome, Edge, Firefox)");

                System.out.println("ExtentReports initialized successfully: " + REPORT_FILE);
            } catch (Exception e) {
                System.err.println("Failed to initialize ExtentReports: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static synchronized void createTest(String testName, String description) {
        if (extentReports == null) return;
        ExtentTest test = extentReports.createTest(testName, description);
        extentTest.set(test);
        System.out.println("Created test: " + testName);
    }

    public static ExtentTest getTest() { return extentTest.get(); }

    public static void setDriver(WebDriver driver) { storedDriver = driver; }

    public static void logPass(String message) { log(Status.PASS, message); }
    public static void logFail(String message) { log(Status.FAIL, message); }
    public static void logInfo(String message) { log(Status.INFO, message); }
    public static void logWarning(String message) { log(Status.WARNING, message); }
    public static void logSkip(String message) { log(Status.SKIP, message); }

    private static void log(Status status, String message) {
        ExtentTest test = getTest();
        if (test != null) test.log(status, message);
        else System.out.println(status + ": " + message);
    }

    public static void addScreenshot(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = SCREENSHOT_FOLDER + File.separator + screenshotName + ".png";
            FileUtils.copyFile(source, new File(dest));
            ExtentTest test = getTest();
            if (test != null) {
                test.log(Status.INFO, "Screenshot: " + screenshotName,
                        MediaEntityBuilder.createScreenCaptureFromPath(dest).build());
            }
        } catch (Exception e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
        }
    }

    public static void addScreenshot(String screenshotName) {
        if (storedDriver != null) addScreenshot(storedDriver, screenshotName);
        else System.err.println("Driver not set. Cannot take screenshot: " + screenshotName);
    }

    public static void assignCategory(String... categories) {
        ExtentTest test = getTest();
        if (test != null) test.assignCategory(categories);
    }

    public static void assignAuthor(String... authors) {
        ExtentTest test = getTest();
        if (test != null) test.assignAuthor(authors);
    }

    public static void markTestResult(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS -> test.log(Status.PASS, "Test Passed");
                case ITestResult.FAILURE -> test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
                case ITestResult.SKIP -> test.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
            }
        }
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("ExtentReports flushed successfully");
        }
    }

    public static void cleanup() { extentTest.remove(); }

    public static void logTestData(String testData) {
        ExtentTest test = getTest();
        if (test != null) test.log(Status.INFO, "Test Data: " + testData);
    }

    public static void logEnvironmentInfo(WebDriver driver) {
        try {
            if (driver != null) {
                org.openqa.selenium.remote.RemoteWebDriver remoteDriver = (org.openqa.selenium.remote.RemoteWebDriver) driver;
                org.openqa.selenium.Capabilities caps = remoteDriver.getCapabilities();
                ExtentTest test = getTest();
                if (test != null) {
                    test.log(Status.INFO, "Browser: " + caps.getBrowserName());
                    test.log(Status.INFO, "Browser Version: " + caps.getBrowserVersion());
                    test.log(Status.INFO, "Platform: " + caps.getPlatformName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error logging environment info: " + e.getMessage());
        }
    }

    public static void addTestSummary(String summary) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, "=== TEST SUMMARY ===");
            test.log(Status.INFO, summary);
        }
    }
}
