package com.functional.tests;


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
import com.functional.listeners.LoginExtentTestListener;
import java.time.Duration;
import java.util.List;

@Listeners({LoginExtentTestListener.class})
public class LoginTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    String baseUrl = "https://automationexercise.com/";
    String loginUrl = "https://automationexercise.com/login";

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("edge") String browser) {
        try {
            System.out.println("Setting up test environment with browser: " + browser);
            
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("brave")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);
            
            driver.get(baseUrl);
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

    private void navigateToLogin() {
        driver.get(loginUrl);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    private void fillLoginForm(String email, String password) {
        WebElement emailField = findElementSafely(By.xpath("//input[@data-qa='login-email']"));
        WebElement passwordField = findElementSafely(By.xpath("//input[@data-qa='login-password']"));
        
        if (emailField != null && passwordField != null) {
            emailField.clear();
            emailField.sendKeys(email);
            passwordField.clear();
            passwordField.sendKeys(password);
        }
    }
    // Add this method to your LoginTestSuite.java class
private WebElement findLogoutButton() {
    String[] logoutXPaths = {
        "//a[contains(text(),'Logout')]",
        "//a[text()='Logout']",
        "//a[@href='/logout']",
        "//li//a[contains(text(),'Logout')]",
        "//nav//a[contains(text(),'Logout')]",
        "//header//a[contains(text(),'Logout')]",
        "//ul[@class='navbar-nav']//a[contains(text(),'Logout')]"
    };
    
    for (String xpath : logoutXPaths) {
        try {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            for (WebElement element : elements) {
                if (element.isDisplayed() && element.isEnabled()) {
                    return element;
                }
            }
        } catch (Exception e) {
            // Continue to next locator
        }
    }
    return null;
}
    private void logoutIfLoggedIn() {
        try {
            // Check if user is logged in by looking for "Logged in as" text or Logout button
            if (driver.getPageSource().contains("Logged in as") || 
                driver.getPageSource().contains("Logout")) {
                
                // Find logout button using a more direct approach
                List<WebElement> logoutButtons = driver.findElements(By.xpath("//a[contains(text(),'Logout')]"));
                
                if (!logoutButtons.isEmpty()) {
                    WebElement logoutBtn = logoutButtons.get(0);
                    scrollToElement(logoutBtn);
                    logoutBtn.click();
                    Thread.sleep(2000);
                    System.out.println("User logged out successfully");
                } else {
                    System.out.println("Logout button not found, but user appears to be logged in");
                }
            }
        } catch (Exception e) {
            System.out.println("Logout attempt failed: " + e.getMessage());
            // Force navigate to login page as fallback
            driver.get(loginUrl);
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
    }

    @Test(priority = 1)
    public void TC_ECOM_Login_001_ValidCredentials() {
        try {
            logoutIfLoggedIn(); // Ensure clean state
            navigateToLogin();
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                boolean status = driver.getCurrentUrl().equals(baseUrl) || 
                               driver.getPageSource().contains("Logged in as") ||
                               !driver.getCurrentUrl().contains("login");
                
                logResult("TC_ECOM_Login_001 - Valid Credentials", status);
                
                // Logout after successful login
                if (status) {
                    logoutIfLoggedIn();
                }
                
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_001 - Valid Credentials", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_001 - Valid Credentials", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 2)
public void TC_ECOM_Login_002_InvalidCredentials() {
    try {
        logoutIfLoggedIn();
        navigateToLogin();
        fillLoginForm("keerthanashetty0024", "abc@1"); // Invalid email format and wrong password
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        if (loginBtn != null) {
            loginBtn.click();
            Thread.sleep(3000); // Increased wait time
            
            // More comprehensive error detection
            boolean loginFailed = driver.getCurrentUrl().contains("login") ||
                                driver.getPageSource().contains("Your email or password is incorrect!") ||
                                driver.getPageSource().contains("incorrect") ||
                                driver.getPageSource().contains("error") ||
                                driver.getPageSource().contains("invalid") ||
                                !driver.getPageSource().contains("Logged in as");
            
            logResult("TC_ECOM_Login_002 - Invalid Credentials", loginFailed);
            Assert.assertTrue(loginFailed, "Should show error for invalid credentials");
        } else {
            logResult("TC_ECOM_Login_002 - Invalid Credentials", false);
            Assert.fail("Login button not found");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_002 - Invalid Credentials", false);
        Assert.fail(e.getMessage());
    }
}

    @Test(priority = 3)
    public void TC_ECOM_Login_003_BlankEmailField() {
        try {
            navigateToLogin();
            fillLoginForm("", "abc@2");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(1000);
                
                WebElement emailField = findElementSafely(By.xpath("//input[@data-qa='login-email']"));
                String validationMessage = emailField.getAttribute("validationMessage");
                
                boolean status = validationMessage != null && !validationMessage.isEmpty();
                logResult("TC_ECOM_Login_003 - Blank Email Field", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_003 - Blank Email Field", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_003 - Blank Email Field", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 4)
    public void TC_ECOM_Login_004_BlankPasswordField() {
        try {
            navigateToLogin();
            fillLoginForm("keerthanashetty0024@gmail.com", "");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(1000);
                
                WebElement passwordField = findElementSafely(By.xpath("//input[@data-qa='login-password']"));
                String validationMessage = passwordField.getAttribute("validationMessage");
                
                boolean status = validationMessage != null && !validationMessage.isEmpty();
                logResult("TC_ECOM_Login_004 - Blank Password Field", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_004 - Blank Password Field", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_004 - Blank Password Field", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 5)
    public void TC_ECOM_Login_005_BothFieldsBlank() {
        try {
            logoutIfLoggedIn(); // Ensure logged out state first
            navigateToLogin();
            fillLoginForm("", "");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(1000);
                
                WebElement emailField = findElementSafely(By.xpath("//input[@data-qa='login-email']"));
                String validationMessage = emailField.getAttribute("validationMessage");
                
                boolean status = validationMessage != null && !validationMessage.isEmpty();
                logResult("TC_ECOM_Login_005 - Both Fields Blank", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_005 - Both Fields Blank", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_005 - Both Fields Blank", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 6)
    public void TC_ECOM_Login_006_PasswordFieldMasked() {
        try {
            navigateToLogin();
            
            WebElement passwordField = findElementSafely(By.xpath("//input[@data-qa='login-password']"));
            if (passwordField != null) {
                passwordField.sendKeys("abc@123");
                
                String inputType = passwordField.getAttribute("type");
                boolean status = "password".equals(inputType);
                
                logResult("TC_ECOM_Login_006 - Password Field Masked", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_006 - Password Field Masked", false);
                Assert.fail("Password field not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_006 - Password Field Masked", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 7)
    public void TC_ECOM_Login_007_ValidEmailWrongPassword() {
        try {
            navigateToLogin();
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@12");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                boolean status = driver.getCurrentUrl().contains("login") && 
                               (driver.getPageSource().contains("incorrect") ||
                                driver.getPageSource().contains("error"));
                
                logResult("TC_ECOM_Login_007 - Valid Email Wrong Password", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_007 - Valid Email Wrong Password", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_007 - Valid Email Wrong Password", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 8)
    public void TC_ECOM_Login_008_InvalidEmailRightPassword() {
        try {
            navigateToLogin();
            fillLoginForm("keerthanashetty024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                boolean status = driver.getCurrentUrl().contains("login") && 
                               (driver.getPageSource().contains("incorrect") ||
                                driver.getPageSource().contains("error"));
                
                logResult("TC_ECOM_Login_008 - Invalid Email Right Password", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_008 - Invalid Email Right Password", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_008 - Invalid Email Right Password", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 9)
    public void TC_ECOM_Login_009_ErrorMessageDisappears() {
        try {
            navigateToLogin();
            
            // First trigger error with blank field
            fillLoginForm("", "");
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            loginBtn.click();
            Thread.sleep(1000);
            
            // Then enter valid data
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
            loginBtn.click();
            Thread.sleep(2000);
            
            boolean status = !driver.getCurrentUrl().contains("login") ||
                           !driver.getPageSource().contains("error");
            
            logResult("TC_ECOM_Login_009 - Error Message Disappears", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_ECOM_Login_009 - Error Message Disappears", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 10)
    public void TC_ECOM_Login_010_EmailFormatValidation() {
        try {
        	logoutIfLoggedIn();
            navigateToLogin();
            fillLoginForm("keerthanashetty024gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(1000);
                
                WebElement emailField = findElementSafely(By.xpath("//input[@data-qa='login-email']"));
                String validationMessage = emailField.getAttribute("validationMessage");
                
                boolean status = validationMessage != null && validationMessage.contains("@");
                logResult("TC_ECOM_Login_010 - Email Format Validation", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_ECOM_Login_010 - Email Format Validation", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_010 - Email Format Validation", false);
            Assert.fail(e.getMessage());
        }
    }

   @Test(priority = 11)
public void TC_ECOM_Login_011_MaximumLengthValidation() {
    try {
        logoutIfLoggedIn(); // Ensure clean state
        navigateToLogin();
        
        // Create very long email and password (>100 characters)
        StringBuilder longEmailBuilder = new StringBuilder();
        StringBuilder longPasswordBuilder = new StringBuilder();
        
        for (int i = 0; i < 95; i++) {
            longEmailBuilder.append("a");
            longPasswordBuilder.append("b");
        }
        String longEmail = longEmailBuilder.toString() + "@example.com";
        String longPassword = longPasswordBuilder.toString();
        
        System.out.println("Testing with email length: " + longEmail.length());
        System.out.println("Testing with password length: " + longPassword.length());
        
        fillLoginForm(longEmail, longPassword);
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        if (loginBtn != null) {
            loginBtn.click();
            Thread.sleep(3000);
            
            // Expected: Should display error message
            // Actual: Website accepts long input (doesn't work as expected)
            boolean errorDisplayed = driver.getCurrentUrl().contains("login") &&
                                   (driver.getPageSource().contains("error") ||
                                    driver.getPageSource().contains("invalid") ||
                                    driver.getPageSource().contains("too long") ||
                                    driver.getPageSource().contains("maximum"));
            
            // Check if long input was incorrectly accepted
            boolean longInputAccepted = !driver.getCurrentUrl().contains("login") ||
                                      driver.getPageSource().contains("Logged in as");
            
            logResult("TC_ECOM_Login_011 - Maximum Length Validation", errorDisplayed);
            
            System.out.println("Debug - Error displayed: " + errorDisplayed);
            System.out.println("Debug - Long input accepted: " + longInputAccepted);
            System.out.println("Debug - Current URL: " + driver.getCurrentUrl());
            
            if (errorDisplayed) {
                Assert.assertTrue(true, "EXPECTED TO FAIL BUT PASSED: Length validation is working properly");
            } else {
                // This is the expected behavior - test should fail because validation doesn't work
                Assert.fail("EXPECTED FAIL: Long email/password (>100 chars) should show error but was accepted - length validation is missing");
            }
        } else {
            logResult("TC_ECOM_Login_011 - Maximum Length Validation", false);
            Assert.fail("Login button not found");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_011 - Maximum Length Validation", false);
        Assert.fail("Test failed with exception: " + e.getMessage());
    }
}

@Test(priority = 12)
public void TC_ECOM_Login_012_MixedCaseEmail() {
    try {
        logoutIfLoggedIn(); // Ensure clean state
        navigateToLogin();
        fillLoginForm("KEERTHANASHETTY0024@GMAIL.COM", "abc@123");
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        if (loginBtn != null) {
            loginBtn.click();
            Thread.sleep(3000);
            
            // Check if login succeeded
            boolean loginSucceeded = !driver.getCurrentUrl().contains("login") &&
                                   (driver.getPageSource().contains("Logged in as") ||
                                    driver.getCurrentUrl().equals(baseUrl));
            
            // Debug information
            System.out.println("Debug - Login succeeded: " + loginSucceeded);
            System.out.println("Debug - Current URL: " + driver.getCurrentUrl());
            System.out.println("Debug - Page contains 'Logged in as': " + driver.getPageSource().contains("Logged in as"));
            
            logResult("TC_ECOM_Login_012 - Mixed Case Email", loginSucceeded);
            
            if (loginSucceeded) {
                // Clean up by logging out
                logoutIfLoggedIn();
                Assert.assertTrue(true, "EXPECTED TO FAIL BUT PASSED: Mixed case email worked unexpectedly");
            } else {
                // This is the actual behavior - mixed case email doesn't work
                // Expected: Should work, Actual: Doesn't work, Status: Fail
                Assert.fail("EXPECTED TO PASS BUT FAILED: Mixed case email should work but was rejected");
            }
        } else {
            logResult("TC_ECOM_Login_012 - Mixed Case Email", false);
            Assert.fail("Login button not found");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_012 - Mixed Case Email", false);
        Assert.fail("Test failed with exception: " + e.getMessage());
    }
}
    @Test(priority = 13)
    public void TC_ECOM_Login_013_RedirectToHomePage() {
        try {
            logoutIfLoggedIn(); // Ensure clean state
            navigateToLogin();
            
            // Use the CORRECT email address (with 0024, not 024)
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(3000); // Increased wait time for redirect
                
                // More flexible URL checking - check if redirected to home page
                String currentUrl = driver.getCurrentUrl();
                boolean status = currentUrl.equals(baseUrl) || 
                            currentUrl.equals(baseUrl.replaceAll("/$", "")) || // without trailing slash
                            currentUrl.startsWith(baseUrl) && 
                            (currentUrl.equals(baseUrl) || currentUrl.equals(baseUrl + "#") || 
                                !currentUrl.contains("login"));
                
                // Additional check - verify login was successful
                boolean loginSuccessful = driver.getPageSource().contains("Logged in as") || 
                                        !driver.getCurrentUrl().contains("login");
                
                // Both conditions should be true for a successful redirect
                boolean finalStatus = status && loginSuccessful;
                
                logResult("TC_ECOM_Login_013 - Redirect To Home Page", finalStatus);
                
                // Debug information
                System.out.println("Current URL: " + currentUrl);
                System.out.println("Base URL: " + baseUrl);
                System.out.println("URL Status: " + status);
                System.out.println("Login Successful: " + loginSuccessful);
                
                // Logout if login was successful
                if (loginSuccessful) {
                    logoutIfLoggedIn();
                }
                
                Assert.assertTrue(finalStatus, "Login should redirect to home page. Current URL: " + currentUrl);
            } else {
                logResult("TC_ECOM_Login_013 - Redirect To Home Page", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_013 - Redirect To Home Page", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 14)
    public void TC_ECOM_Login_014_InvalidCredentialsError() {
        try {
            logoutIfLoggedIn(); // Ensure clean state
            navigateToLogin();
            
            // Use a properly formatted email that doesn't exist to test server-side validation
            fillLoginForm("keerthanashetty024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                // Check if login failed (user should remain on login page or see error)
                boolean loginFailed = driver.getCurrentUrl().contains("login") &&
                                    (driver.getPageSource().contains("error") ||
                                    driver.getPageSource().contains("incorrect") ||
                                    driver.getPageSource().contains("invalid") ||
                                    driver.getPageSource().contains("Your email or password is incorrect!") ||
                                    !driver.getPageSource().contains("Logged in as"));
                
                logResult("TC_ECOM_Login_014 - Invalid Credentials Error", loginFailed);
                
                // Debug information to see what's happening
                System.out.println("Current URL: " + driver.getCurrentUrl());
                System.out.println("Page contains 'error': " + driver.getPageSource().contains("error"));
                System.out.println("Page contains 'incorrect': " + driver.getPageSource().contains("incorrect"));
                System.out.println("Page contains 'Logged in as': " + driver.getPageSource().contains("Logged in as"));
                
                if (loginFailed) {
                    Assert.assertTrue(true, "PASS: Invalid credentials properly rejected");
                } else {
                    Assert.fail("FAIL: Invalid credentials were accepted - authentication validation missing");
                }
            } else {
                logResult("TC_ECOM_Login_014 - Invalid Credentials Error", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_014 - Invalid Credentials Error", false);
            Assert.fail(e.getMessage());
        }
    }
    @Test(priority = 15)
public void TC_ECOM_Login_015_AccountDeletion() {
    try {
        logoutIfLoggedIn();
        
        // First login to access delete account functionality
        navigateToLogin();
        fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        loginBtn.click();
        Thread.sleep(3000);
        
        // Verify login successful
        boolean loginSuccessful = driver.getPageSource().contains("Logged in as");
        
        if (loginSuccessful) {
            // Look for Delete Account button
            String[] deleteAccountXPaths = {
                "//a[contains(text(),'Delete Account')]",
                "//a[@href='/delete_account']",
                "//li//a[contains(text(),'Delete')]",
                "//nav//a[contains(text(),'Delete Account')]"
            };
            
            WebElement deleteBtn = null;
            for (String xpath : deleteAccountXPaths) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            deleteBtn = element;
                            break;
                        }
                    }
                    if (deleteBtn != null) break;
                } catch (Exception e) {
                    // Continue to next locator
                }
            }
            
            if (deleteBtn != null) {
                scrollToElement(deleteBtn);
                deleteBtn.click();
                Thread.sleep(3000);
                
                // Check if account deletion was successful
                boolean accountDeleted = driver.getCurrentUrl().contains("delete") ||
                                       driver.getPageSource().contains("Account Deleted") ||
                                       driver.getPageSource().contains("deleted successfully");
                
                logResult("TC_ECOM_Login_015 - Account Deletion", accountDeleted);
                Assert.assertTrue(accountDeleted, "Account should be deleted successfully");
            } else {
                logResult("TC_ECOM_Login_015 - Account Deletion", false);
                Assert.fail("Delete Account button not found");
            }
        } else {
            logResult("TC_ECOM_Login_015 - Account Deletion", false);
            Assert.fail("Login failed - cannot test account deletion");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_015 - Account Deletion", false);
        Assert.fail(e.getMessage());
    }
}

@Test(priority = 16, dependsOnMethods = {"TC_ECOM_Login_015_AccountDeletion"})
public void TC_ECOM_Login_016_DeletedAccountLogin() {
    try {
        navigateToLogin();
        
        // Use deleted account credentials (note: missing @ in email as per table)
        fillLoginForm("keerthanashetty024gmail.com", "abc@123");
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        if (loginBtn != null) {
            loginBtn.click();
            Thread.sleep(3000);
            
            // Should display error message for deleted account
            boolean errorDisplayed = driver.getCurrentUrl().contains("login") &&
                                   (driver.getPageSource().contains("error") ||
                                    driver.getPageSource().contains("incorrect") ||
                                    driver.getPageSource().contains("not found") ||
                                    driver.getPageSource().contains("invalid") ||
                                    !driver.getPageSource().contains("Logged in as"));
            
            logResult("TC_ECOM_Login_016 - Deleted Account Login", errorDisplayed);
            Assert.assertTrue(errorDisplayed, "Should display error for deleted account credentials");
        } else {
            logResult("TC_ECOM_Login_016 - Deleted Account Login", false);
            Assert.fail("Login button not found");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_016 - Deleted Account Login", false);
        Assert.fail(e.getMessage());
    }
}

    @Test(priority = 17)
    public void TC_ECOM_Login_017_PasswordCaseSensitivity() {
        try {
            navigateToLogin();
            // Try login with password in different case
            fillLoginForm("keerthanashetty0024@gmail.com", "ABC@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                // Should fail if password is case-sensitive
                boolean loginFailed = driver.getCurrentUrl().contains("login") &&
                                     driver.getPageSource().contains("incorrect");
                
                logResult("TC_ECOM_Login_017 - Password Case Sensitivity", loginFailed);
                Assert.assertTrue(loginFailed);
            } else {
                logResult("TC_ECOM_Login_017 - Password Case Sensitivity", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_017 - Password Case Sensitivity", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 18)
    public void TC_ECOM_Login_018_BrowserBackAfterLogout() {
        try {
            logoutIfLoggedIn(); // Ensure clean state
            
            // First login
            navigateToLogin();
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            loginBtn.click();
            Thread.sleep(2000);
            
            // Then logout
            WebElement logoutBtn = findElementSafely(By.xpath("//a[contains(text(),'Logout')]"));
            if (logoutBtn != null) {
                logoutBtn.click();
                Thread.sleep(2000);
                
                // Try browser back button
                driver.navigate().back();
                Thread.sleep(2000);
                
                // Should not return to authenticated page (this test expects FAIL)
                boolean backButtonSecurityWorks = driver.getCurrentUrl().contains("login") ||
                                                 !driver.getPageSource().contains("Logged in as");
                
                logResult("TC_ECOM_Login_018 - Browser Back After Logout", backButtonSecurityWorks);
                
                if (!backButtonSecurityWorks) {
                    Assert.fail("BUG: User can access authenticated pages after logout using back button");
                } else {
                    Assert.assertTrue(true, "Back button security working correctly");
                }
            } else {
                logResult("TC_ECOM_Login_018 - Browser Back After Logout", false);
                Assert.fail("Logout button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_018 - Browser Back After Logout", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 19)
    public void TC_ECOM_Login_019_RedirectToLastVisitedPage() {
        try {
            logoutIfLoggedIn(); // Ensure clean state
            
            // Visit a specific page first
            driver.get(baseUrl + "products");
            Thread.sleep(1000);
            
            // Then login
            navigateToLogin();
            fillLoginForm("keerthanashetty0024@gmail.com", "abc@123");
            
            WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
            if (loginBtn != null) {
                loginBtn.click();
                Thread.sleep(2000);
                
                // Check if redirected to products page (this test expects FAIL)
                boolean redirectedToLastPage = driver.getCurrentUrl().contains("products");
                
                logResult("TC_ECOM_Login_019 - Redirect To Last Visited Page", redirectedToLastPage);
                
                // Clean up
                logoutIfLoggedIn();
                
                if (!redirectedToLastPage) {
                    Assert.fail("BUG: User not redirected to last visited page after login");
                } else {
                    Assert.assertTrue(true, "User correctly redirected to last visited page");
                }
            } else {
                logResult("TC_ECOM_Login_019 - Redirect To Last Visited Page", false);
                Assert.fail("Login button not found");
            }
        } catch (Exception e) {
            logResult("TC_ECOM_Login_019 - Redirect To Last Visited Page", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 20)
public void TC_ECOM_Login_020_SessionPersistence() {
    try {
        String browserType = "chrome"; // Default browser type
        
        // Login first
        logoutIfLoggedIn(); // Ensure clean state
        navigateToLogin();
        fillLoginForm("keerthanashetty124@gmail.com", "abc@123");
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        loginBtn.click();
        Thread.sleep(3000);
        
        // Check if login was successful before closing browser
        boolean loginSuccessful = driver.getPageSource().contains("Logged in as") ||
                                !driver.getCurrentUrl().contains("login");
        
        // Remember which browser we're using before quitting
        if (driver instanceof ChromeDriver) {
            browserType = "chrome";
        } else if (driver instanceof EdgeDriver) {
            browserType = "edge";
        } else {
            browserType = "firefox";
        }
        
        if (loginSuccessful) {
            System.out.println("Initial login successful, testing session persistence...");
            
            // Close browser completely
            driver.quit();
            Thread.sleep(2000);
            
            // Reinitialize browser based on the type we were using
            if (browserType.equals("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (browserType.equals("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Navigate to home page
            driver.get(baseUrl);
            Thread.sleep(3000);
            
            // Check if still logged in
            boolean stillLoggedIn = driver.getPageSource().contains("Logged in as");
            
            // Debug information
            System.out.println("After browser restart:");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page contains 'Logged in as': " + stillLoggedIn);
            System.out.println("Page contains 'Login': " + driver.getPageSource().contains("Login"));
            
            logResult("TC_ECOM_Login_020 - Session Persistence", stillLoggedIn);
            
            if (stillLoggedIn) {
                Assert.assertTrue(true, "PASS: Session persisted across browser restart");
                // Clean up by logging out
                logoutIfLoggedIn();
            } else {
                // This might be expected behavior - many sites don't persist sessions across browser restarts
                Assert.fail("FAIL: Session did not persist across browser restart. Note: This might be expected behavior for security reasons.");
            }
        } else {
            logResult("TC_ECOM_Login_020 - Session Persistence", false);
            Assert.fail("Initial login was not successful");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_020 - Session Persistence", false);
        System.out.println("Error details: " + e.getMessage());
        e.printStackTrace();
        Assert.fail(e.getMessage());
    }
}

    @Test(priority = 21)
    public void TC_ECOM_Login_021_VerifySuccessfulLogoutOperation() throws InterruptedException {
        navigateToLogin();
        fillLoginForm("keerthanashetty124@gmail.com", "abc@123");

        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        loginBtn.click();
        Thread.sleep(2000);

        WebElement logoutBtn = findLogoutButton();
        Assert.assertNotNull(logoutBtn, "Logout button not found.");
        logoutBtn.click();
        Thread.sleep(2000);

        boolean onLoginPage = driver.getCurrentUrl().contains("login")
                && !driver.getPageSource().contains("Logged in as");

        logResult("TC_ECOM_Login_021 - Verify Successful Logout", onLoginPage);
        Assert.assertTrue(onLoginPage, "User should be redirected to login page after logout.");
    }

    @Test(priority = 22)
    public void TC_ECOM_Login_022_VerifyLogoutButtonNotVisibleWhenLoggedOut() throws InterruptedException {
        logoutIfLoggedIn(); // Ensure logged-out state
        navigateToLogin();

        // Look for logout button
        List<WebElement> logoutBtns = driver.findElements(By.xpath("//a[contains(text(),'Logout')]"));
        boolean logoutVisible = logoutBtns.stream().anyMatch(WebElement::isDisplayed);

        // Login/Signup button should be visible instead
        List<WebElement> loginSignupBtns = driver.findElements(By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]"));
        boolean loginVisible = loginSignupBtns.stream().anyMatch(WebElement::isDisplayed);

        boolean status = !logoutVisible && loginVisible;
        logResult("TC_ECOM_Login_022 - Logout Button Not Visible When Logged Out", status);
        Assert.assertTrue(status, "Logout button should not be visible when logged out.");
    }

    @Test(priority = 23)
    public void TC_ECOM_Login_023_VerifyLogoutButtonRapidClicks() throws InterruptedException {
        navigateToLogin();
        fillLoginForm("keerthanashetty124@gmail.com", "abc@123");

        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        loginBtn.click();
        Thread.sleep(2000);

        WebElement logoutBtn = findLogoutButton();
        Assert.assertNotNull(logoutBtn, "Logout button not found.");

        // Rapid clicks
        for (int i = 0; i < 3; i++) {
            try {
                logoutBtn.click();
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }

        boolean loggedOut = driver.getCurrentUrl().contains("login")
                && !driver.getPageSource().contains("Logged in as");

        logResult("TC_ECOM_Login_023 - Logout Button Rapid Clicks", loggedOut);
        Assert.assertTrue(loggedOut, "User should be logged out without errors on rapid clicks.");
    }
@Test(priority = 24)
public void TC_ECOM_Login_024_LoginPromptAfterLogout() {
    try {
        logoutIfLoggedIn(); // Ensure clean state
        
        // First login
        navigateToLogin();
        fillLoginForm("keerthanashetty124@gmail.com", "abc@123");
        
        WebElement loginBtn = findElementSafely(By.xpath("//button[@data-qa='login-button']"));
        if (loginBtn != null) {
            loginBtn.click();
            Thread.sleep(3000);
            
            // Verify login was successful
            boolean loginSuccessful = driver.getPageSource().contains("Logged in as") ||
                                    !driver.getCurrentUrl().contains("login");
            
            if (!loginSuccessful) {
                logResult("TC_ECOM_Login_024 - Login Prompt After Logout", false);
                Assert.fail("Initial login failed");
                return;
            }
            
            // Then logout using enhanced logout finder
            WebElement logoutBtn = findLogoutButton();
            
            if (logoutBtn != null) {
                scrollToElement(logoutBtn);
                logoutBtn.click();
                Thread.sleep(3000);
                
                // Check that login/signup button appears after logout
                String[] loginSignupXPaths = {
                    "//a[contains(text(),'Signup') or contains(text(),'Login')]",
                    "//a[contains(text(),'Sign up')]",
                    "//a[text()='Signup / Login']",
                    "//a[@href='/login']",
                    "//nav//a[contains(text(),'Login')]",
                    "//header//a[contains(text(),'Login')]"
                };
                
                boolean loginPromptVisible = false;
                WebElement loginSignupBtn = null;
                
                for (String xpath : loginSignupXPaths) {
                    try {
                        List<WebElement> elements = driver.findElements(By.xpath(xpath));
                        for (WebElement element : elements) {
                            if (element.isDisplayed()) {
                                loginSignupBtn = element;
                                loginPromptVisible = true;
                                break;
                            }
                        }
                        if (loginPromptVisible) break;
                    } catch (Exception e) {
                        // Continue to next locator
                    }
                }
                
                // Additional check - verify we're actually logged out
                boolean loggedOut = driver.getCurrentUrl().contains("login") ||
                                  !driver.getPageSource().contains("Logged in as");
                
                boolean status = loginPromptVisible && loggedOut;
                
                // Debug information
                System.out.println("Login prompt visible: " + loginPromptVisible);
                System.out.println("Logged out: " + loggedOut);
                System.out.println("Current URL: " + driver.getCurrentUrl());
                
                if (!loginPromptVisible) {
                    System.out.println("Available navigation links:");
                    List<WebElement> navLinks = driver.findElements(By.xpath("//nav//a | //header//a"));
                    for (WebElement link : navLinks) {
                        try {
                            if (link.isDisplayed() && !link.getText().trim().isEmpty()) {
                                System.out.println("Nav link: " + link.getText().trim());
                            }
                        } catch (Exception e) {
                            // Skip problematic elements
                        }
                    }
                }
                
                logResult("TC_ECOM_Login_024 - Login Prompt After Logout", status);
                
                if (status) {
                    Assert.assertTrue(true, "Login/signup prompt appears after logout as expected");
                } else {
                    Assert.fail("Login/signup prompt not visible after logout");
                }
            } else {
                logResult("TC_ECOM_Login_024 - Login Prompt After Logout", false);
                Assert.fail("Logout button not found after successful login");
            }
        } else {
            logResult("TC_ECOM_Login_024 - Login Prompt After Logout", false);
            Assert.fail("Login button not found");
        }
    } catch (Exception e) {
        logResult("TC_ECOM_Login_024 - Login Prompt After Logout", false);
        Assert.fail("Test failed with exception: " + e.getMessage());
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