package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;


public class ApiTestingUIPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for the top navigation bar
    private By homeIcon = By.xpath("//a[contains(text(),'Home')]");
    private By productsIcon = By.xpath("//a[contains(text(),'Products')]");
    private By cartIcon = By.xpath("//a[contains(text(),'Cart')]");
    private By signupLoginIcon = By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]");
    private By testCasesIcon = By.xpath("//a[contains(text(),'Test Cases')]");
    private By apiTestingIcon = By.xpath("//a[contains(text(),'API Testing')]");
    private By videoTutorialsIcon = By.xpath("//a[contains(text(),'Video Tutorials')]");
    private By contactUsIcon = By.xpath("//a[contains(text(),'Contact us')]");
    private By logo = By.xpath("//img[@alt='Website for automation practice']");

    // Locators for the subscription section at the bottom of the page
    private By subscriptionEmailField = By.id("susbscribe_email");
    private By subscribeButton = By.id("subscribe");

    // Locators for the API list sections
    private By apiListSections = By.xpath("//div[contains(@class,'panel-heading')]");
    private By firstApiSection = By.xpath("(//div[contains(@class,'panel-heading')])[1]");

    public ApiTestingUIPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Common methods for interacting with the page elements
    public void navigateToApiTestingPage() {
        driver.get("https://automationexercise.com/api_list");
    }

    public WebElement getApiTestingIcon() {
        return driver.findElement(apiTestingIcon);
    }
    
    public void clickApiTestingIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(apiTestingIcon)).click();
    }

    public void clickLogo() {
        WebElement logoElement = driver.findElement(logo);
        WebElement logoLink = logoElement.findElement(By.xpath("./ancestor::a[1]"));
        logoLink.click();
    }

    public void clickHomeIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(homeIcon)).click();
    }

    public void clickProductsIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(productsIcon)).click();
    }

    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }

    public void clickSignupLoginIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginIcon)).click();
    }

    public void clickTestCasesIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(testCasesIcon)).click();
    }

    public void clickVideoTutorialsIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(videoTutorialsIcon)).click();
    }

    public void clickContactUsIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(contactUsIcon)).click();
    }
    
    public void scrollToSubscriptionSection() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    
    public void enterSubscriptionEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(subscriptionEmailField));
        emailField.clear();
        emailField.sendKeys(email);
    }
    
    public void clickSubscribeButton() {
        wait.until(ExpectedConditions.elementToBeClickable(subscribeButton)).click();
    }

    public void expandFirstApiSection() {
        WebElement firstApiSectionElement = wait.until(ExpectedConditions.elementToBeClickable(firstApiSection));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstApiSectionElement);
        firstApiSectionElement.click();
    }

    // Getter methods for locators (to be used in test methods for assertions)
    public By getHomeIconLocator() {
        return homeIcon;
    }

    public By getProductsIconLocator() {
        return productsIcon;
    }

    public By getCartIconLocator() {
        return cartIcon;
    }

    public By getSignupLoginIconLocator() {
        return signupLoginIcon;
    }

    public By getTestCasesIconLocator() {
        return testCasesIcon;
    }

    public By getApiTestingIconLocator() {
        return apiTestingIcon;
    }
    
    public By getVideoTutorialsIconLocator() {
        return videoTutorialsIcon;
    }

    public By getContactUsIconLocator() {
        return contactUsIcon;
    }

    public By getLogoLocator() {
        return logo;
    }
    
    public By getSubscriptionEmailFieldLocator() {
        return subscriptionEmailField;
    }

    public By getSubscribeButtonLocator() {
        return subscribeButton;
    }

    public By getApiListSectionsLocator() {
        return apiListSections;
    }
}