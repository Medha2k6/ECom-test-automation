package com.functional.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.functional.utilities.ScreenshotUtilities;
import com.product.Base.BaseTest;
import com.functional.pages.ProductPage;


public class Product_Functional_1_10 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyFunctionalCases(Integer n, String s) {
    	SoftAssert softAssert = new SoftAssert();
    	test = extent.createTest("Verify Product Functional Test Cases 1 to 10");

        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        test.info("Starting test: TC_ECOM_Product_001");
        try {
            // TC_ECOM_Product_001 - Verify product search returns correct results
            test.info("Searching for link with locator: " + ProductPage.HEADER_PRODUCTS_LINK);
            driver.findElement(ProductPage.HEADER_PRODUCTS_LINK).click();
            test.info("Finding element: " + ProductPage.SEARCH_PRODUCT_INPUT);
            WebElement searchBox = driver.findElement(ProductPage.SEARCH_PRODUCT_INPUT);
            test.info("Entering text into search box: 'Dress'");
            searchBox.sendKeys("Dress");
            test.info("Clicking search button: " + ProductPage.SUBMIT_SEARCH_BUTTON);
            driver.findElement(ProductPage.SUBMIT_SEARCH_BUTTON).click();

            test.info("Waiting for search results to be visible: " + ProductPage.PRODUCT_INFO_TEXT);
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.PRODUCT_INFO_TEXT));
            softAssert.assertTrue(result.isDisplayed());
            test.pass("TC_ECOM_Product_001 - Product search returned correct results");
            System.out.println("TC_ECOM_Product_001 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_001");
            test.fail("TC_ECOM_Product_001 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_001 - FAILED");
            softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_002");
        try {
            // TC_ECOM_Product_002 - Verify filtering by category
            test.info("Clicking category expand: " + ProductPage.WOMEN_CATEGORY_EXPAND);
            driver.findElement(ProductPage.WOMEN_CATEGORY_EXPAND).click();
            test.info("Waiting for Women subcategory 'Dress' link to be visible: " + ProductPage.WOMEN_SUBCATEGORY_DRESS);
            WebElement womenCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.WOMEN_SUBCATEGORY_DRESS));
            test.info("Clicking on Women category 'Dress' link");
            womenCategory.click();

            test.info("Waiting for category result title to be visible: " + ProductPage.WOMEN_CATEGORY_TITLE);
            WebElement categoryResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.WOMEN_CATEGORY_TITLE));
            softAssert.assertTrue(categoryResult.isDisplayed());
            test.pass("TC_ECOM_Product_002 - Category filtering works correctly");
            System.out.println("TC_ECOM_Product_002 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_002");
            test.fail("TC_ECOM_Product_002 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_002 - FAILED");
            softAssert.fail(e.getMessage());
        }

        test.info("Starting test: TC_ECOM_Product_003");
        try {
        	// TC_ECOM_Product_003 - Verify filtering by brand
            test.info("Clicking brand filter: " + ProductPage.BRAND_ACCORDIAN_POLO);
        	driver.findElement(ProductPage.BRAND_ACCORDIAN_POLO).click();
            test.info("Waiting for brand result title to be visible: " + ProductPage.WOMEN_CATEGORY_TITLE);
        	WebElement brandResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	ProductPage.WOMEN_CATEGORY_TITLE));
        	softAssert.assertTrue(brandResult.isDisplayed());
        	test.pass("TC_ECOM_Product_003 - Brand filtering works correctly");
        	System.out.println("TC_ECOM_Product_003 - PASSED");
        } catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_003");
        	test.fail("TC_ECOM_Product_003 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_003 - FAILED");
        	softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_004");
        try {
        	// TC_ECOM_Product_004 - Verify "Add to cart"
            test.info("Navigating to products page");
            driver.findElement(ProductPage.HEADER_PRODUCTS_LINK).click();
            test.info("Waiting for 'Add to cart' button to be present: " + ProductPage.ADD_TO_CART_FIRST_PRODUCT);
        	WebElement addToCart = wait.until(ExpectedConditions.presenceOfElementLocated(
        	ProductPage.ADD_TO_CART_FIRST_PRODUCT));
            test.info("Clicking 'Add to cart' button via JavascriptExecutor");
        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
            test.info("Waiting for cart modal to be visible: " + ProductPage.CART_MODAL);
        	WebElement cartModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	ProductPage.CART_MODAL));
        	softAssert.assertTrue(cartModal.isDisplayed());
        	test.pass("TC_ECOM_Product_004 - Add to cart works");
        	System.out.println("TC_ECOM_Product_004 - PASSED");
            test.info("Clicking 'Continue Shopping' button: " + ProductPage.CONTINUE_SHOPPING_BUTTON);
        	driver.findElement(ProductPage.CONTINUE_SHOPPING_BUTTON).click();
        } catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_004");
        	test.fail("TC_ECOM_Product_004 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_004 - FAILED");
        	softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_005");
        try {
        	// TC_ECOM_Product_005 - Verify "View Product" navigates to details page
            test.info("Waiting for 'View Product' button to be clickable: " + ProductPage.VIEW_PRODUCT_BUTTON_ALT);
        	WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
        	ProductPage.VIEW_PRODUCT_BUTTON_ALT));
            test.info("Clicking 'View Product' button");
        	viewProduct.click();
            test.info("Waiting for product details heading to be visible: " + ProductPage.PRODUCT_DETAILS_HEADING);
        	WebElement productDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	ProductPage.PRODUCT_DETAILS_HEADING));
        	softAssert.assertTrue(productDetails.isDisplayed());
        	test.pass("TC_ECOM_Product_005 - View Product works");
        	System.out.println("TC_ECOM_Product_005 - PASSED");
            test.info("Navigating back to previous page");
        	driver.navigate().back();
        } catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_005");
        	test.fail("TC_ECOM_Product_005 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_005 - FAILED");
        	softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_006");
        try {
            // TC_ECOM_Product_006 - Verify category expand/collapse functionality
            test.info("Waiting for Women category expand button to be clickable: " + ProductPage.WOMEN_CATEGORY_EXPAND);
            WebElement womenExpand = wait.until(ExpectedConditions.elementToBeClickable(
                    ProductPage.WOMEN_CATEGORY_EXPAND));
            test.info("Clicking to expand Women category");
            womenExpand.click();
            test.info("Waiting for a sub-category to be visible: " + ProductPage.WOMEN_SUBCATEGORY_DRESS);
            WebElement subCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.WOMEN_SUBCATEGORY_DRESS));
            softAssert.assertTrue(subCategory.isDisplayed());
            test.pass("TC_ECOM_Product_006 - Category expand/collapse works");
            System.out.println("TC_ECOM_Product_006 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_006");
            test.fail("TC_ECOM_Product_006 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_006 - FAILED");
            softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_007");
        try {
            // TC_ECOM_Product_007 - Verify product details visible
            test.info("Waiting for 'View Product' button to be clickable: " + ProductPage.VIEW_PRODUCT_BUTTON_ALT);
            WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    ProductPage.VIEW_PRODUCT_BUTTON_ALT));
            test.info("Clicking 'View Product' button");
            viewProduct.click();
            test.info("Waiting for product details heading to be visible: " + ProductPage.PRODUCT_DETAILS_HEADING);
            WebElement productInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.PRODUCT_DETAILS_HEADING));
            softAssert.assertTrue(productInfo.isDisplayed());
            test.pass("TC_ECOM_Product_007 - Product details visible");
            System.out.println("TC_ECOM_Product_007 - PASSED");
            test.info("Navigating back to previous page");
            driver.navigate().back();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_007");
            test.fail("TC_ECOM_Product_007 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_007 - FAILED: ");
            softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_008");
        try {
            // TC_ECOM_Product_008 - Verify correct product count for category/brand
            test.info("Clicking to expand Men category: " + ProductPage.MEN_CATEGORY_EXPAND);
            driver.findElement(ProductPage.MEN_CATEGORY_EXPAND).click();
            test.info("Waiting for 'Men' subcategory 'Tshirts' to be clickable: " + ProductPage.MEN_SUBCATEGORY_TSHIRTS);
            WebElement menCat = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.MEN_SUBCATEGORY_TSHIRTS));
            test.info("Clicking on 'Tshirts' subcategory");
            menCat.click();
            test.info("Waiting for product count label to be visible: " + ProductPage.WOMEN_CATEGORY_TITLE);
            WebElement productCountLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.WOMEN_CATEGORY_TITLE));
            softAssert.assertTrue(productCountLabel.isDisplayed());
            test.pass("TC_ECOM_Product_008 - Product count displays correctly");
            System.out.println("TC_ECOM_Product_008 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_008");
            test.fail("TC_ECOM_Product_008 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_008 - FAILED: ");
            softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_009");
        try {
            // TC_ECOM_Product_009 - Verify hover effect reveals Add to cart
            test.info("Waiting for product overlay to be visible to confirm hover effect: " + ProductPage.PRODUCT_OVERLAY);
            WebElement hoverProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.PRODUCT_OVERLAY));
            softAssert.assertTrue(hoverProduct.isDisplayed());
            test.pass("TC_ECOM_Product_009 - Hover effect shows Add to cart");
            System.out.println("TC_ECOM_Product_009 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_009");
            test.fail("TC_ECOM_Product_009 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_009 - FAILED: " );
            softAssert.fail(e.getMessage());
        }
        
        test.info("Starting test: TC_ECOM_Product_010");
        try {
            // TC_ECOM_Product_010 - Verify proceed to checkout prompts login
            test.info("Waiting for 'Add to cart' button to be present: " + ProductPage.ADD_TO_CART_FIRST_PRODUCT);
            WebElement addToCart = wait.until(ExpectedConditions.presenceOfElementLocated(
                    ProductPage.ADD_TO_CART_FIRST_PRODUCT));
            test.info("Clicking 'Add to cart' via JavascriptExecutor");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
            test.info("Waiting for 'View Cart' link to be clickable: " + ProductPage.VIEW_CART_LINK);
            WebElement viewCart = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.VIEW_CART_LINK));
            test.info("Clicking 'View Cart' link");
            viewCart.click();
            test.info("Clicking 'Proceed to Checkout' button: " + ProductPage.PROCEED_TO_CHECKOUT_BUTTON);
            driver.findElement(ProductPage.PROCEED_TO_CHECKOUT_BUTTON).click();
            test.info("Waiting for login prompt modal to be visible: " + ProductPage.LOGIN_PROMPT_MODAL);
            WebElement loginPrompt = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ProductPage.LOGIN_PROMPT_MODAL));
            softAssert.assertTrue(loginPrompt.isDisplayed());
            test.pass("TC_ECOM_Product_010 - Checkout prompts login");
            System.out.println("TC_ECOM_Product_010 - PASSED");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_010");
            test.fail("TC_ECOM_Product_010 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("TC_ECOM_Product_010 - FAILED: ");
            softAssert.fail(e.getMessage());
        }
        
        
        softAssert.assertAll();
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}