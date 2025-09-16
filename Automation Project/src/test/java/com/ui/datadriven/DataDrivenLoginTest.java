package com.ui.datadriven;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;
import java.util.List;
import com.ui.base.*;
import com.ui.utilities.ExcelUtils;
import com.ui.utilities.ScreenshotUtilities;

public class DataDrivenLoginTest extends BaseTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        String reportPath = System.getProperty("user.dir") 
            + "/reports/datadriven_reports/LoginSignupExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void executeTestCases() {
        String excelPath = System.getProperty("user.dir") + "/src/test/resources/login.xlsx";  
        List<String[]> testData = ExcelUtils.getTestData(excelPath);

        // Start from 1 to skip header row
        for (int i = 1; i < testData.size(); i++) {
            String testType = testData.get(i)[0].trim();
            String username = testData.get(i)[1].trim();
            String password = testData.get(i)[2].trim();

            if ("Signup".equalsIgnoreCase(testType)) {
                signupTest(username, password, i);
            } else if ("Login".equalsIgnoreCase(testType)) {
                loginTest(username, password, i);
            } else {
                System.out.println("Unknown TestType at row " + i + ": " + testType);
            }
        }
    }

    private void signupTest(String username, String email, int testCaseNumber) {
        test = extent.createTest("Test Case " + testCaseNumber + " - Signup");
        try {
            test.log(Status.INFO, "Starting Signup Test with Name: " + username + ", Email: " + email);
            driver.get("https://automationexercise.com/signup");

            test.log(Status.INFO, "Navigated to Signup page.");
            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys(username);
            test.log(Status.INFO, "Entered username: " + username);

            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys(email);
            test.log(Status.INFO, "Entered email: " + email);

            // Add additional signup fields if required here

            driver.findElement(By.xpath("//button[@type='submit']")).click();
            test.log(Status.INFO, "Clicked on signup button.");

            // TODO: Add assertions to validate successful signup
            test.log(Status.PASS, "Signup test passed for user: " + username);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "signup_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Signup test failed for user: " + username + ". Error: " + e.getMessage());
            test.addScreenCaptureFromPath(screenshotPath, "Signup Failure Screenshot");
            System.err.println("Signup test failed. Screenshot saved at: " + screenshotPath);
        }
    }

    private void loginTest(String username, String password, int testCaseNumber) {
        test = extent.createTest("Test Case " + testCaseNumber + " - Login");
        try {
            test.log(Status.INFO, "Starting Login Test with Username: " + username + ", Password: " + password);
            driver.get("https://automationexercise.com/login");
            test.log(Status.INFO, "Navigated to Login page.");

            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys(username);
            test.log(Status.INFO, "Entered username: " + username);

            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys(password);
            test.log(Status.INFO, "Entered password.");

            driver.findElement(By.xpath("//button[@type='submit']")).click();
            test.log(Status.INFO, "Clicked on login button.");

            boolean loginSuccess = driver.getCurrentUrl().contains("account");

            if ("WrongPassword".equalsIgnoreCase(password)) {
                if (loginSuccess) {
                    throw new AssertionError("Expected login failure, but login succeeded.");
                } else {
                    test.log(Status.PASS, "Login test passed (as expected failure) for user: " + username);
                }
            } else {
                if (!loginSuccess) {
                    throw new AssertionError("Expected login success, but login failed.");
                } else {
                    test.log(Status.PASS, "Login test passed for user: " + username);
                }
            }
        } catch (AssertionError ae) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "login_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Assertion failed: " + ae.getMessage());
            test.addScreenCaptureFromPath(screenshotPath, "Login Failure Screenshot");
            System.err.println("Login assertion failed. Screenshot saved at: " + screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "login_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Login test failed for user: " + username + ". Error: " + e.getMessage());
            test.addScreenCaptureFromPath(screenshotPath, "Login Failure Screenshot");
            System.err.println("Login test failed. Screenshot saved at: " + screenshotPath);
        }
    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
    }
}