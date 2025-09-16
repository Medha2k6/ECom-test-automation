package com.functional.pages;

import org.openqa.selenium.By;

public class ProductPage {

    // Header & Navigation Locators
    public static final By PRODUCTS_LINK = By.xpath("//*[@id='header']//a[contains(text(),'Products')]");
    public static final By CART_LINK = By.xpath("//*[@id='header']//a[contains(text(),'Cart')]");
    public static final By HEADER_PRODUCTS_LINK = By.xpath("//*[@id='header']/div/div/div/div[2]/div/ul/li[2]/a");

    // Search Locators
    public static final By SEARCH_PRODUCT_INPUT = By.id("search_product");
    public static final By SUBMIT_SEARCH_BUTTON = By.id("submit_search");
    public static final By SEARCHED_PRODUCTS_TITLE = By.xpath("//h2[contains(text(),'Searched Products')]");
    public static final By PRODUCT_INFO_TEXT = By.xpath("//div[@class='productinfo text-center']");
    public static final By PRODUCT_OVERLAY = By.xpath("(//div[@class='product-overlay'])[1]");
    public static final By FEATURES_ITEMS_GRID = By.xpath("//div[@class='features_items']");

    // Category & Brand Locators
    public static final By CATEGORY_ACCORDIAN = By.xpath("//*[@id='accordian']");
    public static final By WOMEN_CATEGORY_EXPAND = By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a");
    public static final By WOMEN_SUBCATEGORY_DRESS = By.xpath("//*[@id=\"Women\"]/div/ul/li[1]/a");
    public static final By BRAND_POLO = By.xpath("//div[@class='brands_products']//a[contains(text(),'Polo')]");
    public static final By BRAND_ACCORDIAN_POLO = By.xpath("/html/body/section/div/div[2]/div[1]/div/div[2]/div/ul/li[2]/a");
    public static final By WOMEN_CATEGORY_TITLE = By.xpath("/html/body/section/div/div[2]/div[2]/div/h2");
    public static final By MEN_CATEGORY_EXPAND = By.xpath("//*[@id=\"accordian\"]/div[2]/div[1]/h4/a");
    public static final By MEN_SUBCATEGORY_TSHIRTS = By.xpath("//*[@id=\"Men\"]/div/ul/li[1]/a");

    // Product & Cart Locators
    public static final By PRODUCT_OVERLAY_CONTAINER = By.xpath("(//div[@class='single-products'])[1]");
    public static final By ADD_TO_CART_FIRST_PRODUCT = By.xpath("(//a[text()='Add to cart'])[1]");
    public static final By ADD_TO_CART_SECOND_PRODUCT = By.xpath("(//a[text()='Add to cart'])[4]");
    public static final By VIEW_PRODUCT_BUTTON = By.xpath("(//a[text()='View Product'])[1]");
    public static final By VIEW_PRODUCT_BUTTON_ALT = By.xpath("/html/body/section[2]/div/div/div[2]/div/div[4]/div/div[2]/ul/li/a");
    public static final By CART_MODAL = By.xpath("//*[@id='cartModal']//h4");
    public static final By CONTINUE_SHOPPING_BUTTON = By.xpath("//*[@id='cartModal']//button[text()='Continue Shopping']");
    public static final By VIEW_CART_LINK = By.xpath("//*[@id='cartModal']//u");
    public static final By PRODUCT_QUANTITY_FIELD = By.id("quantity");
    public static final By ADD_TO_CART_PRODUCT_DETAILS_BUTTON = By.xpath("//button[@class='btn btn-default cart']");
    public static final By CART_ROWS = By.xpath("//*[@id='cart_info_table']/tbody/tr");
    public static final By CART_PERSISTENCE_ELEMENT = By.xpath("//tbody/tr");
    public static final By PROCEED_TO_CHECKOUT_BUTTON = By.xpath("//*[@id='do_action']/div[1]/div/div/a");
    public static final By LOGIN_PROMPT_MODAL = By.xpath("//*[@id=\"checkoutModal\"]/div/div/div[2]/p[1]");
    
    // Product Details Locators
    public static final By PRODUCT_DETAILS_HEADING = By.xpath("/html/body/section/div/div/div[2]/div[2]/div[2]/div/h2");

    // Review Form Locators
    public static final By REVIEW_NAME_FIELD = By.id("name");
    public static final By REVIEW_EMAIL_FIELD = By.id("email");
    public static final By REVIEW_TEXT_AREA = By.id("review");
    public static final By SUBMIT_REVIEW_BUTTON = By.id("button-review");

    // Subscription Locators
    public static final By SUBSCRIBE_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By ALERT_DANGER_MESSAGE = By.xpath("//div[contains(@class,'alert-danger')]");
    
}