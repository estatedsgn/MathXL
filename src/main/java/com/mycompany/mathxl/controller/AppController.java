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

import java.util.*;

public class AppController {
    private Map<String, Map<String, List<Double>>> importedData;
    private Map<String, Map<String, Map<String, Object>>> calculatedStats;
    private boolean dataLoaded = false;

    public void loadExcelData(String filePath) throws Exception {
        importedData = ExcelService.importFromExcel(filePath);
        dataLoaded = true;
    }

    public List<String> getAvailableSheets() {
        if (importedData == null) return Collections.emptyList();
        return new ArrayList<>(importedData.keySet());
    }

    public void processData(String sheetName) {
        Map<String, List<Double>> sheetData = importedData.get(sheetName);
        calculatedStats = new HashMap<>();
        calculatedStats.put(sheetName, StatsCalculator.calculateAllStatsForColumns(sheetData));
    }

    public void exportData(String outputPath) throws Exception {
        ExcelService.exportToExcel(calculatedStats, outputPath);
    }
}