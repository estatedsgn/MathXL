/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nyaku
 */
package com.mycompany.mathxl.view;
import com.mycompany.mathxl.controller.AppController;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;

public class AppView extends JFrame {
    private JButton inputButton;
    private JButton processButton;
    private JButton writeButton;
    private JTextField filePathField;
    private AppController controller;

    public AppView() {
        super("Статистика из Excel");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);

        filePathField = new JTextField();
        filePathField.setEditable(false);

        inputButton = new JButton("Прочитать данные");
        processButton = new JButton("Обработать данные");
        writeButton = new JButton("Записать данные в файл");

        processButton.setEnabled(false);
        writeButton.setEnabled(false);

        add(filePathField);
        add(inputButton);
        add(processButton);
        add(writeButton);

        controller = new AppController();

        // Обработчики событий
        inputButton.addActionListener(this::handleImport);
        processButton.addActionListener(this::handleProcess);
        writeButton.addActionListener(this::handleExport);
    }

    private void handleImport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filePathField.setText(file.getAbsolutePath());

            try {
                controller.loadExcelData(file.getAbsolutePath());
                processButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Данные успешно загружены.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка импорта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleProcess(ActionEvent e) {
        if (controller.hasData()) {
            controller.calculateStats();
            JOptionPane.showMessageDialog(this, "Статистика рассчитана.");
            writeButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Нет данных для обработки.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExport(ActionEvent e) {
        if (controller.hasStats()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String outputPath = fileChooser.getSelectedFile().getAbsolutePath() + "/output.xlsx";
                try {
                    controller.exportStats(outputPath);
                    JOptionPane.showMessageDialog(this, "Результат сохранён в: " + outputPath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Нет данных для экспорта.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}