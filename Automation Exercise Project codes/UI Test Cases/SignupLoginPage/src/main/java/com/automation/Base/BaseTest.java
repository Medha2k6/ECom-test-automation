package com.automation.Base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.automation.utilities.ExtentManager;
import com.aventstack.extentreports.ExtentReports;

import org.testng.annotations.AfterSuite;



import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    
    protected WebDriver driver;
    protected ExtentReports extent;
   
    @BeforeSuite
    public void setupreport()
    {
    	extent = ExtentManager.getinstance();
    }
   
    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
    
    @AfterSuite
    public void flushreport()
    {
    }
}




