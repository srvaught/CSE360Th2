package com.example.askas.gui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchPatientPage {

    public VBox createSearchPatientLayout() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("search-patient-root");

        Label searchLabel = new Label("Search Patient Record");
        searchLabel.getStyleClass().add("search-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.getStyleClass().add("search-field");

        DatePicker birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("Date of Birth");
        birthDatePicker.getStyleClass().add("search-field");

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");

        HBox searchFields = new HBox(10, nameField, birthDatePicker, searchButton);
        searchFields.setAlignment(Pos.CENTER);

        // Add additional search parameters if needed

        layout.getChildren().addAll(searchLabel, searchFields);

        return layout;
    }
}
