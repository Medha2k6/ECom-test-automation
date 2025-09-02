package proj11.runner;

import org.testng.TestNG;
import proj11.listeners.ExtentTestListener;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        
        // Set up test classes
        List<String> suites = new ArrayList<>();
        suites.add("testng.xml"); // Path to your TestNG XML file
        testng.setTestSuites(suites);
        
        // Add listeners
        testng.addListener(new ExtentTestListener());
        
        // Set system properties for browser if needed
        System.setProperty("browser", "chrome"); // Default browser
        
        // Run tests
        System.out.println("Starting Automation Exercise Home Page Test Suite...");
        testng.run();
        System.out.println("Test execution completed. Check the Extent Report for detailed results.");
    }
    
    public static void runWithBrowser(String browser) {
        System.setProperty("browser", browser);
        main(new String[]{});
    }
    
    public static void runSpecificTest(String testMethodName) {
        TestNG testng = new TestNG();
        
        // Try to use the available test class
        try {
            Class<?> testClass = Class.forName("proj11.HomePageTestSuite");
            testng.setTestClasses(new Class[]{testClass});
        } catch (ClassNotFoundException e) {
            try {
                Class<?> simpleTestClass = Class.forName("proj11.SimpleHomePageTestSuite");
                testng.setTestClasses(new Class[]{simpleTestClass});
            } catch (ClassNotFoundException ex) {
                System.err.println("No test class found: " + ex.getMessage());
                return;
            }
        }
        
        testng.addListener(new ExtentTestListener());
        testng.run();
    }
}