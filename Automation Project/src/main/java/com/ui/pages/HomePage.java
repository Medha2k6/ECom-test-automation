package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URL
    private static final String HOME_URL = "https://automationexercise.com/";

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

    // Category and Brand Locators
    public static final By CATEGORY_SECTION = By.xpath("//h2[contains(text(),'Category')]");
    public static final By CATEGORY_WOMEN = By.xpath("//a[@href='#Women']");
    public static final By CATEGORY_MEN = By.xpath("//a[@href='#Men']");
    public static final By CATEGORY_KIDS = By.xpath("//a[@href='#Kids']");
    public static final By BRANDS_SECTION = By.xpath("//div[@class='brands_products']");
    public static final By FIRST_BRAND_LINK = By.xpath("//div[@class='brands_products']//a[1]");

    // Products and Features
    public static final By ALL_PRODUCTS_TITLE = By.xpath("//h2[contains(text(),'All Products')]");
    public static final By FIRST_PRODUCT_ADD_TO_CART_BTN = By.xpath("(//a[contains(@class,'add-to-cart')])[1]");
    public static final By VIEW_CART_LINK = By.xpath("//u[text()='View Cart']");
    public static final By PRODUCT_DETAILS_LINK = By.xpath("(//a[contains(text(),'View Product')])[1]");

    // Footer and Subscription
    public static final By FOOTER_SECTION = By.xpath("//footer");
    public static final By SUBSCRIPTION_HEADER = By.xpath("//h2[contains(text(),'Subscription')]");
    public static final By SUBSCRIPTION_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By SUBSCRIPTION_SUCCESS_MESSAGE = By.xpath("//div[@class='alert-success alert']");
    public static final By SCROLL_UP_BUTTON = By.id("scrollUp");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the home page directly.
     */
    public void navigateToHomePage() {
        driver.get(HOME_URL);
        wait.until(ExpectedConditions.urlToBe(HOME_URL));
    }

    /**
     * Clicks on a main navigation header link.
     * @param linkLocator The By locator of the link to click.
     */
    public void clickHeaderLink(By linkLocator) {
        wait.until(ExpectedConditions.elementToBeClickable(linkLocator)).click();
    }

    /**
     * Clicks the "Add to Cart" button for the first product.
     */
    public void addFirstProductToCart() {
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='single-products'])[1]")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstProduct);
        
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(FIRST_PRODUCT_ADD_TO_CART_BTN));
        addToCartBtn.click();
    }

    /**
     * Fills the subscription email field and clicks the subscribe button.
     * @param email The email address to enter.
     */
    public void subscribeWithEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBSCRIPTION_EMAIL_FIELD));
        emailInput.sendKeys(email);
        driver.findElement(SUBSCRIBE_BUTTON).click();
    }
}