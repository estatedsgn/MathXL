/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.controller;

import com.mycompany.mathxl.model.ExcelService;
import com.mycompany.mathxl.model.StatsCalculator;

import java.io.File;
import java.util.*;

public class AppController {
    private Map<String, Map<String, List<Double>>> importedData;
    private Map<String, Object> calculatedStats;
    private String selectedSheet;
    private boolean dataLoaded = false;

    public void loadExcelData(String filePath) throws Exception {
        importedData = ExcelService.importFromExcel(filePath);
        dataLoaded = true;
    }

    public List<String> getAvailableSheets() {
        if (importedData == null || importedData.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(importedData.keySet());
    }

    public void processData(String selectedSheet) throws Exception {
        if (importedData == null || !importedData.containsKey(selectedSheet)) {
            throw new IllegalArgumentException("Лист не найден: " + selectedSheet);
        }

        this.selectedSheet = selectedSheet;
        calculatedStats = new HashMap<>();

        Map<String, List<Double>> sheetData = importedData.get(selectedSheet);
        List<String> columnNames = new ArrayList<>(sheetData.keySet());


        Map<String, Object> singleColumnStats = new LinkedHashMap<>();
        for (String columnName : columnNames) {
            List<Double> data = sheetData.get(columnName);
            if (data.size() < 2) {
                continue;
            }
            singleColumnStats.putAll(StatsCalculator.calculateSingleColumnStats(columnName, data));
        }
        calculatedStats.put("Статистика", singleColumnStats);


        Map<String, Object> covarianceStats = new LinkedHashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            for (int j = 0; j < columnNames.size(); j++) {
                String col1 = columnNames.get(i);
                String col2 = columnNames.get(j);
                List<Double> data1 = sheetData.get(col1);
                List<Double> data2 = sheetData.get(col2);

                if (data1.size() != data2.size() || data1.size() < 2) {
                    continue;
                }

                double covariance = StatsCalculator.calculateCovariance(data1, data2);
                String pairKey = col1 + " - " + col2;
                covarianceStats.put(pairKey, covariance);
            }
        }
        calculatedStats.put("Ковариации", covarianceStats);
    }
    public void exportData(String outputPath, boolean separateFiles) throws Exception {
        if (calculatedStats == null || calculatedStats.isEmpty()) {
            throw new IllegalStateException("Нет данных для экспорта");
        }

        if (separateFiles) {
            File outputDir = new File(outputPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            ExcelService.exportSingleSheetStats(
                (Map<String, Object>) calculatedStats.get("Статистика"),
                outputPath + File.separator + selectedSheet + ".xlsx"
            );
            ExcelService.exportCovarianceStats(
                (Map<String, Object>) calculatedStats.get("Ковариации"),
                outputPath + File.separator + selectedSheet + "_Ковариации.xlsx"
            );
        } else {
            ExcelService.exportToExcel(
                (Map<String, Object>) calculatedStats.get("Статистика"),
                (Map<String, Object>) calculatedStats.get("Ковариации"),
                outputPath
            );
        }
    }

    public Map<String, Object> getCalculatedStats() {
        return (Map<String, Object>) calculatedStats.get("Статистика");
    }

    public Map<String, Object> getCovarianceStats() {
        return (Map<String, Object>) calculatedStats.get("Ковариации");
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }
}