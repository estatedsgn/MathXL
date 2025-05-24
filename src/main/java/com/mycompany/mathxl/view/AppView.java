/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.view; // Изменено

import com.mycompany.mathxl.controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppView {

    public void show(Stage primaryStage) {
        Button importBtn = new Button("Импорт из Excel");
        Button exportBtn = new Button("Экспорт в Excel");
        Button exitBtn = new Button("Выход");

        VBox root = new VBox(10, importBtn, exportBtn, exitBtn);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MathXl — Статистика из Excel"); // Изменено
        primaryStage.show();

        AppController controller = new AppController(primaryStage);
        importBtn.setOnAction(controller::handleImport);
        exportBtn.setOnAction(controller::handleExport);
        exitBtn.setOnAction(e -> System.exit(0));
    }
}