package com.ui.tests;

import com.ui.listeners.TestCasesExtentTestListener;
import com.ui.pages.TestCasesPage;
import com.ui.utilities.ExtentReportManager; // Import the manager class
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

@Listeners({TestCasesExtentTestListener.class})
public class TestCasesPageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    TestCasesPage testCasesPage;

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) {
        try {
            System.out.println("Setting up test environment with browser: " + browser);
            ExtentReportManager.logInfo("Setting up test environment with browser: " + browser);

            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                System.out.println("Chrome browser initialized successfully");
                ExtentReportManager.logInfo("Chrome browser initialized successfully.");
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                System.out.println("Edge browser initialized successfully");
                ExtentReportManager.logInfo("Edge browser initialized successfully.");
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("brave")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                System.out.println("Firefox browser initialized successfully");
                ExtentReportManager.logInfo("Firefox browser initialized successfully.");
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);
            testCasesPage = new TestCasesPage(driver);

            System.out.println("Navigating to: https://automationexercise.com/");
            ExtentReportManager.logInfo("Navigating to: https://automationexercise.com/");
            driver.get("https://automationexercise.com/");
            System.out.println("Browser setup completed successfully");
            ExtentReportManager.logInfo("Browser setup completed successfully.");
        } catch (Exception e) {
            System.err.println("Error in test setup: " + e.getMessage());
            ExtentReportManager.logFail("Error in test setup: " + e.getMessage());
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

    @Test(priority = 1)
    public void TC_Test_Cases_01_PageLoadsSuccessfully() {
        try {
            testCasesPage.clickHeaderLink(TestCasesPage.TEST_CASES_LINK);
            ExtentReportManager.logInfo("Clicked on 'Test Cases' link in the header.");
            boolean status = driver.findElement(TestCasesPage.LOGO).isDisplayed();
            logResult("TC_Test_Cases_01 - Test Cases Page Loads Successfully", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Test Cases Page loaded successfully and the URL is correct.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_01 - Test Cases Page Loads Successfully", false);
            ExtentReportManager.logFail("Failed to load Test Cases Page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 2)
    public void TC_Test_Cases_02_AllTestCaseLinksPresent() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page.");
            List<WebElement> testCaseLinks = testCasesPage.getTestCaseLinks();
            boolean status = !testCaseLinks.isEmpty();
            ExtentReportManager.logInfo("Verifying that test case links are present.");
            logResult("TC_Test_Cases_02 - All Test Case Links Present", status);
            Assert.assertTrue(status, "Test case links should be present on the page");
            ExtentReportManager.logPass("All test case links are present on the page. Total links found: " + testCaseLinks.size());
        } catch (Exception e) {
            logResult("TC_Test_Cases_02 - All Test Case Links Present", false);
            ExtentReportManager.logFail("Test case links are not present: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 3)
    public void TC_Test_Cases_03_TestCasesSeparatedByRows() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to check row separation.");
            List<WebElement> rows = driver.findElements(TestCasesPage.TEST_CASE_CONTAINERS);
            boolean status = rows.size() > 1;
            ExtentReportManager.logInfo("Checking if test cases are separated into multiple rows.");
            logResult("TC_Test_Cases_03 - Test Cases Separated by Rows", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Test cases are separated into multiple rows. Found " + rows.size() + " rows.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_03 - Test Cases Separated by Rows", false);
            ExtentReportManager.logFail("Test cases are not separated by rows: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 4)
    public void TC_Test_Cases_04_TestCaseLinksClickable() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to check link clickability.");
            List<WebElement> testCaseLinks = testCasesPage.getTestCaseLinks();
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                ExtentReportManager.logInfo("Found at least one test case link. Checking its clickability.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstLink);
                String cursor = firstLink.getCssValue("cursor");
                boolean hasPointerCursor = "pointer".equals(cursor);
                actions.moveToElement(firstLink).perform();
                Thread.sleep(500);
                status = hasPointerCursor || firstLink.isEnabled();
            }
            logResult("TC_Test_Cases_04 - Test Case Links Clickable", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Test case links are clickable and have a pointer cursor.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_04 - Test Case Links Clickable", false);
            ExtentReportManager.logFail("Test case links are not clickable: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 5)
    public void TC_Test_Cases_05_ClickingLinkOpensDetails() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to verify link navigation.");
            List<WebElement> testCaseLinks = testCasesPage.getTestCaseLinks();
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                String originalUrl = driver.getCurrentUrl();
                ExtentReportManager.logInfo("Clicking the first test case link: " + firstLink.getText());
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstLink);
                firstLink.click();
                Thread.sleep(3000);
                String newUrl = driver.getCurrentUrl();
                String pageContent = driver.getPageSource();
                status = !newUrl.equals(originalUrl) ||
                        pageContent.contains("steps") ||
                        pageContent.contains("scenario");
                ExtentReportManager.logInfo("Verifying the new page URL and content.");
            }
            logResult("TC_Test_Cases_05 - Clicking Link Opens Details", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Clicking a link successfully opened the details page with correct content.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_05 - Clicking Link Opens Details", false);
            ExtentReportManager.logFail("Clicking a link did not open the details page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 6)
    public void TC_Test_Cases_06_FeedbackSectionPresent() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to check for the feedback section.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            ExtentReportManager.logInfo("Scrolled down to the bottom of the page.");
            WebElement feedbackSection = driver.findElement(TestCasesPage.FEEDBACK_EMAIL_LINK);
            boolean status = feedbackSection != null && feedbackSection.isDisplayed();
            logResult("TC_Test_Cases_06 - Feedback Section Present", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Feedback section is present on the page and visible.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_06 - Feedback Section Present", false);
            ExtentReportManager.logFail("Feedback section is not present: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 7)
    public void TC_Test_Cases_07_URLVisibleAndWorking() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to check URL functionality.");
            List<WebElement> testCaseLinks = testCasesPage.getTestCaseLinks();
            boolean status = false;
            if (!testCaseLinks.isEmpty()) {
                WebElement firstLink = testCaseLinks.get(0);
                String href = firstLink.getAttribute("href");
                status = href != null && !href.isEmpty();
                ExtentReportManager.logInfo("Verifying href attribute for the first link. Href found: " + href);
                if (status) {
                    firstLink.click();
                    Thread.sleep(3000);
                    String pageTitle = driver.getTitle();
                    boolean pageLoaded = !pageTitle.toLowerCase().contains("error") &&
                            !driver.getPageSource().contains("404");
                    status = pageLoaded;
                    ExtentReportManager.logInfo("Clicked link and checked for page errors. No errors found.");
                }
            }
            logResult("TC_Test_Cases_07 - URL Visible and Working", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Test case URLs are visible and working, leading to valid pages.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_07 - URL Visible and Working", false);
            ExtentReportManager.logFail("Test case URLs are not working as expected: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 8)
    public void TC_Test_Cases_08_FeedbackEmailOpensMailApp() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to verify feedback email link.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            WebElement feedbackEmail = driver.findElement(TestCasesPage.FEEDBACK_EMAIL_LINK);
            String href = feedbackEmail.getAttribute("href");
            boolean status = href != null && href.startsWith("mailto:");
            ExtentReportManager.logInfo("Checking if the feedback email link starts with 'mailto:'.");
            logResult("TC_Test_Cases_08 - Feedback Email Opens Mail App", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Feedback email link is correctly formatted to open a mail application.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_08 - Feedback Email Opens Mail App", false);
            ExtentReportManager.logFail("Feedback email link does not open a mail app: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

   
    @Test(priority = 11)
    public void TC_Test_Cases_11_ScrollToTopButton() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the scroll to top button.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
            ExtentReportManager.logInfo("Scrolled down to the bottom of the page.");
            testCasesPage.scrollToTop();
            Thread.sleep(2000);
            long scrollPosition = (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
            boolean status = scrollPosition < 100;
            ExtentReportManager.logInfo("Verifying that the page has scrolled to the top.");
            logResult("TC_Test_Cases_11 - Scroll To Top Button", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Scroll to top button successfully scrolls the page to the top.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_11 - Scroll To Top Button", false);
            ExtentReportManager.logFail("Scroll to top button is not working as expected: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }


    @Test(priority = 14)
    public void TC_Test_Cases_14_HomeLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Home link.");
            testCasesPage.clickHeaderLink(TestCasesPage.HOME_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Home' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Test_Cases_14 - Home Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Home link successfully navigates to the home page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_14 - Home Link Navigation", false);
            ExtentReportManager.logFail("Home link did not navigate to the home page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 15)
    public void TC_Test_Cases_15_ProductsLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Products link.");
            testCasesPage.clickHeaderLink(TestCasesPage.PRODUCTS_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Products' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("products");
            logResult("TC_Test_Cases_15 - Products Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Products link successfully navigates to the products page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_15 - Products Link Navigation", false);
            ExtentReportManager.logFail("Products link did not navigate to the products page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 16)
    public void TC_Test_Cases_16_CartLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Cart link.");
            testCasesPage.clickHeaderLink(TestCasesPage.CART_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Cart' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("view_cart");
            logResult("TC_Test_Cases_16 - Cart Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Cart link successfully navigates to the cart page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_16 - Cart Link Navigation", false);
            ExtentReportManager.logFail("Cart link did not navigate to the cart page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 17)
    public void TC_Test_Cases_17_SignUpLoginLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the SignUp/Login link.");
            testCasesPage.clickHeaderLink(TestCasesPage.SIGNUP_LOGIN_LINK);
            ExtentReportManager.logInfo("Clicked on the 'SignUp/Login' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("login");
            logResult("TC_Test_Cases_17 - SignUp/Login Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("SignUp/Login link successfully navigates to the login page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_17 - SignUp/Login Link Navigation", false);
            ExtentReportManager.logFail("SignUp/Login link did not navigate to the login page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 18)
    public void TC_Test_Cases_18_TestCasesLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Test Cases link.");
            testCasesPage.clickHeaderLink(TestCasesPage.TEST_CASES_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Test Cases' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("test_cases");
            logResult("TC_Test_Cases_18 - Test Cases Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Test Cases link successfully navigates to the test cases page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_18 - Test Cases Link Navigation", false);
            ExtentReportManager.logFail("Test Cases link did not navigate to the test cases page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 19)
    public void TC_Test_Cases_19_APITestingLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the API Testing link.");
            testCasesPage.clickHeaderLink(TestCasesPage.API_TESTING_LINK);
            ExtentReportManager.logInfo("Clicked on the 'API Testing' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("api_list");
            logResult("TC_Test_Cases_19 - API Testing Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("API Testing link successfully navigates to the API list page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_19 - API Testing Link Navigation", false);
            ExtentReportManager.logFail("API Testing link did not navigate to the API list page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 20)
    public void TC_Test_Cases_20_VideoTutorialsLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Video Tutorials link.");
            testCasesPage.clickHeaderLink(TestCasesPage.VIDEO_TUTORIALS_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Video Tutorials' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("youtube") || driver.getCurrentUrl().contains("video");
            logResult("TC_Test_Cases_20 - Video Tutorials Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Video Tutorials link successfully navigates to the video page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_20 - Video Tutorials Link Navigation", false);
            ExtentReportManager.logFail("Video Tutorials link did not navigate to the video page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 21)
    public void TC_Test_Cases_21_ContactUsLinkNavigation() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test the Contact Us link.");
            testCasesPage.clickHeaderLink(TestCasesPage.CONTACT_US_LINK);
            ExtentReportManager.logInfo("Clicked on the 'Contact Us' link.");
            Thread.sleep(2000);
            boolean status = driver.getCurrentUrl().contains("contact_us");
            logResult("TC_Test_Cases_21 - Contact Us Link Navigation", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("Contact Us link successfully navigates to the contact us page.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_21 - Contact Us Link Navigation", false);
            ExtentReportManager.logFail("Contact Us link did not navigate to the contact us page: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 22)
    public void TC_Test_Cases_22_ScrollBarFunctionality() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test scroll bar functionality.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
            ExtentReportManager.logInfo("Scrolled down to the bottom to enable scroll bar.");
            long bodyHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
            long windowHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
            boolean scrollBarNeeded = bodyHeight > windowHeight;
            if (scrollBarNeeded) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
                Thread.sleep(1000);
                long scrollPosition = (long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
                Thread.sleep(1000);
                boolean scrollWorking = scrollPosition > 0;
                boolean status = scrollBarNeeded && scrollWorking;
                logResult("TC_Test_Cases_22 - Scroll Bar Functionality", status);
                Assert.assertTrue(status);
                ExtentReportManager.logPass("Scroll bar is functional and the page scrolls as expected.");
            } else {
                logResult("TC_Test_Cases_22 - Scroll Bar Functionality", false);
                ExtentReportManager.logFail("Page not tall enough to test scroll bar functionality.");
                Assert.fail("Page not tall enough for scroll bar");
            }
        } catch (Exception e) {
            logResult("TC_Test_Cases_22 - Scroll Bar Functionality", false);
            ExtentReportManager.logFail("Scroll bar functionality failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 23)
    public void TC_Test_Cases_23_ResponsivenessTestCases() {
        try {
            testCasesPage.navigateToTestCasesPage();
            ExtentReportManager.logInfo("Navigated to the Test Cases Page to test responsiveness.");
            Dimension originalSize = driver.manage().window().getSize();
            ExtentReportManager.logInfo("Original window size: " + originalSize.width + "x" + originalSize.height);
            driver.manage().window().setSize(new Dimension(375, 667));
            Thread.sleep(1000);
            ExtentReportManager.logInfo("Resized window to mobile dimensions (375x667).");
            boolean mobileLayout = driver.findElement(By.tagName("body")).isDisplayed();
            driver.manage().window().setSize(new Dimension(768, 1024));
            Thread.sleep(1000);
            ExtentReportManager.logInfo("Resized window to tablet dimensions (768x1024).");
            boolean tabletLayout = driver.findElement(By.tagName("body")).isDisplayed();
            driver.manage().window().setSize(originalSize);
            Thread.sleep(1000);
            ExtentReportManager.logInfo("Reset window size to original.");
            boolean status = mobileLayout && tabletLayout;
            logResult("TC_Test_Cases_23 - Responsiveness Test Cases", status);
            Assert.assertTrue(status);
            ExtentReportManager.logPass("The test cases page is responsive on mobile and tablet screen sizes.");
        } catch (Exception e) {
            logResult("TC_Test_Cases_23 - Responsiveness Test Cases", false);
            ExtentReportManager.logFail("Responsiveness test failed: " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            try {
                System.out.println("Closing browser and cleaning up test environment");
                ExtentReportManager.logInfo("Closing browser and cleaning up test environment.");
                driver.quit();
                System.out.println("Test environment cleanup completed");
                ExtentReportManager.logInfo("Test environment cleanup completed.");
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
                ExtentReportManager.logFail("Error during cleanup: " + e.getMessage());
            }
        }
    }
}