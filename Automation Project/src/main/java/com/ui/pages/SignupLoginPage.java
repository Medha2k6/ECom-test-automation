package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignupLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URLs
    private static final String HOME_URL = "https://automationexercise.com/";
    private static final String SIGNUP_LOGIN_URL = "https://automationexercise.com/login";

    // Header Links
    public static final By HOME_LINK = By.xpath("//a[contains(text(),'Home')]");
    public static final By PRODUCTS_LINK = By.xpath("//a[contains(text(),'Products')]");
    public static final By CART_LINK = By.xpath("//a[contains(text(),'Cart')]");
    public static final By SIGNUP_LOGIN_LINK = By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]");
    public static final By TEST_CASES_LINK = By.xpath("//a[contains(text(),'Test Cases')]");
    public static final By API_TESTING_LINK = By.xpath("//a[contains(text(),'API Testing')]");
    public static final By VIDEO_TUTORIALS_LINK = By.xpath("//a[contains(text(),'Video Tutorials')]");
    public static final By CONTACT_US_LINK = By.xpath("//a[contains(text(),'Contact us')]");
    public static final By WEBSITE_LOGO = By.xpath("//img[@alt='Website for automation practice']");
    public static final By LOGOUT_LINK = By.xpath("//a[contains(text(),'Logout')]");

    // Signup Form Locators
    public static final By NEW_USER_SIGNUP_HEADER = By.xpath("//h2[contains(text(),'New User Signup!')]");
    public static final By SIGNUP_NAME_FIELD = By.name("name");
    public static final By SIGNUP_EMAIL_FIELD = By.xpath("//input[@data-qa='signup-email']");
    public static final By SIGNUP_BUTTON = By.xpath("//button[@data-qa='signup-button']");
    public static final By SIGNUP_SUCCESS_MESSAGE = By.xpath("//b[contains(text(),'Account Created!')]");
    public static final By SIGNUP_DUPLICATE_EMAIL_ERROR = By.xpath("//p[contains(text(),'Email Address already exist!')]");
    public static final By ENTER_ACCOUNT_INFO_TEXT = By.xpath("//b[contains(text(),'ENTER ACCOUNT INFORMATION')]");

    // Login Form Locators
    public static final By LOGIN_TO_YOUR_ACCOUNT_HEADER = By.xpath("//h2[contains(text(),'Login to your account')]");
    public static final By LOGIN_EMAIL_FIELD = By.xpath("//input[@data-qa='login-email']");
    public static final By LOGIN_PASSWORD_FIELD = By.xpath("//input[@data-qa='login-password']");
    public static final By LOGIN_BUTTON = By.xpath("//button[@data-qa='login-button']");
    public static final By LOGIN_INCORRECT_MESSAGE = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    // Other Page Locators
    public static final By OR_SEPARATOR = By.xpath("//*[contains(text(),'OR')]");
    public static final By SUBSCRIPTION_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By GO_TO_TOP_BUTTON = By.id("scrollUp");
    
    public SignupLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigates to the signup/login page.
     */
    public void navigateToSignupLoginPage() {
        driver.get(SIGNUP_LOGIN_URL);
        wait.until(ExpectedConditions.urlContains("login"));
    }

    /**
     * Clicks on the 'Signup / Login' link in the header.
     */
    public void clickSignupLoginLink() {
        WebElement signupLoginLink = wait.until(ExpectedConditions.elementToBeClickable(SIGNUP_LOGIN_LINK));
        signupLoginLink.click();
    }

    /**
     * Fills and submits the signup form.
     * @param name The name to use for signup.
     * @param email The email to use for signup.
     */
    public void signup(String name, String email) {
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(SIGNUP_NAME_FIELD));
        WebElement emailField = driver.findElement(SIGNUP_EMAIL_FIELD);
        
        nameField.clear();
        nameField.sendKeys(name);
        emailField.clear();
        emailField.sendKeys(email);
        driver.findElement(SIGNUP_BUTTON).click();
    }
    
    /**
     * Fills and submits the login form.
     * @param email The email for login.
     * @param password The password for login.
     */
    public void login(String email, String password) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_EMAIL_FIELD));
        WebElement passwordField = driver.findElement(LOGIN_PASSWORD_FIELD);
        
        emailField.clear();
        emailField.sendKeys(email);
        passwordField.clear();
        passwordField.sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }
    
    /**
     * Checks if the user is currently logged in.
     * @return true if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return !driver.findElements(LOGOUT_LINK).isEmpty();
    }
    
    /**
     * Clicks the logout link if it is present.
     */
    public void logout() {
        if (isLoggedIn()) {
            WebElement logoutLink = driver.findElement(LOGOUT_LINK);
            logoutLink.click();
            wait.until(ExpectedConditions.urlContains("login"));
        }
    }
}