package com.ui.utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtilities {

    /**
     * Captures a screenshot and saves it to a specified folder.
     * @param driver The WebDriver instance.
     * @param testName The name of the test, used for the filename.
     * @param folderName The subfolder name to save the screenshot in.
     * @return The absolute file path of the saved screenshot.
     */
    private static String capture(WebDriver driver, String testName, String folderName) {
        try {
            // Create screenshots directory with subfolder
            String screenshotDir = System.getProperty("user.dir")
                    + File.separator + "test-output"
                    + File.separator + "screenshots"
                    + File.separator + folderName;
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = dir.getAbsolutePath() + File.separator + fileName;

            // Capture screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);

            FileUtils.copyFile(sourceFile, destFile);

            System.out.println("Screenshot captured: " + filePath);
            // Return relative path for reporting tools
            return "./screenshots/" + folderName + "/" + fileName;

        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Overloaded method with a default folder "general".
     * @param driver The WebDriver instance.
     * @param testName The name of the test, used for the filename.
     * @return The relative file path of the saved screenshot.
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        return capture(driver, testName, "general");
    }

    /**
     * Capture screenshot on PASS with a custom folder.
     * @param driver The WebDriver instance.
     * @param testName The name of the test.
     * @param folderName The custom subfolder name.
     * @return The relative file path.
     */
    public static String captureScreenshotOnPass(WebDriver driver, String testName, String folderName) {
        return capture(driver, testName + "_PASSED", folderName + File.separator + "pass");
    }
    
    /**
     * Capture screenshot on PASS with a default folder.
     * @param driver The WebDriver instance.
     * @param testName The name of the test.
     * @return The relative file path.
     */
    public static String captureScreenshotOnPass(WebDriver driver, String testName) {
        return captureScreenshotOnPass(driver, testName, "general");
    }

    /**
     * Capture screenshot on FAIL with a custom folder.
     * @param driver The WebDriver instance.
     * @param testName The name of the test.
     * @param folderName The custom subfolder name.
     * @return The relative file path.
     */
    public static String captureScreenshotOnFailure(WebDriver driver, String testName, String folderName) {
        return capture(driver, testName + "_FAILED", folderName + File.separator + "fail");
    }

    /**
     * Capture screenshot on FAIL with a default folder.
     * @param driver The WebDriver instance.
     * @param testName The name of the test.
     * @return The relative file path.
     */
    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        return captureScreenshotOnFailure(driver, testName, "general");
    }
}