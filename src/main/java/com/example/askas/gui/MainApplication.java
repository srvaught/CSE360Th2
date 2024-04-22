package com.example.askas.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        NavigationController navigationController = new NavigationController(primaryStage);
        navigationController.showLoginPage();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
