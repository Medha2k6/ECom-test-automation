package com.functional.datadriven;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;

public class ExcelDiagnosticTest {

    @Test
    public void diagnoseExcelFile() {
        String excelPath = "C:\\Users\\Keerthana\\eclipse-workspace\\Functional Test Case\\src\\test\\java\\resources\\contact.xlsx";
        
        System.out.println("=== EXCEL FILE DIAGNOSTIC TEST ===\n");
        
        // Step 1: Check if file exists
        File file = new File(excelPath);
        System.out.println("1. File Path: " + excelPath);
        System.out.println("   File exists: " + file.exists());
        System.out.println("   File is readable: " + file.canRead());
        System.out.println("   Absolute path: " + file.getAbsolutePath());
        
        if (!file.exists()) {
            System.out.println("\n❌ ERROR: File does not exist at the specified path!");
            System.out.println("\nPossible locations to check:");
            
            // Check alternative locations
            String[] alternativePaths = {
                "/Users/srimedha/Downloads/contact.xlsx",
                "/Users/srimedha/Downloads/Functional Test Cases/contact.xlsx",
                "/Users/srimedha/Downloads/Functional Test Cases/src/test/resources/contact.xlsx",
                "./src/test/resources/contact.xlsx",
                "./contact.xlsx"
            };
            
            for (String altPath : alternativePaths) {
                File altFile = new File(altPath);
                if (altFile.exists()) {
                    System.out.println("   ✓ Found at: " + altPath);
                } else {
                    System.out.println("   ✗ Not at: " + altPath);
                }
            }
            return;
        }
        
        // Step 2: Try to open and read the Excel file
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            
            System.out.println("\n2. Successfully opened Excel file!");
            System.out.println("   Number of sheets: " + workbook.getNumberOfSheets());
            
            // Step 3: List all sheet names
            System.out.println("\n3. Available sheets:");
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String sheetName = workbook.getSheetName(i);
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("   Sheet " + (i+1) + ": \"" + sheetName + "\" (Rows: " + 
                                 sheet.getPhysicalNumberOfRows() + ")");
            }
            
            // Step 4: Try to get specific sheets
            System.out.println("\n4. Testing sheet access:");
            String[] testSheetNames = {"Sheet1", "Sheet", "Table 1", "Data", "Contact"};
            for (String testName : testSheetNames) {
                Sheet testSheet = workbook.getSheet(testName);
                if (testSheet != null) {
                    System.out.println("   ✓ Found sheet: \"" + testName + "\"");
                } else {
                    System.out.println("   ✗ Sheet not found: \"" + testName + "\"");
                }
            }
            
            // Step 5: Read first sheet's data (if exists)
            if (workbook.getNumberOfSheets() > 0) {
                Sheet firstSheet = workbook.getSheetAt(0);
                System.out.println("\n5. First sheet preview (\"" + workbook.getSheetName(0) + "\"):");
                
                int maxRowsToShow = Math.min(5, firstSheet.getPhysicalNumberOfRows());
                for (int row = 0; row < maxRowsToShow; row++) {
                    Row currentRow = firstSheet.getRow(row);
                    if (currentRow != null) {
                        System.out.print("   Row " + row + ": ");
                        int maxColsToShow = Math.min(8, currentRow.getPhysicalNumberOfCells());
                        for (int col = 0; col < maxColsToShow; col++) {
                            Cell cell = currentRow.getCell(col);
                            if (cell != null) {
                                System.out.print("[" + getCellValueAsString(cell) + "] ");
                            } else {
                                System.out.print("[empty] ");
                            }
                        }
                        System.out.println();
                    }
                }
            }
            
            workbook.close();
            fis.close();
            
            System.out.println("\n✅ DIAGNOSTIC COMPLETE - Use the correct sheet name from above in your test!");
            
        } catch (Exception e) {
            System.out.println("\n❌ ERROR reading Excel file:");
            e.printStackTrace();
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }
}