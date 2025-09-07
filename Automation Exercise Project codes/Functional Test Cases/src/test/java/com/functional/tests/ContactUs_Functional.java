package com.functional.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import com.functional.listeners.ExtentTestNGListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;

/**
 * Contact Us Functional Test Suite
 * Runs all 23 test cases with Extent Reports integration.
 * 
 * NOTE:
 * - ExtentTestNGListener.driver is set in @BeforeClass
 * - Screenshots are captured on failures automatically
 */
@Listeners(com.functional.listeners.ExtentTestNGListener.class)
public class ContactUs_Functional {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;
    private static final String BASE_URL = "https://automationexercise.com";

    // Locators
    private final By LINK_CONTACT = By.xpath("//a[@href='/contact_us' or contains(normalize-space(.),'Contact')]");
    private final By HDR_GET_IN_TOUCH = By.xpath("//h2[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'GET IN TOUCH')]");
    private final By FLD_NAME = By.cssSelector("input[data-qa='name'], input[name='name']");
    private final By FLD_EMAIL = By.cssSelector("input[data-qa='email'], input[name='email']");
    private final By FLD_SUBJ = By.cssSelector("input[data-qa='subject'], input[name='subject']");
    private final By FLD_MSG = By.cssSelector("textarea[data-qa='message'], textarea[name='message']");
    private final By FLD_UPLOAD = By.cssSelector("input[type='file'][name='upload_file']");
    private final By BTN_SUBMIT = By.cssSelector("input[data-qa='submit-button'], input[type='submit'][name='submit']");
    private final By MSG_SUCCESS = By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success')]");
    private final By BTN_HOME = By.cssSelector("a[href='/']");
    private final By FOOTER_SUB_INPUT = By.cssSelector("input[id='susbscribe_email'], footer input[type='email']");
    private final By FOOTER_SUB_BTN = By.cssSelector("button#subscribe, button.subscribe");

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Optional: uncomment for faster runs (no UI)
        // options.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);

        driver.manage().window().maximize();

        // Use only explicit waits (avoid implicit wait delays)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        // Make driver accessible to Extent listener
        ExtentTestNGListener.driver = driver;

        // Open Contact Us page once at the start
        driver.get(BASE_URL + "/contact_us");
        wait.until(ExpectedConditions.visibilityOfElementLocated(HDR_GET_IN_TOUCH));
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @BeforeMethod
    public void goToContactUs() {
        driver.get(BASE_URL + "/contact_us");
        wait.until(ExpectedConditions.visibilityOfElementLocated(HDR_GET_IN_TOUCH));
    }

