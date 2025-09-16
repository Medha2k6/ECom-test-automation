package com.functional.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.product.Base.BaseTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.functional.utilities.ScreenshotUtilities;
import com.functional.pages.ProductPage;

public class Product_Functional_21_33 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyProductFunctional_21_33(Integer n, String s) {
        SoftAssert softAssert = new SoftAssert();
        test = extent.createTest("Verify Product Functional Test Cases 21 to 33");

        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        test.info("Starting test: TC_ECOM_Product_021");
        // TC_021 - Verify adding different products increases cart with distinct entries
        try {
            test.info("Navigating to products page: " + ProductPage.PRODUCTS_LINK);
            driver.findElement(ProductPage.PRODUCTS_LINK).click();
            test.info("Waiting for first product's 'Add to cart' button to be present: " + ProductPage.ADD_TO_CART_FIRST_PRODUCT);
            WebElement firstProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                ProductPage.ADD_TO_CART_FIRST_PRODUCT));
            test.info("Clicking 'Add to cart' for the first product via JavascriptExecutor");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);

            test.info("Waiting for cart modal for first product: " + ProductPage.CART_MODAL);
            WebElement cartModal1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                ProductPage.CART_MODAL));
            Assert.assertTrue(cartModal1.isDisplayed(), "Cart modal not displayed for first product");
            test.info("Clicking 'Continue Shopping' button: " + ProductPage.CONTINUE_SHOPPING_BUTTON);
            driver.findElement(ProductPage.CONTINUE_SHOPPING_BUTTON).click();

            test.info("Waiting for second product's 'Add to cart' button to be present: " + ProductPage.ADD_TO_CART_SECOND_PRODUCT);
            WebElement secondProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                ProductPage.ADD_TO_CART_SECOND_PRODUCT));
            test.info("Clicking 'Add to cart' for the second product via JavascriptExecutor");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondProduct);

            test.info("Waiting for cart modal for second product: " + ProductPage.CART_MODAL);
            WebElement cartModal2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                ProductPage.CART_MODAL));
            Assert.assertTrue(cartModal2.isDisplayed(), "Cart modal not displayed for second product");
            test.info("Clicking 'Continue Shopping' button: " + ProductPage.CONTINUE_SHOPPING_BUTTON);
            driver.findElement(ProductPage.CONTINUE_SHOPPING_BUTTON).click();

            test.info("Navigating to the cart page: " + ProductPage.CART_LINK);
            driver.findElement(ProductPage.CART_LINK).click();

            test.info("Waiting for cart rows to be present: " + ProductPage.CART_ROWS);
            List<WebElement> cartRows = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(ProductPage.CART_ROWS));
            softAssert.assertTrue(cartRows.size() >= 2, "TC_021: Cart does not contain distinct products");

            test.pass("TC_021 - Multiple distinct products added successfully");
            System.out.println("TC_021 PASS");

        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_021");
            test.fail("TC_021 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_021: " + e.getMessage());
            System.out.println("TC_021 - FAILED");
        }
        
        test.info("Starting test: TC_ECOM_Product_022");
        // TC_022 - Verify cart persists after page reload
        try {
            test.info("Navigating to the cart page: " + ProductPage.CART_LINK);
            driver.findElement(ProductPage.CART_LINK).click();
            test.info("Reloading the page");
            driver.navigate().refresh();
            test.info("Waiting for cart persistence element to be visible: " + ProductPage.CART_PERSISTENCE_ELEMENT);
            WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.CART_PERSISTENCE_ELEMENT));
            softAssert.assertTrue(cart.isDisplayed(), "TC_022: Cart did not persist after reload");
            test.pass("TC_022 - Cart persisted after reload");
            System.out.println("TC_022 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_022");
            test.fail("TC_022 failed: " + e.getMessage(), 
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_022: " + e.getMessage());
            System.out.println("TC_022 FAIL");
        }
   
        test.info("Navigating back to products page for TC_023");
        
        
        test.info("Starting test: TC_ECOM_Product_023");
        // TC_023 - Quantity counter functionality
        try {
        	driver.get("https://automationexercise.com/products");
            test.info("Waiting for 'View Product' button to be clickable: " + ProductPage.VIEW_PRODUCT_BUTTON);
            wait.until(ExpectedConditions.elementToBeClickable(ProductPage.VIEW_PRODUCT_BUTTON)).click();
            test.info("Waiting for quantity field to be visible: " + ProductPage.PRODUCT_QUANTITY_FIELD);
            WebElement qtyField = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.PRODUCT_QUANTITY_FIELD));
            test.info("Clearing quantity field");
            qtyField.clear();
            test.info("Entering quantity '3'");
            qtyField.sendKeys("3");
            softAssert.assertEquals(qtyField.getAttribute("value"), "3", "TC_023: Quantity not updated");
            test.pass("TC_023 - Quantity counter works fine");
            System.out.println("TC_023 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_023");
            test.fail("TC_023 failed: " + e.getMessage(), 
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_023: " + e.getMessage());
            System.out.println("TC_023 FAIL");
        }
     
        test.info("Navigating back to products page for review tests (TC_024-033)");
        

        
        softAssert.assertAll();
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}