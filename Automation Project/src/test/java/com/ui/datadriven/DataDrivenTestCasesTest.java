package com.ui.datadriven;

import org.apache.poi.ss.usermodel.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ui.base.BaseTest;
import com.ui.utilities.ScreenshotUtilities; // Import the ScreenshotUtilities class


import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class DataDrivenTestCasesTest extends BaseTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        String reportPath = System.getProperty("user.dir") 
            + "/reports/datadriven_reports/DataDrivenTestCasesExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @DataProvider(name = "testDataFromExcel")
    public Object[][] getTestData() {
        String excelPath = System.getProperty("user.dir") + "/src/test/resources/test case data.xlsx";  
        List<String[]> data = new ArrayList<>();
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(new File(excelPath));
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                if (row == null) {
                    continue;
                }

                String[] rowData = new String[3];
                for (int i = 0; i < 3; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    DataFormatter formatter = new DataFormatter();
                    rowData[i] = formatter.formatCellValue(cell).trim();
                }
                
                if (!rowData[0].isEmpty() || !rowData[1].isEmpty() || !rowData[2].isEmpty()) {
                    data.add(rowData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, possibly by returning an empty array or logging
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Object[][] testData = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            testData[i] = data.get(i);
        }
        return testData;
    }

    @Test(dataProvider = "testDataFromExcel")
    public void emailSubscriptionTest(String testCaseName, String email, String expectedOutcome) {
        test = extent.createTest("Test Case - " + testCaseName);
        try {
            test.log(Status.INFO, "Navigating to URL: https://automationexercise.com/test_cases");
            driver.get("https://automationexercise.com/test_cases");
            
            WebElement emailField = driver.findElement(By.id("susbscribe_email"));
            WebElement subscribeButton = driver.findElement(By.id("subscribe"));
            
            test.log(Status.INFO, "Attempting subscription with email: '" + email + "'");
            
            if (email.equalsIgnoreCase("BLANK")) {
                emailField.clear();
                test.log(Status.INFO, "Cleared email field for 'BLANK' test case.");
            } else {
                emailField.sendKeys(email);
                test.log(Status.INFO, "Entered email: '" + email + "' into the field.");
            }
            
            subscribeButton.click();
            test.log(Status.INFO, "Clicked the subscribe button.");
            Thread.sleep(2000); 

            String pageSource = driver.getPageSource().toLowerCase();
            String validationMessage = emailField.getAttribute("validationMessage");
            
            boolean isSubscribedSuccessfully = pageSource.contains("subscribed successfully");
            boolean hasValidationMessage = validationMessage != null && !validationMessage.isEmpty();
            
            test.log(Status.INFO, "Page source contains 'subscribed successfully': " + isSubscribedSuccessfully);
            test.log(Status.INFO, "Validation message found: " + hasValidationMessage + ", Message: " + validationMessage);

            if ("Invalid".equalsIgnoreCase(expectedOutcome)) {
                
            	Assert.assertFalse(isSubscribedSuccessfully, "BUG: Invalid email was accepted, but it should have failed.");
                Assert.assertTrue(hasValidationMessage, "Expected a validation message for invalid email, but none was found.");
                test.log(Status.PASS, "Assertion Passed: The invalid email was correctly rejected with a validation message.");
                
            } else if ("Valid".equalsIgnoreCase(expectedOutcome)) {
            	Assert.assertTrue(isSubscribedSuccessfully, "Expected successful subscription, but the success message was not found.");
                Assert.assertFalse(hasValidationMessage, "BUG: A validation message was displayed for a valid email.");
                test.log(Status.PASS, "Assertion Passed: The valid email was successfully subscribed.");
            } else if ("Blank".equalsIgnoreCase(expectedOutcome)) {
                // Assert that a validation message is present.
                Assert.assertTrue(hasValidationMessage, "Expected a validation message for blank email, but none was found.");
                test.log(Status.PASS, "Assertion Passed: A validation message was correctly displayed for the blank email.");
            }
            
            test.pass(testCaseName + " passed as expected.");
        } catch (AssertionError ae) {
            test.log(Status.FAIL, "Assertion Failed: " + ae.getMessage());
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseName);
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
            }
            Assert.fail(ae.getMessage());
        } catch (Exception e) {
            test.log(Status.FAIL, "Test Failed due to an unexpected exception: " + e.getMessage());
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseName);
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath, "Screenshot on Exception");
            }
            test.log(Status.FAIL, e);
            Assert.fail(e.getMessage());
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