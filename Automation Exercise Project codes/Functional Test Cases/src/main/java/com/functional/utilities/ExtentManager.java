package com.functional.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class ExtentManager {

    private static ThreadLocal<ExtentReports> extent = new ThreadLocal<>();

    private ExtentManager() {
        // prevent instantiation
    }

   
    public static ExtentReports getInstance(String reportName) {
        if (extent.get() == null) {
            String projectPath = System.getProperty("user.dir");
            String reportPath = projectPath + "/reports/" + reportName;

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            ExtentReports ext = new ExtentReports();
            ext.attachReporter(spark);

            extent.set(ext);
        }
        return extent.get();
    }

   
    public static ExtentReports createInstance(String fullPath) {
        ExtentSparkReporter spark = new ExtentSparkReporter(fullPath);
        ExtentReports ext = new ExtentReports();
        ext.attachReporter(spark);
        return ext;
    }

    /**
     * Flush and remove the current ThreadLocal ExtentReports instance.
     */
    public static void flushReport() {
        if (extent.get() != null) {
            extent.get().flush();
            extent.remove();
        }
    }
}
