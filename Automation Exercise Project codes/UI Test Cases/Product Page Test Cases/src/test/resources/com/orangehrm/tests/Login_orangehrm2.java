package com.orangehrm.tests;
 
import java.io.File;
import java.io.IOException;
 
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
 
import com.orangehrm.Base.BaseTest;
import com.orangehrm.pages.loginpage;
import com.orangehrm.utilities.ExcelUtilities;
 
@Listeners(com.orangehrm.listeners.TestListener.class)   // Attach listener here
public class Login_orangehrm2 extends BaseTest {
 
    static String projectpath = System.getProperty("user.dir");
 
    @Test(dataProvider = "logindata")
    public void verifyLogin(String username, String password) throws IOException {
 
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
 
        loginpage obj = new loginpage(driver);
        obj.enterusername(username);
        obj.enterpassword(password);
        obj.clickonbutton();
 
        // Validation: Dashboard
        if (obj.dashisdisplayed()) {
            test.pass("Login successful for user: " + username);
        } else {
            test.fail("Login unsuccessful for user: " + username);
            // Screenshot is handled automatically by Listener on failure
        }
 
        // Validation: Title
        if (driver.getTitle().equals("OrangeHRM")) {
            test.pass("Title matched successfully.");
        } else {
            test.fail("Title mismatch.");
        }
    }
 
    @DataProvider
    public Object[][] logindata() throws IOException {
        return ExcelUtilities.getdata(
            projectpath + File.separator + "src" + File.separator + "test" + File.separator + "resources"
            + File.separator + "Orangehrm_Testdata" + File.separator + "data.xlsx",
            "Sheet1"
        );
    }
}
 