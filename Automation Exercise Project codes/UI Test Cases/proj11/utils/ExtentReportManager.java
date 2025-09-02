package proj11.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    public static synchronized void initializeExtentReport() {
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport_" + timestamp + ".html";
            
            // Create test-output directory if it doesn't exist
            File testOutputDir = new File(System.getProperty("user.dir") + "/test-output");
            if (!testOutputDir.exists()) {
                testOutputDir.mkdirs();
            }
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            // Configure the reporter
            sparkReporter.config().setDocumentTitle("Automation Exercise Home Page Test Report");
            sparkReporter.config().setReportName("Home Page Functionality Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");
            
            // Initialize ExtentReports
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Set system information
            extentReports.setSystemInfo("Application", "Automation Exercise Website");
            extentReports.setSystemInfo("Application URL", "https://automationexercise.com/");
            extentReports.setSystemInfo("Test Environment", "QA");
            extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Browser", "Multi-browser (Chrome, Edge, Firefox)");
            
            System.out.println("ExtentReports initialized successfully: " + reportPath);
        }
    }
    
    public static synchronized void createTest(String testName, String description) {
        if (extentReports == null) {
            initializeExtentReport();
        }
        ExtentTest test = extentReports.createTest(testName, description);
        extentTest.set(test);
        System.out.println("Created test: " + testName);
    }
    
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.PASS, message);
        } else {
            System.out.println("PASS: " + message);
        }
    }
    
    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.FAIL, message);
        } else {
            System.out.println("FAIL: " + message);
        }
    }
    
    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        } else {
            System.out.println("INFO: " + message);
        }
    }
    
    public static void logWarning(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.WARNING, message);
        } else {
            System.out.println("WARNING: " + message);
        }
    }
    
    public static void logSkip(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.SKIP, message);
        } else {
            System.out.println("SKIP: " + message);
        }
    }
    
    public static void addScreenshot(String screenshotPath) {
        try {
            ExtentTest test = getTest();
            if (test != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            System.out.println("Could not attach screenshot: " + e.getMessage());
        }
    }
    
    public static void assignCategory(String... categories) {
        ExtentTest test = getTest();
        if (test != null) {
            test.assignCategory(categories);
        }
    }
    
    public static void assignAuthor(String... authors) {
        ExtentTest test = getTest();
        if (test != null) {
            test.assignAuthor(authors);
        }
    }
    
    public static void markTestResult(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    test.log(Status.PASS, "Test Passed");
                    break;
                case ITestResult.FAILURE:
                    test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
                    break;
                case ITestResult.SKIP:
                    test.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
                    break;
                default:
                    break;
            }
        }
    }
    
    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("ExtentReports flushed successfully");
        }
    }
    
    public static void removeTest() {
        extentTest.remove();
    }
}