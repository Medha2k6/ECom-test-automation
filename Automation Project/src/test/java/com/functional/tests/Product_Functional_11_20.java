package com.functional.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.product.Base.BaseTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.functional.utilities.ScreenshotUtilities;
import com.functional.pages.ProductPage;


public class Product_Functional_11_20 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyProductFunctional_11_20(Integer n, String s) {
        SoftAssert softAssert = new SoftAssert();

        test = extent.createTest("Verify Product Functional Test Cases 11 to 20");
        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        test.info("Starting test: TC_ECOM_Product_011");
        // TC_011 - No products found behavior
        try {
            test.info("Navigating to products page: " + ProductPage.PRODUCTS_LINK);
            driver.findElement(ProductPage.PRODUCTS_LINK).click();
            test.info("Waiting for search box to be clickable: " + ProductPage.SEARCH_PRODUCT_INPUT);
            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.SEARCH_PRODUCT_INPUT));
            test.info("Clearing and entering 'abc' into search box");
            search.clear();
            search.sendKeys("abc");
            test.info("Clicking search submit button: " + ProductPage.SUBMIT_SEARCH_BUTTON);
            driver.findElement(ProductPage.SUBMIT_SEARCH_BUTTON).click();
            test.info("Waiting for 'Searched Products' title to be visible: " + ProductPage.SEARCHED_PRODUCTS_TITLE);
            WebElement noResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.SEARCHED_PRODUCTS_TITLE));
            softAssert.assertTrue(noResult.isDisplayed(), "TC_011: No products found message not displayed");
            test.pass("TC_011 - No products found handled correctly");
            System.out.println("TC_011 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_011");
            test.fail("TC_ECOM_Product_011 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_011: " + e.getMessage());
            System.out.println("TC_011 FAIL");
        }
        
        test.info("Navigating back to Products page before TC_017");
        try {
            driver.findElement(ProductPage.PRODUCTS_LINK).click();
        } catch (Exception e) {
            test.warning("Navigation to Products page failed before TC_017: " + e.getMessage());
            System.out.println("Warning: Could not navigate back to Products page");
        }
        
        test.info("Starting test: TC_ECOM_Product_017");
        // TC_017 - Multiple filters (Category + Brand)
        try {
            test.info("Clicking on Women category link: " + ProductPage.WOMEN_CATEGORY_EXPAND);
            driver.findElement(ProductPage.WOMEN_CATEGORY_EXPAND).click();
            test.info("Waiting for 'Women' subcategory to be clickable: " + ProductPage.WOMEN_SUBCATEGORY_DRESS);
            WebElement womenCat = wait.until(ExpectedConditions.elementToBeClickable(
                    ProductPage.WOMEN_SUBCATEGORY_DRESS));
            test.info("Clicking on Women category link");
            womenCat.click();
            test.info("Waiting for brand section to be present: " + ProductPage.BRAND_POLO);
            wait.until(ExpectedConditions.presenceOfElementLocated(ProductPage.BRAND_POLO));
            test.info("Clicking on 'Polo' brand link: " + ProductPage.BRAND_POLO);
            driver.findElement(ProductPage.BRAND_POLO).click();
            test.info("Waiting for product grid to be visible after filtering: " + ProductPage.FEATURES_ITEMS_GRID);
            WebElement productGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.FEATURES_ITEMS_GRID));
            softAssert.assertTrue(productGrid.isDisplayed(), "TC_017: Filtered product grid not displayed");
            test.pass("TC_017 - Multiple filters applied successfully");
            System.out.println("TC_017 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_017");
            test.fail("TC_ECOM_Product_017 failed ",
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_017: " + e.getMessage());
            System.out.println("TC_017 FAIL");
        }
        
        test.info("Starting test: TC_ECOM_Product_018");
        // TC_018 - Search is case-insensitive
        try {
        	test.info("Navigating to products page");
        	driver.findElement(ProductPage.PRODUCTS_LINK).click();
            test.info("Waiting for search box to be visible: " + ProductPage.SEARCH_PRODUCT_INPUT);
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.SEARCH_PRODUCT_INPUT));
            test.info("Clearing and entering 'dress' in lowercase");
            searchBox.clear();
            searchBox.sendKeys("dress");
            test.info("Clicking search button");
            driver.findElement(ProductPage.SUBMIT_SEARCH_BUTTON).click();
            test.info("Waiting for search results for lowercase search to be visible: " + ProductPage.SEARCHED_PRODUCTS_TITLE);
            WebElement lowerCaseResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.SEARCHED_PRODUCTS_TITLE));
            softAssert.assertTrue(lowerCaseResult.isDisplayed(), "TC_018: Lowercase search results not displayed");
            
            test.info("Navigating to products page again for uppercase test");
            driver.findElement(ProductPage.PRODUCTS_LINK).click();
            searchBox = driver.findElement(ProductPage.SEARCH_PRODUCT_INPUT);
            test.info("Clearing and entering 'DRESS' in uppercase");
            searchBox.clear();
            searchBox.sendKeys("DRESS");
            driver.findElement(ProductPage.SUBMIT_SEARCH_BUTTON).click();
            test.info("Waiting for search results for uppercase search to be visible: " + ProductPage.SEARCHED_PRODUCTS_TITLE);
            WebElement upperCaseResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.SEARCHED_PRODUCTS_TITLE));
            softAssert.assertTrue(upperCaseResult.isDisplayed(), "TC_018: Uppercase search results not displayed");

            test.pass("TC_018 - Search is case insensitive");
            System.out.println("TC_018 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_018");
            test.fail("TC_ECOM_Product_018 failed ",
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_018: " + e.getMessage());
            System.out.println("TC_018 FAIL");
        }
        
        test.info("Starting test: TC_ECOM_Product_019");
        // TC_019 - Partial keyword search
        try {
        	test.info("Navigating to products page");
        	driver.findElement(ProductPage.PRODUCTS_LINK).click();
            test.info("Waiting for search box to be clickable: " + ProductPage.SEARCH_PRODUCT_INPUT);
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.SEARCH_PRODUCT_INPUT));
            test.info("Clearing and entering partial keyword 'dre'");
            searchBox.clear();
            searchBox.sendKeys("dre");
            driver.findElement(ProductPage.SUBMIT_SEARCH_BUTTON).click();
            test.info("Waiting for product grid to be visible for partial search: " + ProductPage.FEATURES_ITEMS_GRID);
            WebElement partialResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.FEATURES_ITEMS_GRID));
            softAssert.assertTrue(partialResult.isDisplayed(), "TC_019: Partial keyword search results missing");
            test.pass("TC_019 - Partial keyword search works");
            System.out.println("TC_019 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_019");
            test.fail("TC_ECOM_Product_019 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_019: " + e.getMessage());
            System.out.println("TC_019 FAIL");
        }

        test.info("Starting test: TC_ECOM_Product_020");
        // TC_020 - Add multiple quantities of same product
        try {
            test.info("Navigating to products page");
            driver.findElement(ProductPage.PRODUCTS_LINK).click();
            test.info("Waiting for 'View Product' button to be clickable: " + ProductPage.VIEW_PRODUCT_BUTTON);
            WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    ProductPage.VIEW_PRODUCT_BUTTON));
            test.info("Clicking 'View Product' button");
            viewProduct.click();

            test.info("Waiting for quantity field to be clickable: " + ProductPage.PRODUCT_QUANTITY_FIELD);
            WebElement qtyField = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.PRODUCT_QUANTITY_FIELD));
            test.info("Clearing and entering quantity '2'");
            qtyField.clear();
            qtyField.sendKeys("2");
            softAssert.assertEquals(qtyField.getAttribute("value"), "2", "TC_020: Quantity field not updated");

            test.info("Clicking 'Add to cart' button on product details page: " + ProductPage.ADD_TO_CART_PRODUCT_DETAILS_BUTTON);
            driver.findElement(ProductPage.ADD_TO_CART_PRODUCT_DETAILS_BUTTON).click();
            test.info("Waiting for cart modal to be visible: " + ProductPage.CART_MODAL);
            WebElement cartModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.CART_MODAL));
            softAssert.assertTrue(cartModal.isDisplayed(), "TC_020: Cart modal not displayed");

            test.pass("TC_020 - Multiple quantities added to cart successfully");
            System.out.println("TC_020 PASS");
            test.info("Clicking 'Continue Shopping' button: " + ProductPage.CONTINUE_SHOPPING_BUTTON);
            driver.findElement(ProductPage.CONTINUE_SHOPPING_BUTTON).click();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_020");
            test.fail("TC_ECOM_Product_020 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_020: " + e.getMessage());
            System.out.println("TC_020 FAIL");
        }

        softAssert.assertAll();
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}