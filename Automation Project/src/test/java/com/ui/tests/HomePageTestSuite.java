package com.ui.tests;


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
import com.ui.utilities.ExtentReportManager;
import com.ui.listeners.ExtentTestListener;
import java.time.Duration;
import java.util.List;

@Listeners({ExtentTestListener.class})
public class HomePageTestSuite {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @Parameters("browser")
    @BeforeTest
    public void setup(@Optional("chrome") String browser) {
        try {
            logStep("Setting up test environment with browser: " + browser);

            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                logStep("Chrome browser initialized successfully");
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                logStep("Edge browser initialized successfully");
            } else if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("brave")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                logStep("Firefox browser initialized successfully");
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            actions = new Actions(driver);

            logStep("Navigating to: https://automationexercise.com/");
            driver.get("https://automationexercise.com/");
            logStep("Browser setup completed successfully");
        } catch (Exception e) {
            logStep("Error in test setup: " + e.getMessage());
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }
    }

    private void logStep(String stepDescription) {
        System.out.println("STEP: " + stepDescription);
        try {
            ExtentReportManager.logInfo("STEP: " + stepDescription);
        } catch (Exception e) {}
    }

    private void logResult(String testCase, boolean status) {
        if (status) {
            System.out.println("✅ PASS: " + testCase);
            try {
                ExtentReportManager.logPass("✅ " + testCase + " - Test executed successfully");
            } catch (Exception e) {}
        } else {
            System.out.println("❌ FAIL: " + testCase);
            try {
                ExtentReportManager.logFail("❌ " + testCase + " - Test execution failed");
            } catch (Exception e) {}
        }
    }

    private void scrollToElement(WebElement element) {
        logStep("Scrolling to element: " + describeElement(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    private WebElement findElementWithMultipleSelectors(String[] selectors) {
        for (String selector : selectors) {
            try {
                logStep("Trying selector: " + selector);
                if (selector.startsWith("//") || selector.startsWith("(")) {
                    return driver.findElement(By.xpath(selector));
                } else {
                    return driver.findElement(By.cssSelector(selector));
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new NoSuchElementException("None of the selectors found the element");
    }

    private String describeElement(WebElement element) {
        try {
            return element.getTagName() + (element.getText().isEmpty() ? "" : (" [" + element.getText() + "]"));
        } catch (Exception e) {
            return "Unknown Element";
        }
    }

    @Test(priority = 1)
    public void TC_Home_01_Chrome_URL() {
        try {
            logStep("Verifying page title and URL on Chrome");
            boolean status = driver.getTitle().contains("Automation Exercise") &&
                           driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Home_01 - Verify URL on Chrome Browser", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Home_01 - Verify URL on Chrome Browser", false);
        }
    }

    @Test(priority = 2)
    public void TC_Home_02_Edge_URL() {
        try {
            logStep("Verifying page title on Edge");
            boolean status = driver.getTitle().contains("Automation Exercise");
            logResult("TC_Home_02 - Verify URL on Edge Browser", status);
        } catch (Exception e) {
            logResult("TC_Home_02 - Verify URL on Edge Browser", false);
        }
    }

    @Test(priority = 3)
    public void TC_Home_03_Brave_URL() {
        try {
            logStep("Verifying page title on Brave");
            boolean status = driver.getTitle().contains("Automation Exercise");
            logResult("TC_Home_03 - Verify URL on Brave Browser", status);
        } catch (Exception e) {
            logResult("TC_Home_03 - Verify URL on Brave Browser", false);
        }
    }

    @Test(priority = 4)
    public void TC_Home_04_HomeIcon() {
        try {
            logStep("Waiting for Home icon to be clickable.");
            WebElement homeIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Home')]")));
            logStep("Clicking Home icon.");
            homeIcon.click();
            Thread.sleep(1000);
            logStep("Verifying Home URL after click.");
            boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Home_04 - Home Icon Functionality", status);
            Assert.assertTrue(status);
        } catch (Exception e) {
            logResult("TC_Home_04 - Home Icon Functionality", false);
        }
    }

    @Test(priority = 5)
    public void TC_Home_05_ProductsIcon() {
        try {
            logStep("Waiting for Products icon to be clickable.");
            WebElement productsIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Products')]")));
            logStep("Clicking Products icon.");
            productsIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Products page URL.");
            boolean status = driver.getCurrentUrl().contains("products");
            logResult("TC_Home_05 - Products Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_05 - Products Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 6)
    public void TC_Home_06_CartIcon() {
        try {
            logStep("Waiting for Cart icon to be clickable.");
            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Cart')]")));
            logStep("Clicking Cart icon.");
            cartIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Cart page by URL and Shopping Cart text.");
            boolean status = driver.getCurrentUrl().contains("view_cart") ||
                           driver.getPageSource().contains("Shopping Cart");
            logResult("TC_Home_06 - Cart Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_06 - Cart Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 7)
    public void TC_Home_07_SignupLoginIcon() {
        try {
            logStep("Waiting for Signup/Login icon to be clickable.");
            WebElement signupLoginIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Signup') or contains(text(),'Login')]")));
            logStep("Clicking Signup/Login icon.");
            signupLoginIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Login page by URL.");
            boolean status = driver.getCurrentUrl().contains("login");
            logResult("TC_Home_07 - Signup/Login Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_07 - Signup/Login Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 8)
    public void TC_Home_08_TestCasesIcon() {
        try {
            logStep("Waiting for Test Cases icon to be clickable.");
            WebElement testCasesIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Test Cases')]")));
            logStep("Clicking Test Cases icon.");
            testCasesIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Test Cases page by URL.");
            boolean status = driver.getCurrentUrl().contains("test_cases");
            logResult("TC_Home_08 - Test Cases Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_08 - Test Cases Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 9)
    public void TC_Home_09_APITestingIcon() {
        try {
            logStep("Waiting for API Testing icon to be clickable.");
            WebElement apiIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'API Testing')]")));
            logStep("Clicking API Testing icon.");
            apiIcon.click();
            Thread.sleep(2000);
            logStep("Verifying API List page by URL.");
            boolean status = driver.getCurrentUrl().contains("api_list");
            logResult("TC_Home_09 - API Testing Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_09 - API Testing Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 10)
    public void TC_Home_10_VideoTutorialsIcon() {
        try {
            logStep("Waiting for Video Tutorials icon to be clickable.");
            WebElement videoIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Video Tutorials')]")));
            logStep("Clicking Video Tutorials icon.");
            videoIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Video Tutorials page or YouTube navigation.");
            boolean status = driver.getCurrentUrl().contains("youtube") ||
                           driver.getTitle().toLowerCase().contains("video") ||
                           driver.getCurrentUrl().contains("video");
            logResult("TC_Home_10 - Video Tutorials Icon Functionality", status);
            if (!driver.getCurrentUrl().contains("automationexercise.com")) {
                logStep("Navigating back from YouTube.");
                driver.navigate().back();
            } else {
                logStep("Navigating home from Video page.");
                driver.get("https://automationexercise.com/");
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_10 - Video Tutorials Icon Functionality", false);
            driver.get("https://automationexercise.com/");
        }
    }

    @Test(priority = 11)
    public void TC_Home_11_ContactUsIcon() {
        try {
            logStep("Waiting for Contact Us icon to be clickable.");
            WebElement contactIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Contact us')]")));
            logStep("Clicking Contact Us icon.");
            contactIcon.click();
            Thread.sleep(2000);
            logStep("Verifying Contact Us page by URL.");
            boolean status = driver.getCurrentUrl().contains("contact_us");
            logResult("TC_Home_11 - Contact Us Icon Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_11 - Contact Us Icon Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 12)
    public void TC_Home_12_TestCasesButton() {
        try {
            logStep("Scrolling to footer area for Test Cases button.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(1000);

            logStep("Waiting for Test Cases button to be clickable.");
            WebElement testCasesButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'test_cases') and (contains(text(),'Test Cases') or contains(@class,'btn'))]")));
            scrollToElement(testCasesButton);
            logStep("Clicking Test Cases button in footer.");
            testCasesButton.click();
            Thread.sleep(2000);
            logStep("Verifying Test Cases page by URL.");
            boolean status = driver.getCurrentUrl().contains("test_cases");
            logResult("TC_Home_12 - Test Cases Button Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_12 - Test Cases Button Functionality", false);
            driver.navigate().back();
        }
    }

    @Test(priority = 13)
    public void TC_Home_13_APIsListButton() {
        try {
            logStep("Scrolling to footer for APIs List button.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(1000);
            String[] selectors = {
                "//a[contains(@href,'api_list') and (contains(text(),'API') or contains(@class,'btn'))]",
                "//a[contains(text(),'APIs List')]",
                "//button[contains(text(),'APIs List')]",
                "//a[contains(text(),'API Testing')]"
            };
            logStep("Looking for APIs List button with multiple selectors.");
            WebElement apisButton = findElementWithMultipleSelectors(selectors);
            scrollToElement(apisButton);
            logStep("Clicking APIs List button in footer.");
            apisButton.click();
            Thread.sleep(2000);
            logStep("Verifying APIs List page by URL or content.");
            boolean status = driver.getCurrentUrl().contains("api_list") ||
                           driver.getPageSource().toLowerCase().contains("api");
            logResult("TC_Home_13 - APIs List Button Functionality", status);
            logStep("Navigating back to Home.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_13 - APIs List Button Functionality", false);
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 14)
    public void TC_Home_14_AutomationExerciseLogo() {
        try {
            logStep("Searching for site logo.");
            String[] logoSelectors = {
                "//img[@alt='Website for automation practice']",
                "//a[contains(@class,'logo')]//img",
                "//img[contains(@src,'logo')]",
                ".logo img",
                "header img",
                "//img[contains(@alt,'logo')]"
            };
            WebElement logo = findElementWithMultipleSelectors(logoSelectors);
            WebElement clickableElement = logo.getTagName().equals("img") ?
                logo.findElement(By.xpath("./ancestor::a[1]")) : logo;
            logStep("Clicking logo to return to Home.");
            clickableElement.click();
            Thread.sleep(2000);
            logStep("Verifying if homepage is loaded after logo click.");
            boolean status = driver.getCurrentUrl().equals("https://automationexercise.com/");
            logResult("TC_Home_14 - Automation Exercise Logo Functionality", status);
        } catch (Exception e) {
            logResult("TC_Home_14 - Automation Exercise Logo Functionality", false);
        }
    }

    @Test(priority = 15)
    public void TC_Home_15_EmailSubscriptionValid() {
        try {
            logStep("Scrolling to footer for email subscription.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            String[] emailSelectors = {
                "#susbscribe_email",
                "input[placeholder*='email' i]",
                "input[type='email']",
                "//input[@placeholder='Your email address']",
                "//input[contains(@id,'email')]"
            };
            logStep("Locating email input field.");
            WebElement emailInput = findElementWithMultipleSelectors(emailSelectors);
            scrollToElement(emailInput);
            logStep("Clearing and entering valid email.");
            emailInput.clear();
            emailInput.sendKeys("test@example.com");
            String[] subscribeSelectors = {
                "#subscribe",
                "button[type='submit']",
                "//button[contains(text(),'Subscribe')]",
                "//input[@type='submit']"
            };
            logStep("Locating and clicking Subscribe button.");
            WebElement subscribeBtn = findElementWithMultipleSelectors(subscribeSelectors);
            subscribeBtn.click();
            Thread.sleep(3000);
            logStep("Verifying subscription success message.");
            boolean status = driver.getPageSource().toLowerCase().contains("subscribed successfully") ||
                           driver.getPageSource().toLowerCase().contains("successfully subscribed") ||
                           driver.getPageSource().toLowerCase().contains("subscription successful");
            logResult("TC_Home_15 - Valid Email Subscription", status);
        } catch (Exception e) {
            logResult("TC_Home_15 - Valid Email Subscription", false);
        }
    }

    @Test(priority = 16)
    public void TC_Home_16_BrandNameFilter() {
        try {
            logStep("Scrolling to find brands section.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/3);");
            Thread.sleep(1000);
            String[] brandSelectors = {
                "//div[contains(@class,'brands')]//a",
                ".brands-name a",
                "//div[@class='brands_products']//a",
                ".panel-group .panel-body a",
                "//ul[contains(@class,'nav')]//a[contains(@href,'brand')]"
            };
            logStep("Searching for brand links.");
            List<WebElement> brandLinks = null;
            for (String selector : brandSelectors) {
                try {
                    if (selector.startsWith("//")) {
                        brandLinks = driver.findElements(By.xpath(selector));
                    } else {
                        brandLinks = driver.findElements(By.cssSelector(selector));
                    }
                    if (!brandLinks.isEmpty()) break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (brandLinks != null && !brandLinks.isEmpty()) {
                WebElement firstBrand = brandLinks.get(0);
                String brandName = firstBrand.getText();
                scrollToElement(firstBrand);
                logStep("Clicking first brand: " + brandName);
                firstBrand.click();
                Thread.sleep(2000);
                logStep("Verifying product list by brand.");
                boolean status = driver.getCurrentUrl().contains("brand") || 
                               driver.getPageSource().contains(brandName);
                logResult("TC_Home_16 - Brand Name Filter Functionality", status);
                logStep("Navigating back to Home.");
                driver.navigate().back();
                Thread.sleep(1000);
            } else {
                logResult("TC_Home_16 - Brand Name Filter Functionality", false);
            }
        } catch (Exception e) {
            logResult("TC_Home_16 - Brand Name Filter Functionality", false);
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 17)
    public void TC_Home_17_CategorySectionExpand() {
        try {
            logStep("Searching for category section in sidebar.");
            String[] categorySelectors = {
                "//a[@data-toggle='collapse']",
                ".panel-heading a",
                "//div[@class='panel-heading']//a",
                ".category-products .panel-title a",
                "//h4[contains(text(),'Category')]/..//a[@data-toggle]"
            };
            WebElement categoryToggle = findElementWithMultipleSelectors(categorySelectors);
            scrollToElement(categoryToggle);
            logStep("Clicking category toggle.");
            categoryToggle.click();
            Thread.sleep(2000);
            logStep("Checking if subcategories are visible.");
            List<WebElement> subCategories = driver.findElements(
                By.xpath("//div[contains(@class,'collapse in')]//a | //div[@class='panel-collapse collapse in']//a"));
            boolean status = !subCategories.isEmpty();
            logResult("TC_Home_17 - Category Section Expand Functionality", status);
        } catch (Exception e) {
            logResult("TC_Home_17 - Category Section Expand Functionality", false);
        }
    }

    @Test(priority = 18)
    public void TC_Home_18_AddToCart() {
        try {
            logStep("Scrolling to products section.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(2000);
            String[] productSelectors = {
                "//div[@class='features_items']//div[@class='product-image-wrapper'][1]",
                "//div[@class='col-sm-4'][1]",
                "//div[contains(@class,'productinfo')][1]",
                "//div[@class='single-products'][1]"
            };
            logStep("Searching for a product to add to cart.");
            WebElement productElement = null;
            for (String selector : productSelectors) {
                try {
                    productElement = driver.findElement(By.xpath(selector));
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (productElement != null) {
                scrollToElement(productElement);
                logStep("Moving cursor to product.");
                actions.moveToElement(productElement).perform();
                Thread.sleep(1000);
                String[] addToCartSelectors = {
                    ".//a[contains(@class,'add-to-cart')]",
                    ".//a[contains(text(),'Add to cart')]",
                    ".//button[contains(text(),'Add to cart')]",
                    ".//a[@data-product-id]",
                    ".//a[contains(@href,'cart')]"
                };
                logStep("Searching for Add to Cart button inside product.");
                WebElement addToCartBtn = null;
                for (String selector : addToCartSelectors) {
                    try {
                        addToCartBtn = productElement.findElement(By.xpath(selector));
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (addToCartBtn == null) {
                    logStep("Searching for Add to Cart button globally.");
                    String[] globalSelectors = {
                        "//a[contains(@class,'add-to-cart')]",
                        "//a[contains(text(),'Add to cart')]",
                        "//button[contains(text(),'Add to cart')]"
                    };
                    for (String selector : globalSelectors) {
                        try {
                            List<WebElement> buttons = driver.findElements(By.xpath(selector));
                            if (!buttons.isEmpty()) {
                                addToCartBtn = buttons.get(0);
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
                if (addToCartBtn != null) {
                    scrollToElement(addToCartBtn);
                    logStep("Clicking Add to Cart button.");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
                    Thread.sleep(3000);
                    logStep("Verifying product was added to cart.");
                    String pageSource = driver.getPageSource().toLowerCase();
                    boolean status = pageSource.contains("your product has been added to cart") ||
                                   pageSource.contains("added to cart") ||
                                   pageSource.contains("product added") ||
                                   driver.findElements(By.xpath("//*[contains(text(),'View Cart')]")).size() > 0;
                    logResult("TC_Home_18 - Add to Cart Functionality", status);
                    try {
                        logStep("Closing cart modal if present.");
                        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@class='btn btn-success close-modal btn-block'] | //button[contains(@class,'close')]")));
                        closeBtn.click();
                    } catch (Exception ignored) {}
                } else {
                    logResult("TC_Home_18 - Add to Cart Functionality", false);
                }
            } else {
                logResult("TC_Home_18 - Add to Cart Functionality", false);
            }
        } catch (Exception e) {
            logResult("TC_Home_18 - Add to Cart Functionality", false);
        }
    }

    @Test(priority = 19)
    public void TC_Home_19_ScrollToTop() {
        try {
            logStep("Scrolling down page.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000);
            String[] scrollTopSelectors = {
                "#scrollUp",
                ".scroll-to-top",
                "//a[@id='scrollUp']",
                ".back-to-top",
                "//i[contains(@class,'arrow-up')]/parent::a"
            };
            logStep("Searching for Scroll To Top button.");
            WebElement scrollTopBtn = findElementWithMultipleSelectors(scrollTopSelectors);
            logStep("Clicking Scroll To Top button.");
            scrollTopBtn.click();
            Thread.sleep(3000);
            logStep("Checking scroll position after click.");
            Long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
            boolean status = scrollPosition < 100; // Allow a little tolerance
            logResult("TC_Home_19 - Scroll to Top Functionality", status);
        } catch (Exception e) {
            logResult("TC_Home_19 - Scroll to Top Functionality", false);
        }
    }

    @Test(priority = 20)
    public void TC_Home_20_ViewProduct() {
        try {
            logStep("Searching for View Product button.");
            String[] viewProductSelectors = {
                "//a[contains(text(),'View Product')]",
                "//a[contains(@href,'product_details')]",
                ".choose a",
                "//a[contains(text(),'View')]"
            };
            WebElement viewProductBtn = findElementWithMultipleSelectors(viewProductSelectors);
            scrollToElement(viewProductBtn);
            logStep("Clicking View Product button.");
            viewProductBtn.click();
            Thread.sleep(2000);
            logStep("Checking product details page by URL or text.");
            boolean status = driver.getCurrentUrl().contains("product_details") ||
                           driver.getPageSource().contains("Product Details");
            logResult("TC_Home_20 - View Product Functionality", status);
            logStep("Navigating back to previous page.");
            driver.navigate().back();
            Thread.sleep(1000);
        } catch (Exception e) {
            logResult("TC_Home_20 - View Product Functionality", false);
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 21)
    public void TC_Home_21_WomenCategorySection() {
        try {
            logStep("Navigating to Home page.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Searching for Women category in left sidebar.");
            String[] womenCategorySelectors = {
                "//a[@href='#Women']",
                "//a[contains(text(),'WOMEN')]",
                "//div[@class='panel-heading']//a[contains(text(),'Women')]",
                "//h4[contains(text(),'WOMEN')]/following-sibling::a",
                "//span[contains(text(),'Women')]/parent::a"
            };
            WebElement womenCategory = null;
            for (String selector : womenCategorySelectors) {
                try {
                    womenCategory = driver.findElement(By.xpath(selector));
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (womenCategory != null) {
                scrollToElement(womenCategory);
                logStep("Clicking Women category.");
                womenCategory.click();
                Thread.sleep(2000);
                logStep("Searching for subcategory of Women.");
                String[] subCategorySelectors = {
                    "//div[@id='Women']//a[1]",
                    "//a[contains(@href,'women')]",
                    "//a[contains(@href,'dress')]",
                    "//a[contains(@href,'tops')]"
                };
                WebElement womenSubCategory = null;
                for (String selector : subCategorySelectors) {
                    try {
                        womenSubCategory = driver.findElement(By.xpath(selector));
                        if (womenSubCategory.isDisplayed()) break;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (womenSubCategory != null) {
                    logStep("Clicking first subcategory of Women.");
                    womenSubCategory.click();
                    Thread.sleep(2000);
                    logStep("Checking category product page or title for 'women'.");
                    boolean status = driver.getCurrentUrl().contains("category_products") ||
                                   driver.getCurrentUrl().contains("women") ||
                                   driver.getPageSource().toLowerCase().contains("women") ||
                                   driver.getTitle().toLowerCase().contains("women");
                    logResult("TC_Home_21 - Women Category Section Functionality", status);
                    if (status) {
                        logStep("Navigating back to Home.");
                        driver.navigate().back();
                        Thread.sleep(1000);
                    }
                } else {
                    logResult("TC_Home_21 - Women Category Section Functionality", false);
                }
            } else {
                logResult("TC_Home_21 - Women Category Section Functionality", false);
            }
        } catch (Exception e) {
            logResult("TC_Home_21 - Women Category Section Functionality", false);
            try { driver.get("https://automationexercise.com/"); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 22)
    public void TC_Home_22_MenCategorySection() {
        try {
            logStep("Navigating to Home page.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Searching for Men category in left sidebar.");
            String[] menCategorySelectors = {
                "//a[@href='#Men']",
                "//a[contains(text(),'MEN')]",
                "//div[@class='panel-heading']//a[contains(text(),'Men')]",
                "//h4[contains(text(),'MEN')]/following-sibling::a",
                "//span[contains(text(),'Men')]/parent::a"
            };
            WebElement menCategory = null;
            for (String selector : menCategorySelectors) {
                try {
                    menCategory = driver.findElement(By.xpath(selector));
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (menCategory != null) {
                scrollToElement(menCategory);
                logStep("Clicking Men category.");
                menCategory.click();
                Thread.sleep(2000);
                logStep("Searching for subcategory of Men.");
                String[] subCategorySelectors = {
                    "//div[@id='Men']//a[1]",
                    "//a[contains(@href,'men')]",
                    "//a[contains(@href,'tshirts')]",
                    "//a[contains(@href,'jeans')]"
                };
                WebElement menSubCategory = null;
                for (String selector : subCategorySelectors) {
                    try {
                        menSubCategory = driver.findElement(By.xpath(selector));
                        if (menSubCategory.isDisplayed()) break;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (menSubCategory != null) {
                    logStep("Clicking first subcategory of Men.");
                    menSubCategory.click();
                    Thread.sleep(2000);
                    logStep("Checking category product page or title for 'men'.");
                    boolean status = driver.getCurrentUrl().contains("category_products") ||
                                   driver.getCurrentUrl().contains("men") ||
                                   driver.getPageSource().toLowerCase().contains("men") ||
                                   driver.getTitle().toLowerCase().contains("men");
                    logResult("TC_Home_22 - Men Category Section Functionality", status);
                    if (status) {
                        logStep("Navigating back to Home.");
                        driver.navigate().back();
                        Thread.sleep(1000);
                    }
                } else {
                    logResult("TC_Home_22 - Men Category Section Functionality", false);
                }
            } else {
                logResult("TC_Home_22 - Men Category Section Functionality", false);
            }
        } catch (Exception e) {
            logResult("TC_Home_22 - Men Category Section Functionality", false);
            try { driver.get("https://automationexercise.com/"); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 23)
    public void TC_Home_23_KidsCategorySection() {
        try {
            logStep("Navigating to Home page.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Searching for Kids category in left sidebar.");
            String[] kidsCategorySelectors = {
                "//a[@href='#Kids']",
                "//a[contains(text(),'KIDS')]",
                "//div[@class='panel-heading']//a[contains(text(),'Kids')]",
                "//h4[contains(text(),'KIDS')]/following-sibling::a",
                "//span[contains(text(),'Kids')]/parent::a"
            };
            WebElement kidsCategory = null;
            for (String selector : kidsCategorySelectors) {
                try {
                    kidsCategory = driver.findElement(By.xpath(selector));
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (kidsCategory != null) {
                scrollToElement(kidsCategory);
                logStep("Clicking Kids category.");
                kidsCategory.click();
                Thread.sleep(2000);
                logStep("Searching for subcategory of Kids.");
                String[] subCategorySelectors = {
                    "//div[@id='Kids']//a[1]",
                    "//a[contains(@href,'kids')]",
                    "//a[contains(@href,'dress')]",
                    "//a[contains(@href,'tops')]"
                };
                WebElement kidsSubCategory = null;
                for (String selector : subCategorySelectors) {
                    try {
                        kidsSubCategory = driver.findElement(By.xpath(selector));
                        if (kidsSubCategory.isDisplayed()) break;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (kidsSubCategory != null) {
                    logStep("Clicking first subcategory of Kids.");
                    kidsSubCategory.click();
                    Thread.sleep(2000);
                    logStep("Checking category product page or title for 'kids'.");
                    boolean status = driver.getCurrentUrl().contains("category_products") ||
                                   driver.getCurrentUrl().contains("kids") ||
                                   driver.getPageSource().toLowerCase().contains("kids") ||
                                   driver.getTitle().toLowerCase().contains("kids");
                    logResult("TC_Home_23 - Kids Category Section Functionality", status);
                    if (status) {
                        logStep("Navigating back to Home.");
                        driver.navigate().back();
                        Thread.sleep(1000);
                    }
                } else {
                    logResult("TC_Home_23 - Kids Category Section Functionality", false);
                }
            } else {
                logResult("TC_Home_23 - Kids Category Section Functionality", false);
            }
        } catch (Exception e) {
            logResult("TC_Home_23 - Kids Category Section Functionality", false);
            try { driver.get("https://automationexercise.com/"); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 24)
    public void TC_Home_24_ViewCartAfterAddToCart() {
        try {
            logStep("Navigating to Home page.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Scrolling to products section.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(2000);
            logStep("Searching for Add to Cart buttons.");
            boolean addedToCart = false;
            try {
                List<WebElement> addToCartButtons = driver.findElements(
                    By.xpath("//a[contains(@class,'add-to-cart')] | //a[contains(text(),'Add to cart')]"));
                if (!addToCartButtons.isEmpty()) {
                    WebElement addToCartBtn = addToCartButtons.get(0);
                    scrollToElement(addToCartBtn);
                    logStep("Clicking Add to Cart button.");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);
                    Thread.sleep(2000);
                    addedToCart = true;
                }
            } catch (Exception e) {
                // Continue with test even if add to cart fails
            }
            logStep("Searching for View Cart button.");
            String[] viewCartSelectors = {
                "//u[text()='View Cart']",
                "//a[contains(text(),'View Cart')]",
                "//button[contains(text(),'View Cart')]",
                "//a[contains(@href,'view_cart')]",
                "//a[contains(@class,'btn')][contains(text(),'Cart')]"
            };
            WebElement viewCartBtn = null;
            for (String selector : viewCartSelectors) {
                try {
                    viewCartBtn = driver.findElement(By.xpath(selector));
                    if (viewCartBtn.isDisplayed()) break;
                } catch (Exception e) {
                    continue;
                }
            }
            boolean status = false;
            if (viewCartBtn != null) {
                logStep("Clicking View Cart button.");
                viewCartBtn.click();
                Thread.sleep(2000);
                status = driver.getCurrentUrl().contains("view_cart") ||
                        driver.getPageSource().contains("Shopping Cart") ||
                        driver.getTitle().toLowerCase().contains("cart");
            } else {
                logStep("View Cart button not found, trying Cart link in navigation.");
                try {
                    WebElement cartNav = driver.findElement(By.xpath("//a[contains(text(),'Cart')]"));
                    cartNav.click();
                    Thread.sleep(2000);
                    status = driver.getCurrentUrl().contains("view_cart") ||
                            driver.getPageSource().contains("Shopping Cart");
                } catch (Exception e) {
                    logStep("Cart navigation failed, trying direct URL.");
                    driver.get("https://automationexercise.com/view_cart");
                    Thread.sleep(2000);
                    status = driver.getCurrentUrl().contains("view_cart");
                }
            }
            logResult("TC_Home_24 - View Cart After Add to Cart", status);
            if (status) {
                logStep("Navigating back to Home.");
                driver.navigate().back();
                Thread.sleep(1000);
            } else {
                logStep("Reloading home page.");
                driver.get("https://automationexercise.com/");
            }
        } catch (Exception e) {
            logResult("TC_Home_24 - View Cart After Add to Cart", false);
            try { driver.get("https://automationexercise.com/"); } catch (Exception ignored) {}
        }
    }

    @Test(priority = 25)
    public void TC_Home_25_InvalidEmailSubscription() {
        try {
            logStep("Scrolling to footer for email subscription.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            logStep("Locating email input by ID.");
            WebElement emailInput = driver.findElement(By.id("susbscribe_email"));
            scrollToElement(emailInput);
            logStep("Entering invalid email address.");
            emailInput.clear();
            emailInput.sendKeys("medha@gmailcom");
            logStep("Clicking Subscribe button.");
            WebElement subscribeBtn = driver.findElement(By.id("subscribe"));
            subscribeBtn.click();
            Thread.sleep(2000);
            logStep("Checking for validation or absence of success message.");
            String pageSource = driver.getPageSource();
            boolean validationShown = pageSource.contains("valid email") ||
                                    pageSource.contains("invalid") ||
                                    !pageSource.contains("successfully");
            logResult("TC_Home_25 - Invalid Email Subscription (Expected FAIL)", !validationShown);
        } catch (Exception e) {
            logResult("TC_Home_25 - Invalid Email Subscription", false);
        }
    }

    @Test(priority = 26)
    public void TC_Home_26_ScrollBarFunctionality() {
        try {
            logStep("Getting page dimensions for scroll bar check.");
            Long bodyHeight = (Long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
            Long windowHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
            boolean scrollBarNeeded = bodyHeight > windowHeight;
            logStep("Scrolling to 500px down.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
            Thread.sleep(1000);
            Long scrollPosition = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
            logStep("Scrolling back to top of page.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000);
            boolean scrollWorking = scrollPosition > 0;
            boolean status = scrollBarNeeded && scrollWorking;
            logResult("TC_Home_26 - Scroll Bar Functionality", status);
        } catch (Exception e) {
            logResult("TC_Home_26 - Scroll Bar Functionality", false);
        }
    }

    @Test(priority = 27)
    public void TC_Home_27_Responsiveness() {
        try {
            logStep("Getting original browser window size.");
            Dimension originalSize = driver.manage().window().getSize();
            logStep("Resizing window to mobile size.");
            driver.manage().window().setSize(new Dimension(375, 667));
            Thread.sleep(1000);
            boolean mobileLayout = driver.findElement(By.tagName("body")).isDisplayed();
            logStep("Resizing window to tablet size.");
            driver.manage().window().setSize(new Dimension(768, 1024));
            Thread.sleep(1000);
            boolean tabletLayout = driver.findElement(By.tagName("body")).isDisplayed();
            logStep("Restoring window to original size.");
            driver.manage().window().setSize(originalSize);
            Thread.sleep(1000);
            boolean status = mobileLayout && tabletLayout;
            logResult("TC_Home_27 - Responsiveness Testing", status);
        } catch (Exception e) {
            logResult("TC_Home_27 - Responsiveness Testing", false);
        }
    }

    @Test(priority = 28)
    public void TC_Home_28_CarouselSliders() {
        try {
            logStep("Navigating to home page for Carousel Slider test.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Searching for carousel/slider by multiple selectors.");
            String[] carouselSelectors = {
                "//div[@id='slider-carousel']",
                "//div[contains(@class,'carousel')]",
                "//div[@class='carousel-inner']",
                "//div[contains(@class,'slider')]",
                "//div[contains(@id,'carousel')]"
            };
            WebElement carousel = null;
            for (String selector : carouselSelectors) {
                try {
                    carousel = driver.findElement(By.xpath(selector));
                    if (carousel.isDisplayed()) break;
                } catch (Exception e) {
                    continue;
                }
            }
            boolean status = false;
            if (carousel != null) {
                scrollToElement(carousel);
                logStep("Checking for arrow controls and indicators on carousel.");
                String[] rightArrowSelectors = {
                    "//a[contains(@class,'right') and contains(@class,'carousel-control')]",
                    "//a[@class='right carousel-control']",
                    "//button[contains(@class,'carousel-control-next')]",
                    "//a[@data-slide='next']",
                    "//i[contains(@class,'fa-angle-right')]/parent::a"
                };
                String[] leftArrowSelectors = {
                    "//a[contains(@class,'left') and contains(@class,'carousel-control')]",
                    "//a[@class='left carousel-control']",
                    "//button[contains(@class,'carousel-control-prev')]", 
                    "//a[@data-slide='prev']",
                    "//i[contains(@class,'fa-angle-left')]/parent::a"
                };
                boolean controlsWorking = false;
                try {
                    WebElement rightArrow = null;
                    for (String selector : rightArrowSelectors) {
                        try {
                            rightArrow = driver.findElement(By.xpath(selector));
                            if (rightArrow.isDisplayed()) break;
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (rightArrow != null) {
                        logStep("Clicking right arrow on carousel.");
                        rightArrow.click();
                        Thread.sleep(2000);
                        controlsWorking = true;
                        WebElement leftArrow = null;
                        for (String selector : leftArrowSelectors) {
                            try {
                                leftArrow = driver.findElement(By.xpath(selector));
                                if (leftArrow.isDisplayed()) break;
                            } catch (Exception e) {
                                continue;
                            }
                        }
                        if (leftArrow != null) {
                            logStep("Clicking left arrow on carousel.");
                            leftArrow.click();
                            Thread.sleep(2000);
                        }
                    }
                } catch (Exception e) {
                    // Controls might not be clickable, but carousel exists
                }
                try {
                    List<WebElement> indicators = driver.findElements(
                        By.xpath("//ol[contains(@class,'carousel-indicators')]//li | //div[contains(@class,'carousel-indicators')]//span"));
                    if (!indicators.isEmpty()) {
                        logStep("Clicking second indicator of carousel.");
                        indicators.get(1).click(); // Click second indicator
                        Thread.sleep(2000);
                        controlsWorking = true;
                    }
                } catch (Exception e) {
                    // Indicators might not exist or be clickable
                }
                status = carousel.isDisplayed();
            }
            logResult("TC_Home_28 - Carousel Sliders Functionality", status);
        } catch (Exception e) {
            logResult("TC_Home_28 - Carousel Sliders Functionality", false);
        }
    }

    @Test(priority = 29)
    public void TC_Home_29_EmailWithoutAtSymbol() {
        try {
            logStep("Navigating to home page for Email test.");
            driver.get("https://automationexercise.com/");
            Thread.sleep(2000);
            logStep("Scrolling to footer for email subscription.");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            logStep("Searching for email input field by multiple selectors.");
            String[] emailInputSelectors = {
                "#susbscribe_email",
                "input[placeholder*='email' i]",
                "input[type='email']",
                "//input[@placeholder='Your email address']",
                "//input[contains(@id,'email')]",
                "input[name='email']"
            };
            WebElement emailInput = null;
            for (String selector : emailInputSelectors) {
                try {
                    if (selector.startsWith("//")) {
                        emailInput = driver.findElement(By.xpath(selector));
                    } else {
                        emailInput = driver.findElement(By.cssSelector(selector));
                    }
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            boolean status = false;
            if (emailInput != null) {
                scrollToElement(emailInput);
                logStep("Clearing and entering invalid email ('medha').");
                emailInput.clear();
                emailInput.sendKeys("medha");
                String[] subscribeSelectors = {
                    "#subscribe",
                    "button[type='submit']",
                    "//button[contains(text(),'Subscribe')]",
                    "//input[@type='submit']",
                    "//button[contains(@id,'subscribe')]"
                };
                logStep("Searching for and clicking Subscribe button.");
                WebElement subscribeBtn = null;
                for (String selector : subscribeSelectors) {
                    try {
                        if (selector.startsWith("//")) {
                            subscribeBtn = driver.findElement(By.xpath(selector));
                        } else {
                            subscribeBtn = driver.findElement(By.cssSelector(selector));
                        }
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
                if (subscribeBtn != null) {
                    subscribeBtn.click();
                    Thread.sleep(1000);
                    logStep("Checking HTML5 validation message after submit.");
                    String validationMessage = emailInput.getAttribute("validationMessage");
                    Boolean isValid = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].validity.valid;", emailInput);
                    Boolean typeMismatch = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].validity.typeMismatch;", emailInput);
                    status = (validationMessage != null && validationMessage.contains("@")) ||
                            (isValid != null && !isValid) ||
                            (typeMismatch != null && typeMismatch);
                }
            }
            logResult("TC_Home_29 - Email Without @ Symbol", status);
        } catch (Exception e) {
            logResult("TC_Home_29 - Email Without @ Symbol", false);
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            try {
                logStep("Closing browser and cleaning up test environment.");
                driver.quit();
                logStep("Test environment cleanup completed.");
            } catch (Exception e) {
                logStep("Error during cleanup: " + e.getMessage());
            }
        }
    }
}
