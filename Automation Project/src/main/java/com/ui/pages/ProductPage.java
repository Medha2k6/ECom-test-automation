package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Header and Nav Links
    public static final By HOME_LINK = By.xpath("//a[contains(text(),'Home')]");
    public static final By PRODUCTS_LINK = By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a");
    public static final By CART_LINK = By.xpath("//a[contains(text(),'Cart')]");
    public static final By SIGNUP_LOGIN_LINK = By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]");
    public static final By TEST_CASES_LINK = By.xpath("/html/body/header/div/div/div/div[2]/div/ul/li[5]/a");
    public static final By API_TESTING_LINK = By.xpath("/html/body/header/div/div/div/div[2]/div/ul/li[6]/a");
    public static final By VIDEO_TUTORIALS_LINK = By.xpath("/html/body/header/div/div/div/div[2]/div/ul/li[7]/a");
    public static final By CONTACT_US_LINK = By.xpath("//a[contains(text(),'Contact us')]");
    public static final By WEBSITE_LOGO = By.xpath("//img[@alt='Website for automation practice']");

    // Product Page Specific Locators
    public static final By ALL_PRODUCTS_TITLE = By.xpath("//h2[contains(text(),'All Products')]");
    public static final By SEARCH_INPUT_FIELD = By.id("search_product");
    public static final By SEARCH_BUTTON = By.id("submit_search");
    public static final By SEARCH_RESULT = By.xpath("/html/body/section[2]/div/div/div[2]/div/div[3]/div/div[1]/div[2]/div");
    //public static final By NO_PRODUCT_FOUND_MESSAGE = By.xpath("//h2[contains(text(),'No Products Found!')]");
    public static final By SEARCHED_PRODUCTS_TITLE = By.xpath("//h2[contains(text(),'Searched Products')]");
    public static final By PRODUCT_ITEMS = By.xpath("//div[@class='features_items']//div[contains(@class,'single-products')]");


    // Category and Brand Locators
    public static final By CATEGORY_WOMEN_LINK = By.xpath("//a[@href='#Women']");
    public static final By CATEGORY_MEN_LINK = By.xpath("//a[@href='#Men']");
    public static final By CATEGORY_KIDS_LINK = By.xpath("//a[@href='#Kids']");
    public static final By WOMEN_SUBCATEGORY_LINK = By.xpath("//div[@id='Women']//a[contains(text(),'Dress')]");
    public static final By MEN_SUBCATEGORY_LINK = By.xpath("//div[@id='Men']//a[contains(text(),'Tshirts')]");
    public static final By KIDS_SUBCATEGORY_LINK = By.xpath("//div[@id='Kids']//a[contains(text(),'Dress')]");
    public static final By FIRST_BRAND_LINK = By.xpath("//div[@class='brands_products']//a[1]");
    public static final By BRAND_ACCORDIAN_POLO = By.xpath("//a[@href='/brand_products/Polo']");


    // Product Item Locators
    public static final By FIRST_PRODUCT_ITEM = By.xpath("(//div[@class='single-products'])[1]");
    public static final By FIRST_PRODUCT_NAME = By.xpath("(//div[@class='productinfo text-center']/p)[1]");
    public static final By FIRST_PRODUCT_PRICE = By.xpath("(//div[@class='productinfo text-center']/h2)[1]");
    public static final By FIRST_PRODUCT_ADD_TO_CART_BTN = By.xpath("(//a[contains(text(),'Add to cart')])[1]");
    public static final By FIRST_PRODUCT_VIEW_PRODUCT_LINK = By.xpath("(//a[contains(text(),'View Product')])[1]");
    public static final By ADD_TO_CART_MODAL = By.id("cartModal");
    public static final By CONTINUE_SHOPPING_BTN = By.xpath("//button[text()='Continue Shopping']");
    public static final By VIEW_CART_LINK_IN_MODAL = By.xpath("//u[text()='View Cart']");
    public static final By PRODUCT_DETAILS_SECTION = By.xpath("//div[@class='product-information']");

    // Footer Locators
    public static final By SUBSCRIPTION_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By SUBSCRIPTION_SUCCESS_MESSAGE = By.id("success-subscribe");
    public static final By SUBSCRIPTION_ERROR_MESSAGE = By.xpath("//div[contains(@class,'alert-danger')]");
    public static final By SCROLL_UP_BUTTON = By.id("scrollUp");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Clicks on a main navigation header link.
     * @param linkLocator The By locator of the link to click.
     */
    public void clickHeaderLink(By linkLocator) {
        wait.until(ExpectedConditions.elementToBeClickable(linkLocator)).click();
    }
    
    /**
     * Clicks on a brand link.
     * @param brandLocator The By locator of the brand link to click.
     */
    public void clickBrandLink(By brandLocator) {
        wait.until(ExpectedConditions.elementToBeClickable(brandLocator)).click();
    }

    /**
     * Enters a search query and clicks the search button.
     * @param query The search term to enter.
     */
    public void searchForProduct(String query) {
        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT_FIELD));
        searchBar.sendKeys(query);
        driver.findElement(SEARCH_BUTTON).click();
    }
    
    /**
     * Adds the first displayed product to the cart.
     */
    public void addFirstProductToCart() {
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_PRODUCT_ITEM));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstProduct);
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(FIRST_PRODUCT_ADD_TO_CART_BTN));
        addToCartBtn.click();
    }

    /**
     * Navigates to a specific product's details page.
     */
    public void viewFirstProductDetails() {
        WebElement viewProductLink = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_PRODUCT_VIEW_PRODUCT_LINK));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", viewProductLink);
        viewProductLink.click();
    }
    
    /**
     * Subscribes with the given email address in the footer.
     * @param email The email address for subscription.
     */
    public void subscribeWithEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBSCRIPTION_EMAIL_FIELD));
        emailField.clear();
        emailField.sendKeys(email);
        driver.findElement(SUBSCRIBE_BUTTON).click();
    }
}