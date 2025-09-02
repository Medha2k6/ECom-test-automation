package proj11.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import proj11.utils.ExtentReportManager;
import proj11.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ContactUsExtentTestListener implements ITestListener {
    
    private static final Map<String, String> testDescriptions = new HashMap<>();
    
    static {
        // Initialize test descriptions for Contact Us page
        testDescriptions.put("TC_Contact_Us_1_NavigateToContactUs", "Verify user can navigate to Contact Us page from home");
        testDescriptions.put("TC_Contact_Us_2_ProductsIcon", "Verify Products icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_3_CartIcon", "Verify Cart icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_4_SignupLoginIcon", "Verify Signup/Login icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_5_TestCasesIcon", "Verify Test Cases icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_6_APITestingIcon", "Verify API Testing icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_7_VideoTutorialsIcon", "Verify Video Tutorials icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_8_ContactUsIcon", "Verify Contact Us icon functionality from Contact Us page");
        testDescriptions.put("TC_Contact_Us_9_WebsiteLogo", "Verify presence of website logo on Contact Us page");
        testDescriptions.put("TC_Contact_Us_10_GetInTouchForm", "Verify presence of 'Get In Touch' form section");
        testDescriptions.put("TC_Contact_Us_11_NameFieldInput", "Verify name field accepts input in Contact form");
        testDescriptions.put("TC_Contact_Us_12_ValidEmailInput", "Verify email field accepts valid email format");
        testDescriptions.put("TC_Contact_Us_13_InvalidEmailInput", "Verify email field rejects invalid email format");
        testDescriptions.put("TC_Contact_Us_14_SubjectFieldInput", "Verify subject field allows input");
        testDescriptions.put("TC_Contact_Us_15_MessageFieldInput", "Verify message field allows input");
        testDescriptions.put("TC_Contact_Us_16_FileUpload", "Verify user can upload a file");
        testDescriptions.put("TC_Contact_Us_17_SendButtonEnabled", "Verify Send button is enabled after all fields are filled");
        testDescriptions.put("TC_Contact_Us_18_FormSubmissionValid", "Verify form submission with all valid inputs");
        testDescriptions.put("TC_Contact_Us_19_AlertAfterSubmission", "Verify alert appears after form submission");
        testDescriptions.put("TC_Contact_Us_20_FooterVisible", "Verify footer is visible on Contact Us page");
        testDescriptions.put("TC_Contact_Us_21_SubscriptionFieldVisible", "Verify subscription field is visible in footer");
        testDescriptions.put("TC_Contact_Us_22_SubscriptionValidEmail", "Verify subscription accepts valid email in Contact page");
        testDescriptions.put("TC_Contact_Us_23_SubscriptionInvalidEmail", "Verify email subscription field doesn't accept invalid email address");
        testDescriptions.put("TC_Contact_Us_24_ScrollBarPresence", "Verify presence of scroll bar on Contact Us page");
        testDescriptions.put("TC_Contact_Us_25_HomeIconRedirect", "Verify clicking 'Home' icon redirects to homepage");
        testDescriptions.put("TC_Contact_Us_26_LogoRedirect", "Verify clicking on automationexercise logo redirects to homepage");
        testDescriptions.put("TC_Contact_Us_27_BlankNameValidation", "Verify name field doesn't allow blank submission");
        testDescriptions.put("TC_Contact_Us_28_BlankEmailValidation", "Verify email field doesn't allow blank submission");
        testDescriptions.put("TC_Contact_Us_29_BlankSubjectValidation", "Verify subject field doesn't allow blank submission");
        testDescriptions.put("TC_Contact_Us_30_BlankMessageValidation", "Verify message field doesn't allow blank submission");
        testDescriptions.put("TC_Contact_Us_31_AllFieldsBlankExceptEmail", "Verify Contact Us form throws error when all fields are blank except email");
        testDescriptions.put("TC_Contact_Us_32_FormSubmissionWithoutFile", "Verify file upload is optional and form works without file");
        testDescriptions.put("TC_Contact_Us_33_FeedbackEmailLink", "Verify clicking feedback email link opens default email client");
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Contact Us test suite: " + context.getName());
        ExtentReportManager.initializeExtentReport();
        System.out.println("ExtentReports initialized for Contact Us suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing Contact Us test suite: " + context.getName());
        ExtentReportManager.flushReport();
        System.out.println("ExtentReports flushed for Contact Us suite: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = testDescriptions.getOrDefault(testName, "Contact Us page test: " + testName);
        
        try {
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.assignCategory("Contact Us Page Tests");
            ExtentReportManager.assignAuthor("QA Team");
            ExtentReportManager.logInfo("Starting test: " + testName);
            System.out.println("Starting test: " + testName);
        } catch (Exception e) {
            System.err.println("Error creating test in ExtentReports: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            ExtentReportManager.logPass("Test Passed: " + testName);
            
            // Capture screenshot on pass (optional)
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnPass(driver, testName);
                if (screenshotPath != null) {
                    ExtentReportManager.addScreenshot(screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error logging test success: " + e.getMessage());
        }
        
        System.out.println("PASS: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        
        try {
            ExtentReportManager.logFail("Test Failed: " + testName);
            ExtentReportManager.logFail("Error: " + errorMessage);
            
            // Capture screenshot on failure
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshotOnFailure(driver, testName);
                if (screenshotPath != null) {
                    ExtentReportManager.addScreenshot(screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error logging test failure: " + e.getMessage());
        }
        
        System.out.println("FAIL: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";
        
        try {
            String description = testDescriptions.getOrDefault(testName, "Contact Us page test: " + testName);
            ExtentReportManager.createTest(testName, description);
            ExtentReportManager.logSkip("Test Skipped: " + testName);
            ExtentReportManager.logSkip("Reason: " + skipReason);
        } catch (Exception e) {
            System.err.println("Error logging test skip: " + e.getMessage());
        }
        
        System.out.println("SKIP: " + testName + " - " + skipReason);
    }
    
    private WebDriver getDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(testInstance);
        } catch (Exception e) {
            System.err.println("Could not get driver instance for screenshot: " + e.getMessage());
            return null;
        }
    }
}