package com.functional.datadriven;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.functional.utilities.ExcelUtilities;
import com.functional.utilities.ScreenshotUtilities; // Added this import
import com.product.Base.BaseTest;

import java.util.List;

public class DataDrivenLoginTest extends BaseTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("LoginTestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void executeLoginTests() {
    	String excelPath = System.getProperty("user.dir") + "/src/test/resources/login_data.xlsx";
        List<String[]> testData = ExcelUtilities.getData(excelPath);

        for (int i = 0; i < testData.size(); i++) {
            String username = testData.get(i)[0];
            String password = testData.get(i)[1];
            test = extent.createTest("Login Test - Case " + (i + 1));
            try {
                test.info("Username: '" + username + "', Password: '" + password + "'");

                driver.get("https://automationexercise.com/login");
                driver.findElement(By.name("email")).clear();
                driver.findElement(By.name("email")).sendKeys(username);
                driver.findElement(By.name("password")).clear();
                driver.findElement(By.name("password")).sendKeys(password);
                driver.findElement(By.xpath("//button[@type='submit']")).click();

                Thread.sleep(2000); // Wait for page update
                
                String currentUrl = driver.getCurrentUrl();
                test.info("Current URL after login: " + currentUrl);

                boolean logoutPresent = false;
                try {
                    logoutPresent = driver.findElements(By.linkText("Logout")).size() > 0;
                } catch (NoSuchElementException ignored) {}

                String bodyText = driver.findElement(By.tagName("body")).getText();
                test.info("Page BODY text (first 500 chars): " +
                        bodyText.substring(0, Math.min(500, bodyText.length())).replace("\n", " "));

                // Modern "success" checking: URL or Logout link OR check for a greeting or unique element
                boolean loginSuccess = currentUrl.contains("account") || logoutPresent;

                // Determine expected result
                boolean shouldSucceed = !( "WrongPassword".equalsIgnoreCase(password)
                        || username.isEmpty() || password.isEmpty()
                        || username.equalsIgnoreCase("invaliduser@example.com")
                        || username.equalsIgnoreCase("injection@example.com") );

                if (shouldSucceed) {
                    if (!loginSuccess)
                        throw new AssertionError("Expected login success, but it failed.");
                    test.pass("Login succeeded as expected.");
                    // log out if we logged in
                    try {
                        driver.findElement(By.linkText("Logout")).click();
                        Thread.sleep(1000);
                        test.info("Logged out after success.");
                    } catch (Exception e) {
                        test.warning("Could not log out after login: " + e.getMessage());
                    }
                } else {
                    if (loginSuccess)
                        throw new AssertionError("Expected login failure, but login succeeded.");
                    test.pass("Login failed as expected for invalid credentials.");
                }
            } catch (AssertionError ae) {
                test.fail("Assertion failed: " + ae.getMessage());
                String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "Login_Test_Case_" + (i + 1), "login_failures");
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failed Screenshot");
                }
            } catch (Exception e) {
                test.fail("Test failed due to exception: " + e.getMessage());
                String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "Login_Test_Case_" + (i + 1), "login_failures");
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failed Screenshot");
                }
            }
        }
    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) driver.quit();
        extent.flush();
    }
}