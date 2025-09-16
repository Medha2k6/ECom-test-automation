package com.ui.tests;

import com.aventstack.extentreports.Status;
import com.ui.listeners.SignupLoginExtentTestListener;
import com.ui.pages.SignupLoginPage;
import com.ui.utilities.ExtentReportManager;
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

@Listeners({SignupLoginExtentTestListener.class})
public class SignupLoginPageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    SignupLoginPage signupLoginPage;
    String signupLoginUrl = "https://automationexercise.com/login";
    String homeUrl = "https://automationexercise.com/";

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
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            actions = new Actions(driver);
            signupLoginPage = new SignupLoginPage(driver);

            System.out.println("Navigating to: " + homeUrl);
            driver.get(homeUrl);
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

    private void navigateToSignupLogin() throws InterruptedException {
        try {
            ExtentReportManager.logInfo("Navigating to the home page.");
            driver.get(homeUrl);
            Thread.sleep(1000);
            ExtentReportManager.logInfo("Clicking the 'Signup / Login' link.");
            signupLoginPage.clickSignupLoginLink();
            Thread.sleep(2000);
        } catch (Exception e) {
            ExtentReportManager.logInfo("Could not click 'Signup / Login' link, navigating directly to the login page.");
            driver.get(signupLoginUrl);
            Thread.sleep(2000);
        }
    }

    @Test(priority = 15)
    public void TC_Signup_Login_15_HomeIconFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the Home icon.");
            driver.findElement(SignupLoginPage.HOME_LINK).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the current URL is the Home URL.");
            boolean status = driver.getCurrentUrl().equals(homeUrl);
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The current URL is the home page URL.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The current URL is not the home page URL. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_15 - Home Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_15 - Home Icon Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 16)
    public void TC_Signup_Login_16_ProductsIconFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the Products icon.");
            driver.findElement(SignupLoginPage.PRODUCTS_LINK).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the current URL contains 'products'.");
            boolean status = driver.getCurrentUrl().contains("products");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The URL contains 'products', indicating successful navigation.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The URL does not contain 'products'. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_16 - Products Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_16 - Products Icon Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 17)
    public void TC_Signup_Login_17_CartIconFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the Cart icon.");
            driver.findElement(SignupLoginPage.CART_LINK).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the current URL contains 'view_cart' or the page source contains 'Shopping Cart'.");
            boolean status = driver.getCurrentUrl().contains("view_cart") || driver.getPageSource().contains("Shopping Cart");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: Navigated to the cart page.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: Failed to navigate to the cart page.");
            }
            
            logResult("TC_Signup_Login_17 - Cart Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_17 - Cart Icon Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 18)
    public void TC_Signup_Login_18_TestCasesIconFunctionality() throws InterruptedException {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the 'Test Cases' link.");
            driver.findElement(SignupLoginPage.TEST_CASES_LINK).click();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Waiting for the URL to contain 'test_cases'.");
            boolean status = wait.until(driver -> driver.getCurrentUrl().contains("test_cases"));
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The URL contains 'test_cases'.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The URL does not contain 'test_cases'. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_18_TestCasesIconFunctionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_18_TestCasesIconFunctionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 19)
    public void TC_Signup_Login_19_APITestingIconFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the 'API Testing' link.");
            driver.findElement(SignupLoginPage.API_TESTING_LINK).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the URL contains 'api_list'.");
            boolean status = driver.getCurrentUrl().contains("api_list");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The URL contains 'api_list'.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The URL does not contain 'api_list'. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_19 - API Testing Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_19 - API Testing Icon Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 20)
    public void TC_Signup_Login_20_VideoTutorialsIconFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the 'Video Tutorials' link.");
            driver.findElement(SignupLoginPage.VIDEO_TUTORIALS_LINK).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the URL or title contains 'youtube' or 'video'.");
            boolean status = driver.getCurrentUrl().contains("youtube") || driver.getTitle().toLowerCase().contains("video") || driver.getCurrentUrl().contains("video");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: Navigated to a page related to video tutorials.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: Failed to navigate to a page related to video tutorials. Actual URL: " + driver.getCurrentUrl() + " and Title: " + driver.getTitle());
            }
            
            logResult("TC_Signup_Login_20 - Video Tutorials Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_20 - Video Tutorials Icon Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 21)
    public void TC_Signup_Login_21_ContactUsIconFunctionality() throws InterruptedException {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the 'Contact Us' link.");
            driver.findElement(SignupLoginPage.CONTACT_US_LINK).click();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Waiting for the URL to contain 'contact_us'.");
            boolean status = wait.until(driver -> driver.getCurrentUrl().contains("contact_us"));
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The URL contains 'contact_us'.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The URL does not contain 'contact_us'. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_21_ContactUsIconFunctionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_21_ContactUsIconFunctionality", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 25)
    public void TC_Signup_Login_25_PasswordFieldMasked() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Locating the login password field.");
            WebElement loginPasswordField = driver.findElement(SignupLoginPage.LOGIN_PASSWORD_FIELD);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Entering 'TestPassword123' into the password field.");
            loginPasswordField.clear();
            loginPasswordField.sendKeys("TestPassword123");
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 3: Checking the 'type' attribute of the password field to verify if it's masked.");
            String inputType = loginPasswordField.getAttribute("type");
            boolean status = "password".equals(inputType);
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The password field is masked as expected (type='password').");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The password field is not masked. Actual type: " + inputType);
            }
            
            logResult("TC_Signup_Login_25 - Password Field Masked", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_25 - Password Field Masked", false);
            Assert.fail(e.getMessage());
        }
    }

    

    @Test(priority = 27)
    public void TC_Signup_Login_27_AutomationExerciseLogoFunctionality() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the website logo.");
            driver.findElement(SignupLoginPage.WEBSITE_LOGO).click();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying if the URL is the home URL.");
            boolean status = driver.getCurrentUrl().equals(homeUrl);
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: Clicking the logo navigated back to the home page.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: Clicking the logo did not navigate back to the home page. Actual URL: " + driver.getCurrentUrl());
            }
            
            logResult("TC_Signup_Login_27 - Automation Exercise Logo Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_27 - Automation Exercise Logo Functionality", false);
            Assert.fail(e.getMessage());
        }
    }

    

    @Test(priority = 29)
    public void TC_Signup_Login_29_LoginSignupPageLoads() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to the home page.");
            driver.get(homeUrl);
            Thread.sleep(1000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Clicking the 'Signup / Login' link.");
            signupLoginPage.clickSignupLoginLink();
            Thread.sleep(2000);
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 2: Verifying that the URL contains 'login' and the page contains expected text.");
            boolean status = driver.getCurrentUrl().contains("login") && (driver.getPageSource().contains("Login to your account") || driver.getPageSource().contains("New User Signup"));
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The Login/Signup page loaded correctly.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The Login/Signup page did not load as expected.");
            }
            
            logResult("TC_Signup_Login_29 - Login Signup Page Loads", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_29 - Login Signup Page Loads", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 30)
    public void TC_Signup_Login_30_LoginAccountSectionPresence() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Verifying the presence of the 'Login to your account' section.");
            boolean status = driver.getPageSource().contains("Login to your account");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: 'Login to your account' section is present on the page.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: 'Login to your account' section is not present on the page.");
            }
            
            logResult("TC_Signup_Login_30 - Login Account Section Presence", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_30 - Login Account Section Presence", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 31)
    public void TC_Signup_Login_31_NewUserSignupSectionPresence() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Verifying the presence of the 'New User Signup!' section.");
            boolean status = driver.getPageSource().contains("New User Signup!");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: 'New User Signup!' section is present on the page.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: 'New User Signup!' section is not present on the page.");
            }
            
            logResult("TC_Signup_Login_31 - New User Signup Section Presence", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_31 - New User Signup Section Presence", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 32)
    public void TC_Signup_Login_32_ORSeparatorPresence() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Pre-condition: Navigating to Signup/Login page.");
            navigateToSignupLogin();
            
            ExtentReportManager.getTest().log(Status.INFO, "Step 1: Verifying the presence of the 'OR' separator.");
            boolean status = driver.getPageSource().contains("OR");
            
            if (status) {
                ExtentReportManager.getTest().log(Status.PASS, "Verification successful: The 'OR' separator is present on the page.");
            } else {
                ExtentReportManager.getTest().log(Status.FAIL, "Verification failed: The 'OR' separator is not present on the page.");
            }
            
            logResult("TC_Signup_Login_32 - OR Separator Presence", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test failed due to an exception: " + e.getMessage());
            logResult("TC_Signup_Login_32 - OR Separator Presence", false);
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