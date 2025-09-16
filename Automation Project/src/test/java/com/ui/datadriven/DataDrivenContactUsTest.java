package com.ui.datadriven;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.AfterSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;

import java.io.File;
import java.util.List;
import com.ui.base.*;
import com.ui.pages.ContactUsPage;
import com.ui.utilities.ExcelUtils;
import com.ui.utilities.ScreenshotUtilities;

public class DataDrivenContactUsTest extends BaseTest {

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupExtentReport() {
        String reportPath = System.getProperty("user.dir") 
            + "/reports/datadriven_reports/ContactUsExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void executeTestCases() {
        String excelPath = System.getProperty("user.dir") + "/src/test/resources/contactus.xlsx";  
        List<String[]> testData = ExcelUtils.getTestData(excelPath);

        for (int i = 0; i < testData.size(); i++) {
            String testType = testData.get(i)[0].trim();
            String name = testData.get(i)[1].trim();
            String email = testData.get(i)[2].trim();
            String subject = testData.get(i)[3].trim();
            String message = testData.get(i)[4].trim();
            String file = testData.get(i)[5].trim();
            String expectedStatus = testData.get(i)[6].trim();

            runContactUsTest(testType, name, email, subject, message, file, expectedStatus, i);
        }
    }

    private void runContactUsTest(String testType, String name, String email, String subject,
                                  String message, String file, String expectedStatus, int testCaseNumber) {
        test = extent.createTest("Test Case " + testCaseNumber + " - " + testType);

        try {
            ContactUsPage contactUsPage = new ContactUsPage(driver);
            contactUsPage.navigateToContactUsPage();
            test.log(Status.INFO, "Navigated to Contact Us page");

            contactUsPage.fillContactForm(name, email, subject, message);
            test.log(Status.INFO, "Filled contact form with Name=" + name + ", Email=" + email);

            if (!file.equalsIgnoreCase("N/A") && !file.isEmpty()) {
                String filePath = System.getProperty("user.dir") + "/src/test/resources/" + file;
                driver.findElement(ContactUsPage.FILE_UPLOAD_INPUT).sendKeys(filePath);
                test.log(Status.INFO, "Uploaded file: " + filePath);
            }

            contactUsPage.clickSubmitButton();
            test.log(Status.INFO, "Clicked Submit button");

            boolean successDisplayed = contactUsPage.isFormSubmissionSuccessful();

            if ("Pass".equalsIgnoreCase(expectedStatus)) {
                if (!successDisplayed) {
                    throw new AssertionError("Expected success, but form submission failed");
                }
                test.log(Status.PASS, "Contact Us test passed for: " + testType);
            } else {
                if (successDisplayed) {
                    throw new AssertionError("Expected failure, but form submission succeeded");
                }
                test.log(Status.PASS, "Contact Us test failed as expected for: " + testType);
            }

        } catch (AssertionError ae) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "contactus_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Assertion failed: " + ae.getMessage());
            test.fail("Screenshot of failure", MediaEntityBuilder.createScreenCaptureFromPath(new File(screenshotPath).getAbsolutePath()).build());

        } catch (Exception e) {
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, "contactus_failure_" + testCaseNumber);
            test.log(Status.FAIL, "Unexpected error: " + e.getMessage());
            test.fail("Screenshot of failure", MediaEntityBuilder.createScreenCaptureFromPath(new File(screenshotPath).getAbsolutePath()).build());

        }
    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
    }
}
