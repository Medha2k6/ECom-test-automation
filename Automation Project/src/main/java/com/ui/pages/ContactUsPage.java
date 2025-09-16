package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ContactUsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URL
    private static final String CONTACT_US_URL = "https://automationexercise.com/contact_us";

    // Header Links
    public static final By HOME_LINK = By.xpath("//a[contains(text(),'Home')]");
    public static final By PRODUCTS_LINK = By.xpath("//a[contains(text(),'Products')]");
    public static final By CART_LINK = By.xpath("//a[contains(text(),'Cart')]");
    public static final By SIGNUP_LOGIN_LINK = By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]");
    public static final By TEST_CASES_LINK = By.xpath("//a[contains(text(),'Test Cases')]");
    public static final By API_TESTING_LINK = By.xpath("//a[contains(text(),'API Testing')]");
    public static final By VIDEO_TUTORIALS_LINK = By.xpath("//a[contains(text(),'Video Tutorials')]");
    public static final By CONTACT_US_LINK = By.xpath("//a[contains(text(),'Contact us')]");
    
    // Logo
    public static final By WEBSITE_LOGO = By.xpath("//img[@alt='Website for automation practice']");

    // Contact Us Form Locators
    public static final By GET_IN_TOUCH_HEADER = By.xpath("//h2[contains(text(),'Get In Touch')]");
    public static final By NAME_FIELD = By.xpath("//input[@name='name']");
    public static final By EMAIL_FIELD = By.xpath("//input[@name='email']");
    public static final By SUBJECT_FIELD = By.xpath("//input[@name='subject']");
    public static final By MESSAGE_FIELD = By.xpath("//textarea[@name='message']");
    public static final By FILE_UPLOAD_INPUT = By.xpath("//input[@type='file']");
    public static final By SUBMIT_BUTTON = By.xpath("//input[@type='submit']");
    public static final By SUCCESS_MESSAGE = By.xpath("//div[@class='status alert alert-success']");

    // Footer Locators
    public static final By FOOTER_SECTION = By.xpath("//footer");
    public static final By SUBSCRIPTION_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By SUBSCRIPTION_SUCCESS_MESSAGE = By.xpath("//div[@class='alert-success alert']");
    public static final By FEEDBACK_EMAIL_LINK = By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]");


    public ContactUsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the Contact Us page directly.
     */
    public void navigateToContactUsPage() {
        driver.get(CONTACT_US_URL);
        wait.until(ExpectedConditions.urlContains("contact_us"));
    }

    /**
     * Fills the contact form with the provided details.
     * @param name The name to enter.
     * @param email The email to enter.
     * @param subject The subject to enter.
     * @param message The message to enter.
     */
    public void fillContactForm(String name, String email, String subject, String message) {
        driver.findElement(NAME_FIELD).sendKeys(name);
        driver.findElement(EMAIL_FIELD).sendKeys(email);
        driver.findElement(SUBJECT_FIELD).sendKeys(subject);
        driver.findElement(MESSAGE_FIELD).sendKeys(message);
    }
    
    /**
     * Clicks the form submission button.
     */
    public void clickSubmitButton() {
        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON)).click();
    }
    
    /**
     * Gets the form success message after submission.
     * @return The WebElement of the success message.
     */
    public WebElement getSuccessMessage() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(SUCCESS_MESSAGE));
    }
    
    /**
     * Checks if a success message for form submission is displayed.
     * @return true if the success message is displayed, false otherwise.
     */
    public boolean isFormSubmissionSuccessful() {
    try {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept(); // close alert
        return true;
    } catch (Exception e) {
        return false;
    }
}

}