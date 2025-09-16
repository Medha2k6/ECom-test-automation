package com.functional.pages;

import org.openqa.selenium.By;

public class TestCasesPage {

    // Main page elements
    public static final String TEST_CASES_URL = "https://automationexercise.com/test_cases";
    public static final By TEST_CASES_HEADER = By.xpath("//h2[contains(text(),'Test Cases')]");

    // Locators for the first 26 test cases (links)
    public static final By TC01_REGISTER_USER = By.linkText("Test Case 1: Register User");
    public static final By TC02_LOGIN_CORRECT = By.linkText("Test Case 2: Login User with correct email and password");
    public static final By TC03_LOGIN_INCORRECT = By.linkText("Test Case 3: Login User with incorrect email and password");
    public static final By TC04_LOGOUT_USER = By.linkText("Test Case 4: Logout User");
    public static final By TC05_REGISTER_EXISTING = By.linkText("Test Case 5: Register User with existing email");
    public static final By TC06_CONTACT_US_FORM = By.linkText("Test Case 6: Contact Us Form");
    public static final By TC07_VERIFY_TEST_CASES_PAGE = By.linkText("Test Case 7: Verify Test Cases Page");
    public static final By TC08_ALL_PRODUCTS_DETAIL = By.linkText("Test Case 8: Verify All Products and product detail page");
    public static final By TC09_SEARCH_PRODUCT = By.linkText("Test Case 9: Search Product");
    public static final By TC10_SUBSCRIPTION_HOME = By.linkText("Test Case 10: Verify Subscription in home page");
    public static final By TC11_SUBSCRIPTION_CART = By.linkText("Test Case 11: Verify Subscription in Cart page");
    public static final By TC12_ADD_PRODUCTS_IN_CART = By.linkText("Test Case 12: Add Products in Cart");
    public static final By TC13_VERIFY_QTY_IN_CART = By.linkText("Test Case 13: Verify Product quantity in Cart");
    public static final By TC14_PLACE_ORDER_REGISTER_WHILE = By.linkText("Test Case 14: Place Order: Register while Checkout");
    public static final By TC15_PLACE_ORDER_REGISTER_BEFORE = By.linkText("Test Case 15: Place Order: Register before Checkout");
    public static final By TC16_PLACE_ORDER_LOGIN_BEFORE = By.linkText("Test Case 16: Place Order: Login before Checkout");
    public static final By TC17_REMOVE_PRODUCTS_FROM_CART = By.xpath("//*[@id=\"form\"]/div/div[18]/div/div[1]/h4/a");
    public static final By TC18_VIEW_CATEGORY_PRODUCTS = By.linkText("Test Case 18: View Category Products");
    public static final By TC19_VIEW_AND_CART_BRAND_PRODUCTS = By.linkText("Test Case 19: View & Cart Brand Products");
    public static final By TC20_SEARCH_PRODUCTS_VERIFY_CART_AFTER_LOGIN = By.linkText("Test Case 20: Search Products and Verify Cart After Login");
    public static final By TC21_ADD_REVIEW_ON_PRODUCT = By.linkText("Test Case 21: Add review on product");
    public static final By TC22_ADD_TO_CART_FROM_RECOMMENDED = By.linkText("Test Case 22: Add to cart from Recommended items");
    public static final By TC23_VERIFY_ADDRESS_IN_CHECKOUT = By.linkText("Test Case 23: Verify address details in checkout page");
    public static final By TC24_DOWNLOAD_INVOICE_AFTER_PURCHASE = By.linkText("Test Case 24: Download Invoice after purchase order");
    public static final By TC25_SCROLL_UP_USING_ARROW = By.linkText("Test Case 25: Scroll Up using 'Arrow' button and Scroll Down");
    public static final By TC26_SCROLL_UP_WITHOUT_ARROW = By.linkText("Test Case 26: Scroll Up without 'Arrow' button and Scroll Down");

    // Locators for subscription form (footer)
    public static final By SUBSCRIPTION_EMAIL_INPUT = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By SUBSCRIPTION_SUCCESS_MESSAGE = By.xpath("//div[contains(@class,'alert-success') and contains(text(),'You have been successfully subscribed!')]");

    // Other locators
    public static final By FEEDBACK_MAILTO_LINK = By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]");
    public static final By ALL_TEST_CASE_LINKS = By.xpath("//div[@class='container']//a[starts-with(@href,'/test_case/')]");
}