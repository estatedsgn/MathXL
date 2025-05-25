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
    private Map<String, List<Double>> importedData;
    private Map<String, Map<String, Object>> calculatedStats;

    public void loadExcelData(String filePath) throws Exception {
        importedData = ExcelService.importFromExcel(filePath);
        calculatedStats = new LinkedHashMap<>();
    }

    public void calculateStats() {
        for (Map.Entry<String, List<Double>> entry : importedData.entrySet()) {
            calculatedStats.put(entry.getKey(), StatsCalculator.calculateAllStats(entry.getValue()));
        }
    }

    public void exportStats(String outputPath) throws Exception {
        ExcelService.exportToExcel(calculatedStats, outputPath);
    }

    public boolean hasData() {
        return importedData != null && !importedData.isEmpty();
    }

    public boolean hasStats() {
        return calculatedStats != null && !calculatedStats.isEmpty();
    }
}