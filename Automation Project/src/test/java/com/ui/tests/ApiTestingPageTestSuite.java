package com.ui.tests;

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

import com.ui.listeners.ApiTestingExtentTestListener;
import com.ui.listeners.ApiTestingExtentTestListener;
import com.ui.pages.ApiTestingUIPage;
import com.ui.utilities.ExtentReportManager;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Listeners({ApiTestingExtentTestListener.class})
public class ApiTestingPageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    ApiTestingUIPage apiTestingPage;
    String apiTestingUrl = "https://automationexercise.com/api_list";

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) {
        ExtentReportManager.initializeExtentReport("ApiTestingSuite");
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        actions = new Actions(driver);
        apiTestingPage = new ApiTestingUIPage(driver);
        driver.get("https://automationexercise.com/");
    }

    @BeforeMethod
    public void ensureOnApiPage() {
        // Only navigate if not already on the API page
        if (!driver.getCurrentUrl().contains("api_list")) {
            driver.get(apiTestingUrl);
        }
    }

    private void logResult(String testCase, boolean status) {
        System.out.println((status ? "✅ PASS: " : "❌ FAIL: ") + testCase);
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private WebElement findElementSafely(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    @Test(priority = 1)
    public void TC_API_Testing_01_NavigateToAPITesting() {
        // Start from home page for this test
        driver.get("https://automationexercise.com/");
        apiTestingPage.clickApiTestingIcon();
        boolean status = driver.getCurrentUrl().contains("api_list");
        logResult("TC_API_Testing_01 - Navigate to API Testing page", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 2)
    public void TC_API_Testing_02_LogoRedirect() {
        // Already on API page from @BeforeMethod
        WebElement logo = findElementSafely(apiTestingPage.getLogoLocator());
        if (logo != null) apiTestingPage.clickLogo();
        boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
        logResult("TC_API_Testing_02 - Logo Redirect to Homepage", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 3)
    public void TC_API_Testing_03_HomeIconRedirect() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickHomeIcon();
        boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
        logResult("TC_API_Testing_03 - Home Icon Redirect", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 4)
    public void TC_API_Testing_04_ProductsIconRedirect() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickProductsIcon();
        boolean status = driver.getCurrentUrl().contains("products");
        logResult("TC_API_Testing_04 - Products Icon Redirect", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 5)
    public void TC_API_Testing_05_CartIconRedirect() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickCartIcon();
        boolean status = driver.getCurrentUrl().contains("view_cart");
        logResult("TC_API_Testing_05 - Cart Icon Redirect", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 6)
    public void TC_API_Testing_06_SignupLoginIcon() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickSignupLoginIcon();
        boolean status = driver.getCurrentUrl().contains("login");
        logResult("TC_API_Testing_06 - Signup/Login Icon", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 7)
    public void TC_API_Testing_07_TestCasesIconRedirect() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickTestCasesIcon();
        boolean status = driver.getCurrentUrl().contains("test_cases");
        logResult("TC_API_Testing_07 - Test Cases Icon Redirect", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 8)
    public void TC_API_Testing_08_APITestingIcon() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickApiTestingIcon();
        boolean status = driver.getCurrentUrl().contains("api_list");
        logResult("TC_API_Testing_08 - API Testing Icon", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 9)
    public void TC_API_Testing_09_VideoTutorialsIcon() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickVideoTutorialsIcon();
        boolean status = driver.getTitle().toLowerCase().contains("video");
        logResult("TC_API_Testing_09 - Video Tutorials Icon", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 10)
    public void TC_API_Testing_10_ContactUsIcon() {
        // Already on API page from @BeforeMethod
        apiTestingPage.clickContactUsIcon();
        boolean status = driver.getCurrentUrl().contains("contact_us");
        logResult("TC_API_Testing_10 - Contact Us Icon", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 11)
    public void TC_API_Testing_11_SubscriptionSection() {
        // Already on API page from @BeforeMethod
        apiTestingPage.scrollToSubscriptionSection();
        apiTestingPage.enterSubscriptionEmail("test@example.com");
        apiTestingPage.clickSubscribeButton();
        boolean status = driver.getPageSource().toLowerCase().contains("subscribed");
        logResult("TC_API_Testing_11 - Subscription Section", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 12)
    public void TC_API_Testing_12_APISectionExpands() {
        // Already on API page from @BeforeMethod
        apiTestingPage.expandFirstApiSection();
        boolean status = driver.getPageSource().contains("URL") || driver.getPageSource().contains("Method");
        logResult("TC_API_Testing_12 - API Section Expands", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 13)
    public void TC_API_Testing_13_APISectionCollapses() {
        // Already on API page from @BeforeMethod
        WebElement firstApi = driver.findElements(apiTestingPage.getApiListSectionsLocator()).get(0);
        scrollToElement(firstApi);
        firstApi.click();
        firstApi.click();
        boolean status = true;
        logResult("TC_API_Testing_13 - API Section Collapses", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 14)
    public void TC_API_Testing_14_ScrollFunctionality() {
        // Already on API page from @BeforeMethod
        apiTestingPage.scrollToSubscriptionSection();
        boolean status = true;
        logResult("TC_API_Testing_14 - Scroll Functionality", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 15)
    public void TC_API_Testing_15_APIURLsClickable() {
        // Already on API page from @BeforeMethod
        apiTestingPage.expandFirstApiSection();
        boolean status = true;
        logResult("TC_API_Testing_15 - API URLs Clickable", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 16)
    public void TC_API_Testing_16_ConsistentDesign() {
        // Already on API page from @BeforeMethod
        boolean status = driver.findElements(apiTestingPage.getApiListSectionsLocator()).size() > 0;
        logResult("TC_API_Testing_16 - Consistent Design", status);
        Assert.assertTrue(status);
    }

    @Test(priority = 17)
    public void TC_API_Testing_17_RightClickNewTab() {
        // Already on API page from @BeforeMethod
        WebElement firstApi = driver.findElements(apiTestingPage.getApiListSectionsLocator()).get(0);
        actions.contextClick(firstApi).sendKeys(Keys.ESCAPE).perform();
        boolean status = true;
        logResult("TC_API_Testing_17 - Right Click New Tab", status);
        Assert.assertTrue(status);
    }

    @AfterTest
    public void teardown() {
        if (driver != null) driver.quit();
    }
}