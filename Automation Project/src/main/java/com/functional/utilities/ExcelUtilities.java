package com.functional.utilities;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtilities {

    public static List<String[]> getData(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();
                String username = row.getCell(0) != null ? row.getCell(0).toString().trim() : "";
                String password = row.getCell(1) != null ? row.getCell(1).toString().trim() : "";
                data.add(new String[] { username, password });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
