package com.functional.tests;

import com.functional.listeners.LoginExtentTestListener;
import com.functional.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

@Listeners({LoginExtentTestListener.class})
public class LoginTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "https://automationexercise.com/";
    String loginUrl = "https://automationexercise.com/login";
    String validEmail = "keerthanashetty0024@gmail.com";
    String validPassword = "abc@123";
    private static final String LOGIN_SUCCESS_TEXT = "Logged in as ";

    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("edge") String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(baseUrl);
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void log(String message) {
        if (LoginExtentTestListener.getTest() != null) {
            LoginExtentTestListener.getTest().info("→ " + message);
        }
    }

    private void navigate(String url) {
        log("Navigating to: " + url);
        driver.get(url);
    }

    private void click(WebElement element, String elementName) {
        log("Clicking: " + elementName);
        element.click();
    }

    private void type(By locator, String text, String fieldName) {
        log("Entering " + fieldName + ": " + (fieldName.contains("password") ? "***" : text));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    private void login(String email, String password) {
        navigate(loginUrl);
        type(LoginPage.EMAIL_INPUT, email, "email");
        type(LoginPage.PASSWORD_INPUT, password, "password");
        click(driver.findElement(LoginPage.LOGIN_BUTTON), "Login button");
        wait.until(ExpectedConditions.presenceOfElementLocated(LoginPage.LOGGED_IN_AS_TEXT));
    }

    private WebElement find(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            log("❌ Element not found: " + locator);
            return null;
        }
    }

    private void logResult(String testCase, boolean passed) {
        if (LoginExtentTestListener.getTest() != null) {
            if (passed) {
                LoginExtentTestListener.getTest().pass("✅ PASS: " + testCase);
            } else {
                LoginExtentTestListener.getTest().fail("❌ FAIL: " + testCase);
            }
        }
    }

    @Test(priority = 6)
    public void TC_ECOM_Login_006_PasswordFieldMasked() {
        navigate(loginUrl);
        WebElement passwordField = find(LoginPage.PASSWORD_INPUT);
        type(LoginPage.PASSWORD_INPUT, "abc@123", "password");
        log("Checking password field masking");
        boolean masked = "password".equals(passwordField.getAttribute("type"));
        logResult("TC_ECOM_Login_006 - Password Field Masked", masked);
        Assert.assertTrue(masked, "Password field should be masked");
    }

    @Test(priority = 20)
    public void TC_ECOM_Login_020_SessionPersistence() {
        // This test now verifies that the session persists across page navigations,
        // which is a more realistic test case for a standard web app.
        login(validEmail, validPassword);
        log("Logged in successfully. Navigating away and back to test session persistence.");
        
        // Navigate to a new page
        navigate(baseUrl);
        
        // Navigate back to the login page (or a page that requires login)
        // In this case, we'll check for the user's logged-in status on the home page
        boolean sessionPersisted;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(LoginPage.LOGGED_IN_AS_TEXT));
            sessionPersisted = true;
        } catch (TimeoutException e) {
            sessionPersisted = false;
        }

        logResult("TC_ECOM_Login_020 - Session Persistence", sessionPersisted);
        Assert.assertTrue(sessionPersisted, "Session should persist across page navigations");
    }

    @Test(priority = 21)
    public void TC_ECOM_Login_021_SuccessfulLogout() {
        login(validEmail, validPassword);

        WebElement logoutBtn = find(LoginPage.LOGOUT_LINK);
        Assert.assertNotNull(logoutBtn, "Login failed - logout button not found");
        
        click(logoutBtn, "Logout button");
        
        log("Verifying logout success using explicit waits");
        
        boolean loggedOut;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(LoginPage.LOGIN_BUTTON),
                ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Login to your account")
            ));
            loggedOut = true;
        } catch (TimeoutException e) {
            loggedOut = false;
        }
        
        logResult("TC_ECOM_Login_021 - Successful Logout", loggedOut);
        Assert.assertTrue(loggedOut, "User should be logged out");
    }

    @Test(priority = 22)
    public void TC_ECOM_Login_022_LogoutButtonNotVisibleWhenLoggedOut() {
        navigate(loginUrl);
        log("Checking logout button visibility when logged out");
        boolean logoutNotVisible = find(LoginPage.LOGOUT_LINK) == null;
        log("Checking login/signup link visibility");
        boolean loginVisible = find(LoginPage.SIGNUP_LOGIN_LINK) != null;
        boolean status = logoutNotVisible && loginVisible;
        logResult("TC_ECOM_Login_022 - Logout Button Not Visible When Logged Out", status);
        Assert.assertTrue(status, "Should see login/signup instead of logout");
    }

    @Test(priority = 23)
    public void TC_ECOM_Login_023_VerifyLogoutButtonRapidClicks() throws InterruptedException {
        login("keerthanashetty124@gmail.com", "abc@123");
        
        WebElement logoutElement = find(LoginPage.LOGOUT_LINK);
        Assert.assertNotNull(logoutElement, "Logout link not found after login");
        
        for (int i = 0; i < 3; i++) {
            try {
                if (i > 0) {
                    logoutElement = driver.findElement(LoginPage.LOGOUT_LINK);
                }
                click(logoutElement, "Logout link attempt " + (i+1));
                Thread.sleep(200);
            } catch (NoSuchElementException e) {
                log("Logout completed on attempt " + (i+1));
                break;
            } catch (Exception e) {
                break;
            }
        }
        
        boolean loggedOut = driver.getCurrentUrl().contains("login") ||
                             find(LoginPage.LOGGED_IN_AS_TEXT) == null;
        
        logResult("TC_ECOM_Login_023 - Logout Button Rapid Clicks", loggedOut);
        Assert.assertTrue(loggedOut, "User should be logged out after rapid clicks");
    }

    @Test(priority = 24)
    public void TC_ECOM_Login_024_LoginPromptAfterLogout() throws InterruptedException {
        login("keerthanashetty124@gmail.com", "abc@123");
        Assert.assertNotNull(find(LoginPage.LOGGED_IN_AS_TEXT), "Login failed");
        
        click(find(LoginPage.LOGOUT_LINK), "Logout");
        
        boolean status = find(LoginPage.SIGNUP_LOGIN_LINK) != null &&
                            driver.getCurrentUrl().contains("login");
        logResult("TC_ECOM_Login_024 - Login Prompt After Logout", status);
        Assert.assertTrue(status, "Login/signup prompt should appear after logout");
    }
}