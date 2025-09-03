package functional;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import functional.listeners.TestCasesPageDetailedExtentTestListener;
import java.time.Duration;
import java.util.List;

@Listeners({TestCasesPageDetailedExtentTestListener.class})
public class TestCasesPageDetailedTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    String testCasesUrl = "https://automationexercise.com/test_cases";

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) throws Exception {
        try {
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
            driver.get(testCasesUrl);
            Thread.sleep(1500);
        } catch (Exception e) {
            if (driver != null) driver.quit();
            throw e;
        }
    }

    private void logResult(String testCase, boolean status) {
        if (status) System.out.println("✅ PASS: " + testCase);
        else System.out.println("❌ FAIL: " + testCase);
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    private WebElement findElementSafely(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    private void navigateBackToTestCases() {
        driver.get(testCasesUrl);
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    // ---------------------------------
    // Test Cases 1–26 (Page Links)
    // ---------------------------------

    @Test(priority = 1) 
    public void TC01_RegisterUser()      { runTestCase("Test Case 1: Register User", "Register User"); }

    @Test(priority = 2) 
    public void TC02_LoginCorrect()     { runTestCase("Test Case 2: Login User with correct email and password", "Login"); }

    @Test(priority = 3) 
    public void TC03_LoginIncorrect()   { runTestCase("Test Case 3: Login User with incorrect email and password", "Login"); }

    @Test(priority = 4)
    public void TC04_LogoutUser()        { runTestCase("Test Case 4: Logout User", "Logout"); }

    @Test(priority = 5)
    public void TC05_RegisterExisting()  { runTestCase("Test Case 5: Register User with existing email", "existing"); }

    @Test(priority = 6)
    public void TC06_ContactUsForm()     { runTestCase("Test Case 6: Contact Us Form", "Contact Us"); }

    @Test(priority = 7)
    public void TC07_VerifyTestCasesPage() { runTestCase("Test Case 7: Verify Test Cases Page", "Test Case"); }

    @Test(priority = 8)
    public void TC08_AllProductsDetail() { runTestCase("Test Case 8: Verify All Products and product detail page", "Products"); }

    @Test(priority = 9)
    public void TC09_SearchProduct()     { runTestCase("Test Case 9: Search Product", "Search Product"); }

    @Test(priority = 10)
    public void TC10_SubscriptionHome()  { runTestCase("Test Case 10: Verify Subscription in home page", "Subscription"); }

    @Test(priority = 11)
    public void TC11_SubscriptionCart()  { runTestCase("Test Case 11: Verify Subscription in Cart page", "Subscription"); }

    @Test(priority = 12)
    public void TC12_AddProductsInCart() { runTestCase("Test Case 12: Add Products in Cart", "Cart"); }

    @Test(priority = 13)
    public void TC13_VerifyQtyInCart()   { runTestCase("Test Case 13: Verify Product quantity in Cart", "quantity"); }

    @Test(priority = 14)
    public void TC14_PlaceOrderRegisterWhile() { runTestCase("Test Case 14: Place Order: Register while Checkout", "Place Order"); }

    @Test(priority = 15)
    public void TC15_PlaceOrderRegisterBefore() { runTestCase("Test Case 15: Place Order: Register before Checkout", "Place Order"); }

    @Test(priority = 16)
    public void TC16_PlaceOrderLoginBefore() { runTestCase("Test Case 16: Place Order: Login before Checkout", "Place Order"); }

    @Test(priority = 17)
    public void TC17_RemoveProductsFromCart() { runTestCase("Test Case 17: Remove Products from Cart", "Remove"); }

    @Test(priority = 18)
    public void TC18_ViewCategoryProducts() { runTestCase("Test Case 18: View Category Products", "Category"); }

    @Test(priority = 19)
    public void TC19_ViewAndCartBrandProducts() { runTestCase("Test Case 19: View & Cart Brand Products", "Brand"); }

    @Test(priority = 20)
    public void TC20_SearchProductsVerifyCartAfterLogin() { runTestCase("Test Case 20: Search Products and Verify Cart After Login", "Cart After Login"); }

    @Test(priority = 21)
    public void TC21_AddReviewOnProduct() { runTestCase("Test Case 21: Add review on product", "review"); }

    @Test(priority = 22)
    public void TC22_AddToCartFromRecommended() { runTestCase("Test Case 22: Add to cart from Recommended items", "Recommended"); }

    @Test(priority = 23)
    public void TC23_VerifyAddressInCheckout() { runTestCase("Test Case 23: Verify address details in checkout page", "address"); }

    @Test(priority = 24)
    public void TC24_DownloadInvoiceAfterPurchase() { runTestCase("Test Case 24: Download Invoice after purchase order", "Invoice"); }

    @Test(priority = 25)
    public void TC25_ScrollUpUsingArrow() { runTestCase("Test Case 25: Scroll Up using 'Arrow' button and Scroll Down", "Scroll Up"); }

    @Test(priority = 26)
    public void TC26_ScrollUpWithoutArrow() { runTestCase("Test Case 26: Scroll Up without 'Arrow' button and Scroll Down", "Scroll Up"); }

    // ---------------------------------
    // Test Cases 27–35 (Extended)
    // ---------------------------------

    @Test(priority = 27)
    public void TC27_AllLinksCorrectScenarios() {
        // Prior existing logic - e.g. checking multiple test_case links
        List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
        boolean allLinksWork = true;
        int count = 0;
        for (WebElement link : links) {
            if (count >= 5) break;
            scrollToElement(link);
            String txt = link.getText();
            String before = driver.getCurrentUrl();
            link.click();
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            boolean ok = driver.getCurrentUrl().contains("test_case") || driver.getPageSource().contains("scenario");
            if (!ok) {
                allLinksWork = false;
                System.out.println("Incorrect scenario for link: " + txt);
            }
            navigateBackToTestCases();
            count++;
        }
        logResult("Test Case 27: All Links Open Correct Scenarios", allLinksWork);
        Assert.assertTrue(allLinksWork);
    }

    @Test(priority = 28)
    public void TC28_NoBrokenLinks() {
        List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href,'test_case') or contains(text(),'Test Case')]"));
        boolean noBroken = true;
        int count = 0;
        for (WebElement link : links) {
            if (count >= 10) break;
            scrollToElement(link);
            String href = link.getAttribute("href");
            link.click();
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            String src = driver.getPageSource().toLowerCase();
            // Only check for actual HTTP error pages, not generic "error" text
            if (src.contains("404 not found") || src.contains("500 internal server error") || 
                driver.getTitle().toLowerCase().contains("page not found")) {
                noBroken = false;
                System.out.println("Broken link: " + href);
            }
            navigateBackToTestCases();
            count++;
        }
        logResult("Test Case 28: No Links Are Broken", noBroken);
        Assert.assertTrue(noBroken);
    }

    @Test(priority = 29)
    public void TC29_VerticalScrollSupport() {
        Long bodyHeight = (Long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
        Long windowHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
        boolean scrollNeeded = bodyHeight > windowHeight;
        if (scrollNeeded) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500)");
            Long first = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Long second = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
            boolean ok = first > 0 && second > first;
            logResult("Test Case 29: Vertical Scroll Support", ok);
            Assert.assertTrue(ok);
        } else {
            logResult("Test Case 29: Vertical Scroll Support", true);
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 30)
    public void TC30_FeedbackMailtoLink() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement emailLink = findElementSafely(By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]"));
        if (emailLink == null) {
            emailLink = findElementSafely(By.xpath("//a[contains(text(),'feedback@automationexercise.com')]"));
        }
        if (emailLink != null) {
            scrollToElement(emailLink);
            boolean ok = emailLink.getAttribute("href").contains("mailto:");
            logResult("Test Case 30: Feedback Mailto Link", ok);
            Assert.assertTrue(ok);
        } else {
            logResult("Test Case 30: Feedback Mailto Link", false);
            Assert.fail("Feedback link not found");
        }
    }

    @Test(priority = 31)
    public void TC31_ValidEmailSubscription() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        String random = "test" + System.currentTimeMillis() + "@example.com";
        email.clear();
        email.sendKeys(random);
        btn.click();
        boolean ok = driver.getPageSource().toLowerCase().contains("success");
        logResult("Test Case 31: Valid Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 32)
    public void TC32_InvalidEmailSubscription() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        email.clear();
        email.sendKeys("keerthana0024@g");
        btn.click();
        boolean ok = !driver.getPageSource().toLowerCase().contains("success");
        logResult("Test Case 32: Invalid Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 33)
    public void TC33_BlankEmailSubscription() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        email.clear();
        btn.click();
        String msg = email.getAttribute("validationMessage");
        boolean ok = msg != null && !msg.isEmpty();
        logResult("Test Case 33: Blank Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    @Test(priority = 34)
    public void TC34_DuplicateEmailSubscription() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        
        // First subscription
        email.clear();
        email.sendKeys("keerthanashetty542@gmail.com");
        btn.click();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        
        // Second subscription with same email
        navigateBackToTestCases();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        email = findElementSafely(By.id("susbscribe_email"));
        btn = findElementSafely(By.id("subscribe"));
        email.clear();
        email.sendKeys("keerthanashetty542@gmail.com");
        btn.click();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        
        // Check if second subscription was allowed (this is the bug)
        boolean secondSubscriptionAllowed = driver.getPageSource().toLowerCase().contains("success") ||
                                        driver.getPageSource().toLowerCase().contains("subscribed");
        
        logResult("Test Case 34: Duplicate Email Subscription", !secondSubscriptionAllowed);
        
        // Test should FAIL if duplicate subscription is allowed
        if (secondSubscriptionAllowed) {
            Assert.fail("BUG: Duplicate email subscription was allowed - no duplicate detection");
        } else {
            Assert.assertTrue(true, "Duplicate email properly rejected");
        }
    }

    @Test(priority = 35)
    public void TC35_LongEmailSubscription() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement email = findElementSafely(By.id("susbscribe_email"));
        WebElement btn = findElementSafely(By.id("subscribe"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 95; i++) sb.append('a');
        String longEmail = sb.toString() + "@example.com";
        email.clear();
        email.sendKeys(longEmail);
        btn.click();
        boolean ok = !driver.getPageSource().toLowerCase().contains("success");
        logResult("Test Case 35: Long Email Subscription", ok);
        Assert.assertTrue(ok);
    }

    private void runTestCase(String linkText, String keyword) {
        try {
            WebElement link = findElementSafely(By.linkText(linkText));
            if (link == null) {
                link = findElementSafely(By.partialLinkText(linkText.split(":")[0]));
            }
            if (link != null) {
                scrollToElement(link);
                String before = driver.getCurrentUrl();
                link.click();
                Thread.sleep(1500);
                boolean ok = !driver.getCurrentUrl().equals(before)
                             || driver.getPageSource().toLowerCase().contains(keyword.toLowerCase());
                logResult(linkText, ok);
                Assert.assertTrue(ok);
                navigateBackToTestCases();
            } else {
                logResult(linkText, false);
                Assert.fail(linkText + " link not found");
            }
        } catch (Exception e) {
            logResult(linkText, false);
            Assert.fail(e.getMessage());
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
