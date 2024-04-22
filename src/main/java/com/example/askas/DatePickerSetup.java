package com.example.askas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DatePickerSetup extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("dd/MM/yyyy"); // Set the prompt text

        // Define the date format and a date converter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Setting the date converter
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        // If the string could not be parsed, return null and possibly show an error message
                        return null;
                    }
                } else {
                    return null; // If the string is empty, return null
                }
            }
        });

        VBox vbox = new VBox(datePicker);
        Scene scene = new Scene(vbox, 200, 100);
        primaryStage.setTitle("DatePicker Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
