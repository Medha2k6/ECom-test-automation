package com.product.tests;

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
import com.product.Base.BaseTest;
import com.product.utilities.ScreenshotUtilities;

public class Product_Functional_1_10 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyFunctionalCases(Integer n, String s) {
    	SoftAssert softAssert = new SoftAssert();
    	test = extent.createTest("Verify Product Functional Test Cases 1 to 10");

        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // TC_ECOM_Product_001 - Verify product search returns correct results
            driver.findElement(By.xpath("//*[@id='header']/div/div/div/div[2]/div/ul/li[2]/a")).click();
            WebElement searchBox = driver.findElement(By.id("search_product"));
            searchBox.sendKeys("Dress");
            driver.findElement(By.id("submit_search")).click();

            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='productinfo text-center']")));
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

        try {
            // TC_ECOM_Product_002 - Verify filtering by category
            driver.findElement(By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a")).click();
            WebElement womenCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"Women\"]/div/ul/li[1]/a")));
            womenCategory.click();

            WebElement categoryResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/section/div/div[2]/div[2]/div/h2")));
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


        	try {
        	// TC_ECOM_Product_003 - Verify filtering by brand
        	driver.findElement(By.xpath("/html/body/section/div/div[2]/div[1]/div/div[2]/div/ul/li[2]/a")).click();
        	WebElement brandResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	By.xpath("/html/body/section/div/div[2]/div[2]/div/h2")));
        	softAssert.assertTrue(brandResult.isDisplayed());
        	test.pass("TC_ECOM_Product_003 - Brand filtering works correctly");
        	System.out.println("TC_ECOM_Product_003 - PASSED");
        	} catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_003");
        	test.fail("TC_ECOM_Product_003 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_003 - FAILED");
        	softAssert.fail(e.getMessage());
        	}
        	

        	try {
        	// TC_ECOM_Product_004 - Verify "Add to cart"
        	driver.findElement(By.xpath("//*[@id='header']/div/div/div/div[2]/div/ul/li[2]/a")).click();
        	WebElement addToCart = wait.until(ExpectedConditions.presenceOfElementLocated(
        	By.xpath("(//a[text()='Add to cart'])[1]")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
        	WebElement cartModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	By.xpath("//*[@id='cartModal']//h4")));
        	softAssert.assertTrue(cartModal.isDisplayed());
        	test.pass("TC_ECOM_Product_004 - Add to cart works");
        	System.out.println("TC_ECOM_Product_004 - PASSED");
        	driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
        	} catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_004");
        	test.fail("TC_ECOM_Product_004 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_004 - FAILED");
        	softAssert.fail(e.getMessage());
        	}
        	


        	try {
        	// TC_ECOM_Product_005 - Verify "View Product" navigates to details page
        	WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
        	By.xpath("/html/body/section[2]/div/div/div[2]/div/div[4]/div/div[2]/ul/li/a")));
        	viewProduct.click();
        	WebElement productDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(
        	By.xpath("/html/body/section/div/div/div[2]/div[2]/div[2]/div/h2")));
        	softAssert.assertTrue(productDetails.isDisplayed());
        	test.pass("TC_ECOM_Product_005 - View Product works");
        	System.out.println("TC_ECOM_Product_005 - PASSED");
        	driver.navigate().back();
        	} catch (Exception e) {
        	String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_005");
        	test.fail("TC_ECOM_Product_005 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        	System.out.println("TC_ECOM_Product_005 - FAILED");
        	softAssert.fail(e.getMessage());
        	}
        	
        	 try {
                 // TC_ECOM_Product_006 - Verify category expand/collapse functionality
                 WebElement womenExpand = wait.until(ExpectedConditions.elementToBeClickable(
                         By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a")));
                 womenExpand.click();
                 WebElement subCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
                         By.xpath("//*[@id=\"Women\"]/div/ul/li[1]/a")));
                 softAssert.assertTrue(subCategory.isDisplayed());
                 test.pass("TC_ECOM_Product_006 - Category expand/collapse works");
                 System.out.println("TC_ECOM_Product_006 - PASSED");
             } catch (Exception e) {
                 String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_006");
                 test.fail("TC_ECOM_Product_006 failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                 System.out.println("TC_ECOM_Product_006 - FAILED");
                 softAssert.fail(e.getMessage());
             }

       
        	 try {
        		    // TC_ECOM_Product_007 - Verify product details visible
        		    WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
        		            By.xpath("/html/body/section[2]/div/div/div[2]/div/div[4]/div/div[2]/ul/li/a")));
        		    viewProduct.click();
        		    WebElement productInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
        		            By.xpath("/html/body/section/div/div/div[2]/div[2]/div[2]/div/h2")));
        		    softAssert.assertTrue(productInfo.isDisplayed());
        		    test.pass("TC_ECOM_Product_007 - Product details visible");
        		    System.out.println("TC_ECOM_Product_007 - PASSED");
        		    driver.navigate().back();
        		} catch (Exception e) {
        		    String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_007");
        		    test.fail("TC_ECOM_Product_007 failed: " + e.getMessage(),
        		            MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        		    System.out.println("TC_ECOM_Product_007 - FAILED: ");
        		    softAssert.fail(e.getMessage());
        		}

        		try {
        		    // TC_ECOM_Product_008 - Verify correct product count for category/brand
        		    driver.findElement(By.xpath("//*[@id=\"accordian\"]/div[2]/div[1]/h4/a")).click();
        		    WebElement menCat = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Men\"]/div/ul/li[1]/a")));
        		    menCat.click();
        		    WebElement productCountLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
        		            By.xpath("/html/body/section/div/div[2]/div[2]/div/h2")));
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

        		try {
        		    // TC_ECOM_Product_009 - Verify hover effect reveals Add to cart
        		    WebElement hoverProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
        		            By.xpath("(//div[@class='product-overlay'])[1]")));
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

        		try {
        		    // TC_ECOM_Product_010 - Verify proceed to checkout prompts login
        		    WebElement addToCart = wait.until(ExpectedConditions.presenceOfElementLocated(
        		            By.xpath("(//a[text()='Add to cart'])[1]")));
        		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);
        		    WebElement viewCart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='cartModal']//u")));
        		    viewCart.click();
        		    driver.findElement(By.xpath("//*[@id='do_action']/div[1]/div/div/a"))
        		            .click();
        		    WebElement loginPrompt = wait.until(ExpectedConditions.visibilityOfElementLocated(
        		            By.xpath("//*[@id=\"checkoutModal\"]/div/div/div[2]/p[1]")));
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
        		 
        		

    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}
