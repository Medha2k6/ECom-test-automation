package proj11;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import proj11.listeners.ExtentTestListener;
import java.time.Duration;
import java.util.List;

@Listeners({ExtentTestListener.class})
public class TestCasesPageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    String testCasesUrl = "https://automationexercise.com/test_cases";

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) {
        try {
            System.out.println("Setting up test environment with browser: " + browser);
            
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                System.out.println("Chrome browser initialized successfully");
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                System.out.println("Edge browser initialized successfully");
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("brave")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                System.out.println("Firefox browser initialized successfully");
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);
            
            System.out.println("Navigating to: https://automationexercise.com/");
            driver.get("https://automationexercise.com/");
            System.out.println("Browser setup completed successfully");
        } catch (Exception e) {
            System.err.println("Error in test setup: " + e.getMessage());
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }
    }

    private void logResult(String testCase, boolean status) {
        if (status) {
            System.out.println("✅ PASS: " + testCase);
        } else {
            System.out.println("❌ FAIL: " + testCase);
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    private WebElement findElementSafely(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    @Test(priority = 1)
    public void TC_Test_Cases_01_PageLoadsSuccessfully() {
        try {
            WebElement testCasesIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Test Cases')]")));
            testCasesIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("test_cases") && 
                           driver.getPageSource().contains("Test Cases");
            logResult("TC_Test_Cases_01 - Test Cases Page Loads Successfully", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_01 - Test Cases Page Loads Successfully", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 2)
    public void TC_Test_Cases_02_AllTestCaseLinksPresent() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            // Look for test case links in various formats
            List<WebElement> testCaseLinks = driver.findElements(By.xpath("//a[contains(@href,'test_case')]"));
            if (testCaseLinks.isEmpty()) {
                testCaseLinks = driver.findElements(By.xpath("//div[contains(@class,'test')]//a"));
            }
            if (testCaseLinks.isEmpty()) {
                testCaseLinks = driver.findElements(By.xpath("//table//a | //ul//a | //ol//a"));
            }
            
            boolean status = !testCaseLinks.isEmpty();
            logResult("TC_Test_Cases_02 - All Test Case Links Present", status);
            Assert.assertTrue(status, "Test case links should be present on the page");
        } catch (Exception e) {
            logResult("TC_Test_Cases_02 - All Test Case Links Present", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 3)
    public void TC_Test_Cases_03_TestCasesSeparatedByRows() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            // Check if test cases are in table rows or list items
            List<WebElement> rows = driver.findElements(By.xpath("//tr | //li | //div[contains(@class,'row')]"));
            
            boolean status = rows.size() > 1; // Should have multiple rows/items
            logResult("TC_Test_Cases_03 - Test Cases Separated by Rows", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_03 - Test Cases Separated by Rows", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 4)
    public void TC_Test_Cases_04_TestCaseLinksClickable() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            List<WebElement> testCaseLinks = driver.findElements(By.xpath("//a[contains(@href,'test_case')] | //a[contains(text(),'Test Case')]"));
            if (testCaseLinks.isEmpty()) {
                testCaseLinks = driver.findElements(By.xpath("//a"));
            }
            
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                scrollToElement(firstLink);
                
                // Check if link shows pointer cursor
                String cursor = firstLink.getCssValue("cursor");
                boolean hasPointerCursor = "pointer".equals(cursor);
                
                // Try to hover over the link
                actions.moveToElement(firstLink).perform();
                Thread.sleep(500);
                
                status = hasPointerCursor || firstLink.isEnabled();
            }
            
            logResult("TC_Test_Cases_04 - Test Case Links Clickable", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_04 - Test Case Links Clickable", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 5)
    public void TC_Test_Cases_05_ClickingLinkOpensDetails() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            List<WebElement> testCaseLinks = driver.findElements(By.xpath("//a[contains(@href,'test_case')] | //a[contains(text(),'Test Case')]"));
            if (testCaseLinks.isEmpty()) {
                testCaseLinks = driver.findElements(By.xpath("//a"));
            }
            
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                String originalUrl = driver.getCurrentUrl();
                
                scrollToElement(firstLink);
                firstLink.click();
                Thread.sleep(3000);
                
                // Check if URL changed or content expanded
                String newUrl = driver.getCurrentUrl();
                String pageContent = driver.getPageSource();
                
                status = !newUrl.equals(originalUrl) || 
                        pageContent.contains("steps") || 
                        pageContent.contains("scenario") ||
                        pageContent.contains("expected") ||
                        pageContent.contains("actual");
            }
            
            logResult("TC_Test_Cases_05 - Clicking Link Opens Details", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_05 - Clicking Link Opens Details", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 6)
    public void TC_Test_Cases_06_FeedbackSectionPresent() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
            
            // Look for feedback section
            WebElement feedbackSection = findElementSafely(By.xpath("//div[contains(text(),'Feedback') or contains(text(),'feedback')]"));
            if (feedbackSection == null) {
                feedbackSection = findElementSafely(By.xpath("//h2[contains(text(),'Feedback')] | //h3[contains(text(),'Feedback')]"));
            }
            if (feedbackSection == null) {
                // Look for email contact in footer area
                feedbackSection = findElementSafely(By.xpath("//a[contains(@href,'mailto')]"));
            }
            
            boolean status = feedbackSection != null && feedbackSection.isDisplayed();
            logResult("TC_Test_Cases_06 - Feedback Section Present", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_06 - Feedback Section Present", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 7)
    public void TC_Test_Cases_07_URLVisibleAndWorking() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            List<WebElement> testCaseLinks = driver.findElements(By.xpath("//a[contains(@href,'test_case')]"));
            if (testCaseLinks.isEmpty()) {
                testCaseLinks = driver.findElements(By.xpath("//a"));
            }
            
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                scrollToElement(firstLink);
                
                String href = firstLink.getAttribute("href");
                boolean hasValidHref = href != null && !href.isEmpty();
                
                if (hasValidHref) {
                    firstLink.click();
                    Thread.sleep(3000);
                    
                    // Check if page loads without error
                    String pageTitle = driver.getTitle();
                    boolean pageLoaded = !pageTitle.toLowerCase().contains("error") && 
                                       !driver.getPageSource().contains("404");
                    
                    status = hasValidHref && pageLoaded;
                }
            }
            
            logResult("TC_Test_Cases_07 - URL Visible and Working", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_07 - URL Visible and Working", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 8)
    public void TC_Test_Cases_08_FeedbackEmailOpensMailApp() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement feedbackEmail = findElementSafely(By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]"));
            if (feedbackEmail == null) {
                feedbackEmail = findElementSafely(By.xpath("//a[contains(text(),'feedback@automationexercise.com')]"));
            }
            if (feedbackEmail == null) {
                feedbackEmail = findElementSafely(By.xpath("//a[contains(@href,'mailto')]"));
            }
            
            boolean status = false;
            if (feedbackEmail != null && feedbackEmail.isDisplayed()) {
                String href = feedbackEmail.getAttribute("href");
                status = href != null && href.startsWith("mailto:");
                
                // Don't actually click as it opens email client
                logResult("TC_Test_Cases_08 - Feedback Email Opens Mail App", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_Test_Cases_08 - Feedback Email Opens Mail App", false);
                Assert.fail("Feedback email link not found");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_08 - Feedback Email Opens Mail App", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 9)
    public void TC_Test_Cases_09_ValidEmailSubscription() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement emailField = findElementSafely(By.id("susbscribe_email"));
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys("test@example.com");
                
                WebElement subscribeBtn = findElementSafely(By.id("subscribe"));
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(3000);
                    
                    boolean status = driver.getPageSource().toLowerCase().contains("subscribed successfully") ||
                                   driver.getPageSource().toLowerCase().contains("success");
                    logResult("TC_Test_Cases_09 - Valid Email Subscription", status);
                    Assert.assertTrue(status);
                } else {
                    logResult("TC_Test_Cases_09 - Valid Email Subscription", false);
                    Assert.fail("Subscribe button not found");
                }
            } else {
                logResult("TC_Test_Cases_09 - Valid Email Subscription", false);
                Assert.fail("Email subscription field not found");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_09 - Valid Email Subscription", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 10)
    public void TC_Test_Cases_10_InvalidEmailSubscription() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement emailField = findElementSafely(By.id("susbscribe_email"));
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys("a@g"); // Invalid email as per test document
                
                WebElement subscribeBtn = findElementSafely(By.id("subscribe"));
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(2000);
                    
                    // Check if invalid email was accepted (this is the bug per test document)
                    boolean invalidEmailAccepted = driver.getPageSource().toLowerCase().contains("subscribed successfully") ||
                                                 driver.getPageSource().toLowerCase().contains("success");
                    
                    // Test should FAIL if invalid email is accepted (as per test document - marked as FAIL)
                    logResult("TC_Test_Cases_10 - Invalid Email Subscription", !invalidEmailAccepted);
                    
                    if (invalidEmailAccepted) {
                        Assert.fail("BUG: Invalid email 'a@g' was accepted - proper email validation missing");
                    } else {
                        Assert.assertTrue(true, "Invalid email properly rejected");
                    }
                } else {
                    logResult("TC_Test_Cases_10 - Invalid Email Subscription", false);
                    Assert.fail("Subscribe button not found");
                }
            } else {
                logResult("TC_Test_Cases_10 - Invalid Email Subscription", false);
                Assert.fail("Email subscription field not found");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_10 - Invalid Email Subscription", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 11)
    public void TC_Test_Cases_11_ScrollToTopButton() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            // Scroll down first
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
            
            WebElement scrollToTopBtn = findElementSafely(By.id("scrollUp"));
            if (scrollToTopBtn == null) {
                scrollToTopBtn = findElementSafely(By.xpath("//a[contains(@class,'scroll-top')] | //*[contains(@class,'scroll-up')]"));
            }
            
            boolean status = false;
            if (scrollToTopBtn != null && scrollToTopBtn.isDisplayed()) {
                scrollToTopBtn.click();
                Thread.sleep(2000);
                
                Long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
                status = scrollPosition < 100; // Should be near top
            }
            
            logResult("TC_Test_Cases_11 - Scroll To Top Button", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_11 - Scroll To Top Button", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 12)
    public void TC_Test_Cases_12_BlankEmailSubscription() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement emailField = findElementSafely(By.id("susbscribe_email"));
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear(); // Leave blank
                
                WebElement subscribeBtn = findElementSafely(By.id("subscribe"));
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(2000);
                    
                    // Check for validation message
                    String validationMessage = emailField.getAttribute("validationMessage");
                    boolean status = validationMessage != null && 
                                   validationMessage.toLowerCase().contains("fill");
                    
                    logResult("TC_Test_Cases_12 - Blank Email Subscription", status);
                    Assert.assertTrue(status);
                } else {
                    logResult("TC_Test_Cases_12 - Blank Email Subscription", false);
                    Assert.fail("Subscribe button not found");
                }
            } else {
                logResult("TC_Test_Cases_12 - Blank Email Subscription", false);
                Assert.fail("Email subscription field not found");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_12 - Blank Email Subscription", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 13)
    public void TC_Test_Cases_13_LogoNavigatesToHome() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement logo = findElementSafely(By.xpath("//img[@alt='Website for automation practice']"));
            if (logo == null) {
                logo = findElementSafely(By.xpath("//img[contains(@src,'logo')]"));
            }
            
            boolean status = false;
            if (logo != null) {
                WebElement logoLink = logo.findElement(By.xpath("./ancestor::a[1]"));
                logoLink.click();
                Thread.sleep(2000);
                
                status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            }
            
            logResult("TC_Test_Cases_13 - Logo Navigates to Home", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_13 - Logo Navigates to Home", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 14)
    public void TC_Test_Cases_14_HomeLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Home')]")));
            homeLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Test_Cases_14 - Home Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_14 - Home Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 15)
    public void TC_Test_Cases_15_ProductsLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement productsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Products')]")));
            productsLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("products");
            logResult("TC_Test_Cases_15 - Products Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_15 - Products Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 16)
    public void TC_Test_Cases_16_CartLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Cart')]")));
            cartLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("view_cart");
            logResult("TC_Test_Cases_16 - Cart Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_16 - Cart Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 17)
    public void TC_Test_Cases_17_SignUpLoginLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement signupLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]")));
            signupLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("login");
            logResult("TC_Test_Cases_17 - SignUp/Login Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_17 - SignUp/Login Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 18)
    public void TC_Test_Cases_18_TestCasesLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement testCasesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Test Cases')]")));
            testCasesLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("test_cases");
            logResult("TC_Test_Cases_18 - Test Cases Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_18 - Test Cases Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 19)
    public void TC_Test_Cases_19_APITestingLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement apiLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'API Testing')]")));
            apiLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("api_list");
            logResult("TC_Test_Cases_19 - API Testing Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_19 - API Testing Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 20)
    public void TC_Test_Cases_20_VideoTutorialsLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement videoLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Video Tutorials')]")));
            videoLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("youtube") || 
                           driver.getCurrentUrl().contains("video");
            logResult("TC_Test_Cases_20 - Video Tutorials Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_20 - Video Tutorials Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 21)
    public void TC_Test_Cases_21_ContactUsLinkNavigation() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            WebElement contactLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Contact us')]")));
            contactLink.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("contact_us");
            logResult("TC_Test_Cases_21 - Contact Us Link Navigation", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_21 - Contact Us Link Navigation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 22)
    public void TC_Test_Cases_22_ScrollBarFunctionality() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            Long bodyHeight = (Long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
            Long windowHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
            
            boolean scrollBarNeeded = bodyHeight > windowHeight;
            
            if (scrollBarNeeded) {
                // Test scroll functionality
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
                Thread.sleep(1000);
                Long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
                
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
                Thread.sleep(1000);
                
                boolean scrollWorking = scrollPosition > 0;
                boolean status = scrollBarNeeded && scrollWorking;
                
                logResult("TC_Test_Cases_22 - Scroll Bar Functionality", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_Test_Cases_22 - Scroll Bar Functionality", false);
                Assert.fail("Page not tall enough for scroll bar");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_22 - Scroll Bar Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 23)
    public void TC_Test_Cases_23_ResponsivenessTestCases() {
        try {
            driver.get(testCasesUrl);
            Thread.sleep(2000);
            
            Dimension originalSize = driver.manage().window().getSize();
            
            // Test mobile size
            driver.manage().window().setSize(new Dimension(375, 667));
            Thread.sleep(1000);
            boolean mobileLayout = driver.findElement(By.tagName("body")).isDisplayed();
            
            // Test tablet size
            driver.manage().window().setSize(new Dimension(768, 1024));
            Thread.sleep(1000);
            boolean tabletLayout = driver.findElement(By.tagName("body")).isDisplayed();
            
            // Restore original size
            driver.manage().window().setSize(originalSize);
            Thread.sleep(1000);
            
            boolean status = mobileLayout && tabletLayout;
            logResult("TC_Test_Cases_23 - Responsiveness Test Cases", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Test_Cases_23 - Responsiveness Test Cases", false);
            Assert.fail(e.getMessage());
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            try {
                System.out.println("Closing browser and cleaning up test environment");
                driver.quit();
                System.out.println("Test environment cleanup completed");
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}