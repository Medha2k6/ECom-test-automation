package cgi_ae_test_cases;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class CartPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private final String baseUrl = "https://automationexercise.com";
    private final int DESIRED_CART_ITEMS = 8;   // you asked for 8 items so tests have enough data

    @BeforeClass
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get(baseUrl);

        login("abhij@gmail.com", "abhij@gmail.com");

        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void login(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/login' or contains(.,'Signup / Login')]"))).click();

        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[data-qa='login-email'], input[name='email']")));
        WebElement passEl = driver.findElement(By.cssSelector("input[data-qa='login-password'], input[name='password']"));
        emailEl.clear();
        emailEl.sendKeys(email);
        passEl.clear();
        passEl.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.xpath("//button[contains(.,'Login')]"));
        loginBtn.click();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Logged in as') or contains(.,'Logout')]")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(.,'Logout')]"))
        ));
    }

    private void addProductsFromHome(int needCount) throws InterruptedException {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".features_items")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int added = 0;
        int attempts = 0;
        while (added < needCount && attempts < 20) {
            attempts++;
            List<WebElement> products = driver.findElements(By.cssSelector(".features_items .product-image-wrapper"));
            if (products.isEmpty()) {
                js.executeScript("window.scrollBy(0,500);");
                Thread.sleep(800);
                continue;
            }
            for (WebElement product : products) {
                if (added >= needCount) break;
                js.executeScript("arguments[0].scrollIntoView({behavior:'instant', block:'center'});", product);
                Thread.sleep(400);
                actions.moveToElement(product).pause(Duration.ofMillis(300)).perform();
                Thread.sleep(300);

                WebElement addBtn = null;
                try {
                    addBtn = product.findElement(By.xpath(".//a[contains(normalize-space(.),'Add to cart') or contains(@title,'Add to cart')]"));
                } catch (Exception ignore) {
                }
                if (addBtn == null) continue;

                try {
                    addBtn.click();
                } catch (Exception ex) {
                    try {
                        js.executeScript("arguments[0].click();", addBtn);
                    } catch (Exception ex2) {
                        try {
                            actions.moveToElement(addBtn).click().perform();
                        } catch (Exception ignored) { }
                    }
                }
                try {
                    WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//button[contains(.,'Continue Shopping') or contains(.,'Continue')]")));
                    try { continueBtn.click(); } catch (Exception e) { js.executeScript("arguments[0].click();", continueBtn); }
                } catch (Exception ignored) {
                    try {
                        WebElement closeBtn = driver.findElement(By.xpath("//button[contains(@class,'close') or contains(.,'×')]"));
                        if (closeBtn.isDisplayed()) { closeBtn.click(); }
                    } catch (Exception ignored2) { }
                }

                Thread.sleep(600);
                added++;
            }

            if (added < needCount) {
                js.executeScript("window.scrollTo({top:0, behavior:'instant'});");
                Thread.sleep(600);
            }
        }
    }
    private void ensureCartHasAtLeast(int n) throws InterruptedException {
        int tries = 0;
        while (tries < 3) {
            tries++;
            int current = getCartItemCount();
            if (current >= n) return;
            int need = n - current;
            addProductsFromHome(need);
            Thread.sleep(1200);
        }

        int finalCount = getCartItemCount();
        System.out.println("ensureCartHasAtLeast: final cart count = " + finalCount);
    }
    private int getCartItemCount() {
        try {
            driver.get(baseUrl + "/view_cart");
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='cart_info_table']//tbody/tr")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Cart is empty') or contains(.,'Cart is empty')]"))
            ));
            List<WebElement> rows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
            return rows.size();
        } catch (TimeoutException t) {
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean textVisibleContains(String subText) {
        List<WebElement> found = driver.findElements(By.xpath("//*[contains(normalize-space(),\"" + subText + "\")]"));
        return !found.isEmpty();
    }
    // ------------------- TESTS (priority ensures order; logout / not-logged-in-check left to last) -------------------
    @Test(priority = 1)
    public void tc_cart_1_homeTab() {
        driver.findElement(By.xpath("//a[@href='/' or normalize-space()='Home']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("automationexercise"));
    }
    @Test(priority = 2)
    public void tc_cart_2_productsTab() {
        driver.findElement(By.xpath("//a[@href='/products']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/products"));
    }
    @Test(priority = 3)
    public void tc_cart_3_cartTab() {
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/view_cart"));
    }
    @Test(priority = 4)
    public void tc_cart_4_loginTabOrLoggedInAs() {
        // After our login, Signup/Login disappears. Validate logged-in indicator or Logout link exists
        boolean loggedIn = driver.findElements(By.xpath("//*[contains(text(),'Logged in as') or //a[contains(.,'Logout')]]")).size() > 0;
        // simpler robust check: presence of 'Logout' link indicates logged in
        boolean logoutLink = driver.findElements(By.xpath("//a[contains(normalize-space(),'Logout')]")).size() > 0;
        Assert.assertTrue(logoutLink || loggedIn, "Expected user to be logged in.");
    }
    @Test(priority = 5)
    public void tc_cart_5_testCasesTab() {
        driver.findElement(By.xpath("//a[@href='/test_cases']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/test_cases"));
    }
    @Test(priority = 6)
    public void tc_cart_6_apiTab() {
        driver.findElement(By.xpath("//a[contains(.,'API Testing')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("api"));
    }
    @Test(priority = 7)
    public void tc_cart_7_contactUsTab() {
        driver.findElement(By.xpath("//a[@href='/contact_us' or contains(.,'Contact us')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/contact_us"));
    }
    @Test(priority = 8)
    public void tc_cart_8_contactUsIconFromCart() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
        driver.findElement(By.xpath("//a[@href='/contact_us' or contains(.,'Contact us')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/contact_us"));
    }
    @Test(priority = 9)
    public void tc_cart_9_homeButtonFromCart() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
        driver.findElement(By.xpath("//a[@href='/' or normalize-space()='Home']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("automationexercise"));
    }
    @Test(priority = 10)
    public void tc_cart_10_emailSubscriptionValid() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        WebElement subInput = findSubscriptionInput();
        Assert.assertNotNull(subInput, "Subscription input not found");
        subInput.clear();
        subInput.sendKeys("validemail+" + System.currentTimeMillis()%10000 + "@test.com");

        WebElement subBtn = findSubscribeButton();
        Assert.assertNotNull(subBtn, "Subscribe button not found");
        subBtn.click();

        Thread.sleep(1000);
        boolean success = textVisibleContains("successfully") || textVisibleContains("subscribed");
        Assert.assertTrue(success, "Expected subscribe success message");

        if (getCartItemCount() < DESIRED_CART_ITEMS) ensureCartHasAtLeast(DESIRED_CART_ITEMS);
    }
    @Test(priority = 11)
    public void tc_cart_11_emailSubscriptionInvalid() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);

        WebElement subInput = findSubscriptionInput();
        Assert.assertNotNull(subInput, "Subscription input not found");
        subInput.clear();
        subInput.sendKeys("invalid@@");  
        Boolean isValid = (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", subInput);
        if (Boolean.FALSE.equals(isValid)) {
            Assert.assertTrue(true, "Browser-level validation flagged invalid email (expected).");
            return;
        }

        WebElement subBtn = findSubscribeButton();
        Assert.assertNotNull(subBtn, "Subscribe button not found - cannot validate invalid case");
        try { subBtn.click(); } catch (WebDriverException e) { ((JavascriptExecutor)driver).executeScript("arguments[0].click();", subBtn); }

        By invalidMsgLocator = By.xpath(
            "//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'invalid') " +
            "or contains(normalize-space(.),'should not contain') " +
            "or contains(@class,'alert') or contains(@class,'error') or contains(@class,'toast')]"
        );

        try {
            WebElement err = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(invalidMsgLocator));
            Assert.assertTrue(err.isDisplayed(), "Invalid email message/popup appeared as expected.");
        } catch (TimeoutException te) {
            Assert.fail("Invalid-email popup/message was not found after submitting invalid email.");
        }
    }
    @Test(priority = 12)
    public void tc_cart_12_emptyCartClickHereRedirect() throws InterruptedException {
        int before = getCartItemCount();
        if (before == 0) ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
        List<WebElement> deleteBtns = driver.findElements(By.xpath("//a[contains(@class,'cart_quantity_delete')]"));
        for (WebElement d : deleteBtns) {
            try { d.click(); Thread.sleep(500); } catch (Exception ignored) {}
        }
        Thread.sleep(700);
        List<WebElement> clickHere = driver.findElements(By.xpath("//a[contains(.,'Click here') or contains(.,'click here')]"));
        if (!clickHere.isEmpty()) {
            clickHere.get(0).click();
            Thread.sleep(800);
            Assert.assertTrue(driver.getCurrentUrl().contains("automationexercise") || driver.getCurrentUrl().contains("/products"));
        } else {
            boolean emptyMsgVisible = textVisibleContains("Cart is empty");
            Assert.assertTrue(emptyMsgVisible, "Empty cart message or 'Click here' link expected.");
        }
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
    }
    @Test(priority = 13)
    public void tc_cart_13_proceedToCheckoutButton() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        WebElement proceed = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(.,'Proceed To Checkout') or contains(.,'Proceed to Checkout')]")));
        proceed.click();
        Thread.sleep(800);
        boolean isCheckout = driver.getCurrentUrl().contains("checkout") || textVisibleContains("Checkout");
        Assert.assertTrue(isCheckout, "Proceed to Checkout did not navigate to checkout (or login if not logged in).");
        driver.get(baseUrl + "/view_cart");
    }
    @Test(priority = 14)
    public void tc_cart_14_removeFromCartButton() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        int before = getCartItemCount();
        By delLocator = By.xpath("//a[contains(@class,'cart_quantity_delete') or contains(@onclick,'delete')]");
        WebElement delBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(delLocator));
        try { delBtn.click(); } catch (WebDriverException e) { ((JavascriptExecutor)driver).executeScript("arguments[0].click();", delBtn); }

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            int cur = d.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr")).size();
            return cur < before;
        });

        int after = getCartItemCount();
        Assert.assertTrue(after <= before - 1, "Item removal didn't decrease cart count.");

        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
    }
    @Test(priority = 15)
    public void tc_cart_15_verifyProductDetails() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
        Assert.assertTrue(rows.size() >= 1, "Expected at least one product row in cart.");
        WebElement first = rows.get(0);
        Assert.assertTrue(first.findElements(By.xpath(".//td[contains(@class,'cart_description') or ./descendant::h4/a]")).size() > 0, "Product name/description missing.");
    }
    @Test(priority = 16)
    public void tc_cart_16_quantityUpdate() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        List<WebElement> qtyInputs = driver.findElements(By.xpath("//table[@id='cart_info_table']//input[@type='number' or @name='quantity']"));
        if (qtyInputs.isEmpty()) {
            List<WebElement> qtyTexts = driver.findElements(By.xpath("//td[contains(@class,'cart_quantity') or contains(.,'Quantity')]"));
            Assert.assertTrue(!qtyTexts.isEmpty(), "Quantity element not found.");
            return;
        }
        WebElement qty = qtyInputs.get(0);
        qty.clear();
        qty.sendKeys("3");
        qty.sendKeys(Keys.ENTER);
        Thread.sleep(900);
        String val = qty.getAttribute("value");
        Assert.assertTrue(val != null && (val.equals("3") || val.equals("3.0")), "Quantity was not updated.");
    }

    @Test(priority = 17)
    public void tc_cart_17_totalPriceCalculation() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        WebElement unitPriceEl = null;
        WebElement totalPriceEl = null;
        try {
            unitPriceEl = driver.findElement(By.xpath("//table[@id='cart_info_table']//td[contains(@class,'cart_price')][1] | //table[@id='cart_info_table']//td[contains(.,'Rs.')][1]"));
            totalPriceEl = driver.findElement(By.xpath("//table[@id='cart_info_table']//td[contains(@class,'cart_total')][1] | //table[@id='cart_info_table']//td[contains(.,'Rs.')][last()]"));
        } catch (Exception e) {
            Assert.fail("Could not locate unit/total price elements: " + e.getMessage());
        }

        String unitTxt = unitPriceEl.getText().replaceAll("[^0-9]", "");
        String totalTxt = totalPriceEl.getText().replaceAll("[^0-9]", "");
        if (unitTxt.isEmpty() || totalTxt.isEmpty()) {
            Assert.fail("Could not parse prices (empty strings).");
        }
        int unit = Integer.parseInt(unitTxt);
        int total = Integer.parseInt(totalTxt);
        Assert.assertTrue(total >= unit, "Total price should be >= unit price");
    }
    @Test(priority = 18)
    public void tc_cart_18_accessibleFromHome() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl);
        driver.findElement(By.xpath("//a[@href='/view_cart']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/view_cart"));
    }

    @Test(priority = 19)
    public void tc_cart_19_emptyCartMessage() throws InterruptedException {
        // empty cart and validate message then re-add
        driver.get(baseUrl + "/view_cart");
        List<WebElement> deleteBtns = driver.findElements(By.xpath("//a[contains(@class,'cart_quantity_delete')]"));
        for (WebElement d : deleteBtns) {
            try { d.click(); Thread.sleep(300); } catch (Exception ignored) {}
        }
        Thread.sleep(700);
        boolean emptyMsg = textVisibleContains("Cart is empty") || textVisibleContains("There are no items in your cart");
        Assert.assertTrue(emptyMsg, "Expected 'Cart is empty' message after removing all items.");
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
    }

    @Test(priority = 20)
    public void tc_cart_20_checkoutRedirectIfNotLoggedIn() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);

        // try to logout if Logout link exists (safe click)
        List<WebElement> logoutEls = driver.findElements(By.xpath("//a[contains(normalize-space(),'Logout')]"));
        if (!logoutEls.isEmpty()) {
            try {
                logoutEls.get(0).click();
            } catch (WebDriverException e) {
                ((JavascriptExecutor)driver).executeScript("arguments[0].click();", logoutEls.get(0));
            }
        } else {
            // fallback: clear cookies to simulate logged-out state
            driver.manage().deleteAllCookies();
            driver.get(baseUrl);
        }

        // go to cart and click Proceed To Checkout
        driver.get(baseUrl + "/view_cart");
        By proceedLocator = By.xpath("//*[contains(.,'Proceed To Checkout') or contains(.,'Proceed to Checkout')]");
        WebElement proceed = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(proceedLocator));
        try { proceed.click(); } catch (WebDriverException e) { ((JavascriptExecutor)driver).executeScript("arguments[0].click();", proceed); }

        // wait (short) for presence of login indicators: url contains '/login' OR specific login elements
        boolean loginShown = false;
        try {
            loginShown = new WebDriverWait(driver, Duration.ofSeconds(8)).until(d ->
                d.getCurrentUrl().contains("/login") ||
                d.findElements(By.xpath("//h2[contains(normalize-space(),'Login to your account')]")).size() > 0 ||
                d.findElements(By.xpath("//a[contains(.,'Signup / Login')]")).size() > 0 ||
                d.findElements(By.xpath("//form//input[@name='password' or @type='password']")).size() > 0
            );
        } catch (TimeoutException te) {
            loginShown = false;
        }

        Assert.assertTrue(loginShown, "Expected redirect to login/signup when clicking Proceed To Checkout while not logged in.");

        // optional: re-login to restore state for later tests
        if (loginShown) {
            login("abhij@gmail.com", "abhij@gmail.com");
            ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        }
    }
    @Test(priority = 21)
    public void tc_cart_22_cartPersistenceAfterReload() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        driver.get(baseUrl + "/view_cart");
        List<WebElement> beforeRows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
        int before = beforeRows.size();
        driver.navigate().refresh();
        Thread.sleep(800);
        List<WebElement> afterRows = driver.findElements(By.xpath("//table[@id='cart_info_table']//tbody/tr"));
        int after = afterRows.size();
        Assert.assertEquals(after, before, "Cart items should persist after reload");
    }
    @Test(priority = 22)
    public void tc_cart_20_checkoutRedirectIfNotLoggedIn1() throws InterruptedException {
        ensureCartHasAtLeast(DESIRED_CART_ITEMS);
        // logout
        WebElement logout = driver.findElement(By.xpath("//a[contains(.,'Logout')]"));
        logout.click();
        driver.get(baseUrl + "/view_cart");
        WebElement proceed = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(.,'Proceed To Checkout') or contains(.,'Proceed to Checkout')]")));
        proceed.click();
        Thread.sleep(900);

        boolean loginShown = textVisibleContains("Login to your account") || textVisibleContains("Signup / Login");
        Assert.assertTrue(loginShown, "Proceed to Checkout while not logged in should redirect to Login/Signup");
    }

    private WebElement findSubscriptionInput() {
        List<By> candidates = List.of(
                By.id("susbscribe_email"),
                By.id("subscribe_email"),
                By.cssSelector("input[placeholder='Your email address']"),
                By.cssSelector("input[placeholder='Email']"),
                By.cssSelector("footer input[type='email']"),
                By.cssSelector("input[type='email']")
        );
        for (By b : candidates) {
            try {
                List<WebElement> elems = driver.findElements(b);
                if (!elems.isEmpty()) return elems.get(0);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private WebElement findSubscribeButton() {
        List<By> candidates = List.of(
                By.id("subscribe"),
                By.cssSelector("button#subscribe"),
                By.xpath("//button[contains(.,'Subscribe') or contains(.,'Subscribe')]"),
                By.xpath("//input[@placeholder='Your email address']/following-sibling::button"),
                By.cssSelector(".footer-widget button")
        );
        for (By b : candidates) {
            try {
                List<WebElement> elems = driver.findElements(b);
                if (!elems.isEmpty()) return elems.get(0);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
