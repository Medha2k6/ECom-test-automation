package com.ui.datadriven;

import com.ui.base.BaseTest;
import com.ui.pages.ProductPage;
import com.ui.utilities.ScreenshotUtilities;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ProductDataDrivenTest extends BaseTest {
    ExtentReports extent;
    ExtentTest test;
    @BeforeSuite
    public void setupExtentReport() {
        String reportPath = System.getProperty("user.dir") 
            + "/reports/datadriven_reports/ProductDataDrivenExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }
    @Test(dataProvider = "productData")
    public void productTests(String testCaseId, String input, String expectedResult, String testType) {
        test = extent.createTest(testCaseId + " - " + testType);
        ProductPage productPage = new ProductPage(driver);
        driver.get("https://automationexercise.com/products");
        test.info("Navigated to the products page: " + driver.getCurrentUrl());

        try {
            switch (testType) {
                case "EmailSubscription":
                    test.info("Starting test for Email Subscription with input: '" + input + "'. Expected Result: " + expectedResult);
                    productPage.subscribeWithEmail(input);

                    // A simple check for email format validation
                    boolean isInputValidFormat = input.contains("@") && input.contains(".");

                    if (isInputValidFormat && Boolean.parseBoolean(expectedResult)) {
                        // Expected to pass (Valid email)
                        test.info("Input format is valid. Expected result is TRUE. Waiting for success message.");
                        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SUBSCRIPTION_SUCCESS_MESSAGE));
                        Assert.assertTrue(successMsg.isDisplayed(), "Success message should be displayed for valid email.");
                        test.pass("Email subscription with input: '" + input + "' passed as expected.");
                    } else {
                        // Expected to fail (Invalid or blank email)
                        test.info("Input format is invalid or expected result is FALSE. Waiting for an error message.");
                        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SUBSCRIPTION_ERROR_MESSAGE));
                        Assert.assertTrue(errorMsg.isDisplayed(), "Error message should be displayed for invalid/blank email.");
                        test.pass("Email subscription with invalid input '" + input + "' failed as expected.");
                    }
                    break;

                case "ProductSearch":
                    test.info("Starting test for Product Search with input: '" + input + "'. Expected Result: " + expectedResult);
                    productPage.searchForProduct(input);
                    
                    if (Boolean.parseBoolean(expectedResult)) {
                        // Expected to pass (Valid search)
                        test.info("Expected result is TRUE. Waiting for search results.");
                        WebElement searchResultHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SEARCHED_PRODUCTS_TITLE));
                        Assert.assertTrue(searchResultHeader.isDisplayed(), "Search result header should be displayed.");
                        List<WebElement> products = driver.findElements(ProductPage.PRODUCT_ITEMS);
                        Assert.assertFalse(products.isEmpty(), "Product items should be displayed.");
                        test.pass("Product search with input: '" + input + "' returned products as expected.");
                    } else {
                        // Expected to fail (Invalid search)
                        test.info("Expected result is FALSE. Waiting for 'Searched Products' header and no products.");
                        WebElement searchResultHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SEARCHED_PRODUCTS_TITLE));
                        Assert.assertTrue(searchResultHeader.isDisplayed(), "Search result header should be displayed.");
                        List<WebElement> products = driver.findElements(ProductPage.PRODUCT_ITEMS);
                        Assert.assertTrue(products.isEmpty(), "No products should be displayed for invalid search.");
                        test.pass("Invalid product search with input '" + input + "' correctly returned no products.");
                    }
                    break;
                
                case "SearchCharacterLimit":
                    test.info("Starting test for Search Character Limit with a long string input.");
                    WebElement searchField = driver.findElement(ProductPage.SEARCH_INPUT_FIELD);
                    searchField.clear();
                    searchField.sendKeys(input);
                    test.info("Inputting a long string into the search bar.");
                    Assert.assertEquals(String.valueOf(searchField.getAttribute("value").length()), expectedResult);
                    test.pass("Search bar character limit is working as expected. The actual length is: " + searchField.getAttribute("value").length());
                    break;

                case "SpecialCharacterSearch":
                    test.info("Starting test for Special Character Search with input: '" + input + "'. Expected Result: " + expectedResult);
                    productPage.searchForProduct(input);
                    WebElement searchResultHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SEARCHED_PRODUCTS_TITLE));
                    Assert.assertTrue(searchResultHeader.isDisplayed(), "Search result header should be displayed.");
                    List<WebElement> products = driver.findElements(ProductPage.PRODUCT_ITEMS);
                    Assert.assertTrue(products.isEmpty(), "No products should be displayed for special character search.");
                    test.pass("Special characters search returns 'No products found' as expected.");
                    break;

                case "BrandFilter":
                    test.info("Starting test for Brand Filter with input: '" + input + "'. Expected Result: " + expectedResult);
                    productPage.clickBrandLink(ProductPage.BRAND_ACCORDIAN_POLO);
                    
                    if (Boolean.parseBoolean(expectedResult)) {
                        test.info("Expected result is TRUE. Waiting for products to be displayed.");
                        List<WebElement> productsByBrand = driver.findElements(ProductPage.PRODUCT_ITEMS);
                        Assert.assertFalse(productsByBrand.isEmpty(), "Products should be displayed for brand filter.");
                        test.pass("Brand filter for '" + input + "' is working as expected.");
                    } else {
                        test.fail("This test case is expected to pass, but the logic handles a failure state.");
                    }
                    break;
            }
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, testCaseId + "_Fail");
            test.fail("Test case failed due to an exception: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }
    }

    @DataProvider
    public Object[][] productData() throws IOException {
        String excelFilePath = System.getProperty("user.dir") + "/src/test/resources/ProductUIData.xlsx";  
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("SearchTests");

        int rowCount = sheet.getLastRowNum();
        Object[][] data = new Object[rowCount][4];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell testCaseIdCell = row.getCell(0);
            Cell inputCell = row.getCell(1);
            Cell expectedResultCell = row.getCell(2);
            Cell testTypeCell = row.getCell(3);

            data[i - 1][0] = getCellValue(testCaseIdCell);
            data[i - 1][1] = getCellValue(inputCell);
            data[i - 1][2] = getCellValue(expectedResultCell);
            data[i - 1][3] = getCellValue(testTypeCell);
        }

        workbook.close();
        inputStream.close();
        return data;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}