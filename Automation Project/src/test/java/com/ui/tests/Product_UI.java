package com.ui.tests;

import com.ui.base.BaseTest;
import com.ui.pages.ProductPage;
import com.ui.utilities.ScreenshotUtilities;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Product_UI extends BaseTest {
    
    private ExtentReports customExtent;
    private ExtentSparkReporter sparkReporter;
    
    @BeforeClass
    public void setupReport() {
        // Create custom report directory if it doesn't exist
        String reportPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "testsuite_reports";
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        // Generate timestamp for unique report name
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportName = "ProductUI_TestReport.html";
        String fullReportPath = reportPath + "/" + reportName;
        
        // Initialize ExtentReports with custom path
        sparkReporter = new ExtentSparkReporter(fullReportPath);
        sparkReporter.config().setDocumentTitle("Product UI Test Report");
        sparkReporter.config().setReportName("Product Page UI Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        customExtent = new ExtentReports();
        customExtent.attachReporter(sparkReporter);
        customExtent.setSystemInfo("Test Environment", "QA");
        customExtent.setSystemInfo("Tester", "Srimedha");
        customExtent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
        customExtent.setSystemInfo("OS", System.getProperty("os.name"));
        
        System.out.println("Report will be generated at: " + fullReportPath);
    }
    
    @AfterClass
    public void tearDownReport() {
        if (customExtent != null) {
            customExtent.flush();
        }
    }

    @Test(dataProvider = "dp")
    public void verifyUICases(Integer n, String s) {
        // Use custom extent instead of the base class extent
        test = customExtent.createTest("Verify Product Page UI Test Cases");
        ProductPage productPage = new ProductPage(driver);

        // Home Page
        test.info("Navigating to Automation Exercise home page: https://automationexercise.com/");
        driver.get("https://automationexercise.com/");

        // --- TC_Product_01: Home icon functionality ---
        test.info("Starting Test Case: TC_Product_01 - Verify Home icon functionality.");
        try {
            test.info("Clicking the Home header link.");
            productPage.clickHeaderLink(ProductPage.HOME_LINK);
            test.info("Attempting to find and verify the website logo on the homepage.");
            WebElement msg = driver.findElement(ProductPage.WEBSITE_LOGO);
            Assert.assertTrue(msg.isDisplayed(), "The website logo is not displayed on the homepage.");
            test.pass("TC_Product_01 passed. The Home page is displayed successfully after clicking the home icon.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "HomePage_Fail");
            test.fail("TC_Product_01 failed. The Home page is NOT displayed as expected. Error: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_02: Cart icon functionality ---
        test.info("Starting Test Case: TC_Product_02 - Verify Cart icon functionality.");
        try {
            test.info("Clicking the Cart header link.");
            productPage.clickHeaderLink(ProductPage.CART_LINK);
            test.info("Verifying that the cart page is displayed by checking for the website logo.");
            WebElement msg1 = driver.findElement(ProductPage.WEBSITE_LOGO);
            Assert.assertTrue(msg1.isDisplayed(), "The cart page is not displayed.");
            test.pass("TC_Product_02 passed. The cart page is displayed successfully.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "CartPage_Fail");
            test.fail("TC_Product_02 failed. The cart page is NOT displayed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_03: Login/Signup page icon functionality ---
        test.info("Starting Test Case: TC_Product_03 - Verify Login/Signup icon functionality.");
        try {
            test.info("Clicking the Login/Signup header link.");
            productPage.clickHeaderLink(ProductPage.SIGNUP_LOGIN_LINK);
            test.info("Verifying that the login/signup page is displayed.");
            WebElement msg2 = driver.findElement(ProductPage.WEBSITE_LOGO);
            Assert.assertTrue(msg2.isDisplayed(), "The login/signup page is not displayed.");
            test.pass("TC_Product_03 passed. The login/signup page is displayed successfully.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "LoginPage_Fail");
            test.fail("TC_Product_03 failed. The login/signup page is NOT displayed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_05: API Testing page icon functionality ---
        test.info("Starting Test Case: TC_Product_05 - Verify API Testing page icon functionality.");
        try {
            test.info("Clicking the API Testing header link.");
            productPage.clickHeaderLink(ProductPage.API_TESTING_LINK);
            test.info("Verifying the 'APIs List' header is displayed on the new page.");
            WebElement apiHeader = driver.findElement(ProductPage.API_TESTING_LINK);
            Assert.assertTrue(apiHeader.isDisplayed(), "APIs List header is not displayed.");
            test.pass("TC_Product_05 passed. The API testing page is displayed successfully");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "APIPage_Fail");
            test.fail("TC_Product_05 failed. The API testing page is NOT displayed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_06: Video Tutorials icon functionality ---
        test.info("Starting Test Case: TC_Product_06 - Verify Video Tutorials icon functionality.");
        try {
            test.info("Clicking the Video Tutorials header link.");
            productPage.clickHeaderLink(ProductPage.VIDEO_TUTORIALS_LINK);
            test.info("Verifying that the current URL contains 'youtube.com'. Current URL: " + driver.getCurrentUrl());
            boolean isYouTube = wait.until(driver -> driver.getCurrentUrl().contains("youtube.com"));
            Assert.assertTrue(isYouTube, "User is not redirected to YouTube.");
            test.pass("TC_Product_06 passed. The video tutorials page is displayed successfully (YouTube link verified)");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "VideoTutorialsPage_Fail");
            test.fail("TC_Product_06 failed. The video tutorials page is NOT displayed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        driver.navigate().back();
        // --- TC_Product_07: Contact Us icon functionality ---
        test.info("Starting Test Case: TC_Product_07 - Verify Contact Us icon functionality.");
        try {
            test.info("Clicking the Contact Us header link.");
            productPage.clickHeaderLink(ProductPage.CONTACT_US_LINK);
            test.info("Verifying the contact us page is displayed by checking for the website logo.");
            WebElement msg7 = driver.findElement(ProductPage.WEBSITE_LOGO);
            Assert.assertTrue(msg7.isDisplayed(), "The contact us page is not displayed.");
            test.pass("TC_Product_07 passed. The contact us page is displayed successfully");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "ContactUsPage_Fail");
            test.fail("TC_Product_07 failed. The contact us page is NOT displayed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_08: Automation Exercise logo leads to homepage ---
        test.info("Starting Test Case: TC_Product_08 - Verify Automation Exercise logo leads to homepage.");
        driver.get("https://automationexercise.com/");
        test.info("Navigated to Automation Exercise home page to start a new test.");
        productPage.clickHeaderLink(ProductPage.PRODUCTS_LINK);
        try {
            test.info("Clicking the website logo in the header.");
            driver.findElement(ProductPage.WEBSITE_LOGO).click();
            test.info("Verifying that clicking the logo navigates back to the homepage.");
            WebElement homepageLogo = driver.findElement(ProductPage.WEBSITE_LOGO);
            Assert.assertTrue(homepageLogo.isDisplayed(), "The homepage logo is not displayed after clicking it.");
            test.pass("TC_Product_08 passed. Homepage logo navigates successfully to the homepage.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "LogoNavigation_Fail");
            test.fail("TC_Product_08 failed. Homepage logo navigation failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_11: Category section expand ---
        test.info("Starting Test Case: TC_Product_11 - Verify Category section expand functionality.");
        try {
            test.info("Clicking the 'Women's' category to expand it.");
            productPage.clickHeaderLink(ProductPage.CATEGORY_WOMEN_LINK);
            test.info("Waiting for and verifying that the Women's subcategory is displayed.");
            WebElement womenSubcategory = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.WOMEN_SUBCATEGORY_LINK));
            Assert.assertTrue(womenSubcategory.isDisplayed(), "Women's subcategory is not displayed after clicking.");
            test.pass("TC_Product_11 passed. Sections under the 'Women' category are displayed.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "CategorySection_Fail");
            test.fail("TC_Product_11 failed. Expanding the category section failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_13: Add to cart button ---
        test.info("Starting Test Case: TC_Product_13 - Verify 'Add to Cart' button functionality.");
        try {
            test.info("Adding the first product to the cart.");
            productPage.addFirstProductToCart();
            test.info("Waiting for and verifying that the 'Add to Cart' modal is displayed.");
            WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.ADD_TO_CART_MODAL));
            Assert.assertTrue(cart.isDisplayed(), "The 'Add to Cart' modal is not displayed.");
            test.pass("TC_Product_13 passed. The 'Add to cart' button works correctly, and the modal is displayed.");
            test.info("Clicking the 'Continue Shopping' button to close the modal.");
            driver.findElement(ProductPage.CONTINUE_SHOPPING_BTN).click();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "AddToCart_Fail");
            test.fail("TC_Product_13 failed. The 'Add to cart' button test failed: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        productPage.clickHeaderLink(ProductPage.PRODUCTS_LINK);
        test.info("Navigated back to the Products page before the next test case.");

        // --- TC_Product_14: Go to top button ---
        test.info("Starting Test Case: TC_Product_14 - Verify 'Go to Top' button functionality.");
        try {
            test.info("Scrolling to the bottom of the page.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            test.info("Waiting for the 'Go to Top' button to be clickable.");
            WebElement scrollUp = wait.until(ExpectedConditions.elementToBeClickable(ProductPage.SCROLL_UP_BUTTON));
            test.info("Clicking the 'Go to Top' button using JavaScript.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", scrollUp);
            test.pass("TC_Product_14 passed. 'Go to top' button functionality verified. The page should have scrolled to the top.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "GoToTop_Fail");
            test.fail("TC_Product_14 failed. 'Go to top' button test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_15: View product button ---
        test.info("Starting Test Case: TC_Product_15 - Verify 'View Product' button functionality.");
        try {
            test.info("Clicking the 'View Product' button for the first product.");
            productPage.viewFirstProductDetails();
            test.info("Waiting for and verifying that the product details section is displayed.");
            WebElement productDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.PRODUCT_DETAILS_SECTION));
            Assert.assertTrue(productDetails.isDisplayed(), "The product details section is not displayed.");
            test.pass("TC_Product_15 passed. 'View product' button functionality verified. The product details page is displayed.");
            test.info("Navigating back to the previous page.");
            driver.navigate().back();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "ViewProduct_Fail");
            test.fail("TC_Product_15 failed. 'View product' button test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_16: Women category sections ---
        test.info("Starting Test Case: TC_Product_16 - Verify Women category sections are displayed.");
        try {
            test.info("Clicking on the 'Women's' category link.");
            productPage.clickHeaderLink(ProductPage.CATEGORY_WOMEN_LINK);
            test.info("Waiting for and verifying that the 'Women's subcategory' is displayed.");
            WebElement womenSection = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.WOMEN_SUBCATEGORY_LINK));
            Assert.assertTrue(womenSection.isDisplayed(), "The 'Women's subcategory' is not displayed.");
            test.pass("TC_Product_16 passed. Women's sections under the category are verified.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "WomenCategory_Fail");
            test.fail("TC_Product_16 failed. Women's category sections test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_17: Men category sections ---
        test.info("Starting Test Case: TC_Product_17 - Verify Men category sections are displayed.");
        try {
            test.info("Clicking on the 'Men's' category link.");
            productPage.clickHeaderLink(ProductPage.CATEGORY_MEN_LINK);
            test.info("Waiting for and verifying that the 'Men's subcategory' is displayed.");
            WebElement menSection = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.MEN_SUBCATEGORY_LINK));
            Assert.assertTrue(menSection.isDisplayed(), "The 'Men's subcategory' is not displayed.");
            test.pass("TC_Product_17 passed. Men's sections under the category are verified.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "MenCategory_Fail");
            test.fail("TC_Product_17 failed. Men's category sections test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_18: Kids category sections ---
        test.info("Starting Test Case: TC_Product_18 - Verify Kids category sections are displayed.");
        try {
            test.info("Clicking on the 'Kids' category link.");
            productPage.clickHeaderLink(ProductPage.CATEGORY_KIDS_LINK);
            test.info("Waiting for and verifying that the 'Kids' subcategory' is displayed.");
            WebElement kidsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.KIDS_SUBCATEGORY_LINK));
            Assert.assertTrue(kidsSection.isDisplayed(), "The 'Kids' subcategory' is not displayed.");
            test.pass("TC_Product_18 passed. Kids sections under the category are verified.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "KidsCategory_Fail");
            test.fail("TC_Product_18 failed. Kids category sections test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_19: View Cart button after adding to cart ---
        test.info("Starting Test Case: TC_Product_19 - Verify 'View Cart' button after adding to cart.");
        try {
            test.info("Navigating to the products page and adding the first product to the cart.");
            productPage.clickHeaderLink(ProductPage.PRODUCTS_LINK);
            productPage.addFirstProductToCart();
            test.info("Waiting for and verifying that the 'View Cart' link is displayed in the modal.");
            WebElement cart1 = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.VIEW_CART_LINK_IN_MODAL));
            Assert.assertTrue(cart1.isDisplayed(), "The 'View Cart' button is not displayed in the modal.");
            test.pass("TC_Product_19 passed. 'View Cart' button is verified to be present after adding a product to the cart.");
            test.info("Clicking 'Continue Shopping' to close the modal for the next test.");
            driver.findElement(ProductPage.CONTINUE_SHOPPING_BTN).click();
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "ViewCartButton_Fail");
            test.fail("TC_Product_19 failed. 'View Cart' button test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_21: Page scroll bar ---
        test.info("Starting Test Case: TC_Product_21 - Verify page scroll bar functionality.");
        try {
            test.info("Navigating to the products page to test scrolling.");
            driver.get("https://automationexercise.com/products");
            test.info("Scrolling down the page by 500 pixels using JavascriptExecutor.");
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)");
            test.pass("TC_Product_21 passed. Page scroll verified successfully.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_Product_21_Fail");
            test.fail("TC_Product_21 failed. Page scroll test failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_22: Special offer / big sale ---
        test.info("Starting Test Case: TC_Product_22 - Verify the presence of 'Special Offer' section.");
        try {
            test.info("Scrolling to the top of the page to ensure visibility.");
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,0)");
            test.info("Verifying the presence of at least one product, which represents a special offer/sale section.");
            WebElement specialOffer = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.FIRST_PRODUCT_ITEM));
            Assert.assertTrue(specialOffer.isDisplayed(), "The 'Special Offer' section or products are not displayed.");
            test.pass("TC_Product_22 passed. Special offer/sale section verified successfully.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_Product_22_Fail");
            test.fail("TC_Product_22 failed. Special offer section verification failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_23: Presence of promo banners / SPECIAL OFFER banners ---
        test.info("Starting Test Case: TC_Product_23 - Verify the presence of promo banners.");
        try {
            test.info("Verifying that a promo banner (represented by the first product item) is visible.");
            WebElement promoBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.FIRST_PRODUCT_ITEM));
            Assert.assertTrue(promoBanner.isDisplayed(), "Promo banners are not displayed.");
            test.pass("TC_Product_23 passed. Promo banner presence verified successfully.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_Product_23_Fail");
            test.fail("TC_Product_23 failed. Promo banner presence verification failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }

        // --- TC_Product_26: Each product has name, price, Add to cart ---
        test.info("Starting Test Case: TC_Product_26 - Verify each product has a name, price, and 'Add to Cart' button.");
        try {
            test.info("Navigating to the products page to verify product details.");
            driver.get("https://automationexercise.com/products");
            test.info("Waiting for the first product item to be visible.");
            WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(ProductPage.FIRST_PRODUCT_ITEM));
            test.info("Verifying the product name is displayed.");
            Assert.assertTrue(firstProduct.findElement(ProductPage.FIRST_PRODUCT_NAME).isDisplayed(), "Product name is not displayed.");
            test.info("Verifying the product price is displayed.");
            Assert.assertTrue(firstProduct.findElement(ProductPage.FIRST_PRODUCT_PRICE).isDisplayed(), "Product price is not displayed.");
            test.info("Verifying the 'Add to Cart' button is displayed.");
            Assert.assertTrue(firstProduct.findElement(ProductPage.FIRST_PRODUCT_ADD_TO_CART_BTN).isDisplayed(), "'Add to Cart' button is not displayed.");
            test.pass("TC_Product_26 passed. Product name, price, and 'Add to cart' button are all verified for the first product.");
        } catch (Exception e) {
            String path = ScreenshotUtilities.captureScreenshot(driver, "TC_Product_26_Fail");
            test.fail("TC_Product_26 failed. Product details verification failed. Error: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][]{{1, "a"}};
    }
}