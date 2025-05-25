/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelService {
    public static Map<String, List<Double>> importFromExcel(String filePath) throws IOException {
        Map<String, List<Double>> result = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                List<Double> values = new ArrayList<>();
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            values.add(cell.getNumericCellValue());
                        }
                    }
                }
                if (!values.isEmpty()) {
                    result.put(sheet.getSheetName(), values);
                }
            }
        }
        return result;
    }

    public static void exportToExcel(Map<String, Map<String, Object>> stats, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        for (Map.Entry<String, Map<String, Object>> entry : stats.entrySet()) {
            Sheet sheet = workbook.createSheet(entry.getKey());
            int rowIdx = 0;

            for (Map.Entry<String, Object> stat : entry.getValue().entrySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stat.getKey());
                row.createCell(1).setCellValue(stat.getValue().toString());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}