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
    public static Map<String, Map<String, List<Double>>> importFromExcel(String filePath) throws IOException {
        Map<String, Map<String, List<Double>>> result = new LinkedHashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                Map<String, List<Double>> columns = new LinkedHashMap<>();
                Row headerRow = sheet.getRow(sheet.getFirstRowNum());

                for (int col = 0; col < headerRow.getLastCellNum(); col++) {
                    Cell cell = headerRow.getCell(col);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String colName = cell.getStringCellValue().trim();
                        columns.put(colName, new ArrayList<>());
                    }
                }

                for (int rowIdx = sheet.getFirstRowNum() + 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                    Row row = sheet.getRow(rowIdx);
                    if (row == null) continue;

                    for (int col = 0; col < headerRow.getLastCellNum(); col++) {
                        Cell cell = row.getCell(col);
                        if (cell != null) {
                            String colName = headerRow.getCell(col).getStringCellValue().trim();
                            double value;

                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    value = cell.getNumericCellValue();
                                    break;
                                case STRING:
                                    try {
                                        value = Double.parseDouble(cell.getStringCellValue().trim());
                                    } catch (NumberFormatException e) {
                                        continue;
                                    }
                                    break;
                                case FORMULA:
                                    CellType ct = cell.getCachedFormulaResultType();
                                    if (ct == CellType.NUMERIC) {
                                        value = cell.getNumericCellValue();
                                    } else {
                                        continue;
                                    }
                                    break;
                                default:
                                    continue;
                            }

                            columns.get(colName).add(value);
                        }
                    }
                }

                result.put(sheet.getSheetName(), columns);
            }
        }
        return result;
    }
    public static void exportSingleSheetStats(Map<String, Object> stats, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Статистика");

        int rowIdx = 0;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().toString());
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public static void exportCovarianceStats(Map<String, Object> stats, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ковариации");

        int rowIdx = 0;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().toString());
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public static void exportToExcel(Map<String, Object> stats, Map<String, Object> covarianceStats, String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Статистика");

        int rowIdx = 0;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().toString());
        }

        for (Map.Entry<String, Object> entry : covarianceStats.entrySet()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().toString());
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}