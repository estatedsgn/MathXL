/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.controller; // Изменено

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.mycompany.mathxl.model.ExcelService; // Изменено
import com.mycompany.mathxl.model.StatsCalculator; // Изменено

import java.io.File;
import java.util.List;
import java.util.Map;

public class AppController {

    private final Stage stage;
    private List<Double> importedData;

    public AppController(Stage stage) {
        this.stage = stage;
    }

    public void handleImport(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Выберите Excel-файл");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fc.showOpenDialog(stage);

        if (file != null) {
            try {
                Map<String, List<Double>> data = ExcelService.importFromExcel(file.getAbsolutePath());
                importedData = data.values().iterator().next(); // Берём первую вкладку
                showAlert("Файл загружен", "Данные успешно прочитаны!");
            } catch (Exception e) {
                showError("Ошибка импорта", e.getMessage());
            }
        }
    }

    public void handleExport(ActionEvent event) {
        if (importedData == null || importedData.isEmpty()) {
            showError("Ошибка", "Нет данных для экспорта");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Сохранить как");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fc.showSaveDialog(stage);

        if (file != null) {
            try {
                Map<String, Object> stats = StatsCalculator.calculateAllStats(importedData);
                ExcelService.exportToExcel(Map.of("Статистика", stats), file.getAbsolutePath());
                showAlert("Готово", "Статистика сохранена в Excel!");
            } catch (Exception e) {
                showError("Ошибка экспорта", e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
