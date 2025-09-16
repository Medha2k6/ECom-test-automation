package com.ui.base;

import java.time.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.ui.utilities.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ApiTestingBaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected ExtentReports extent;
    protected ExtentTest test;
    protected List<Map<String, String>> testData;
    protected String apiTestingUrl = "https://automationexercise.com/api_list";

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        try {
            extent = ExtentManager.getInstance("ApiTestingExtentReport.html");
            System.out.println("ExtentReports initialized for API Testing Suite");
            
            // Load test data from Excel with detailed debugging
            loadTestDataFromExcel();
            System.out.println("Test data loaded successfully. Total test cases: " + 
                (testData != null ? testData.size() : 0));
                
            // Debug: Print first few test cases
            if (testData != null && !testData.isEmpty()) {
                System.out.println("First test case data:");
                Map<String, String> firstTest = testData.get(0);
                for (String key : firstTest.keySet()) {
                    System.out.println("  " + key + ": " + firstTest.get(key));
                }
            }
        } catch (Exception e) {
            System.err.println("Error in beforeSuite setup: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize test suite", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReports flushed for API Testing Suite");
        }
    }

    @Parameters({"browser"})
    @BeforeTest(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
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
            } else if (browser.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                System.out.println("Firefox browser initialized successfully");
            } else {
                throw new IllegalArgumentException("Browser not supported: " + browser);
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
            e.printStackTrace();
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("Failed to setup browser", e);
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
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

    private void loadTestDataFromExcel() throws IOException {
        testData = new ArrayList<>();
        
        // Debug: Print current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        
        // Try multiple possible paths for the Excel file
        String[] possiblePaths = {
            "src/test/resources/APITestData.xlsx",
            "APITestData.xlsx",
            "test-data/APITestData.xlsx",
            System.getProperty("user.dir") + "/src/test/resources/APITestData.xlsx",
            System.getProperty("user.dir") + "/APITestData.xlsx",
            "src/main/resources/APITestData.xlsx"
        };
        
        String filePath = null;
        System.out.println("Searching for Excel file in the following locations:");
        for (String path : possiblePaths) {
            File file = new File(path);
            System.out.println("  Checking: " + path + " -> Exists: " + file.exists() + ", Can read: " + file.canRead());
            if (file.exists() && file.canRead()) {
                filePath = path;
                System.out.println("Found Excel file at: " + filePath);
                break;
            }
        }
        
        if (filePath == null) {
            System.err.println("Excel file 'APITestData.xlsx' not found in any of the expected locations:");
            for (String path : possiblePaths) {
                System.err.println("  - " + path);
            }
            
            // List files in common directories for debugging
            listFilesInDirectory("src/test/resources/");
            listFilesInDirectory("src/main/resources/");
            listFilesInDirectory(".");
            
            throw new IOException("Excel file 'APITestData.xlsx' not found");
        }
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            System.out.println("Excel file opened successfully");
            System.out.println("Number of sheets: " + workbook.getNumberOfSheets());
            
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Sheet name: " + sheet.getSheetName());
            System.out.println("Number of rows: " + (sheet.getLastRowNum() + 1));
            
            if (sheet.getLastRowNum() < 1) {
                throw new IOException("Excel sheet is empty or has no data rows");
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IOException("Excel sheet has no header row");
            }
            
            // Get column headers
            Map<Integer, String> columnHeaders = new HashMap<>();
            System.out.println("Column headers found:");
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String headerValue = cell.getStringCellValue().trim();
                    if (!headerValue.isEmpty()) {
                        columnHeaders.put(i, headerValue);
                        System.out.println("  Column " + i + ": " + headerValue);
                    }
                }
            }
            
            // Read data rows
            int validRowsProcessed = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();
                    boolean hasData = false;
                    
                    for (int j = 0; j < Math.min(row.getLastCellNum(), columnHeaders.size()); j++) {
                        Cell cell = row.getCell(j);
                        String columnName = columnHeaders.get(j);
                        String cellValue = "";
                        
                        if (cell != null && columnName != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    cellValue = cell.getStringCellValue().trim();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = cell.getDateCellValue().toString();
                                    } else {
                                        cellValue = String.valueOf((long) cell.getNumericCellValue());
                                    }
                                    break;
                                case BOOLEAN:
                                    cellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case BLANK:
                                    cellValue = "";
                                    break;
                                default:
                                    cellValue = "";
                            }
                            
                            if (!cellValue.isEmpty()) {
                                hasData = true;
                            }
                            rowData.put(columnName, cellValue);
                        }
                    }
                    
                    // Only add rows that have TestCaseID
                    if (hasData && rowData.containsKey("TestCaseID") && 
                        !rowData.get("TestCaseID").isEmpty()) {
                        testData.add(rowData);
                        validRowsProcessed++;
                        System.out.println("Processed row " + i + ": " + rowData.get("TestCaseID"));
                    } else {
                        System.out.println("Skipped row " + i + ": No valid TestCaseID found");
                    }
                }
            }
            
            System.out.println("Valid rows processed: " + validRowsProcessed);
            
            if (testData.isEmpty()) {
                throw new IOException("No valid test data found in Excel file. Check that TestCaseID column exists and has values.");
            }
            
        } catch (IOException e) {
            System.err.println("Error loading test data from Excel: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private void listFilesInDirectory(String dirPath) {
        try {
            File dir = new File(dirPath);
            System.out.println("Files in directory '" + dirPath + "':");
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        System.out.println("  " + file.getName() + (file.isDirectory() ? " (DIR)" : ""));
                    }
                } else {
                    System.out.println("  Directory is empty or cannot be read");
                }
            } else {
                System.out.println("  Directory does not exist");
            }
        } catch (Exception e) {
            System.out.println("  Error listing files: " + e.getMessage());
        }
    }

    @DataProvider(name = "apiTestData")
    public Object[][] getTestData() {
        System.out.println("DataProvider called. Test data size: " + (testData != null ? testData.size() : "null"));
        
        if (testData == null || testData.isEmpty()) {
            System.err.println("Test data is null or empty. Available data: " + 
                (testData != null ? testData.size() : "null"));
            throw new RuntimeException("Test data not loaded properly");
        }
        
        System.out.println("Providing test data for " + testData.size() + " test cases");
        Object[][] data = new Object[testData.size()][];
        for (int i = 0; i < testData.size(); i++) {
            data[i] = new Object[]{testData.get(i)};
        }
        return data;
    }

    protected void logResult(String testCase, boolean status) {
        if (status) {
            System.out.println("✅ PASS: " + testCase);
        } else {
            System.out.println("❌ FAIL: " + testCase);
        }
    }
}