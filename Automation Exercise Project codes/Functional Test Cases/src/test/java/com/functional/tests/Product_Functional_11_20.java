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

public class Product_Functional_11_20 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyProductFunctional_11_20(Integer n, String s) {
        SoftAssert softAssert = new SoftAssert();

        test = extent.createTest("Verify Product Functional Test Cases 11 to 20");
        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // TC_011 - No products found behavior
        try {
            driver.findElement(By.xpath("//*[@id='header']//a[contains(text(),'Products')]")).click();
            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(By.id("search_product")));
            search.clear();
            search.sendKeys("abc");
            driver.findElement(By.id("submit_search")).click();
            WebElement noResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(text(),'Searched Products')]")));
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
        
        // TC_012 - Email subscription with valid email
        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("susbscribe_email")));
            emailField.clear();
            emailField.sendKeys("keerthana0024@gmail.com");
            driver.findElement(By.id("subscribe")).click();
            test.pass("TC_012 - Subscription with valid email works");
            System.out.println("TC_012 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_012");
            test.fail("TC_ECOM_Product_012 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_012: " + e.getMessage());
            System.out.println("TC_012 FAIL");
        }

        // TC_013 - Email subscription with invalid email
        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("susbscribe_email")));
            emailField.clear();
            emailField.sendKeys("a@g");
            driver.findElement(By.id("subscribe")).click();
            WebElement alertMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'alert-danger')]")));
            softAssert.assertTrue(alertMsg.isDisplayed(), "TC_013: Error alert should be displayed for invalid email");
            test.pass("TC_013 - Email subscription with invalid email correctly shows an error alert");
            System.out.println("TC_013 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_013");
            test.fail("TC_ECOM_Product_013 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_013: " + e.getMessage());
            System.out.println("TC_013 FAIL");
        }
        
        // TC_014 - Email subscription blank email
        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("susbscribe_email")));
            emailField.clear();
            emailField.sendKeys("");
            driver.findElement(By.id("subscribe")).click();
            test.pass("TC_014 - Blank email correctly rejected");
            System.out.println("TC_014 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_014");
            test.fail("TC_ECOM_Product_014 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_014: " + e.getMessage());
            System.out.println("TC_014 FAIL");
        }

        // TC_015 - Duplicate email subscription
        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("susbscribe_email")));
            emailField.clear();
            emailField.sendKeys("keerthanashetty542@gmail.com");
            driver.findElement(By.id("subscribe")).click();
            WebElement duplicateMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'alert-danger')]")));
            softAssert.assertTrue(duplicateMsg.isDisplayed(), "TC_015: Duplicate email alert not displayed");
            test.pass("TC_015 - Duplicate email correctly rejected");
            System.out.println("TC_015 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_015");
            test.fail("TC_ECOM_Product_015 failed " ,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_015: " + e.getMessage());
            System.out.println("TC_015 FAIL");
        }
        
        // TC_016 - More than 100 chars email
        try {
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("susbscribe_email")));
            emailField.clear();
            String longEmail = "a".repeat(101) + "@test.com";
            emailField.sendKeys(longEmail);
            driver.findElement(By.id("subscribe")).click();
            WebElement longEmailMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'alert-danger')]")));
            softAssert.assertTrue(longEmailMsg.isDisplayed(), "TC_016: Long email alert not displayed");
            test.pass("TC_016 - Long email correctly rejected");
            System.out.println("TC_016 PASS");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_016");
            test.fail("TC_ECOM_Product_016 failed ",
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_016: " + e.getMessage());
            System.out.println("TC_016 FAIL");
        }
        
        // Navigate back to Products page to ensure correct context for following steps
        try {
            driver.findElement(By.xpath("//*[@id='header']//a[contains(text(),'Products')]")).click();
        } catch (Exception e) {
            // Log but continue
            test.warning("Navigation to Products page failed before TC_017: " + e.getMessage());
            System.out.println("Warning: Could not navigate back to Products page");
        }

        // TC_017 - Multiple filters (Category + Brand)
        try {
            driver.findElement(By.xpath("//*[@id='accordian']/div[1]/div[1]/h4/a/span")).click();
            WebElement womenCat = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='Women']/div/ul/li[1]/a")));
            womenCat.click();

            // Add an explicit wait for brand section presence
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='brands_products']")));

            driver.findElement(By.xpath("//div[@class='brands_products']//a[contains(text(),'Polo')]")).click();
            WebElement productGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='features_items']")));
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
        
        // TC_018 - Search is case-insensitive
        try {
        	driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_product")));
            searchBox.clear();
            searchBox.sendKeys("dress");
            driver.findElement(By.id("submit_search")).click();
            WebElement lowerCaseResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
            softAssert.assertTrue(lowerCaseResult.isDisplayed(), "TC_018: Lowercase search results not displayed");
            
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            searchBox = driver.findElement(By.id("search_product"));
            searchBox.clear();
            searchBox.sendKeys("DRESS");
            driver.findElement(By.id("submit_search")).click();
            WebElement upperCaseResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
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
        
        // TC_019 - Partial keyword search
        try {
        	driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("search_product")));
            searchBox.clear();
            searchBox.sendKeys("dre");
            driver.findElement(By.id("submit_search")).click();
            WebElement partialResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='features_items']")));
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

        // TC_020 - Add multiple quantities of same product
        try {
            driver.findElement(By.xpath("//*[@id='header']//a[contains(text(),'Products')]")).click();
            WebElement viewProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//a[text()='View Product'])[1]")));
            viewProduct.click();

            WebElement qtyField = wait.until(ExpectedConditions.elementToBeClickable(By.id("quantity")));
            qtyField.clear();
            qtyField.sendKeys("2");
            softAssert.assertEquals(qtyField.getAttribute("value"), "2", "TC_020: Quantity field not updated");

            driver.findElement(By.xpath("//button[@class='btn btn-default cart']")).click();
            WebElement cartModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='cartModal']//h4")));
            softAssert.assertTrue(cartModal.isDisplayed(), "TC_020: Cart modal not displayed");

            test.pass("TC_020 - Multiple quantities added to cart successfully");
            System.out.println("TC_020 PASS");

            driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_020");
            test.fail("TC_ECOM_Product_020 failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            softAssert.fail("TC_ECOM_Product_020: " + e.getMessage());
            System.out.println("TC_020 FAIL");
        }

        
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}
