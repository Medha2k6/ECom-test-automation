package Project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage {

    WebDriver driver;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
    }

    // === Signup Fields ===
    By nameField = By.name("name");
    By emailField = By.xpath("//input[@data-qa='signup-email']");
    By signupButton = By.xpath("//button[@data-qa='signup-button']");
    By signupForm = By.xpath("//h2[text()='Enter Account Information']");

    // === Navigation Icons ===
    By homeIcon = By.xpath("//a[text()=' Home']");
    By productsIcon = By.xpath("//a[@href='/products']");
    By cartIcon = By.xpath("//a[@href='/view_cart']");
    By testCasesIcon = By.xpath("//a[@href='/test_cases']");
    By apiTestingIcon = By.xpath("//a[@href='/api_list']");
    By videoTutorialsIcon = By.xpath("//a[@href='/video_tutorials']");
    By contactUsIcon = By.xpath("//a[@href='/contact_us']");

    // === Subscription ===
    By subscriptionEmail = By.id("susbscribe_email");
    By subscribeButton = By.id("subscribe");

    // === Scroll ===
    By scrollUpButton = By.id("scrollUp");

    // === Login/Signup Page Elements ===
    By loginSection = By.xpath("//h2[text()='Login to your account']");
    By signupSection = By.xpath("//h2[text()='New User Signup!']");
    By orSeparator = By.xpath("//p[contains(text(),'OR')]");

    // === Signup Methods ===
    public void enterName(String name) { driver.findElement(nameField).sendKeys(name); }
    public void enterEmail(String email) { driver.findElement(emailField).sendKeys(email); }
    public void clickSignupButton() { driver.findElement(signupButton).click(); }
    public boolean isSignupFormDisplayed() { return driver.findElements(signupForm).size() > 0; }

    // === Navigation ===
    public void clickHome() { driver.findElement(homeIcon).click(); }
    public void clickProducts() { driver.findElement(productsIcon).click(); }
    public void clickCart() { driver.findElement(cartIcon).click(); }
    public void clickTestCases() { driver.findElement(testCasesIcon).click(); }
    public void clickApiTesting() { driver.findElement(apiTestingIcon).click(); }
    public void clickVideoTutorials() { driver.findElement(videoTutorialsIcon).click(); }
    public void clickContactUs() { driver.findElement(contactUsIcon).click(); }

    // === Subscription ===
    public void enterSubscriptionEmail(String email) { driver.findElement(subscriptionEmail).sendKeys(email); }
    public void clickSubscribe() { driver.findElement(subscribeButton).click(); }

    // === Scroll ===
    public void scrollToTop() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
    }
    public boolean isScrollUpButtonVisible() { return driver.findElements(scrollUpButton).size() > 0; }

    // === Login/Signup Page Sections ===
    public boolean isLoginSectionVisible() { return driver.findElements(loginSection).size() > 0; }
    public boolean isSignupSectionVisible() { return driver.findElements(signupSection).size() > 0; }
    public boolean isOrSeparatorVisible() { return driver.findElements(orSeparator).size() > 0; }
}
