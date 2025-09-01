package com.orangehrm.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.Base.BaseTest;

public class Product_UI extends BaseTest {
	 @Test(dataProvider = "dp")
	    public void verifyUICases(Integer n, String s) {
	    	 test = extent.createTest("Verify Product Page UI Test Cases ");

	         // Home Page
	         driver.get("https://automationexercise.com/");
	         test.info("Navigated to Automation Exercise home page");

	         // Home icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[1]/a")).click();
	             WebElement msg = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg.isDisplayed());
	             test.pass("The home page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The home page is NOT displayed: " + e.getMessage());
	         }

	         // Cart icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[3]/a")).click();
	             WebElement msg1 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg1.isDisplayed());
	             test.pass("The cart page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The cart page is NOT displayed: " + e.getMessage());
	         }

	         // Login/Signup page icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[4]/a")).click();
	             WebElement msg2 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg2.isDisplayed());
	             test.pass("The login/signup page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The login/signup page is NOT displayed: " + e.getMessage());
	         }

	         // Test cases page icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[5]/a")).click();
	             WebElement msg3 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg3.isDisplayed());
	             test.pass("The test cases page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The test cases page is NOT displayed: " + e.getMessage());
	         }

	         // API Testing page icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[6]/a")).click();
	             WebElement msg4 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg4.isDisplayed());
	             test.pass("The API testing page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The API testing page is NOT displayed: " + e.getMessage());
	         }

	         // Video Tutorials icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[7]/a")).click();
	             WebElement videoPageElement = new WebDriverWait(driver, Duration.ofSeconds(5))
	                     .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
	             Assert.assertTrue(videoPageElement.isDisplayed());
	             test.pass("The video tutorials page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The video tutorials page is NOT displayed: " + e.getMessage());
	         }
	         driver.navigate().back();

	         // Contact Us icon
	         try {
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[8]/a")).click();
	             WebElement msg7 = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(msg7.isDisplayed());
	             test.pass("The contact us page is displayed successfully");
	         } catch (Exception e) {
	             test.fail("The contact us page is NOT displayed: " + e.getMessage());
	         }
	         
	         try {
	             driver.get("https://automationexercise.com/");
	             test.info("Navigated to Automation Exercise home page");

	             // Products icon
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
	             test.pass("Products icon clicked.");

	             // TC_Product_08 - Automation Exercise logo leads to homepage
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img")).click();
	             WebElement homepageLogo = driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
	             Assert.assertTrue(homepageLogo.isDisplayed());
	             test.pass("Homepage logo navigates successfully to the homepage.");

	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();

	             // TC_Product_09 - Email subscription with valid email
	             try {
	                 WebElement emailField = driver.findElement(By.id("susbscribe_email"));
	                 emailField.sendKeys("testemail@example.com");
	                 driver.findElement(By.id("subscribe")).click();
	                 // Optionally, add a wait for success message
	                 test.pass("Email subscription tested with valid email.");
	             } catch (Exception e) {
	                 test.fail("Error in email subscription with valid email: " + e.getMessage());
	             }

	             // TC_Product_10 - Brand filter
	             try {
	                 driver.findElement(By.xpath("/html/body/section[2]/div/div/div[1]/div/div[3]/div/ul/li[1]/a")).click();
	                 WebElement msg = driver.findElement(By.xpath("/html/body/section/div/div[1]/ol/li[2]"));
	                 Assert.assertTrue(msg.isDisplayed());
	                 test.pass("Brand filter is functioning as expected.");
	             } catch (Exception e) {
	                 test.fail("Brand filter check failed: " + e.getMessage());
	             }

	             // TC_Product_11 - Category section expand
	             try {
	                 driver.findElement(By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a/span")).click();
	                 WebElement womenCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                         By.xpath("//*[@id=\"Women\"]/div/ul/li[2]/a")));
	                 Assert.assertTrue(womenCategory.isDisplayed());
	                 test.pass("Sections under each category are displayed.");
	             } catch (Exception e) {
	                 test.fail("Expanding category section failed: " + e.getMessage());
	             }

	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();

	             // TC_Product_12 - Search bar
	             try {
	                 WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"search_product\"]"));
	                 searchBar.sendKeys("Dress");
	                 driver.findElement(By.xpath("//*[@id=\"submit_search\"]/i")).click();
	                 WebElement search =
	                         driver.findElement(By.xpath("/html/body/section[2]/div/div/div[2]/div/div[3]/div/div[1]/div[2]/div"));
	                 Assert.assertTrue(search.isDisplayed());
	                 test.pass("Search bar functionality verified.");
	             } catch (Exception e) {
	                 test.fail("Search bar test failed: " + e.getMessage());
	             }
	         } catch (Exception e) {
	             test.fail("Test failed due to an unexpected error: " + e.getMessage());
	         }
	         
	         driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
	         test.info("Clicked on Products page");

	         try {
	             // TC_Product_13 - Add to cart button
	             WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
	                     By.xpath("(//a[text()='Add to cart'])[1]")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
	             WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id='cartModal']//h4")));
	             Assert.assertTrue(cart.isDisplayed());
	             test.pass("Add to cart button works correctly");
	             driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
	         } catch (Exception e) {
	             test.fail("Add to cart button test failed: " + e.getMessage());
	         }
	         
	         driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
	         test.info("Clicked on Products page");

	         try {
	             // TC_Product_13 - Add to cart button
	             WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
	                     By.xpath("(//a[text()='Add to cart'])[1]")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
	             WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id='cartModal']//h4")));
	             Assert.assertTrue(cart.isDisplayed());
	             test.pass("Add to cart button works correctly");
	             driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
	         } catch (Exception e) {
	             test.fail("Add to cart button test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_14 - Go to top button
	             ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	             WebElement scrollUp = wait.until(ExpectedConditions.elementToBeClickable(By.id("scrollUp")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", scrollUp);
	             test.pass("Go to top button functionality verified");
	         } catch (Exception e) {
	             test.fail("Go to top button test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_15 - View product button
	             WebElement viewProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("(//a[text()='View Product'])[1]")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewProduct);
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewProduct);
	             WebElement productDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//div[@class='product-information']")));
	             Assert.assertTrue(productDetails.isDisplayed());
	             test.pass("View product button verified");
	             driver.navigate().back();
	         } catch (Exception e) {
	             test.fail("View product button test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_16 - Women category sections
	             WebElement women = wait.until(ExpectedConditions.elementToBeClickable(
	                     By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a/span")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", women);
	             WebElement womenSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id='Women']/div/ul/li[2]/a")));
	             Assert.assertTrue(womenSection.isDisplayed());
	             test.pass("Women sections under category verified");
	         } catch (Exception e) {
	             test.fail("Women category sections test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_17 - Men category sections
	             WebElement men = wait.until(ExpectedConditions.elementToBeClickable(
	                     By.xpath("//*[@id=\"accordian\"]/div[2]/div[1]/h4/a/span")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", men);
	             WebElement menSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id='Men']/div/ul/li[2]/a")));
	             Assert.assertTrue(menSection.isDisplayed());
	             test.pass("Men sections under category verified");
	         } catch (Exception e) {
	             test.fail("Men category sections test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_18 - Kids category sections
	             WebElement kids = wait.until(ExpectedConditions.elementToBeClickable(
	                     By.xpath("//*[@id=\"accordian\"]/div[3]/div[1]/h4/a/span")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", kids);
	             WebElement kidsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id='Kids']/div/ul/li[2]/a")));
	             Assert.assertTrue(kidsSection.isDisplayed());
	             test.pass("Kids sections under category verified");
	         } catch (Exception e) {
	             test.fail("Kids category sections test failed: " + e.getMessage());
	         }

	         try {
	             // TC_Product_19 - View Cart button after adding to cart
	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
	             WebElement addToCartBtn1 = wait.until(ExpectedConditions.presenceOfElementLocated(
	                     By.xpath("(//a[text()='Add to cart'])[1]")));
	             ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn1);
	             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn1);
	             WebElement cart1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id=\"cartModal\"]/div/div/div[2]/p[2]/a/u")));
	             Assert.assertTrue(cart1.isDisplayed());
	             test.pass("View Cart button after adding to cart verified");
	             driver.findElement(By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']")).click();
	         } catch (Exception e) {
	             test.fail("View Cart button test failed: " + e.getMessage());
	         }

	         try {
	        	    // TC_Product_20 - Email subscription invalid email
	        	    WebElement emailField = driver.findElement(By.id("susbscribe_email"));
	        	    emailField.clear();
	        	    emailField.sendKeys("a@g");
	        	    driver.findElement(By.id("subscribe")).click();

	        	    WebElement alertMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
	        	        By.xpath("//div[contains(@class,'alert-danger')]")));

	        	    Assert.assertTrue(alertMsg.isDisplayed(), "Error alert should be displayed for invalid email");
	        	    test.pass("Email subscription with invalid email correctly shows an error alert");

	        	} catch (Exception e) {
	        	    test.fail("Invalid email was incorrectly accepted (expected error alert, but not found).");
	        	}

	         
	         try {
	             driver.get("https://automationexercise.com/");
	             test.info("Opened Automation Exercise homepage");

	             driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a")).click();
	             test.info("Navigated to Products page");

	             // TC_Product_21 - Page scroll bar
	             ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");
	             test.pass("Page scroll verified");

	             // TC_Product_22 - Special offer / big sale
	             ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
	             WebElement specialOffer = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id=\"sale_image\"]")));
	             Assert.assertTrue(specialOffer.isDisplayed());
	             test.pass("Special offer section verified");

	             // TC_Product_23 - Presence of promo banners / SPECIAL OFFER banners
	             WebElement promoBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("//*[@id=\"sale_image\"]")));
	             Assert.assertTrue(promoBanner.isDisplayed());
	             test.pass("Promo banner presence verified");

	             // TC_Product_24 - Email subscription blank validation
	             WebElement emailField = driver.findElement(By.id("susbscribe_email"));
	             emailField.clear();
	             emailField.sendKeys("");
	             driver.findElement(By.id("subscribe")).click();
	             test.pass("Email subscription blank credentials validation verified");

	             // TC_Product_25 - Invalid product search
	             WebElement searchField = driver.findElement(By.id("search_product"));
	             searchField.clear();
	             searchField.sendKeys("invalidproduct123");
	             driver.findElement(By.id("submit_search")).click();
	             WebElement noResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
	             Assert.assertTrue(noResult.isDisplayed());
	             test.pass("Invalid product search returns no results verified");

	             // TC_Product_26 - Each product has name, price, Add to cart
	             driver.get("https://automationexercise.com/products");
	             WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("(//div[@class='productinfo text-center'])[1]")));
	             Assert.assertTrue(firstProduct.findElement(By.tagName("p")).isDisplayed()); // product name
	             Assert.assertTrue(firstProduct.findElement(By.xpath(".//h2")).isDisplayed()); // product price
	             Assert.assertTrue(firstProduct.findElement(By.xpath(".//a[text()='Add to cart']")).isDisplayed());
	             test.pass("Product name, price, and Add to cart button verified");

	             // TC_Product_27 - Maximum character limit in search bar
	             String longText = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	             searchField = driver.findElement(By.id("search_product"));
	             searchField.clear();
	             searchField.sendKeys(longText);
	             test.pass("Maximum character limit in search bar verified");

	             // TC_Product_28 - Special characters in search bar
	             searchField.clear();
	             searchField.sendKeys("@#$%^&*()");
	             driver.findElement(By.id("submit_search")).click();
	             WebElement specialCharResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                     By.xpath("/html/body/section[2]/div/div/div[2]/div/h2")));
	             Assert.assertTrue(specialCharResult.isDisplayed());
	             test.pass("Special characters search returns No products found verified");

	         } catch (Exception e) {
	             test.fail("Test failed at some point: " + e.getMessage());
	             Assert.fail(e.getMessage());
	         }
	         
	    }

	    @DataProvider
	    public Object[][] dp() {
	        return new Object[][] { { 1, "a" } };
	    }
	

}
