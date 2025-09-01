package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ThreadLocal<ExtentReports> extent = new ThreadLocal<>();

    private ExtentManager() {}

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
}
