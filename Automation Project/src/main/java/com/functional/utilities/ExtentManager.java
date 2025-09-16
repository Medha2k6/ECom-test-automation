package com.functional.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static final ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
    private static String reportFilePath;

    private ExtentManager() {
        // prevent instantiation
    }
    
    // Generates a unique report name with a timestamp
    public static String getReportPath(String reportName) {
        if (reportFilePath == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String projectPath = System.getProperty("user.dir");
            reportFilePath = projectPath + "/reports/" + reportName.replace(".html", "") + "_" + timestamp + ".html";
        }
        return reportFilePath;
    }

    public static ExtentReports getInstance(String reportName) {
        if (extent.get() == null) {
            String fullPath = getReportPath(reportName);
            ExtentSparkReporter spark = new ExtentSparkReporter(fullPath);
            ExtentReports ext = new ExtentReports();
            ext.attachReporter(spark);
            extent.set(ext);
        }
        return extent.get();
    }

    public static void flushReport() {
        if (extent.get() != null) {
            extent.get().flush();
            extent.remove();
            reportFilePath = null; // Reset for the next run
        }
    }
}