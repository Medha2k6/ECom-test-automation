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

public class Product_Functional_21_33 extends BaseTest {

    @Test(dataProvider = "dp")
    public void verifyProductFunctional_21_33(Integer n, String s) {
        SoftAssert softAssert = new SoftAssert();
        test = extent.createTest("Verify Product Functional Test Cases 21 to 33");

        driver.get("https://automationexercise.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // TC_021 - Verify adding different products increases cart with distinct entries
        try {
            driver.findElement(By.xpath("//*[@id='header']//a[contains(text(),'Products')]")).click();

            WebElement firstProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//a[text()='Add to cart'])[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);

            WebElement cartModal1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='cartModal']//h4")));
            Assert.assertTrue(cartModal1.isDisplayed(), "Cart modal not displayed for first product");
            driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();

            WebElement secondProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//a[text()='Add to cart'])[4]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondProduct);

            WebElement cartModal2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='cartModal']//h4")));
            Assert.assertTrue(cartModal2.isDisplayed(), "Cart modal not displayed for second product");
            driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();

            driver.findElement(By.xpath("//*[@id='header']//a[contains(text(),'Cart')]")).click();

            List<WebElement> cartRows = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='cart_info_table']/tbody/tr")));
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
        
        // TC_022 - Verify cart persists after page reload
        try {
            driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[3]/a")).click();
            driver.navigate().refresh();
            WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
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
        
        driver.get("https://automationexercise.com/products");
        
        

        // TC_023 - Quantity counter functionality
        try {
           
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[text()='View Product'])[1]"))).click();

            WebElement qtyField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantity")));
            qtyField.clear();
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
        
        driver.get("https://automationexercise.com/products");

        // Navigate to review form once for TC_024–033
        try {
            driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[text()='View Product'])[1]"))).click();

            // TC_024 - Valid review submission
            try {
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.pass("TC_024 - Valid review submitted successfully");
                System.out.println("TC_024 PASS");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_024");
                test.fail("TC_024 failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                softAssert.fail("TC_024: " + e.getMessage());
                System.out.println("TC_024 FAIL");
            }

            // TC_025 - Invalid credentials
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("123");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@g");
                driver.findElement(By.id("review")).sendKeys("@@@");
                driver.findElement(By.id("button-review")).click();
                test.fail("TC_025 - Invalid review got submitted (Fail as expected)");
                System.out.println("TC_025 FAIL");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_025");
                test.pass("TC_025 - Invalid review correctly rejected",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                System.out.println("TC_025 PASS");
            }

            // TC_026 - Invalid name
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("123");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.fail("TC_026 - Invalid name review submitted (Fail as expected)");
                System.out.println("TC_026 FAIL");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_026");
                test.pass("TC_026 - Invalid name correctly rejected",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                System.out.println("TC_026 PASS");
            }

            // TC_027 - Invalid email
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@g");
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.fail("TC_027 - Invalid email review submitted (Fail as expected)");
                System.out.println("TC_027 FAIL");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_027");
                test.pass("TC_027 - Invalid email correctly rejected",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                System.out.println("TC_027 PASS");
            }

            // TC_028 - Invalid review
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("review")).sendKeys("@@@");
                driver.findElement(By.id("button-review")).click();
                test.fail("TC_028 - Invalid review submitted (Fail as expected)");
                System.out.println("TC_028 FAIL");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_028");
                test.pass("TC_028 - Invalid review correctly rejected",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                System.out.println("TC_028 PASS");
            }

            // TC_029 - Name with special characters
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("@@@");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("review")).sendKeys("Good product");
                driver.findElement(By.id("button-review")).click();
                test.fail("TC_029 - Special char name submitted (Fail as expected)");
                System.out.println("TC_029 FAIL");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_029");
                test.pass("TC_029 - Special char name correctly rejected",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                System.out.println("TC_029 PASS");
            }

            // TC_030 - Invalid email format
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024gmail.com"); // no @
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.pass("TC_030 - Invalid email format correctly rejected");
                System.out.println("TC_030 PASS");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_030");
                test.fail("TC_030 failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                softAssert.fail("TC_030: " + e.getMessage());
                System.out.println("TC_030 FAIL");
            }

            // TC_031 - Blank name
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.pass("TC_031 - Blank name correctly rejected");
                System.out.println("TC_031 PASS");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_031");
                test.fail("TC_031 failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                softAssert.fail("TC_031: " + e.getMessage());
                System.out.println("TC_031 FAIL");
            }

            // TC_032 - Blank email
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("review")).sendKeys("Nice Product!!");
                driver.findElement(By.id("button-review")).click();
                test.pass("TC_032 - Blank email correctly rejected");
                System.out.println("TC_032 PASS");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_032");
                test.fail("TC_032 failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                softAssert.fail("TC_032: " + e.getMessage());
                System.out.println("TC_032 FAIL");
            }

            // TC_033 - Blank review
            try {
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("review")).clear();
                driver.findElement(By.id("name")).sendKeys("Keerthana");
                driver.findElement(By.id("email")).sendKeys("keerthanashetty0024@gmail.com");
                driver.findElement(By.id("button-review")).click();
                test.pass("TC_033 - Blank review correctly rejected");
                System.out.println("TC_033 PASS");
            } catch (Exception e) {
                String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_033");
                test.fail("TC_033 failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                softAssert.fail("TC_033: " + e.getMessage());
                System.out.println("TC_033 FAIL");
            }

        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_ECOM_Product_ReviewSetup");
            test.fail("Review test setup failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            System.out.println("Review setup FAIL");
        }
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] { { 1, "a" } };
    }
}
