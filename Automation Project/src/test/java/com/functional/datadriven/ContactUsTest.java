package com.functional.datadriven;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import com.functional.utilities.ExcelUtils;
import com.functional.utilities.ScreenshotUtilities;

import io.github.bonigarcia.wdm.WebDriverManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ContactUsTest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest mainTest;
    ExtentTest childTest;

    @BeforeSuite
    public void setupExtentReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ContactUsDataDrivenReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @BeforeClass
    public void setUp() {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @Test
    public void contactUsDataDrivenTest() throws Exception {
        mainTest = extent.createTest("Contact Us Data Driven Test Suite");
        
        String excelPath = System.getProperty("user.dir") + "/src/test/resources/contact.xlsx";
        ExcelUtils.setExcelFile(excelPath, "Sheet 1");

        int rowCount = ExcelUtils.getRowCount();

        for (int i = 1; i <= rowCount; i++) {
            String testType = ExcelUtils.getCellData(i, 0);
            String name = ExcelUtils.getCellData(i, 1);
            String email = ExcelUtils.getCellData(i, 2);
            String subject = ExcelUtils.getCellData(i, 3);
            String message = ExcelUtils.getCellData(i, 4);
            String file = ExcelUtils.getCellData(i, 5);
            String expectedStatus = ExcelUtils.getCellData(i, 6);
            String action = ExcelUtils.getCellData(i, 7);
            
            childTest = mainTest.createNode("TC_" + (i-1) + "_" + testType);
            
            try {
                childTest.log(Status.INFO, "Test Data - Name: " + name + ", Email: " + email);
                childTest.log(Status.INFO, "Expected Status: " + expectedStatus);

                childTest.log(Status.INFO, "Navigating to Contact Us page");
                driver.get("https://automationexercise.com/contact_us");
                
                boolean actualSuccess = false;
                boolean testPassed = false;

                if (testType.equalsIgnoreCase("ContactForm")) {
                    // Fill form with logging
                    if (!isBlank(name)) {
                        childTest.log(Status.INFO, "Entering name: " + name);
                        driver.findElement(By.name("name")).sendKeys(name);
                    }
                    if (!isBlank(email)) {
                        childTest.log(Status.INFO, "Entering email: " + email);
                        driver.findElement(By.name("email")).sendKeys(email);
                    }
                    if (!isBlank(subject)) {
                        childTest.log(Status.INFO, "Entering subject: " + subject);
                        driver.findElement(By.name("subject")).sendKeys(subject);
                    }
                    if (!isBlank(message)) {
                        childTest.log(Status.INFO, "Entering message: " + message);
                        driver.findElement(By.name("message")).sendKeys(message);
                    }
                    if (!isBlank(file)) {
                        childTest.log(Status.INFO, "Uploading file: " + file);
                        WebElement uploadFile = driver.findElement(By.name("upload_file"));
                        String absolutePath = new java.io.File(System.getProperty("user.dir"), file).getAbsolutePath();
                        uploadFile.sendKeys(absolutePath);
                    }

                    childTest.log(Status.INFO, "Clicking submit button");
                    driver.findElement(By.name("submit")).click();

                    // Check if form submitted successfully
                    try {
                        Alert alert = driver.switchTo().alert();
                        childTest.log(Status.INFO, "Alert handled");
                        alert.accept();
                        Thread.sleep(1000);
                        actualSuccess = driver.getPageSource().contains("Success") || 
                                       driver.getPageSource().contains("successfully");
                        childTest.log(Status.INFO, "Success message " + (actualSuccess ? "found" : "not found"));
                    } catch (Exception e) {
                        childTest.log(Status.INFO, "No alert - form validation failed");
                        actualSuccess = false;
                    }
                }

                else if (testType.equalsIgnoreCase("Subscription")) {
                    if (!isBlank(email)) {
                        String emailToUse = email;
                        if (email.contains("100+ chars")) {
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < 95; j++) {
                                sb.append("a");
                            }
                            emailToUse = sb.toString() + "@test.com";
                        }
                        childTest.log(Status.INFO, "Entering subscription email: " + emailToUse);
                        driver.findElement(By.id("susbscribe_email")).sendKeys(emailToUse);
                    }
                    childTest.log(Status.INFO, "Clicking subscribe button");
                    driver.findElement(By.id("subscribe")).click();
                    Thread.sleep(1000);
                    
                    actualSuccess = driver.getPageSource().contains("successfully") || 
                                   driver.getPageSource().contains("subscribed");
                    childTest.log(Status.INFO, "Subscription " + (actualSuccess ? "successful" : "failed"));
                }

                else if (testType.equalsIgnoreCase("Navigation")) {
                    String currentUrl = driver.getCurrentUrl();
                    if (action != null && action.contains("Home")) {
                        childTest.log(Status.INFO, "Clicking Home link");
                        driver.findElement(By.linkText("Home")).click();
                        actualSuccess = !driver.getCurrentUrl().equals(currentUrl);
                        childTest.log(Status.INFO, "Navigation " + (actualSuccess ? "successful" : "failed"));
                    } else {
                        actualSuccess = true;
                    }
                }
                
                // Determine test result
                if (expectedStatus.equalsIgnoreCase("Pass")) {
                    testPassed = actualSuccess;
                } else if (expectedStatus.equalsIgnoreCase("Fail")) {
                    testPassed = !actualSuccess;
                }
                
                // Log final result
                if (testPassed) {
                    childTest.log(Status.PASS, "TEST PASSED - Expected: " + expectedStatus + 
                                ", Actual: " + (actualSuccess ? "Success" : "Failed") + " ✓");
                    String screenshotPath = ScreenshotUtilities.captureScreenshotOnPass(driver, "TC_" + (i-1) + "_" + testType, "ContactUsTests");
                    if (screenshotPath != null) {
                         childTest.addScreenCaptureFromPath(screenshotPath, "Success Screenshot");
                    }
                } else {
                    childTest.log(Status.FAIL, "TEST FAILED - Expected: " + expectedStatus + 
                                ", Actual: " + (actualSuccess ? "Success" : "Failed") + " ✗");
                    String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "TC_" + (i-1) + "_" + testType, "ContactUsTests");
                    if (screenshotPath != null) {
                         childTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                    }
                }
                
            } catch (Exception e) {
                childTest.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
                String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "TC_" + (i-1) + "_" + testType, "ContactUsTests");
                if (screenshotPath != null) {
                    childTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                }
            }
        }
    }
    
    private boolean isBlank(String value) {
        return value == null || value.isEmpty() || value.equalsIgnoreCase("(blank)") || 
               value.equalsIgnoreCase("N/A") || value.trim().isEmpty();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}