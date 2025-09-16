package com.functional.pages;

import org.openqa.selenium.By;

public class UserRegistrationPage {

    // --- Login/Signup Page Locators ---
    public static final By SIGNUP_NAME_INPUT = By.xpath("//input[@name='name']");
    public static final By SIGNUP_EMAIL_INPUT = By.xpath("//input[@data-qa='signup-email']");
    public static final By SIGNUP_BUTTON = By.xpath("//button[@data-qa='signup-button']");
    
    // Locators for logging in with an existing user
    public static final By LOGIN_EMAIL_INPUT = By.xpath("//input[@data-qa='login-email']");
    public static final By LOGIN_PASSWORD_INPUT = By.xpath("//input[@data-qa='login-password']");
    public static final By LOGIN_BUTTON = By.xpath("//button[@data-qa='login-button']");
    
    // --- Account Information Page Locators ---
    public static final By MR_RADIO_BUTTON = By.id("id_gender1");
    public static final By MRS_RADIO_BUTTON = By.id("id_gender2");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By DAYS_DROPDOWN = By.id("days");
    public static final By MONTHS_DROPDOWN = By.id("months");
    public static final By YEARS_DROPDOWN = By.id("years");
    public static final By NEWSLETTER_CHECKBOX = By.id("newsletter");
    public static final By OFFERS_CHECKBOX = By.id("optin");

    // --- Address Information Page Locators ---
    public static final By FIRST_NAME_INPUT = By.id("first_name");
    public static final By LAST_NAME_INPUT = By.id("last_name");
    public static final By COMPANY_INPUT = By.id("company");
    public static final By ADDRESS1_INPUT = By.id("address1");
    public static final By ADDRESS2_INPUT = By.id("address2");
    public static final By COUNTRY_DROPDOWN = By.id("country");
    public static final By STATE_INPUT = By.id("state");
    public static final By CITY_INPUT = By.id("city");
    public static final By ZIPCODE_INPUT = By.id("zipcode");
    public static final By MOBILE_NUMBER_INPUT = By.id("mobile_number");
    public static final By CREATE_ACCOUNT_BUTTON = By.xpath("//button[@data-qa='create-account']");
    
    // --- Success and Error Message Locators ---
    public static final By ACCOUNT_CREATED_MESSAGE = By.xpath("//*[contains(text(),'ACCOUNT CREATED!')]");
    public static final By EMAIL_EXISTS_ERROR = By.xpath("//p[contains(text(), 'Email Address already exist!')]");
    
    // --- Common/Shared Locators (e.g., links on the header/footer) ---
    public static final By DELETE_ACCOUNT_BUTTON = By.xpath("//a[contains(@href, '/delete_account')]");
    public static final By FOOTER_SUBSCRIBE_EMAIL_INPUT = By.id("susbscribe_email");
    public static final By FOOTER_SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By PROCEED_TO_CHECKOUT_BUTTON = By.xpath("//a[contains(text(),'Proceed To Checkout')]");
    public static final By ADD_TO_CART_BUTTON = By.xpath("//a[contains(@class,'add-to-cart')]");
    public static final By LOGOUT_LINK = By.xpath("//a[contains(text(),'Logout')]");
    public static final By CART_IS_EMPTY_MESSAGE = By.xpath("//*[contains(text(),'Cart is empty')]");
}