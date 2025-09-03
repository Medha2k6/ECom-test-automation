package functional.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    
    public static String captureScreenshot(WebDriver driver, String testName) {
        return captureScreenshot(driver, testName, "general");
    }
    
    public static String captureScreenshot(WebDriver driver, String testName, String folderName) {
        try {
            // Create screenshots directory with subfolder - fix the path issue
            String screenshotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + folderName;
            File screenshotDir = new File(screenshotPath);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = screenshotDir.getAbsolutePath() + "/" + fileName;
            
            // Capture screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            FileUtils.copyFile(sourceFile, destFile);
            
            System.out.println("Screenshot captured: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        return captureScreenshotOnFailure(driver, testName, "general");
    }
    
    public static String captureScreenshotOnFailure(WebDriver driver, String testName, String folderName) {
        String screenshotPath = captureScreenshot(driver, testName + "_FAILED", folderName);
        if (screenshotPath != null) {
            functional.utils.ExtentReportManager.logFail("Screenshot captured for failed test");
        }
        return screenshotPath;
    }
    
    public static String captureScreenshotOnPass(WebDriver driver, String testName) {
        return captureScreenshotOnPass(driver, testName, "general");
    }
    
    public static String captureScreenshotOnPass(WebDriver driver, String testName, String folderName) {
        String screenshotPath = captureScreenshot(driver, testName + "_PASSED", folderName);
        if (screenshotPath != null) {
            functional.utils.ExtentReportManager.logPass("Screenshot captured for passed test");
        }
        return screenshotPath;
    }
}