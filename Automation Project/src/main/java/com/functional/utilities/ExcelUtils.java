package com.functional.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

    private static Workbook workbook;
    private static Sheet sheet;
    private static Map<String, Integer> columnMap = new HashMap<>();

    /**
     * Loads an Excel file and selects a sheet.
     * It also populates a map of column headers to their index.
     * @param path The path to the Excel file.
     * @param sheetName The name of the sheet to read.
     * @throws IOException If the file is not found or cannot be read.
     */
    public static void setExcelFile(String path, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        loadColumnHeaders();
    }

    /**
     * Reads the first row of the sheet and populates a map with column names and their respective indices.
     * This allows for easy data retrieval by column name.
     */
    private static void loadColumnHeaders() {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            int colCount = headerRow.getLastCellNum();
            for (int i = 0; i < colCount; i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    columnMap.put(getCellValue(cell), i);
                }
            }
        }
    }

    /**
     * Gets cell data based on row number and column name.
     * @param rowNum The row number (0-based).
     * @param columnName The name of the column.
     * @return The cell value as a String, or an empty string if not found.
     */
    public static String getCellData(int rowNum, String columnName) {
        Integer colNum = columnMap.get(columnName);
        if (colNum == null) {
            return "";
        }
        return getCellData(rowNum, colNum);
    }

    /**
     * Gets cell data based on row and column number.
     * @param rowNum The row number (0-based).
     * @param colNum The column number (0-based).
     * @return The cell value as a String, or an empty string if the row or cell is null.
     */
    public static String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(colNum);
        return getCellValue(cell);
    }

    /**
     * A generic method to get the value of a cell, handling different cell types.
     * @param cell The cell object.
     * @return The cell value as a String.
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).toUpperCase();
            case FORMULA:
                return evaluateFormulaCell(cell);
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Evaluates a formula cell and returns its result as a String.
     * @param cell The cell containing the formula.
     * @return The evaluated formula result as a String.
     */
    private static String evaluateFormulaCell(Cell cell) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case STRING:
                return cellValue.getStringValue();
            case NUMERIC:
                return String.valueOf((long) cellValue.getNumberValue());
            case BOOLEAN:
                return String.valueOf(cellValue.getBooleanValue()).toUpperCase();
            default:
                return "";
        }
    }

    /**
     * Gets all data from a specific row and returns it as a Map.
     * The keys are the column names and the values are the cell data.
     * @param rowNum The row number (0-based).
     * @return A Map containing the row's data.
     */
    public static Map<String, String> getRowDataAsMap(int rowNum) {
        Map<String, String> rowData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            rowData.put(entry.getKey(), getCellData(rowNum, entry.getValue()));
        }
        return rowData;
    }
    

    /**
     * Gets the number of rows containing data, excluding the header row.
     * @return The number of data rows.
     */
    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows() - 1;
    }

    /**
     * Gets the total number of rows, including the header.
     * @return The total number of rows.
     */
    public static int getTotalRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * Closes the workbook and clears the column map.
     * @throws IOException If an I/O error occurs.
     */
    public static void closeExcel() throws IOException {
        if (workbook != null) {
            workbook.close();
            columnMap.clear();
        }
    }
}