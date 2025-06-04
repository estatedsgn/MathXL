/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mathxl;

/**
 *
 * @author nyaku
 */



import com.mycompany.mathxl.view.StatsView;

import javax.swing.*;

public class MathXL {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StatsView view = new StatsView();
            view.setVisible(true);
        });
    }
}