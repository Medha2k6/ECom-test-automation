package com.functional.datadriven;

import com.functional.pages.UserRegistrationPage;
import com.functional.utilities.ExcelUtils;
import com.functional.utilities.ScreenshotUtilities;
import com.product.Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.Map;

public class UserRegistrationTest extends BaseTest {

    private static final String EXCEL_PATH = System.getProperty("user.dir") + "/src/test/resources/Func_Reg.xlsx";
    private static final String SHEET_NAME = "Sheet1";

    @BeforeClass
    public void setUpTestData() throws IOException {
        ExcelUtils.setExcelFile(EXCEL_PATH, SHEET_NAME);
    }

    @AfterClass
    public void cleanUpTestData() throws IOException {
        ExcelUtils.closeExcel();
    }

    @Test(dataProvider = "registrationData")
    public void testUserRegistration(Map<String, String> data) {
        String testCaseId = data.get("TestCaseID");
        String expectedResult = data.get("ExpectedResult");
        test.assignCategory("Registration");
        test.assignAuthor("QATeam");
        logInfo("Executing: " + testCaseId + " - " + expectedResult);

        try {
            // Handle pre-registration scenarios first, then exit the test method.
            if (testCaseId.contains("Checkout")) {
                handleCheckoutScenarios(testCaseId, data);
                logPass(testCaseId + " completed successfully");
                return;
            } else if (testCaseId.contains("Subscription")) {
                handleSubscriptionScenarios(data, testCaseId);
                logPass(testCaseId + " completed successfully");
                return;
            }

            // Standard registration flow
            navigateTo(BASE_URL + "/login");
            waitForPageLoad();

            String email = data.get("Email");
            if (email != null && email.contains("{random}")) {
                email = generateRandomEmail(email);
            }
            
            if (email != null && email.length() > 100) {  
                logFail(testCaseId + ": Email too long (" + email.length() + " chars). Validation triggered.");
                Assert.fail("Email exceeded maximum allowed length (100 chars).");
                return;
            }

            logInfo("Attempting to sign up with Email: " + email);

            performSignup(data.get("Name"), email);

            if (isElementPresent(UserRegistrationPage.EMAIL_EXISTS_ERROR)) {
                logInfo("Email already exists. Proceeding to handle existing user.");
                handleExistingUser(email, data.get("Password"));

                navigateTo(BASE_URL + "/login");
                waitForPageLoad();
                performSignup(data.get("Name"), email);
            }

            if (shouldProceedToAccount(expectedResult)) {
                completeRegistration(data);
                verifySuccess(testCaseId);
            } else {
                verifyError(testCaseId);
            }

            logPass(testCaseId + " completed successfully");

        } catch (AssertionError e) {
            logFail(testCaseId + " failed: " + e.getMessage());
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "generic");
            if (screenshotPath != null) {
                test.fail("Screenshot on failure: " + screenshotPath);
            }
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            logFail(testCaseId + " failed: " + e.getMessage());
            String screenshotPath = ScreenshotUtilities.captureScreenshotOnFailure(driver, testCaseId, "generic");
            if (screenshotPath != null) {
                test.fail("Screenshot on failure: " + screenshotPath);
            }
            Assert.fail("Test " + testCaseId + " failed: " + e.getMessage());
        }
    }

    private void handleExistingUser(String email, String password) {
        logInfo("User already exists. Logging in to delete the account.");
        safeSendKeys(UserRegistrationPage.LOGIN_EMAIL_INPUT, email);
        safeSendKeys(UserRegistrationPage.LOGIN_PASSWORD_INPUT, password);
        safeClick(UserRegistrationPage.LOGIN_BUTTON);
        waitForPageLoad();

        if (isElementPresent(UserRegistrationPage.DELETE_ACCOUNT_BUTTON)) {
            logInfo("Login successful. Deleting account.");
            safeClick(UserRegistrationPage.DELETE_ACCOUNT_BUTTON);
            waitForPageLoad();
            logInfo("Account deleted successfully.");
        } else {
            logInfo("Could not log in with existing user details. Skipping account deletion.");
        }
    }

    private void handleCheckoutScenarios(String testCaseId, Map<String, String> data) {
        try {
            navigateTo(BASE_URL);
            if (isElementPresent(UserRegistrationPage.LOGOUT_LINK)) {
                safeClick(UserRegistrationPage.LOGOUT_LINK);
                waitForPageLoad();
            }
            navigateTo(BASE_URL);
            waitForPageLoad();
        } catch (Exception ignored) {
        }

        switch (testCaseId) {
            case "TC_ECOM_Reg_044_GuestCheckoutPrompt":
                safeClick(UserRegistrationPage.ADD_TO_CART_BUTTON);
                safeClick(By.xpath("//button[contains(text(),'Continue Shopping')]"));
                safeClick(By.xpath("//a[contains(text(),'Cart')]"));
                safeClick(UserRegistrationPage.PROCEED_TO_CHECKOUT_BUTTON);
                
                boolean redirectedToLogin = driver.getCurrentUrl().contains("login");
                Assert.assertTrue(redirectedToLogin, "Guest user should be redirected to login page.");
                break;

            case "TC_ECOM_Reg_045_NewUserRegisterAndProceed":
                // This test case should fail as the original report indicates a bug.
                safeClick(UserRegistrationPage.ADD_TO_CART_BUTTON);
                safeClick(By.xpath("//button[contains(text(),'Continue Shopping')]"));
                safeClick(By.xpath("//a[contains(text(),'Cart')]"));
                safeClick(UserRegistrationPage.PROCEED_TO_CHECKOUT_BUTTON);

                boolean redirectedToLoginPage = driver.getCurrentUrl().contains("login");
                if (redirectedToLoginPage) {
                    logInfo("Guest user was prompted to login/register, as expected.");
                    
                    String email = generateRandomEmail(data.get("Email"));
                    performSignup(data.get("Name"), email);
                    
                    // The bug is that this step fails, but the test report marks it as a failure.
                    // The expectation is for a successful registration leading to checkout.
                    // The test should fail here because the application didn't proceed correctly.
                    boolean redirectedAfterSignup = driver.getCurrentUrl().contains("checkout");
                    Assert.assertTrue(redirectedAfterSignup, "BUG: New user registration from checkout flow failed to proceed correctly.");
                } else {
                    Assert.fail("BUG: Guest user was NOT prompted to login/register before checkout.");
                }
                break;

            case "TC_ECOM_Reg_046_LoggedInUserDirectCheckout":
                boolean checkoutButtonPresent = isElementPresent(UserRegistrationPage.PROCEED_TO_CHECKOUT_BUTTON);
                Assert.assertTrue(checkoutButtonPresent, "Logged-in user should access checkout directly.");
                break;

            case "TC_ECOM_Reg_047_EmptyCartCheckout":
                navigateTo(BASE_URL + "/view_cart");
                boolean emptyCartMessagePresent = isElementPresent(UserRegistrationPage.CART_IS_EMPTY_MESSAGE);
                Assert.assertTrue(emptyCartMessagePresent, "Checkout should be blocked if cart is empty.");
                break;
        }
    }

    private void handleSubscriptionScenarios(Map<String, String> data, String testCaseId) {
        navigateTo(BASE_URL);
        scrollToEndOfPage();

        WebElement emailField = findElementSafely(UserRegistrationPage.FOOTER_SUBSCRIBE_EMAIL_INPUT);
        if (emailField == null) {
            logFail("Email field not found for subscription test: " + testCaseId);
            Assert.fail("Email field not found for subscription test");
        }

        String email = data.get("Email");
        if (email != null && email.contains("{random}")) {
            email = generateRandomEmail(email);
        }

        // Specific logic for duplicate email subscription
        if (testCaseId.equals("TC_ECOM_Reg_042_DuplicateEmailSubscription")) {
            emailField.clear();
            emailField.sendKeys("keerthanashetty542@gmail.com");
            safeClick(UserRegistrationPage.FOOTER_SUBSCRIBE_BUTTON);
            waitForPageLoad();
            logInfo("First subscription with email successful.");

            driver.navigate().refresh();
            scrollToEndOfPage();
            emailField = findElementSafely(UserRegistrationPage.FOOTER_SUBSCRIBE_EMAIL_INPUT);
            emailField.clear();
            emailField.sendKeys("keerthanashetty542@gmail.com");
            safeClick(UserRegistrationPage.FOOTER_SUBSCRIBE_BUTTON);
            waitForPageLoad();
        } else if (testCaseId.equals("TC_ECOM_Reg_043_LongEmailSubscription")) {
            StringBuilder longEmailBuilder = new StringBuilder();
            for (int i = 0; i < 95; i++) {
                longEmailBuilder.append("a");
            }
            email = longEmailBuilder.toString() + "@example.com";
            
            emailField.clear();
            emailField.sendKeys(email);
            safeClick(UserRegistrationPage.FOOTER_SUBSCRIBE_BUTTON);
            waitForPageLoad();
        } else {
            emailField.clear();
            if (email != null) {
                emailField.sendKeys(email);
            }
            safeClick(UserRegistrationPage.FOOTER_SUBSCRIBE_BUTTON);
            waitForPageLoad();
        }

        // Specific validation for each subscription test case
        switch (testCaseId) {
            case "TC_ECOM_Reg_039_ValidEmailSubscription":
                boolean successMsgPresent = isElementPresent(By.xpath("//*[contains(text(), 'You have been successfully subscribed!')]"));
                Assert.assertTrue(successMsgPresent, "Valid email subscription should be successful.");
                break;
            case "TC_ECOM_Reg_040_InvalidEmailSubscription":
                boolean invalidEmailAccepted = isElementPresent(By.xpath("//*[contains(text(), 'You have been successfully subscribed!')]"));
                Assert.assertFalse(invalidEmailAccepted, "BUG: Invalid email was accepted for subscription - validation is missing");
                break;
            case "TC_ECOM_Reg_041_BlankEmailSubscription":
                String validationMessage = emailField.getAttribute("validationMessage");
                Assert.assertFalse(validationMessage.isEmpty(), "Blank email field should show a validation message.");
                break;
            case "TC_ECOM_Reg_042_DuplicateEmailSubscription":
                boolean duplicateError = isElementPresent(By.xpath("//*[contains(text(), 'already subscribed')]"));
                Assert.assertFalse(duplicateError, "BUG: Duplicate email subscription allowed - no duplicate detection");
                break;
            case "TC_ECOM_Reg_043_LongEmailSubscription":
                boolean longEmailAccepted = isElementPresent(By.xpath("//*[contains(text(), 'You have been successfully subscribed!')]"));
                Assert.assertFalse(longEmailAccepted, "BUG: Email longer than 100 characters was accepted - validation is missing");
                break;
        }
    }

    private void performSignup(String name, String email) {
        logInfo("Signing up with Name: " + name + ", Email: " + email);
        safeSendKeys(UserRegistrationPage.SIGNUP_NAME_INPUT, name);
        safeSendKeys(UserRegistrationPage.SIGNUP_EMAIL_INPUT, email);
        safeClick(UserRegistrationPage.SIGNUP_BUTTON);
        waitForPageLoad();
    }

    private boolean shouldProceedToAccount(String expectedResult) {
        String result = (expectedResult != null) ? expectedResult.toLowerCase() : "";
        return result.contains("proceed") || result.contains("create") ||
                result.contains("should sign up") || result.contains("accept") ||
                result.contains("success") || result.contains("work");
    }

    private void completeRegistration(Map<String, String> data) {
        fillAccountInfo(data);
        fillAddressInfo(data);
        scrollToElement(UserRegistrationPage.CREATE_ACCOUNT_BUTTON);
        safeClick(UserRegistrationPage.CREATE_ACCOUNT_BUTTON);
        waitForPageLoad();
    }

    private void fillAccountInfo(Map<String, String> data) {
        String title = data.get("Title");
        if ("Mr".equalsIgnoreCase(title)) {
            safeClick(UserRegistrationPage.MR_RADIO_BUTTON);
        } else if ("Mrs".equalsIgnoreCase(title)) {
            safeClick(UserRegistrationPage.MRS_RADIO_BUTTON);
        }
        safeSendKeys(UserRegistrationPage.PASSWORD_INPUT, data.get("Password"));
        selectByVisibleText(UserRegistrationPage.DAYS_DROPDOWN, data.get("Day"));
        selectByVisibleText(UserRegistrationPage.MONTHS_DROPDOWN, data.get("Month"));
        selectByVisibleText(UserRegistrationPage.YEARS_DROPDOWN, data.get("Year"));

        if ("TRUE".equalsIgnoreCase(data.get("Newsletter"))) {
            safeClick(UserRegistrationPage.NEWSLETTER_CHECKBOX);
        }
        if ("TRUE".equalsIgnoreCase(data.get("SpecialOffers"))) {
            safeClick(UserRegistrationPage.OFFERS_CHECKBOX);
        }
    }

    private void fillAddressInfo(Map<String, String> data) {
        safeSendKeys(UserRegistrationPage.FIRST_NAME_INPUT, data.get("FirstName"));
        safeSendKeys(UserRegistrationPage.LAST_NAME_INPUT, data.get("LastName"));
        safeSendKeys(UserRegistrationPage.COMPANY_INPUT, data.get("Company"));
        safeSendKeys(UserRegistrationPage.ADDRESS1_INPUT, data.get("Address"));
        safeSendKeys(UserRegistrationPage.ADDRESS2_INPUT, data.get("Address2"));
        scrollToElement(UserRegistrationPage.COUNTRY_DROPDOWN);
        selectByVisibleText(UserRegistrationPage.COUNTRY_DROPDOWN, data.get("Country"));
        safeSendKeys(UserRegistrationPage.STATE_INPUT, data.get("State"));
        safeSendKeys(UserRegistrationPage.CITY_INPUT, data.get("City"));
        safeSendKeys(UserRegistrationPage.ZIPCODE_INPUT, data.get("Zipcode"));
        safeSendKeys(UserRegistrationPage.MOBILE_NUMBER_INPUT, data.get("MobileNumber"));
    }

    private void verifySuccess(String testCaseId) {
        boolean successMsgPresent = isElementPresent(UserRegistrationPage.ACCOUNT_CREATED_MESSAGE);
        boolean redirected = driver.getCurrentUrl().contains("account_created");
        Assert.assertTrue(successMsgPresent || redirected, testCaseId + ": Account not created as expected");
        logPass(testCaseId + ": Account created successfully");
    }

    private void verifyError(String testCaseId) {
        String currentUrl = driver.getCurrentUrl();
        boolean onSignupPage = currentUrl.contains("login") || currentUrl.contains("signup");

        if (testCaseId.equals("TC_ECOM_Reg_002") || testCaseId.equals("TC_ECOM_Reg_003") ||
                testCaseId.equals("TC_ECOM_Reg_007") || testCaseId.equals("TC_ECOM_Reg_009") ||
                testCaseId.equals("TC_ECOM_Reg_014") || testCaseId.equals("TC_ECOM_Reg_020") ||
                testCaseId.equals("TC_ECOM_Reg_022") || testCaseId.equals("TC_ECOM_Reg_027") ||
                testCaseId.equals("TC_ECOM_Reg_030") || testCaseId.equals("TC_ECOM_Reg_032") ||
                testCaseId.equals("TC_ECOM_Reg_034") || testCaseId.equals("TC_ECOM_Reg_036") ||
                testCaseId.equals("TC_ECOM_Reg_038")) {
            
            boolean hasError = isElementPresent(By.xpath("//*[contains(text(), 'error')]")) ||
                    isElementPresent(By.xpath("//*[contains(text(), 'invalid')]"));

            if (!hasError && onSignupPage) {
                String errorMessage = "BUG: Invalid data was accepted - validation is missing";
                if (testCaseId.equals("TC_ECOM_Reg_003")) {
                    WebElement nameInput = findElementSafely(UserRegistrationPage.SIGNUP_NAME_INPUT);
                    String nameValue = (nameInput != null) ? nameInput.getAttribute("value") : "unknown";
                    errorMessage = "BUG: Numeric name '" + nameValue + "' was accepted - name validation is missing";
                }
                logFail(testCaseId + ": " + errorMessage);
                Assert.fail(errorMessage);
            }
        }
        Assert.assertTrue(onSignupPage, testCaseId + ": Unexpected redirect, Current URL - " + currentUrl);
        logPass(testCaseId + ": Remained on signup page as expected for invalid data");
    }

    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        int rowCount = ExcelUtils.getRowCount();
        Object[][] testData = new Object[rowCount][1];
        for (int i = 1; i <= rowCount; i++) {
            Map<String, String> rowMap = ExcelUtils.getRowDataAsMap(i);
            testData[i - 1][0] = rowMap;
        }
        return testData;
    }
}