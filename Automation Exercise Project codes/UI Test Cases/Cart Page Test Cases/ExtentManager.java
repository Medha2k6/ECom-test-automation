package cgi_ae_test_cases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Automation Cart Test Report");
            spark.config().setReportName("Cart Test Suite Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Add system/environment info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Tester", "Team A5 - CGI");
        }
        return extent;
    }
}
