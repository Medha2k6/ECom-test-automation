package com.functional.datadriven;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.functional.pages.ProductPage;
import com.functional.utilities.ScreenshotUtilities;
import com.product.Base.BaseTest;

public class ProductDataDrivenTest extends BaseTest {

    @Test(dataProvider = "reviewData")
    public void productReviewTest(String testCaseId, String name, String email, String review, String expectedStatus) {
        SoftAssert softAssert = new SoftAssert();
        test = extent.createTest(testCaseId + " - Product Review Submission");
        test.info("Starting test case: " + testCaseId);
        driver.get("https://automationexercise.com/products");
        test.info("Navigated to products page.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            test.info("Navigating to the product details page to access the review form.");
            wait.until(ExpectedConditions.elementToBeClickable(ProductPage.VIEW_PRODUCT_BUTTON)).click();
            test.info("Clicked 'View Product' button.");
            wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.REVIEW_NAME_FIELD));
            test.info("Review form is visible.");

            test.info("Attempting to submit review with Name: '" + name + "', Email: '" + email + "', Review: '" + review + "'.");

            driver.findElement(ProductPage.REVIEW_NAME_FIELD).sendKeys(name);
            test.info("Entered name: " + name);
            driver.findElement(ProductPage.REVIEW_EMAIL_FIELD).sendKeys(email);
            test.info("Entered email: " + email);
            driver.findElement(ProductPage.REVIEW_TEXT_AREA).sendKeys(review);
            test.info("Entered review text.");
            driver.findElement(ProductPage.SUBMIT_REVIEW_BUTTON).click();
            test.info("Clicked 'Submit' button.");

            if (expectedStatus.equalsIgnoreCase("Pass")) {
                test.info("Test case expected to Pass.");
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Thank you for your review.')]")));
                softAssert.assertTrue(successMessage.isDisplayed(), "Success message not displayed for valid data.");
                test.pass(testCaseId + " passed successfully. Review submitted.");
            } else { // Expected to Fail
                test.info("Test case expected to Fail.");
                try {
                    WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Thank you for your review.')]")));
                    softAssert.fail("Test case was expected to fail, but a success message was found.");
                    test.fail(testCaseId + " failed. Invalid data was accepted.");
                    String path = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "product_review_failures");
                    test.fail("Failed: Invalid data was accepted.", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                } catch (Exception e) {
                    softAssert.assertTrue(true, "Expected failure occurred.");
                    test.pass("Test case correctly failed. Success message was not displayed for invalid data.");
                }
            }
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "product_review_failures");
            test.fail(testCaseId + " failed due to an exception: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail(testCaseId + " failed: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(dataProvider = "subscriptionData")
    public void emailSubscriptionTest(String testCaseId, String email, String expectedStatus) {
        SoftAssert softAssert = new SoftAssert();
        test = extent.createTest(testCaseId + " - Email Subscription Test");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        test.info("Starting test case: " + testCaseId);
        driver.get("https://automationexercise.com/products");
        test.info("Navigated to products page.");

        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.SUBSCRIBE_EMAIL_FIELD));
            test.info("Entering email: '" + email + "'");
            emailField.clear();
            emailField.sendKeys(email);
            driver.findElement(ProductPage.SUBSCRIBE_BUTTON).click();
            test.info("Clicked 'Subscribe' button.");

            if (expectedStatus.equalsIgnoreCase("Pass")) {
                test.info("Test case expected to Pass.");
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'You have been successfully subscribed!')]")));
                softAssert.assertTrue(successMessage.isDisplayed(), "Success message not displayed for valid email.");
                test.pass(testCaseId + " passed successfully. Email subscription was successful.");
            } else { // Expected to Fail
                test.info("Test case expected to Fail.");
                try {
                    WebElement alertMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.ALERT_DANGER_MESSAGE));
                    softAssert.assertTrue(alertMsg.isDisplayed(), "Alert message not displayed for invalid email.");
                    test.pass("Test case correctly failed. Error message was displayed for invalid data.");
                } catch (Exception e) {
                    softAssert.fail("Test case was expected to fail, but no error message was found.");
                    test.fail(testCaseId + " failed. No error message was displayed for invalid data.");
                    String path = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "email_subscription_failures");
                    test.fail("Failed: No error message was displayed for invalid data.", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                }
            }
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "email_subscription_failures");
            test.fail(testCaseId + " failed due to an exception: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail(testCaseId + " failed: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }
    
    @DataProvider(name = "reviewData")
    public Object[][] getReviewData() throws IOException {
    	String excelFilePath = System.getProperty("user.dir") + "/src/test/resources/ProductTestData.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("ReviewData");

        int rowCount = sheet.getLastRowNum();
        Object[][] data = new Object[rowCount][5];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue; // Skip empty rows
            }
            Cell testCaseIdCell = row.getCell(0);
            Cell nameCell = row.getCell(1);
            Cell emailCell = row.getCell(2);
            Cell reviewCell = row.getCell(3);
            Cell expectedStatusCell = row.getCell(4);

            // Correctly handle different cell types
            if (testCaseIdCell.getCellType() == CellType.STRING) {
                data[i - 1][0] = testCaseIdCell.getStringCellValue();
            } else if (testCaseIdCell.getCellType() == CellType.NUMERIC) {
                data[i - 1][0] = String.valueOf((int) testCaseIdCell.getNumericCellValue());
            } else {
                data[i - 1][0] = "";
            }

            data[i - 1][1] = nameCell != null && nameCell.getCellType() == CellType.STRING ? nameCell.getStringCellValue() : "";
            data[i - 1][2] = emailCell != null && emailCell.getCellType() == CellType.STRING ? emailCell.getStringCellValue() : "";
            data[i - 1][3] = reviewCell != null && reviewCell.getCellType() == CellType.STRING ? reviewCell.getStringCellValue() : "";
            data[i - 1][4] = expectedStatusCell != null && expectedStatusCell.getCellType() == CellType.STRING ? expectedStatusCell.getStringCellValue() : "";
        }

        workbook.close();
        inputStream.close();
        return data;
    }

    @DataProvider(name = "subscriptionData")
    public Object[][] getSubscriptionData() throws IOException {
        String excelFilePath = "C:\\Users\\Keerthana\\eclipse-workspace\\Functional Test Case\\src\\test\\java\\resources\\ProductTestData.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("SubscriptionData");

        int rowCount = sheet.getLastRowNum();
        Object[][] data = new Object[rowCount][3];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell testCaseIdCell = row.getCell(0);
            Cell emailCell = row.getCell(1);
            Cell expectedStatusCell = row.getCell(2);

            if (testCaseIdCell.getCellType() == CellType.STRING) {
                data[i - 1][0] = testCaseIdCell.getStringCellValue();
            } else if (testCaseIdCell.getCellType() == CellType.NUMERIC) {
                data[i - 1][0] = String.valueOf((int) testCaseIdCell.getNumericCellValue());
            } else {
                data[i - 1][0] = "";
            }

            data[i - 1][1] = emailCell != null && emailCell.getCellType() == CellType.STRING ? emailCell.getStringCellValue() : "";
            data[i - 1][2] = expectedStatusCell != null && expectedStatusCell.getCellType() == CellType.STRING ? expectedStatusCell.getStringCellValue() : "";
        }

        workbook.close();
        inputStream.close();
        return data;
    }
}