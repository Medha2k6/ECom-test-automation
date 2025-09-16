package com.product.Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.functional.utilities.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static ExtentReports extent;
    protected ExtentTest test;
    protected static final String BASE_URL = "https://automationexercise.com";

    private static int testCaseCounter = 1;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        String reportPath = "ExtentReport.html";
        File reportFile = new File(reportPath);
        if (reportFile.exists()) {
            reportFile.delete();
            System.out.println("Existing Extent Report file deleted: " + reportPath);
        }
        extent = ExtentManager.getInstance(reportPath);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (extent != null) {
            extent.flush();
        }
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, Method method) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            driver = new EdgeDriver(options);
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        
        String tcId = "TC" + testCaseCounter++;
        test = extent.createTest(tcId + " - " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- Utility methods below this line ---
    protected void logInfo(String message) {
        if (test != null) test.log(Status.INFO, message);
        System.out.println(message);
    }

    protected void logPass(String message) {
        if (test != null) test.log(Status.PASS, message);
        System.out.println("✓ " + message);
    }

    protected void logFail(String message) {
        if (test != null) test.log(Status.FAIL, message);
        System.err.println("✗ " + message);
    }

    protected WebElement findElementSafely(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logInfo("→ Element not found");
            return null;
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    protected void safeClick(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(locator));
        }
    }

    protected void safeSendKeys(By locator, String text) {
        if (text == null) return;
        WebElement element = findElementSafely(locator);
        if (element != null) {
            element.clear();
            element.sendKeys(text);
        }
    }

    protected void selectByVisibleText(By locator, String text) {
        if (text == null) return;
        WebElement element = findElementSafely(locator);
        if (element != null) {
            new Select(element).selectByVisibleText(text);
        }
    }

    protected void scrollToElement(By locator) {
        WebElement element = findElementSafely(locator);
        if (element != null) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void scrollToEndOfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void navigateTo(String url) {
        logInfo("→ Navigating to: " + url);
        driver.get(url);
    }

    protected String generateRandomEmail(String template) {
        if (template != null && template.contains("{random}")) {
            return template.replace("{random}", String.valueOf(new Random().nextInt(100000)));
        }
        return template;
    }

    protected void waitForPageLoad() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}