package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URL
    public static final String CART_URL = "https://automationexercise.com/view_cart";
    public static final String HOME_URL = "https://automationexercise.com/";
    public static final String LOGIN_URL = "https://automationexercise.com/login";

    // Navigation and Header Locators
    public static final By HOME_LINK = By.xpath("//a[@href='/' or normalize-space()='Home']");
    public static final By PRODUCTS_LINK = By.xpath("//a[@href='/products']");
    public static final By CART_LINK = By.xpath("//a[@href='/view_cart']");
    public static final By SIGNUP_LOGIN_LINK = By.xpath("//a[@href='/login' or contains(.,'Signup / Login')]");
    public static final By LOGOUT_LINK = By.xpath("//a[contains(normalize-space(),'Logout')]");
    public static final By LOGGED_IN_AS_TEXT = By.xpath("//*[contains(text(),'Logged in as')]");
    public static final By TEST_CASES_LINK = By.xpath("//a[@href='/test_cases']");
    public static final By API_TESTING_LINK = By.xpath("//a[contains(.,'API Testing')]");
    public static final By CONTACT_US_LINK = By.xpath("//a[@href='/contact_us' or contains(.,'Contact us')]");
    public static final By WEBSITE_LOGO = By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img");

    // Cart Content Locators
    public static final By EMPTY_CART_MESSAGE = By.xpath("//*[contains(text(),'Cart is empty') or contains(.,'Cart is empty')]");
    public static final By CLICK_HERE_TO_SHOP_LINK = By.xpath("//a[contains(.,'Click here') or contains(.,'click here')]");
    public static final By CART_TABLE_ROWS = By.xpath("//table[@id='cart_info_table']//tbody/tr");
    public static final By FIRST_PRODUCT_NAME = By.xpath("(//table[@id='cart_info_table']//tbody/tr)[1]//td[contains(@class,'cart_description') or ./descendant::h4/a]");
    public static final By FIRST_PRODUCT_QUANTITY_INPUT = By.xpath("(//table[@id='cart_info_table']//tbody/tr)[1]//input[@type='number' or @name='quantity']");
    public static final By FIRST_PRODUCT_UNIT_PRICE = By.xpath("(//table[@id='cart_info_table']//td[contains(@class,'cart_price')][1] | //table[@id='cart_info_table']//td[contains(.,'Rs.')][1])");
    public static final By FIRST_PRODUCT_TOTAL_PRICE = By.xpath("(//table[@id='cart_info_table']//td[contains(@class,'cart_total')][1] | //table[@id='cart_info_table']//td[contains(.,'Rs.')][last()])");
    public static final By PROCEED_TO_CHECKOUT_BUTTON = By.xpath("//*[contains(.,'Proceed To Checkout') or contains(.,'Proceed to Checkout')]");
    public static final By FIRST_DELETE_BUTTON = By.xpath("(//a[contains(@class,'cart_quantity_delete') or contains(@onclick,'delete')])[1]");

    // Login Page Locators
    public static final By LOGIN_EMAIL_INPUT = By.cssSelector("input[data-qa='login-email'], input[name='email']");
    public static final By LOGIN_PASSWORD_INPUT = By.cssSelector("input[data-qa='login-password'], input[name='password']");
    public static final By LOGIN_BUTTON = By.xpath("//button[contains(.,'Login')]");

    // Subscription Locators
    public static final By SUBSCRIPTION_EMAIL_INPUT = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToCart() {
        driver.get(CART_URL);
        wait.until(ExpectedConditions.urlContains("view_cart"));
    }

    public void clickProceedToCheckout() {
        WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(PROCEED_TO_CHECKOUT_BUTTON));
        proceedButton.click();
    }

    public void removeFirstItem() {
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(FIRST_DELETE_BUTTON));
        deleteButton.click();
    }

    public void updateFirstItemQuantity(String quantity) {
        WebElement quantityInput = wait.until(ExpectedConditions.elementToBeClickable(FIRST_PRODUCT_QUANTITY_INPUT));
        quantityInput.clear();
        quantityInput.sendKeys(quantity);
    }
}