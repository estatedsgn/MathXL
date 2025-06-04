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
import java.awt.event.ActionEvent;
import java.io.File;

public class StatsView extends JFrame {
    private JButton importBtn, processBtn, exportBtn;
    private JTextField filePathField;
    private JComboBox<String> sheetSelector;
    private AppController controller;

    public StatsView() {
        super("Статистика из Excel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        filePathField = new JTextField();
        filePathField.setEditable(false);

        importBtn = new JButton("Импорт");
        processBtn = new JButton("Обработать");
        exportBtn = new JButton("Экспорт");
        sheetSelector = new JComboBox<>();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Путь к файлу:"));
        panel.add(filePathField);
        panel.add(importBtn);
        panel.add(sheetSelector);
        panel.add(processBtn);
        panel.add(exportBtn);

        add(panel);
        controller = new AppController();

        importBtn.addActionListener(this::handleImport);
        processBtn.addActionListener(this::handleProcess);
        exportBtn.addActionListener(this::handleExport);
    }

    private void handleImport(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                controller.loadExcelData(file.getAbsolutePath());
                filePathField.setText(file.getAbsolutePath());
                sheetSelector.removeAllItems();
                for (String sheet : controller.getAvailableSheets()) {
                    sheetSelector.addItem(sheet);
                }
                JOptionPane.showMessageDialog(this, "Данные импортированы");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка импорта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleProcess(ActionEvent e) {
        String selectedSheet = (String) sheetSelector.getSelectedItem();
        if (selectedSheet == null) {
            JOptionPane.showMessageDialog(this, "Выберите лист", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controller.processData(selectedSheet);
            JOptionPane.showMessageDialog(this, "Статистика рассчитана");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка расчёта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExport(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File dir = fc.getSelectedFile();
            try {
                controller.exportData(dir.getAbsolutePath(), true);
                JOptionPane.showMessageDialog(this, "Данные экспортированы");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}