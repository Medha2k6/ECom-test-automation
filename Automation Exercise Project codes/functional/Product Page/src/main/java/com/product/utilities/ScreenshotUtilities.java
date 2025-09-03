package com.product.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtilities {
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Add timestamp to avoid overwriting
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Create folder path
            String dirPath = System.getProperty("user.dir") + File.separator + "screenshots";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Full file path
            String filePath = dirPath + File.separator + screenshotName + "_" + timestamp + ".png";

            // Capture and save
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File(filePath));

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
