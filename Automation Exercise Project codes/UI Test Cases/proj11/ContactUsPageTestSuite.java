package proj11;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import proj11.listeners.ExtentTestListener;
import java.time.Duration;
import java.util.List;
import java.io.File;

@Listeners({ExtentTestListener.class})
public class ContactUsPageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    String contactUsUrl = "https://automationexercise.com/contact_us";

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) {
        try {
            System.out.println("Setting up test environment with browser: " + browser);
            
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                System.out.println("Chrome browser initialized successfully");
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                System.out.println("Edge browser initialized successfully");
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("brave")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                System.out.println("Firefox browser initialized successfully");
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);
            
            System.out.println("Navigating to: https://automationexercise.com/");
            driver.get("https://automationexercise.com/");
            System.out.println("Browser setup completed successfully");
        } catch (Exception e) {
            System.err.println("Error in test setup: " + e.getMessage());
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }
    }

    private void logResult(String testCase, boolean status) {
        if (status) {
            System.out.println("✅ PASS: " + testCase);
        } else {
            System.out.println("❌ FAIL: " + testCase);
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    private WebElement findElementSafely(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    @Test(priority = 1)
    public void TC_Contact_Us_1_NavigateToContactUs() {
        try {
            WebElement contactUsIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Contact us')]")));
            contactUsIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("contact_us") && 
                           driver.getPageSource().contains("Get In Touch");
            logResult("TC_Contact_Us_1 - Navigate to Contact Us page", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_1 - Navigate to Contact Us page", false);
        }
    }

    @Test(priority = 2)
    public void TC_Contact_Us_2_ProductsIcon() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            WebElement productsIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Products')]")));
            productsIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("products");
            logResult("TC_Contact_Us_2 - Products Icon Functionality", status);
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_2 - Products Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 3)
    public void TC_Contact_Us_3_CartIcon() {
        try {
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Cart')]")));
            cartIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("view_cart") || 
                           driver.getPageSource().contains("Shopping Cart");
            logResult("TC_Contact_Us_3 - Cart Icon Functionality", status);
            driver.get(contactUsUrl);
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_3 - Cart Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 4)
    public void TC_Contact_Us_4_SignupLoginIcon() {
        try {
            WebElement signupLoginIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]")));
            signupLoginIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("login");
            logResult("TC_Contact_Us_4 - Signup/Login Icon Functionality", status);
            driver.get(contactUsUrl);
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_4 - Signup/Login Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 5)
    public void TC_Contact_Us_5_TestCasesIcon() {
        try {
            WebElement testCasesIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Test Cases')]")));
            testCasesIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("test_cases");
            logResult("TC_Contact_Us_5 - Test Cases Icon Functionality", status);
            driver.get(contactUsUrl);
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_5 - Test Cases Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 6)
    public void TC_Contact_Us_6_APITestingIcon() {
        try {
            WebElement apiIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'API Testing')]")));
            apiIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("api_list");
            logResult("TC_Contact_Us_6 - API Testing Icon Functionality", status);
            driver.get(contactUsUrl);
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_6 - API Testing Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 7)
    public void TC_Contact_Us_7_VideoTutorialsIcon() {
        try {
            WebElement videoIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Video Tutorials')]")));
            videoIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("youtube") || 
                           driver.getTitle().toLowerCase().contains("video");
            logResult("TC_Contact_Us_7 - Video Tutorials Icon Functionality", status);
            driver.get(contactUsUrl);
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Contact_Us_7 - Video Tutorials Icon Functionality", false);
            driver.get(contactUsUrl);
        }
    }

    @Test(priority = 8)
    public void TC_Contact_Us_8_ContactUsIcon() {
        try {
            WebElement contactIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Contact us')]")));
            contactIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().contains("contact_us") && 
                           driver.getPageSource().contains("Get In Touch");
            logResult("TC_Contact_Us_8 - Contact Us Icon Functionality", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_8 - Contact Us Icon Functionality", false);
        }
    }

    @Test(priority = 9)
    public void TC_Contact_Us_9_WebsiteLogo() {
        try {
            WebElement logo = findElementSafely(By.xpath("//img[@alt='Website for automation practice']"));
            if (logo == null) {
                logo = findElementSafely(By.xpath("//img[contains(@src,'logo')]"));
            }
            
            boolean status = logo != null && logo.isDisplayed();
            logResult("TC_Contact_Us_9 - Website Logo Presence", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_9 - Website Logo Presence", false);
        }
    }

    @Test(priority = 10)
    public void TC_Contact_Us_10_GetInTouchForm() {
        try {
            WebElement getInTouchSection = findElementSafely(By.xpath("//h2[contains(text(),'Get In Touch')]"));
            if (getInTouchSection == null) {
                getInTouchSection = findElementSafely(By.xpath("//div[contains(@class,'contact-form')]"));
            }
            
            boolean status = getInTouchSection != null && getInTouchSection.isDisplayed();
            logResult("TC_Contact_Us_10 - Get In Touch Form Presence", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_10 - Get In Touch Form Presence", false);
        }
    }

    @Test(priority = 11)
    public void TC_Contact_Us_11_NameFieldInput() {
        try {
            WebElement nameField = findElementSafely(By.xpath("//input[@name='name']"));
            if (nameField == null) {
                nameField = findElementSafely(By.xpath("//input[@placeholder='Name']"));
            }
            
            if (nameField != null) {
                scrollToElement(nameField);
                nameField.clear();
                nameField.sendKeys("Darshini");
                
                boolean status = nameField.getAttribute("value").equals("Darshini");
                logResult("TC_Contact_Us_11 - Name Field Input", status);
            } else {
                logResult("TC_Contact_Us_11 - Name Field Input", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_11 - Name Field Input", false);
        }
    }

    @Test(priority = 12)
    public void TC_Contact_Us_12_ValidEmailInput() {
        try {
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            if (emailField == null) {
                emailField = findElementSafely(By.xpath("//input[@type='email']"));
            }
            
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys("darshini25@gmail.com");
                
                boolean status = emailField.getAttribute("value").contains("@");
                logResult("TC_Contact_Us_12 - Valid Email Input", status);
            } else {
                logResult("TC_Contact_Us_12 - Valid Email Input", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_12 - Valid Email Input", false);
        }
    }

    @Test(priority = 13)
    public void TC_Contact_Us_13_InvalidEmailInput() {
        try {
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            if (emailField == null) {
                emailField = findElementSafely(By.xpath("//input[@type='email']"));
            }
            
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys("invalid mail");
                
                // Try to submit to check validation
                WebElement submitBtn = findElementSafely(By.xpath("//input[@type='submit']"));
                if (submitBtn != null) {
                    submitBtn.click();
                    Thread.sleep(1000);
                    
                    // Check if form accepted invalid email (this would be a bug)
                    boolean formAcceptedInvalidEmail = driver.getPageSource().contains("Success") ||
                                                      driver.getPageSource().contains("submitted");
                    
                    // Check for HTML5 validation
                    String validationMessage = emailField.getAttribute("validationMessage");
                    boolean html5ValidationWorking = validationMessage != null && !validationMessage.isEmpty();
                    
                    // Test passes if either: form rejects submission OR HTML5 validation kicks in
                    boolean properValidation = !formAcceptedInvalidEmail || html5ValidationWorking;
                    
                    logResult("TC_Contact_Us_13 - Invalid Email Input Validation", properValidation);
                    
                    if (formAcceptedInvalidEmail && !html5ValidationWorking) {
                        Assert.fail("BUG: Invalid email 'invalid mail' was accepted - email validation is missing");
                    } else {
                        Assert.assertTrue(true, "Email validation working correctly");
                    }
                } else {
                    logResult("TC_Contact_Us_13 - Invalid Email Input", false);
                    Assert.fail("Submit button not found");
                }
            } else {
                logResult("TC_Contact_Us_13 - Invalid Email Input", false);
                Assert.fail("Email field not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_13 - Invalid Email Input", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 14)
    public void TC_Contact_Us_14_SubjectFieldInput() {
        try {
            WebElement subjectField = findElementSafely(By.xpath("//input[@name='subject']"));
            if (subjectField == null) {
                subjectField = findElementSafely(By.xpath("//input[@placeholder='Subject']"));
            }
            
            if (subjectField != null) {
                scrollToElement(subjectField);
                subjectField.clear();
                subjectField.sendKeys("Order Delay");
                
                boolean status = subjectField.getAttribute("value").equals("Order Delay");
                logResult("TC_Contact_Us_14 - Subject Field Input", status);
            } else {
                logResult("TC_Contact_Us_14 - Subject Field Input", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_14 - Subject Field Input", false);
        }
    }

    @Test(priority = 15)
    public void TC_Contact_Us_15_MessageFieldInput() {
        try {
            WebElement messageField = findElementSafely(By.xpath("//textarea[@name='message']"));
            if (messageField == null) {
                messageField = findElementSafely(By.xpath("//textarea[@placeholder='Your Message Here']"));
            }
            
            if (messageField != null) {
                scrollToElement(messageField);
                messageField.clear();
                messageField.sendKeys("I want refund");
                
                boolean status = messageField.getAttribute("value").equals("I want refund");
                logResult("TC_Contact_Us_15 - Message Field Input", status);
            } else {
                logResult("TC_Contact_Us_15 - Message Field Input", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_15 - Message Field Input", false);
        }
    }

    @Test(priority = 16)
    public void TC_Contact_Us_16_FileUpload() {
        try {
            WebElement fileInput = findElementSafely(By.xpath("//input[@type='file']"));
            if (fileInput == null) {
                fileInput = findElementSafely(By.xpath("//input[@name='upload_file']"));
            }
            
            if (fileInput != null) {
                // Create a test file
                String filePath = System.getProperty("user.dir") + "/testfile.txt";
                File testFile = new File(filePath);
                if (!testFile.exists()) {
                    testFile.createNewFile();
                }
                
                scrollToElement(fileInput);
                fileInput.sendKeys(filePath);
                
                boolean status = !fileInput.getAttribute("value").isEmpty();
                logResult("TC_Contact_Us_16 - File Upload", status);
            } else {
                logResult("TC_Contact_Us_16 - File Upload", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_16 - File Upload", false);
        }
    }

    @Test(priority = 17)
    public void TC_Contact_Us_17_SendButtonEnabled() {
        try {
            // Fill all fields first
            fillContactForm("Darshini", "darshini25@gmail.com", "Test Subject", "Test Message");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn == null) {
                sendBtn = findElementSafely(By.xpath("//button[contains(text(),'Submit')]"));
            }
            
            boolean status = sendBtn != null && sendBtn.isEnabled();
            logResult("TC_Contact_Us_17 - Send Button Enabled", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_17 - Send Button Enabled", false);
        }
    }

    @Test(priority = 18)
    public void TC_Contact_Us_18_FormSubmissionValid() {
        try {
            fillContactForm("Darshini", "darshini25@gmail.com", "Test Subject", "Test Message");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(3000);
                
                boolean status = driver.getPageSource().contains("Success") || 
                               driver.getPageSource().contains("submitted successfully") ||
                               driver.getPageSource().contains("Thank you");
                logResult("TC_Contact_Us_18 - Form Submission Valid", status);
                Assert.assertTrue(status);
            } else {
                logResult("TC_Contact_Us_18 - Form Submission Valid", false);
                Assert.fail("Submit button not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_18 - Form Submission Valid", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 19)
    public void TC_Contact_Us_19_AlertAfterSubmission() {
        try {
            fillContactForm("Darshini", "darshini25@gmail.com", "Test Subject", "Test Message");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check for alert
                boolean alertPresent = false;
                try {
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
                    alertPresent = true;
                } catch (Exception e) {
                    // No alert present - check for success message instead
                    alertPresent = driver.getPageSource().contains("Success") ||
                                 driver.getPageSource().contains("Thank you");
                }
                
                boolean status = alertPresent;
                logResult("TC_Contact_Us_19 - Alert After Submission", status);
            } else {
                logResult("TC_Contact_Us_19 - Alert After Submission", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_19 - Alert After Submission", false);
        }
    }

    @Test(priority = 20)
    public void TC_Contact_Us_20_FooterVisible() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement footer = findElementSafely(By.xpath("//footer"));
            if (footer == null) {
                footer = findElementSafely(By.xpath("//div[contains(@class,'footer')]"));
            }
            
            boolean status = footer != null && footer.isDisplayed();
            logResult("TC_Contact_Us_20 - Footer Visible", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_20 - Footer Visible", false);
        }
    }

    @Test(priority = 21)
    public void TC_Contact_Us_21_SubscriptionFieldVisible() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement subscriptionField = findElementSafely(By.id("susbscribe_email"));
            if (subscriptionField == null) {
                subscriptionField = findElementSafely(By.xpath("//input[contains(@placeholder,'email')]"));
            }
            
            boolean status = subscriptionField != null && subscriptionField.isDisplayed();
            logResult("TC_Contact_Us_21 - Subscription Field Visible", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_21 - Subscription Field Visible", false);
        }
    }

    @Test(priority = 22)
    public void TC_Contact_Us_22_SubscriptionValidEmail() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement subscriptionField = findElementSafely(By.id("susbscribe_email"));
            if (subscriptionField != null) {
                scrollToElement(subscriptionField);
                subscriptionField.clear();
                subscriptionField.sendKeys("darshini25@gmail.com");
                
                WebElement subscribeBtn = findElementSafely(By.id("subscribe"));
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(3000);
                    
                    boolean status = driver.getPageSource().toLowerCase().contains("subscribed successfully") ||
                                   driver.getPageSource().toLowerCase().contains("success");
                    logResult("TC_Contact_Us_22 - Subscription Valid Email", status);
                } else {
                    logResult("TC_Contact_Us_22 - Subscription Valid Email", false);
                }
            } else {
                logResult("TC_Contact_Us_22 - Subscription Valid Email", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_22 - Subscription Valid Email", false);
        }
    }

    @Test(priority = 23)
    public void TC_Contact_Us_23_SubscriptionInvalidEmail() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            
            WebElement subscriptionField = findElementSafely(By.id("susbscribe_email"));
            if (subscriptionField != null) {
                scrollToElement(subscriptionField);
                subscriptionField.clear();
                subscriptionField.sendKeys("invalid mail");
                
                WebElement subscribeBtn = findElementSafely(By.id("subscribe"));
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(2000);
                    
                    // Check if invalid email was accepted (this is the bug)
                    boolean invalidEmailAccepted = driver.getPageSource().toLowerCase().contains("subscribed successfully") || 
                                                 driver.getPageSource().toLowerCase().contains("success");
                    
                    // Test should FAIL if invalid email is accepted
                    logResult("TC_Contact_Us_23 - Subscription Invalid Email Validation", !invalidEmailAccepted);
                    
                    if (invalidEmailAccepted) {
                        Assert.fail("BUG: Invalid email 'invalid mail' was accepted for subscription - proper email validation is missing");
                    } else {
                        Assert.assertTrue(true, "Correct: Invalid email was properly rejected");
                    }
                } else {
                    logResult("TC_Contact_Us_23 - Subscription Invalid Email", false);
                    Assert.fail("Subscribe button not found");
                }
            } else {
                logResult("TC_Contact_Us_23 - Subscription Invalid Email", false);
                Assert.fail("Subscription field not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_23 - Subscription Invalid Email", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 24)
    public void TC_Contact_Us_24_ScrollBarPresence() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            Long bodyHeight = (Long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
            Long windowHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
            
            boolean scrollBarNeeded = bodyHeight > windowHeight;
            
            // Test scrolling functionality
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
            Thread.sleep(1000);
            Long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset");
            
            boolean scrollWorking = scrollPosition > 0;
            boolean status = scrollBarNeeded && scrollWorking;
            logResult("TC_Contact_Us_24 - Scroll Bar Presence", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_24 - Scroll Bar Presence", false);
        }
    }

    @Test(priority = 25)
    public void TC_Contact_Us_25_HomeIconRedirect() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            WebElement homeIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Home')]")));
            homeIcon.click();
            Thread.sleep(2000);
            
            boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Contact_Us_25 - Home Icon Redirect", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_25 - Home Icon Redirect", false);
        }
    }

    @Test(priority = 26)
    public void TC_Contact_Us_26_LogoRedirect() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            WebElement logo = findElementSafely(By.xpath("//img[@alt='Website for automation practice']"));
            if (logo == null) {
                logo = findElementSafely(By.xpath("//img[contains(@src,'logo')]"));
            }
            
            if (logo != null) {
                WebElement logoLink = logo.findElement(By.xpath("./ancestor::a[1]"));
                logoLink.click();
                Thread.sleep(2000);
                
                boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
                logResult("TC_Contact_Us_26 - Logo Redirect", status);
            } else {
                logResult("TC_Contact_Us_26 - Logo Redirect", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_26 - Logo Redirect", false);
        }
    }

    @Test(priority = 27)
    public void TC_Contact_Us_27_BlankNameValidation() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            fillContactFormExceptName("darshini25@gmail.com", "bad website", "processing slowly");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check if form was submitted (this should fail - form should not submit)
                boolean formSubmitted = driver.getPageSource().contains("Success") ||
                                      driver.getPageSource().contains("submitted");
                
                // Test passes if form does NOT submit (proper validation)
                boolean status = !formSubmitted;
                logResult("TC_Contact_Us_27 - Blank Name Validation", status);
                
                if (formSubmitted) {
                    Assert.fail("Form submitted without name - validation failed");
                } else {
                    Assert.assertTrue(true, "Form properly rejected blank name field");
                }
            } else {
                logResult("TC_Contact_Us_27 - Blank Name Validation", false);
                Assert.fail("Submit button not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_27 - Blank Name Validation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 28)
    public void TC_Contact_Us_28_BlankEmailValidation() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            // Fill all except email
            WebElement nameField = findElementSafely(By.xpath("//input[@name='name']"));
            WebElement subjectField = findElementSafely(By.xpath("//input[@name='subject']"));
            WebElement messageField = findElementSafely(By.xpath("//textarea[@name='message']"));
            
            if (nameField != null) nameField.sendKeys("Test Name");
            if (subjectField != null) subjectField.sendKeys("bad website");
            if (messageField != null) messageField.sendKeys("processing slowly");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check for validation message
                WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
                String validationMessage = "";
                if (emailField != null) {
                    validationMessage = emailField.getAttribute("validationMessage");
                }
                
                boolean status = validationMessage != null && 
                               validationMessage.toLowerCase().contains("fill");
                logResult("TC_Contact_Us_28 - Blank Email Validation", status);
            } else {
                logResult("TC_Contact_Us_28 - Blank Email Validation", false);
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_28 - Blank Email Validation", false);
        }
    }

    @Test(priority = 29)
    public void TC_Contact_Us_29_BlankSubjectValidation() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            // Fill all except subject
            WebElement nameField = findElementSafely(By.xpath("//input[@name='name']"));
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            WebElement messageField = findElementSafely(By.xpath("//textarea[@name='message']"));
            
            if (nameField != null) nameField.sendKeys("a");
            if (emailField != null) emailField.sendKeys("a@gmail.com");
            if (messageField != null) messageField.sendKeys("processing slowly");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check if form was submitted (should not submit without subject)
                boolean formSubmitted = driver.getPageSource().contains("Success");
                boolean status = !formSubmitted; // Test passes if form does NOT submit
                
                logResult("TC_Contact_Us_29 - Blank Subject Validation", status);
                
                if (formSubmitted) {
                    Assert.fail("Form submitted without subject - validation failed");
                } else {
                    Assert.assertTrue(true, "Form properly rejected blank subject field");
                }
            } else {
                logResult("TC_Contact_Us_29 - Blank Subject Validation", false);
                Assert.fail("Submit button not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_29 - Blank Subject Validation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 30)
    public void TC_Contact_Us_30_BlankMessageValidation() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            // Fill all except message
            WebElement nameField = findElementSafely(By.xpath("//input[@name='name']"));
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            WebElement subjectField = findElementSafely(By.xpath("//input[@name='subject']"));
            
            if (nameField != null) nameField.sendKeys("a");
            if (emailField != null) emailField.sendKeys("a@gmail.com");
            if (subjectField != null) subjectField.sendKeys("bad website");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check if form was submitted (should not submit without message)
                boolean formSubmitted = driver.getPageSource().contains("Success");
                boolean status = !formSubmitted; // Test passes if form does NOT submit
                
                logResult("TC_Contact_Us_30 - Blank Message Validation", status);
                
                if (formSubmitted) {
                    Assert.fail("Form submitted without message - validation failed");
                } else {
                    Assert.assertTrue(true, "Form properly rejected blank message field");
                }
            } else {
                logResult("TC_Contact_Us_30 - Blank Message Validation", false);
                Assert.fail("Submit button not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_30 - Blank Message Validation", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 31)
    public void TC_Contact_Us_31_AllFieldsBlankExceptEmail() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            // Only fill email
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            if (emailField != null) {
                emailField.sendKeys("a@gmail.com");
            }
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn != null) {
                sendBtn.click();
                Thread.sleep(2000);
                
                // Check if form was submitted (should not submit with only email)
                boolean formSubmitted = driver.getPageSource().contains("Success");
                boolean status = !formSubmitted; // Test passes if form does NOT submit
                
                logResult("TC_Contact_Us_31 - All Fields Blank Except Email", status);
                
                if (formSubmitted) {
                    Assert.fail("Form submitted with only email - validation failed");
                } else {
                    Assert.assertTrue(true, "Form properly rejected incomplete submission");
                }
            } else {
                logResult("TC_Contact_Us_31 - All Fields Blank Except Email", false);
                Assert.fail("Submit button not found");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_31 - All Fields Blank Except Email", false);
            Assert.fail(e.getMessage());
        }
    }

    @Test(priority = 32)
    public void TC_Contact_Us_32_FormSubmissionWithoutFile() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(2000);
            
            // Fill all required fields but no file upload
            fillContactForm("Test Name", "test@example.com", "Test Subject", "Test Message");
            
            WebElement sendBtn = findElementSafely(By.xpath("//input[@type='submit']"));
            if (sendBtn == null) {
                sendBtn = findElementSafely(By.xpath("//input[@value='Submit']"));
            }
            if (sendBtn == null) {
                sendBtn = findElementSafely(By.xpath("//button[contains(text(),'Submit')]"));
            }
            
            if (sendBtn != null) {
                scrollToElement(sendBtn);
                sendBtn.click();
                Thread.sleep(5000); // Wait longer for submission
                
                // Check for success messages (multiple variations)
                String pageSource = driver.getPageSource();
                boolean status = pageSource.contains("Success! Your details have been submitted successfully") ||
                               pageSource.contains("Success") ||
                               pageSource.contains("submitted successfully") ||
                               pageSource.contains("Thank you") ||
                               pageSource.contains("message sent");
                
                logResult("TC_Contact_Us_32 - Form Submission Without File", status);
                
                if (status) {
                    Assert.assertTrue(true, "Form submitted successfully without file upload - file upload is optional as expected");
                } else {
                    // Debug: Print what we actually got
                    System.out.println("DEBUG - Page source contains: ");
                    System.out.println("Current URL: " + driver.getCurrentUrl());
                    System.out.println("Page title: " + driver.getTitle());
                    
                    // Check for alert
                    try {
                        Alert alert = driver.switchTo().alert();
                        System.out.println("Alert text: " + alert.getText());
                        alert.accept();
                        status = true; // If there's an alert, consider it success
                        logResult("TC_Contact_Us_32 - Form Submission Without File (via alert)", status);
                        Assert.assertTrue(true, "Form submitted successfully via alert");
                    } catch (Exception alertException) {
                        Assert.fail("Form submission failed - no success message found and no alert. Page source: " + 
                                  pageSource.substring(0, Math.min(500, pageSource.length())));
                    }
                }
            } else {
                logResult("TC_Contact_Us_32 - Form Submission Without File", false);
                Assert.fail("Submit button not found - tried multiple selectors");
            }
        } catch (Exception e) {
            logResult("TC_Contact_Us_32 - Form Submission Without File", false);
            Assert.fail("Exception during test: " + e.getMessage());
        }
    }

    @Test(priority = 33)
    public void TC_Contact_Us_33_FeedbackEmailLink() {
        try {
            driver.get(contactUsUrl);
            Thread.sleep(1000);
            
            WebElement emailLink = findElementSafely(By.xpath("//a[contains(@href,'mailto:feedback@automationexercise.com')]"));
            if (emailLink == null) {
                emailLink = findElementSafely(By.xpath("//a[contains(text(),'feedback@automationexercise.com')]"));
            }
            
            boolean status = emailLink != null && emailLink.isDisplayed();
            if (status) {
                // Don't actually click as it opens email client
                String href = emailLink.getAttribute("href");
                status = href != null && href.contains("mailto:");
            }
            
            logResult("TC_Contact_Us_33 - Feedback Email Link", status);
        } catch (Exception e) {
            logResult("TC_Contact_Us_33 - Feedback Email Link", false);
        }
    }

    // Helper methods
    private void fillContactForm(String name, String email, String subject, String message) {
        try {
            WebElement nameField = findElementSafely(By.xpath("//input[@name='name']"));
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            WebElement subjectField = findElementSafely(By.xpath("//input[@name='subject']"));
            WebElement messageField = findElementSafely(By.xpath("//textarea[@name='message']"));
            
            if (nameField != null) {
                scrollToElement(nameField);
                nameField.clear();
                nameField.sendKeys(name);
            }
            
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys(email);
            }
            
            if (subjectField != null) {
                scrollToElement(subjectField);
                subjectField.clear();
                subjectField.sendKeys(subject);
            }
            
            if (messageField != null) {
                scrollToElement(messageField);
                messageField.clear();
                messageField.sendKeys(message);
            }
            
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error filling contact form: " + e.getMessage());
        }
    }
    
    private void fillContactFormExceptName(String email, String subject, String message) {
        try {
            WebElement emailField = findElementSafely(By.xpath("//input[@name='email']"));
            WebElement subjectField = findElementSafely(By.xpath("//input[@name='subject']"));
            WebElement messageField = findElementSafely(By.xpath("//textarea[@name='message']"));
            
            if (emailField != null) {
                scrollToElement(emailField);
                emailField.clear();
                emailField.sendKeys(email);
            }
            
            if (subjectField != null) {
                scrollToElement(subjectField);
                subjectField.clear();
                subjectField.sendKeys(subject);
            }
            
            if (messageField != null) {
                scrollToElement(messageField);
                messageField.clear();
                messageField.sendKeys(message);
            }
            
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error filling contact form: " + e.getMessage());
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            try {
                System.out.println("Closing browser and cleaning up test environment");
                driver.quit();
                System.out.println("Test environment cleanup completed");
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}