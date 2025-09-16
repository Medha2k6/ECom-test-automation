package com.functional.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtilities {

    private static String capture(WebDriver driver, String screenshotName, String folderName) {
        try {
            // Add timestamp to avoid overwriting
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Save screenshots inside test-output/screenshots/<folderName>
            String baseDir = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "screenshots";
            String dirPath = baseDir + File.separator + folderName;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Full file path
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = dirPath + File.separator + fileName;

            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File(filePath));

            // Return relative path (important for ExtentReports)
            return "./screenshots/" + folderName.replace(File.separator, "/") + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generic method
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        return capture(driver, screenshotName, "generic");
    }

    // Capture screenshot on PASS
    public static String captureScreenshotOnPass(WebDriver driver, String screenshotName, String folderName) {
        return capture(driver, screenshotName + "_PASS", folderName + File.separator + "pass");
    }

    // Capture screenshot on FAIL
    public static String captureScreenshotOnFailure(WebDriver driver, String screenshotName, String folderName) {
        return capture(driver, screenshotName + "_FAIL", folderName + File.separator + "fail");
    }
}
