package com.functional.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import com.functional.listeners.TestCasesPageDetailedExtentTestListener;
import com.functional.pages.TestCasesPage;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.List;

@Listeners({TestCasesPageDetailedExtentTestListener.class})
public class TestCasesPageDetailedTestSuite {
    WebDriver driver;
    WebDriverWait wait;
    String testCasesUrl = "https://automationexercise.com/test_cases";

    // Step logging method for ExtentReport
    private void logStep(String message) {
        ExtentTest extentTest = TestCasesPageDetailedExtentTestListener.getTest();
        if (extentTest != null) {
            extentTest.info(message);
        }
        System.out.println("[STEP] " + message);
    }

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) throws Exception {
        try {
            logStep("Launching browser: " + browser);
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            logStep("Navigating to test cases page: " + testCasesUrl);
            driver.get(testCasesUrl);
            wait.until(ExpectedConditions.urlToBe(testCasesUrl));
        } catch (Exception e) {
            if (driver != null) driver.quit();
            throw e;
        }
    }

    private void logResult(String testCase, boolean status) {
        if (status)
            System.out.println("✅ PASS: " + testCase);
        else
            System.out.println("❌ FAIL: " + testCase);
    }

    private void scrollToElement(WebElement element) {
        logStep("Scrolling to element: " + (element != null ? element.getText() : "null"));
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            // A short pause to ensure the scroll action is complete before the next action
            Thread.sleep(500); 
        } catch (InterruptedException ignored) {
        }
    }

    private WebElement findElementSafely(By locator) {
        try {
            logStep("Finding element: " + locator.toString());
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logStep("Element not found: " + locator.toString());
            return null;
        }
    }

    private void navigateBackToTestCases() {
        logStep("Navigating back to Test Cases page");
        try {
            driver.navigate().back();
            // Wait until the URL is back to the test cases page
            wait.until(ExpectedConditions.urlToBe(testCasesUrl));
        } catch (Exception e) {
            logStep("Could not navigate back using browser back, using get() instead.");
            driver.get(testCasesUrl);
            wait.until(ExpectedConditions.urlToBe(testCasesUrl));
        }
    }

    // ------------ REFACTORED TEST CASES 1–26 (USING PAGE OBJECTS) ------------

    @Test(priority = 1)
    public void TC01_RegisterUser() {
        logStep("Starting test: TC01_RegisterUser");
        runNavigationTest(TestCasesPage.TC01_REGISTER_USER, "Register User");
    }

    @Test(priority = 2)
    public void TC02_LoginCorrect() {
        logStep("Starting test: TC02_LoginCorrect");
        runNavigationTest(TestCasesPage.TC02_LOGIN_CORRECT, "Login");
    }

    @Test(priority = 3)
    public void TC03_LoginIncorrect() {
        logStep("Starting test: TC03_LoginIncorrect");
        runNavigationTest(TestCasesPage.TC03_LOGIN_INCORRECT, "Login");
    }

    @Test(priority = 4)
    public void TC04_LogoutUser() {
        logStep("Starting test: TC04_LogoutUser");
        runNavigationTest(TestCasesPage.TC04_LOGOUT_USER, "Logout");
    }

    @Test(priority = 5)
    public void TC05_RegisterExisting() {
        logStep("Starting test: TC05_RegisterExisting");
        runNavigationTest(TestCasesPage.TC05_REGISTER_EXISTING, "existing");
    }

    @Test(priority = 6)
    public void TC06_ContactUsForm() {
        logStep("Starting test: TC06_ContactUsForm");
        runNavigationTest(TestCasesPage.TC06_CONTACT_US_FORM, "Contact Us");
    }

    @Test(priority = 7)
    public void TC07_VerifyTestCasesPage() {
        logStep("Starting test: TC07_VerifyTestCasesPage");
        runNavigationTest(TestCasesPage.TC07_VERIFY_TEST_CASES_PAGE, "Test Case");
    }

    @Test(priority = 8)
    public void TC08_AllProductsDetail() {
        logStep("Starting test: TC08_AllProductsDetail");
        runNavigationTest(TestCasesPage.TC08_ALL_PRODUCTS_DETAIL, "Products");
    }

    @Test(priority = 9)
    public void TC09_SearchProduct() {
        logStep("Starting test: TC09_SearchProduct");
        runNavigationTest(TestCasesPage.TC09_SEARCH_PRODUCT, "Search Product");
    }

    @Test(priority = 10)
    public void TC10_SubscriptionHome() {
        logStep("Starting test: TC10_SubscriptionHome");
        runNavigationTest(TestCasesPage.TC10_SUBSCRIPTION_HOME, "Subscription");
    }

    @Test(priority = 11)
    public void TC11_SubscriptionCart() {
        logStep("Starting test: TC11_SubscriptionCart");
        runNavigationTest(TestCasesPage.TC11_SUBSCRIPTION_CART, "Subscription");
    }

    @Test(priority = 12)
    public void TC12_AddProductsInCart() {
        logStep("Starting test: TC12_AddProductsInCart");
        runNavigationTest(TestCasesPage.TC12_ADD_PRODUCTS_IN_CART, "Cart");
    }

    @Test(priority = 13)
    public void TC13_VerifyQtyInCart() {
        logStep("Starting test: TC13_VerifyQtyInCart");
        runNavigationTest(TestCasesPage.TC13_VERIFY_QTY_IN_CART, "quantity");
    }

    @Test(priority = 14)
    public void TC14_PlaceOrderRegisterWhile() {
        logStep("Starting test: TC14_PlaceOrderRegisterWhile");
        runNavigationTest(TestCasesPage.TC14_PLACE_ORDER_REGISTER_WHILE, "Place Order");
    }

    @Test(priority = 15)
    public void TC15_PlaceOrderRegisterBefore() {
        logStep("Starting test: TC15_PlaceOrderRegisterBefore");
        runNavigationTest(TestCasesPage.TC15_PLACE_ORDER_REGISTER_BEFORE, "Place Order");
    }

    @Test(priority = 16)
    public void TC16_PlaceOrderLoginBefore() {
        logStep("Starting test: TC16_PlaceOrderLoginBefore");
        runNavigationTest(TestCasesPage.TC16_PLACE_ORDER_LOGIN_BEFORE, "Place Order");
    }

    @Test(priority = 17)
    public void TC17_RemoveProductsFromCart() {
        logStep("Starting test: TC17_RemoveProductsFromCart");
        runNavigationTest(TestCasesPage.TC17_REMOVE_PRODUCTS_FROM_CART, "Remove");
    }

    @Test(priority = 18)
    public void TC18_ViewCategoryProducts() {
        logStep("Starting test: TC18_ViewCategoryProducts");
        runNavigationTest(TestCasesPage.TC18_VIEW_CATEGORY_PRODUCTS, "Category");
    }

    @Test(priority = 19)
    public void TC19_ViewAndCartBrandProducts() {
        logStep("Starting test: TC19_ViewAndCartBrandProducts");
        runNavigationTest(TestCasesPage.TC19_VIEW_AND_CART_BRAND_PRODUCTS, "Brand");
    }

    @Test(priority = 20)
    public void TC20_SearchProductsVerifyCartAfterLogin() {
        logStep("Starting test: TC20_SearchProductsVerifyCartAfterLogin");
        runNavigationTest(TestCasesPage.TC20_SEARCH_PRODUCTS_VERIFY_CART_AFTER_LOGIN, "Cart After Login");
    }

    @Test(priority = 21)
    public void TC21_AddReviewOnProduct() {
        logStep("Starting test: TC21_AddReviewOnProduct");
        runNavigationTest(TestCasesPage.TC21_ADD_REVIEW_ON_PRODUCT, "review");
    }

    @Test(priority = 22)
    public void TC22_AddToCartFromRecommended() {
        logStep("Starting test: TC22_AddToCartFromRecommended");
        runNavigationTest(TestCasesPage.TC22_ADD_TO_CART_FROM_RECOMMENDED, "Recommended");
    }

    @Test(priority = 23)
    public void TC23_VerifyAddressInCheckout() {
        logStep("Starting test: TC23_VerifyAddressInCheckout");
        runNavigationTest(TestCasesPage.TC23_VERIFY_ADDRESS_IN_CHECKOUT, "address");
    }

    @Test(priority = 24)
    public void TC24_DownloadInvoiceAfterPurchase() {
        logStep("Starting test: TC24_DownloadInvoiceAfterPurchase");
        runNavigationTest(TestCasesPage.TC24_DOWNLOAD_INVOICE_AFTER_PURCHASE, "Invoice");
    }

    @Test(priority = 25)
    public void TC25_ScrollUpUsingArrow() {
        logStep("Starting test: TC25_ScrollUpUsingArrow");
        // Re-navigate to the page to ensure a clean state
        driver.get(testCasesUrl); 
        wait.until(ExpectedConditions.urlToBe(testCasesUrl));
        logStep("Navigated to test cases page for a clean state.");
        // Find the scroll-up arrow element
        WebElement arrowButton = findElementSafely(By.id("scrollUp"));
        
        // Assert that the button is present. It might be hidden on initial load.
        Assert.assertNotNull(arrowButton, "Scroll Up button not found.");
        
        // Scroll to the bottom of the page to make the button visible
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logStep("Scrolled to the bottom of the page.");
        
        // Wait for the button to be visible and clickable
        wait.until(ExpectedConditions.elementToBeClickable(arrowButton));
        
        // Click the scroll-up button
        logStep("Clicking scroll-up arrow button.");
        arrowButton.click();
        
        // Wait for a short moment to allow the scroll animation to complete
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        // Verify that the page has scrolled to the top
        long scrollPosition = (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
        boolean isAtTop = scrollPosition == 0;
        logStep("Verified page scroll position is at the top.");
        
        logResult("Test Case 25: Scroll Up using 'Arrow' button", isAtTop);
        Assert.assertTrue(isAtTop, "Page did not scroll to the top using the arrow button.");
    }

    @Test(priority = 26)
    public void TC26_ScrollUpWithoutArrow() {
        logStep("Starting test: TC26_ScrollUpWithoutArrow");
        // Re-navigate to the page to ensure a clean state
        driver.get(testCasesUrl);
        wait.until(ExpectedConditions.urlToBe(testCasesUrl));
        logStep("Navigated to test cases page for a clean state.");

        // Scroll to the bottom of the page
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logStep("Scrolled to the bottom of the page.");
        
        // Use JavaScript to scroll back to the top
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        logStep("Scrolled to the top of the page using JavaScript.");
        
        // Wait for the scroll action to complete
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        // Verify that the page has scrolled to the top
        long scrollPosition = (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
        boolean isAtTop = scrollPosition == 0;
        logStep("Verified page scroll position is at the top.");
        
        logResult("Test Case 26: Scroll Up without 'Arrow' button", isAtTop);
        Assert.assertTrue(isAtTop, "Page did not scroll to the top using Javascript.");
    }
    
    // ------------ EXTENDED TEST CASES 27–35 ------------

    @Test(priority = 27)
    public void TC27_AllLinksCorrectScenarios() {
        logStep("Starting test: TC27_AllLinksCorrectScenarios");
        logStep("Finding all scenario/test case links on page");
        List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
        boolean allLinksWork = true;
        int count = 0;
        for (WebElement link : links) {
            if (count >= 5) break;
            logStep("Scrolling to link: " + link.getText());
            scrollToElement(link);

            String txt = link.getText();
            String before = driver.getCurrentUrl();

            logStep("Clicking link: " + txt);
            link.click();

            try { wait.until(ExpectedConditions.urlContains("test_case")); } catch (Exception e) {}

            boolean ok = driver.getCurrentUrl().contains("test_case") ||
                         driver.getPageSource().contains("scenario");

            logStep("Checked destination URL and page content for: " + txt);

            if (!ok) {
                allLinksWork = false;
                logStep("Incorrect scenario for link: " + txt);
            }
            navigateBackToTestCases();
            // Re-find links after navigating back to avoid StaleElementReferenceException
            links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
            count++;
        }
        logStep("All links opened correct scenarios: " + allLinksWork);
        logResult("Test Case 27: All Links Open Correct Scenarios", allLinksWork);
        Assert.assertTrue(allLinksWork);
    }

    @Test(priority = 28)
    public void TC28_NoBrokenLinks() {
        logStep("Starting test: TC28_NoBrokenLinks");
        logStep("Finding all scenario/test case links on page");
        List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
        boolean noBroken = true;
        int count = 0;
        for (WebElement link : links) {
            if (count >= 10) break;
            scrollToElement(link);
            String href = link.getAttribute("href");
            logStep("Clicking link: " + href);
            link.click();
            try { wait.until(ExpectedConditions.urlContains("/test_case")); } catch (Exception e) {}
            String src = driver.getPageSource().toLowerCase();
            if (src.contains("404 not found") || src.contains("500 internal server error") ||
                driver.getTitle().toLowerCase().contains("page not found")) {
                noBroken = false;
                logStep("Broken link detected: " + href);
            }
            navigateBackToTestCases();
            links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
            count++;
        }
        logStep("All links checked for errors. No broken link found: " + noBroken);
        logResult("Test Case 28: No Links Are Broken", noBroken);
        Assert.assertTrue(noBroken);
    }

    @Test(priority = 29)
    public void TC29_VerticalScrollSupport() {
        logStep("Starting test: TC29_VerticalScrollSupport");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long bodyHeight = ((Number) js.executeScript("return document.documentElement.scrollHeight")).longValue();
        long windowHeight = ((Number) js.executeScript("return window.innerHeight")).longValue();

        boolean scrollNeeded = bodyHeight > windowHeight;
        logStep("Page body height: " + bodyHeight + ", window height: " + windowHeight);

        if (scrollNeeded) {
            logStep("Scrolling down by 500px");
            js.executeScript("window.scrollTo(0, 500)");
            
            // Fix: Cast to Double first, then convert to long
            long first = ((Double) js.executeScript("return window.pageYOffset;")).longValue();
            logStep("Current scroll position: " + first);

            logStep("Scrolling to bottom");
            js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight)");
            
            // Fix: Cast to Double first, then convert to long
            long second = ((Double) js.executeScript("return window.pageYOffset;")).longValue();
            logStep("Current scroll position: " + second);

            boolean ok = first >= 500 && second > first;
            logStep("Scroll position after 500px: " + first + ", after bottom: " + second);

            logResult("Test Case 29: Vertical Scroll Support", ok);
            Assert.assertTrue(ok, "Vertical scrolling did not behave as expected. First=" + first + ", Second=" + second);
        } else {
            logStep("Scroll not needed, page fits in window");
            logResult("Test Case 29: Vertical Scroll Support", true);
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 30)
    public void TC30_FeedbackMailtoLink() {
        logStep("Starting test: TC30_FeedbackMailtoLink");
        logStep("Scrolling to page footer to find feedback link");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        WebElement emailLink = findElementSafely(By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]"));
        if (emailLink == null) {
            logStep("Mailto link not found by href. Trying by text...");
            emailLink = findElementSafely(By.xpath("//a[contains(text(),'feedback@automationexercise.com')]"));
        }
        logStep("Found feedback link element.");

        if (emailLink != null) {
            scrollToElement(emailLink);
            boolean ok = emailLink.getAttribute("href").contains("mailto:");
            logStep("Verified feedback link href attribute.");
            logStep("Feedback mailto href: " + emailLink.getAttribute("href"));
            logResult("Test Case 30: Feedback Mailto Link", ok);
            Assert.assertTrue(ok);
        } else {
            logStep("Feedback link not found");
            logResult("Test Case 30: Feedback Mailto Link", false);
            Assert.fail("Feedback link not found");
        }
    }

    @Test(priority = 31)
    public void TC31_ValidEmailSubscription() {
        logStep("Starting test: TC31_ValidEmailSubscription");
        logStep("Scroll to page footer for subscription");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        logStep("Found email input field and subscribe button.");
        
        Assert.assertNotNull(email, "Email input field not found.");
        Assert.assertNotNull(btn, "Subscribe button not found.");
        
        String random = "test" + System.currentTimeMillis() + "@example.com";
        logStep("Entering valid email: " + random);
        email.clear();
        email.sendKeys(random);

        logStep("Clicking subscription button");
        btn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'alert-success') and contains(text(),'You have been successfully subscribed!')]")));
        logStep("Verified subscription success message is visible.");

        boolean ok = driver.getPageSource().toLowerCase().contains("success");
        logStep("Subscription response contains 'success': " + ok);

        logResult("Test Case 31: Valid Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 32)
    public void TC32_InvalidEmailSubscription() {
        logStep("Starting test: TC32_InvalidEmailSubscription");
        logStep("Scroll to page footer for subscription");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        logStep("Found email input field and subscribe button.");
        
        Assert.assertNotNull(email, "Email input field not found.");
        Assert.assertNotNull(btn, "Subscribe button not found.");

        String invalid = "keerthana0024@g";
        logStep("Entering invalid email: " + invalid);
        email.clear();
        email.sendKeys(invalid);

        logStep("Clicking subscription button");
        btn.click();
        
        boolean ok = !driver.getPageSource().toLowerCase().contains("success");
        logStep("Verified subscription with invalid email rejected: " + ok);

        logResult("Test Case 32: Invalid Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 33)
    public void TC33_BlankEmailSubscription() {
        logStep("Starting test: TC33_BlankEmailSubscription");
        logStep("Scroll to page footer for subscription");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        logStep("Found email input field and subscribe button.");
        
        Assert.assertNotNull(email, "Email input field not found.");
        Assert.assertNotNull(btn, "Subscribe button not found.");

        logStep("Clearing email field and clicking subscribe");
        email.clear();
        btn.click();

        String msg = email.getAttribute("validationMessage");
        boolean ok = msg != null && !msg.isEmpty();
        logStep("Blank email validation message present: " + ok);

        logResult("Test Case 33: Blank Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 34)
    public void TC34_DuplicateEmailSubscription() {
        logStep("Starting test: TC34_DuplicateEmailSubscription");
        logStep("Scroll to page footer for first subscription");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        logStep("Found email input field and subscribe button for first attempt.");
        
        Assert.assertNotNull(email, "Email input field not found.");
        Assert.assertNotNull(btn, "Subscribe button not found.");

        String dup = "keerthanashetty542@gmail.com";
        logStep("Entering email to subscribe: " + dup);
        email.clear();
        email.sendKeys(dup);

        logStep("Clicking subscription button");
        btn.click();
        
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'alert-success')]"))); } catch (Exception e) {}
        logStep("Subscription success message handled for first attempt.");


        // Second attempt
        navigateBackToTestCases();
        logStep("Navigated back to test cases page for second attempt.");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logStep("Scrolled to page footer.");
        email = findElementSafely(By.id("susbscribe_email"));
        btn = findElementSafely(By.id("subscribe"));
        
        Assert.assertNotNull(email, "Email input field not found on second attempt.");
        Assert.assertNotNull(btn, "Subscribe button not found on second attempt.");

        logStep("Entering the same email again to check for duplicate");
        email.clear();
        email.sendKeys(dup);

        btn.click();
        
        boolean secondSubscriptionAllowed = false;
        try {
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'alert-success')]")));
             secondSubscriptionAllowed = true;
        } catch (TimeoutException e) {
            // This is the expected behavior, so do nothing
        }

        logStep("Duplicate email subscription allowed: " + secondSubscriptionAllowed);
        logResult("Test Case 34: Duplicate Email Subscription", !secondSubscriptionAllowed);
        Assert.assertFalse(secondSubscriptionAllowed, "BUG: Duplicate email subscription was allowed - no duplicate detection");
    }

    @Test(priority = 35)
    public void TC35_LongEmailSubscription() {
        logStep("Starting test: TC35_LongEmailSubscription");
        logStep("Scroll to page footer for subscription");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        logStep("Found email input field and subscribe button.");
        
        Assert.assertNotNull(email, "Email input field not found.");
        Assert.assertNotNull(btn, "Subscribe button not found.");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 95; i++) sb.append('a');
        String longEmail = sb.toString() + "@example.com";
        logStep("Entering very long email: " + longEmail);
        email.clear();
        email.sendKeys(longEmail);

        btn.click();
        logStep("Clicked subscription button");

        boolean ok = !driver.getPageSource().toLowerCase().contains("success");
        logStep("Long email subscription rejected: " + ok);

        logResult("Test Case 35: Long Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    // Run/Click Navigation Helper
    private void runNavigationTest(By locator, String keyword) {
        String testCaseName = "Test Case: " + keyword;
        try {
            logStep("Searching for link with locator: " + locator);
            WebElement link = findElementSafely(locator);
            Assert.assertNotNull(link, "Link not found: " + locator);

            scrollToElement(link);
            logStep("Scrolled to element.");

            String beforeUrl = driver.getCurrentUrl();
            logStep("Clicking test case link: " + link.getText());
            link.click();

            try {
                wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(beforeUrl)));
                logStep("URL changed as expected.");
            } catch (TimeoutException e) {
                logStep("URL did not change, checking page content for keyword: " + keyword);
            }

            boolean ok = !driver.getCurrentUrl().equals(beforeUrl) || driver.getPageSource().toLowerCase().contains(keyword.toLowerCase());
            logStep("Validation for click action: " + ok);
            logResult(testCaseName, ok);
            Assert.assertTrue(ok, "Navigation failed or content keyword not found.");

            navigateBackToTestCases();
            logStep("Navigated back successfully.");
            
            driver.get(testCasesUrl);
            wait.until(ExpectedConditions.urlToBe(testCasesUrl));
            logStep("Re-navigated to the base URL for the next test.");

        } catch (Exception e) {
            logStep("Error running test case: " + testCaseName + " - " + e.getMessage());
            logResult(testCaseName, false);
            Assert.fail(e.getMessage());
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        logStep("Closing the browser and cleanup");
        if (driver != null) driver.quit();
    }
}