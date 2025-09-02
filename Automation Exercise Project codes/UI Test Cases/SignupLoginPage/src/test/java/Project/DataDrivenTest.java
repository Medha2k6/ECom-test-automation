package Project;

import org.testng.annotations.*; // TestNG annotations
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.Assert;

import java.io.*;
import java.time.Duration;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDrivenTest { // must be public

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;
    SignupPage signupPage;
    String projectPath = System.getProperty("user.dir");

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        ExtentSparkReporter spark = new ExtentSparkReporter(projectPath + "\\Aug29Signup.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        signupPage = new SignupPage(driver);
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
        extent.flush();
    }

    @DataProvider(name = "userData")
    public Object[][] readExcelData() throws IOException {
        String filePath = projectPath + "\\DataSheet.xlsx";
        FileInputStream fis = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        DataFormatter formatter = new DataFormatter();

        int rows = workbook.getSheetAt(0).getPhysicalNumberOfRows();
        int cols = workbook.getSheetAt(0).getRow(0).getLastCellNum();

        Object[][] data = new Object[rows - 1][cols];
        int dataIndex = 0;

        for (int i = 1; i < rows; i++) {
            if (workbook.getSheetAt(0).getRow(i) == null) continue;
            for (int j = 0; j < cols; j++) {
                data[dataIndex][j] = formatter.formatCellValue(workbook.getSheetAt(0).getRow(i).getCell(j));
            }
            dataIndex++;
        }

        Object[][] finalData = new Object[dataIndex][cols];
        for (int i = 0; i < dataIndex; i++) finalData[i] = data[i];

        workbook.close();
        fis.close();
        return finalData;
    }

    // ✅ Now only 2 parameters
    @Test(dataProvider = "userData")
    public void signupTest(String name, String email) {
        test = extent.createTest("Signup Test for " + name);

        driver.get("https://automationexercise.com/login");
        signupPage.enterName(name);
        signupPage.enterEmail(email);
        signupPage.clickSignupButton();

        if (signupPage.isSignupFormDisplayed()) {
            test.pass("Signup form displayed successfully for: " + name);
        } else if (driver.getPageSource().contains("Email Address already exist!")) {
            test.warning("Email already exists: " + email);
        } else {
            test.fail("Signup failed for: " + name);
            Assert.fail("Signup form not displayed and no error shown!");
        }
    }
}

