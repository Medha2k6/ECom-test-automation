package com.ui.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CartExtentReportManager {
    
    private static ExtentReports extent;
    private static ExtentTest test;
    private static final String REPORT_PATH = "reports/datadriven_reports/";
    
    public static void initializeExtentReport() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportName = "CartPage_DataDriven_TestReport_" + timestamp + ".html";
            
            File reportDir = new File(REPORT_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH + reportName);
            sparkReporter.config().setDocumentTitle("Cart Page Data-Driven Automation Report");
            sparkReporter.config().setReportName("Data-Driven Cart Page Testing");
            sparkReporter.config().setTheme(Theme.DARK);
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            extent.setSystemInfo("Application", "Automation Exercise");
            extent.setSystemInfo("Test Type", "Data-Driven UI Automation");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            
            System.out.println("ExtentReports initialized: " + REPORT_PATH + reportName);
        }
    }
    
    public static void createTest(String testName, String description) {
        if (extent != null) {
            test = extent.createTest(testName, description);
        }
    }
    
    public static void assignCategory(String category) {
        if (test != null) {
            test.assignCategory(category);
        }
    }
    
    public static void assignAuthor(String author) {
        if (test != null) {
            test.assignAuthor(author);
        }
    }
    
    public static void logStep(String stepDescription) {
        if (test != null) {
            test.info("🔹 <b>Step:</b> " + stepDescription);
            System.out.println("STEP: " + stepDescription);
        }
    }
    
    public static void logAction(String action) {
        if (test != null) {
            test.info("▶️ <b>Action:</b> " + action);
            System.out.println("ACTION: " + action);
        }
    }
    
    public static void logNavigation(String navigationDetails) {
        if (test != null) {
            test.info("🧭 <b>Navigation:</b> " + navigationDetails);
            System.out.println("NAVIGATION: " + navigationDetails);
        }
    }
    
    public static void logValidation(String validationDetails) {
        if (test != null) {
            test.info("✔️ <b>Validation:</b> " + validationDetails);
            System.out.println("VALIDATION: " + validationDetails);
        }
    }
    
    public static void logTestData(String testDataDetails) {
        if (test != null) {
            test.info("📊 <b>Test Data:</b> " + testDataDetails);
            System.out.println("TEST DATA: " + testDataDetails);
        }
    }
    
    public static void logExpectedResult(String expectedResult) {
        if (test != null) {
            test.info("🎯 <b>Expected Result:</b> " + expectedResult);
            System.out.println("EXPECTED: " + expectedResult);
        }
    }
    
    public static void logActualResult(String actualResult) {
        if (test != null) {
            test.info("📋 <b>Actual Result:</b> " + actualResult);
            System.out.println("ACTUAL: " + actualResult);
        }
    }
    
    public static void logInfo(String message) {
        if (test != null) {
            test.info("ℹ️ " + message);
            System.out.println("INFO: " + message);
        }
    }
    
    public static void logPass(String message) {
        if (test != null) {
            test.pass("✅ <b>" + message + "</b>");
            System.out.println("PASS: " + message);
        }
    }
    
    public static void logFail(String message) {
        if (test != null) {
            test.fail("❌ <b>" + message + "</b>");
            System.out.println("FAIL: " + message);
        }
    }
    
    public static void logSkip(String message) {
        if (test != null) {
            test.skip("⚠️ <b>" + message + "</b>");
            System.out.println("SKIP: " + message);
        }
    }
    
    public static void logWarning(String message) {
        if (test != null) {
            test.warning("⚠️ <b>" + message + "</b>");
            System.out.println("WARNING: " + message);
        }
    }
    
    public static void addScreenshot(String screenshotPath) {
        if (test != null && screenshotPath != null) {
            try {
                test.addScreenCaptureFromPath(screenshotPath);
                System.out.println("Screenshot added: " + screenshotPath);
            } catch (Exception e) {
                System.err.println("Failed to add screenshot: " + e.getMessage());
            }
        }
    }
    
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReports flushed successfully.");
        }
    }
}