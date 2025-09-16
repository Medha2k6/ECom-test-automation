package com.functional.tests;

import com.functional.listeners.ExtentTestNGListener;
import com.functional.pages.ContactUsPage;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({com.functional.listeners.ExtentTestNGListener.class})
public class ContactUs_Functional {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;
    private ContactUsPage contactUsPage;
    private static final String BASE_URL = "https://automationexercise.com";

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        contactUsPage = new ContactUsPage();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));


        // Open Contact Us page once at the start
        driver.get(BASE_URL + "/contact_us");
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactUsPage.HDR_GET_IN_TOUCH));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @BeforeMethod
    public void goToContactUs() {
        driver.get(BASE_URL + "/contact_us");
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactUsPage.HDR_GET_IN_TOUCH));
    }

    // ---------------- Helpers ----------------
    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm:ss a").format(new Date());
    }
    
    private ExtentTest getTest() {
        return ExtentTestNGListener.getTest();
    }

    private void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
        if (getTest() != null) {
            getTest().info("<b>Info</b> " + getCurrentTime() + " Typing '" + text + "' into field");
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(el);
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
            if (getTest() != null) {
                getTest().info("<b>Info</b> " + getCurrentTime() + " Clicked element successfully");
            }
        } catch (WebDriverException e) {
            js.executeScript("arguments[0].click();", el);
            if (getTest() != null) {
                getTest().info("<b>Info</b> " + getCurrentTime() + " Clicked element using JavaScript");
            }
        }
    }

    private Path createTempFile(String prefix, String suffix, String content) throws IOException {
        Path tmp = Files.createTempFile(prefix, suffix);
        Files.writeString(tmp, content, StandardOpenOption.WRITE);
        return tmp;
    }

    private void acceptAlertIfPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            if (getTest() != null) {
                getTest().info("<b>Info</b> " + getCurrentTime() + " Alert accepted");
            }
        } catch (TimeoutException ignored) {
        }
    }

    @Test(priority = 8)
    public void tc_cu_08_chooseFileClickable() {
        getTest().info("<b>Info</b> " + getCurrentTime() + " Checking if Choose File button is enabled");
        boolean isEnabled = driver.findElement(contactUsPage.FLD_UPLOAD).isEnabled();
        getTest().info("<b>Info</b> " + getCurrentTime() + " Choose File button enabled status: " + isEnabled);
        
        Assert.assertTrue(isEnabled, "❌ Choose file not enabled!");
        getTest().pass("<b>Pass</b> " + getCurrentTime() + " Choose File button is clickable as expected");
    }

    @Test(priority = 11)
    public void tc_cu_11_feedbackMailLinkIsMailto() {
        getTest().info("<b>Info</b> " + getCurrentTime() + " Verifying feedback mail link");
        WebElement mail = driver.findElement(contactUsPage.FEEDBACK_MAIL_LINK);
        String href = mail.getAttribute("href");
        getTest().info("<b>Info</b> " + getCurrentTime() + " Mail link href: " + href);
        
        Assert.assertTrue(href.startsWith("mailto:"));
        getTest().pass("<b>Pass</b> " + getCurrentTime() + " Feedback mail link is correctly configured with mailto");
    }

    @Test(priority = 17)
    public void tc_cu_17_homeAfterSubmitGoesHome() {
        getTest().info("<b>Info</b> " + getCurrentTime() + " Starting home navigation test after form submission");

        getTest().info("<b>Info</b> " + getCurrentTime() + " Filling form fields");
        type(contactUsPage.FLD_NAME, "Tester");
        type(contactUsPage.FLD_EMAIL, "goHome" + System.currentTimeMillis() + "@example.com");
        type(contactUsPage.FLD_SUBJ, "Subject");
        type(contactUsPage.FLD_MSG, "Message");

        getTest().info("<b>Info</b> " + getCurrentTime() + " Submitting form");
        safeClick(contactUsPage.BTN_SUBMIT);
        acceptAlertIfPresent();

        getTest().info("<b>Info</b> " + getCurrentTime() + " Waiting for Home button to be clickable");
        WebElement homeBtn = wait.until(ExpectedConditions.elementToBeClickable(contactUsPage.BTN_HOME));
        safeClick(homeBtn);

        getTest().info("<b>Info</b> " + getCurrentTime() + " Waiting for navigation to home page");
        // A more flexible wait to handle both with and without trailing slash
        wait.until(ExpectedConditions.urlMatches(BASE_URL + "/?"));

        String currentUrl = driver.getCurrentUrl();
        getTest().info("<b>Info</b> " + getCurrentTime() + " Current URL after clicking Home: " + currentUrl);

        Assert.assertTrue(currentUrl.endsWith("/") || currentUrl.endsWith(".com"),
                "❌ Did not navigate to Home page after submit. Current URL: " + currentUrl);
        getTest().pass("<b>Pass</b> " + getCurrentTime() + " Successfully navigated to Home page after form submission");
    }
    @Test(priority = 21)
    public void tc_cu_21_feedbackSubmits() {
        getTest().info("<b>Info</b> " + getCurrentTime() + " Starting feedback submission test");
        
        String email = "ok" + System.currentTimeMillis() + "@example.com";
        getTest().info("<b>Info</b> " + getCurrentTime() + " Filling form with test data");
        getTest().info("<b>Info</b> " + getCurrentTime() + " Name: 'Team', Email: '" + email + "'");
        
        type(contactUsPage.FLD_NAME, "Team");
        type(contactUsPage.FLD_EMAIL, email);
        type(contactUsPage.FLD_SUBJ, "All good");
        type(contactUsPage.FLD_MSG, "Feedback");
        
        getTest().info("<b>Info</b> " + getCurrentTime() + " Submitting feedback form");
        safeClick(contactUsPage.BTN_SUBMIT);
        acceptAlertIfPresent();
        
        boolean successMessagePresent = !driver.findElements(contactUsPage.MSG_SUCCESS).isEmpty();
        getTest().info("<b>Info</b> " + getCurrentTime() + " Success message present: " + successMessagePresent);
        
        Assert.assertFalse(driver.findElements(contactUsPage.MSG_SUCCESS).isEmpty());
        getTest().pass("<b>Pass</b> " + getCurrentTime() + " Feedback submitted successfully");
    }
}