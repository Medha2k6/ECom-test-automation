package com.ui.utilities;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelUtils {

    public static List<String[]> getTestData(String filePath) {
        return getTestData(filePath, -1);
    }

    public static List<String[]> getTestData(String filePath, int numColumns) {
        List<String[]> data = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();
                int colCount = (numColumns == -1) ? row.getLastCellNum() : numColumns;
                String[] rowData = new String[colCount];

                for (int i = 0; i < colCount; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    DataFormatter formatter = new DataFormatter();
                    rowData[i] = formatter.formatCellValue(cell).trim();
                }

                boolean hasData = false;
                for (String cellData : rowData) {
                    if (!cellData.isEmpty()) {
                        hasData = true;
                        break;
                    }
                }

                if (hasData) data.add(rowData);
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    // NEW METHOD: Returns list of maps using first row as headers
    public static Object[][] getTestDataAsMap(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return new Object[0][0]; // empty sheet

            // Read headers
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                DataFormatter formatter = new DataFormatter();
                headers.add(formatter.formatCellValue(cell).trim());
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    DataFormatter formatter = new DataFormatter();
                    rowMap.put(headers.get(i), formatter.formatCellValue(cell).trim());
                }
                dataList.add(rowMap);
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert to Object[][] for TestNG DataProvider
        Object[][] dataArray = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = dataList.get(i);
        }
        return dataArray;
    }
}
