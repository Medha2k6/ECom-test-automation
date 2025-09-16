package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class TestCasesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page URL
    private static final String TEST_CASES_URL = "https://automationexercise.com/test_cases";

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
    public static final By LOGO = By.xpath("//*[@id=\"form\"]/div/div[1]/div/h2/b");
    

    // Test Cases Section Locators
    public static final By TEST_CASES_HEADER = By.xpath("//h2[contains(text(),'Test Cases')]");
    public static final By TEST_CASE_LINKS = By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[5]/a");
    public static final By TEST_CASE_CONTAINERS = By.xpath("//div[contains(@class,'panel-default')]");
    public static final By TEST_CASE_SCENARIO_SECTION = By.xpath("//h4[contains(text(),'Scenario')]");

    // Footer and Subscription Locators
    public static final By FEEDBACK_EMAIL_LINK = By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]");
    public static final By SUBSCRIPTION_EMAIL_FIELD = By.id("susbscribe_email");
    public static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    public static final By SUBSCRIPTION_SUCCESS_MESSAGE = By.xpath("//div[contains(text(),'You have been successfully subscribed!')]");
    public static final By SCROLL_UP_BUTTON = By.id("scrollUp");

    public TestCasesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the Test Cases page directly.
     */
    public void navigateToTestCasesPage() {
        driver.get(TEST_CASES_URL);
        wait.until(ExpectedConditions.urlContains("test_cases"));
    }

    /**
     * Clicks on a main navigation header link.
     * @param linkLocator The By locator of the link to click.
     */
    public void clickHeaderLink(By linkLocator) {
        wait.until(ExpectedConditions.elementToBeClickable(linkLocator)).click();
    }

    /**
     * Subscribes with the given email address in the footer.
     * @param email The email address for subscription.
     */
    public void subscribeWithEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBSCRIPTION_EMAIL_FIELD));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", emailField);
        emailField.clear();
        emailField.sendKeys(email);
        driver.findElement(SUBSCRIBE_BUTTON).click();
    }

    /**
     * Scrolls the page to the top.
     */
    public void scrollToTop() {
        WebElement scrollUpButton = wait.until(ExpectedConditions.elementToBeClickable(SCROLL_UP_BUTTON));
        scrollUpButton.click();
    }

    /**
     * Returns a list of all test case links found on the page.
     * @return List of WebElement objects representing test case links.
     */
    public List<WebElement> getTestCaseLinks() {
        return driver.findElements(TEST_CASE_LINKS);
    }
}