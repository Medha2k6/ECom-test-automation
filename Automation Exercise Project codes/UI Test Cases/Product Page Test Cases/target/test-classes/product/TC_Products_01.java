package product;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class TC_Products_01 {
	WebDriver driver;
	WebDriverWait wait;
	String projectpath=System.getProperty("user.dir");
  @Test(dataProvider = "dp")
  public void f(Integer n, String s) {
	  System.out.println("This is the test");
	  ExtentReports extent=new ExtentReports();
	  ExtentSparkReporter spark=new ExtentSparkReporter(projectpath+"\\tc_product_01.html");
	  extent.attachReporter(spark);
	  ExtentTest test=extent.createTest("Verify the home page icon");
	  driver.get("https://automationexercise.com/");
      
	  driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[1]/a")).click();
      
      WebElement msg=driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img"));
		if(msg.isDisplayed()) {
			Assert.assertTrue(true);
			System.out.println("The home page is displayed");
		}
		else
		{
			Assert.assertTrue(false);
		}
	extent.flush();

		
		}
	  
 
  @BeforeMethod
  public void beforeMethod() {
	  WebDriverManager.chromedriver().setup();
      driver = new ChromeDriver();
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	  
  }

  @AfterMethod
  public void afterMethod() {
	  driver.quit();
	  
	  

  }


  @DataProvider
  public Object[][] dp() {
    return new Object[][] {
      new Object[] { 1, "a" },
      new Object[] { 2, "b" },
    };
  }
 
}
