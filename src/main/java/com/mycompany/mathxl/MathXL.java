/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mathxl;

/**
 *
 * @author nyaku
 */


import com.mycompany.mathxl.view.AppView;


import com.mycompany.mathxl.controller.AppController;



import com.mycompany.mathxl.view.AppView;

import javax.swing.*;

public class MathXL {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppView view = new AppView();
            view.setVisible(true);
        });
    }
}