package com.ui.utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {
    
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    static {
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }
    
    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        return captureScreenshot(driver, testName, "FAILED");
    }
    
    public static String captureScreenshotOnPass(WebDriver driver, String testName) {
        return captureScreenshot(driver, testName, "PASSED");
    }
    
    public static String captureScreenshot(WebDriver driver, String testName, String status) {
        try {
            if (driver == null) {
                System.err.println("WebDriver is null. Cannot capture screenshot.");
                return null;
            }
            
            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String fileName = String.format("%s_%s_%s.png", testName, status, timestamp);
            String fullPath = SCREENSHOT_DIR + fileName;
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(fullPath);
            
            FileUtils.copyFile(sourceFile, destinationFile);
            
            System.out.println("Screenshot captured: " + fullPath);
            return fullPath;
            
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error during screenshot capture: " + e.getMessage());
            return null;
        }
    }
}