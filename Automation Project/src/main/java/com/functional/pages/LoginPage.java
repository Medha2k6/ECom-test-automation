package com.functional.pages;

import org.openqa.selenium.By;

public class LoginPage {

    // Locators for the login page
    public static final By EMAIL_INPUT = By.xpath("//input[@data-qa='login-email']");
    public static final By PASSWORD_INPUT = By.xpath("//input[@data-qa='login-password']");
    public static final By LOGIN_BUTTON = By.xpath("//button[@data-qa='login-button']");

    // Common locators that may be present on multiple pages but are used in login tests
    public static final By LOGGED_IN_AS_TEXT = By.xpath("//*[contains(text(),'Logged in as')]");
    public static final By LOGOUT_LINK = By.xpath("//a[contains(text(),'Logout')]");
    public static final By DELETE_ACCOUNT_LINK = By.xpath("//a[@href='/delete_account']");
    public static final By SIGNUP_LOGIN_LINK = By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]");

    // Locators for validation/error messages
    public static final By INCORRECT_CREDENTIALS_ERROR = By.xpath("//div[contains(text(),'incorrect')]");
    
    // Add the missing ERROR_MESSAGE locator
    public static final By ERROR_MESSAGE = By.xpath("//p[contains(@style,'color') and contains(@style,'red')] | //div[contains(@class,'error')] | //span[contains(@class,'error')] | //p[contains(text(),'incorrect')] | //p[contains(text(),'Invalid')]");
    
    // Additional error message locators for specific validation scenarios
    public static final By EMAIL_REQUIRED_ERROR = By.xpath("//p[contains(text(),'Email') and contains(text(),'required')]");
    public static final By PASSWORD_REQUIRED_ERROR = By.xpath("//p[contains(text(),'Password') and contains(text(),'required')]");
    public static final By INVALID_EMAIL_ERROR = By.xpath("//p[contains(text(),'Invalid') and contains(text(),'email')]");
}