    // ---------------- Helpers ----------------
    private void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(el);
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
        } catch (WebDriverException e) {
            js.executeScript("arguments[0].click();", el);
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
        } catch (TimeoutException ignored) {}
    }

    // ---------------- Test cases ----------------

    @Test(priority = 1)
    public void tc_cu_01_nameAcceptsValid() {
        type(FLD_NAME, "Abhijith Kumar");
        Assert.assertEquals(driver.findElement(FLD_NAME).getAttribute("value"), "Abhijith Kumar");
    }

    @Test(priority = 2)
    public void tc_cu_02_nameRejectsInvalid() {
        type(FLD_NAME, "!!!@@@");
        String value = driver.findElement(FLD_NAME).getAttribute("value");

        // If the field still contains invalid characters, that's a FAIL
        Assert.assertTrue(value == null || value.trim().isEmpty(),
                "❌ Name field incorrectly accepted invalid input: " + value);
    }

    @Test(priority = 3)
    public void tc_cu_03_emailAcceptsValid() {
        String mail = "valid" + System.currentTimeMillis() + "@example.com";
        type(FLD_EMAIL, mail);
        Assert.assertTrue(driver.findElement(FLD_EMAIL).getAttribute("value").contains("@"));
    }

    @Test(priority = 4)
    public void tc_cu_04_emailRejectsInvalid() {
        type(FLD_EMAIL, "bad@g");
        String value = driver.findElement(FLD_EMAIL).getAttribute("value");

        // If invalid email is accepted, FAIL the test
        Assert.assertTrue(value == null || value.trim().isEmpty(),
                "❌ Email field incorrectly accepted invalid input: " + value);
    }


    @Test(priority = 5)
    public void tc_cu_05_subjectAcceptsValid() {
        String subj = "Order #12345 – Question";
        type(FLD_SUBJ, subj);
        Assert.assertEquals(driver.findElement(FLD_SUBJ).getAttribute("value"), subj);
    }

    @Test(priority = 6)
    public void tc_cu_06_subjectRejectsInvalid() {
        type(FLD_SUBJ, "%%%%%%");
        String value = driver.findElement(FLD_SUBJ).getAttribute("value");

        // If invalid subject ("%") is present, test should fail
        Assert.assertFalse(value.contains("%"),
                "❌ Subject field incorrectly accepted invalid input: " + value);
    }


    @Test(priority = 7)
    public void tc_cu_07_messageFieldAcceptsText() {
        String msg = "Hello team, functional test message.";
        type(FLD_MSG, msg);
        Assert.assertEquals(driver.findElement(FLD_MSG).getAttribute("value"), msg);
    }

    @Test(priority = 8)
    public void tc_cu_08_chooseFileClickable() {
        Assert.assertTrue(driver.findElement(FLD_UPLOAD).isEnabled(), "❌ Choose file not enabled!");
    }

    @Test(priority = 9)
    public void tc_cu_09_chosenFileAppears() throws IOException {
        Path file = createTempFile("cu-", ".txt", "test content");
        WebElement upload = driver.findElement(FLD_UPLOAD);
        upload.sendKeys(file.toAbsolutePath().toString());
        Assert.assertTrue(upload.getAttribute("value").contains(".txt"));
    }

    @Test(priority = 10)
    public void tc_cu_10_nameRejectsSpecialCharsStrict() {
        type(FLD_NAME, "@@@@");
        String value = driver.findElement(FLD_NAME).getAttribute("value");

        // If special chars are accepted, this should fail
        Assert.assertFalse(value.contains("@"),
                "❌ Name field incorrectly accepted special characters: " + value);
    }


    @Test(priority = 11)
    public void tc_cu_11_feedbackMailLinkIsMailto() {
        WebElement mail = driver.findElement(By.xpath("//a[starts-with(@href,'mailto:')]"));
        Assert.assertTrue(mail.getAttribute("href").startsWith("mailto:"));
    }

    @Test(priority = 12)
    public void tc_cu_12_footerSubValid() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        type(FOOTER_SUB_INPUT, "sub" + System.currentTimeMillis() + "@example.com");
        safeClick(FOOTER_SUB_BTN);
        Assert.assertFalse(driver.findElements(MSG_SUCCESS).isEmpty());
    }

    @Test(priority = 13)
    public void tc_cu_13_footerSubInvalid() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        type(FOOTER_SUB_INPUT, "bad@@@");
        safeClick(FOOTER_SUB_BTN);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("invalid"));
    }

    @Test(priority = 14)
    public void tc_cu_14_footerSubBlank() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        type(FOOTER_SUB_INPUT, "");
        safeClick(FOOTER_SUB_BTN);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("required"));
    }

    @Test(priority = 15)
    public void tc_cu_15_footerSubDuplicate() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        String dup = "dup" + (System.currentTimeMillis()/10000) + "@example.com";
        type(FOOTER_SUB_INPUT, dup);
        safeClick(FOOTER_SUB_BTN);
        type(FOOTER_SUB_INPUT, dup);
        safeClick(FOOTER_SUB_BTN);
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("already"));
    }

    @Test(priority = 16)
    public void tc_cu_16_footerSubEmailOver100Chars() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        String longEmail = "a".repeat(101) + "@example.com";
        type(FOOTER_SUB_INPUT, longEmail);
        Assert.assertTrue(driver.findElement(FOOTER_SUB_INPUT).getAttribute("value").length() <= 100);
    }

    @Test(priority = 17)
    public void tc_cu_17_homeAfterSubmitGoesHome() {
        type(FLD_NAME, "Tester");
        type(FLD_EMAIL, "goHome" + System.currentTimeMillis() + "@example.com");
        type(FLD_SUBJ, "Subject");
        type(FLD_MSG, "Message");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();

        // ✅ Wait until the Home button is clickable after form submit
        WebElement homeBtn = wait.until(ExpectedConditions.elementToBeClickable(BTN_HOME));
        safeClick(homeBtn);

        // ✅ Now wait for URL to actually change to home
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/"),
                "❌ Did not navigate to Home page after submit. Current URL: " + driver.getCurrentUrl());
    }


    @Test(priority = 18)
    public void tc_cu_18_submitBlankName() {
        type(FLD_NAME, "");
        type(FLD_EMAIL, "blankName@example.com");
        type(FLD_SUBJ, "Test");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();

        // Expectation: blank name should NOT be accepted
        Assert.assertFalse(driver.getPageSource().toLowerCase().contains("name"),
                "❌ Form incorrectly accepted blank name input.");
    }


    @Test(priority = 19)
    public void tc_cu_19_submitBlankEmail() {
        type(FLD_NAME, "Test");
        type(FLD_EMAIL, "");
        type(FLD_SUBJ, "Test");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("email"));
    }

    @Test(priority = 20)
    public void tc_cu_20_submitBlankSubject() {
        type(FLD_NAME, "Test");
        type(FLD_EMAIL, "test@example.com");
        type(FLD_SUBJ, "");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("subject"));
    }

    @Test(priority = 21)
    public void tc_cu_21_feedbackSubmits() {
        type(FLD_NAME, "Team");
        type(FLD_EMAIL, "ok" + System.currentTimeMillis() + "@example.com");
        type(FLD_SUBJ, "All good");
        type(FLD_MSG, "Feedback");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();
        Assert.assertFalse(driver.findElements(MSG_SUCCESS).isEmpty());
    }

    @Test(priority = 22)
    public void tc_cu_22_submitValidCredentials() {
        type(FLD_NAME, "Valid User");
        type(FLD_EMAIL, "valid" + System.currentTimeMillis() + "@example.com");
        type(FLD_SUBJ, "Subject OK");
        type(FLD_MSG, "Message OK");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();
        Assert.assertFalse(driver.findElements(MSG_SUCCESS).isEmpty());
    }

    @Test(priority = 23)
    public void tc_cu_23_submitInvalidCredentials() {
        type(FLD_NAME, "@@@");
        type(FLD_EMAIL, "bad@@@");
        type(FLD_SUBJ, "###");
        type(FLD_MSG, "");
        safeClick(BTN_SUBMIT);
        acceptAlertIfPresent();
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("invalid") ||
                          driver.getPageSource().toLowerCase().contains("error"));
    }
}
