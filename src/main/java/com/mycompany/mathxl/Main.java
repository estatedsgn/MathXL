/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mathxl;

/**
 *
 * @author nyaku
 */


import com.mycompany.mathxl.view.AppView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppView view = new AppView();
        view.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
