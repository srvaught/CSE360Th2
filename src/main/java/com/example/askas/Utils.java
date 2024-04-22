package com.example.askas;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidPassword(String password) {
        if (password.isBlank()) {
            showAlert("Error", "You must enter a password");
            return false;
        } else if (password.length() < 8) {
            showAlert("Invalid Password", "Password must be at least 8 characters");
        return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;

            // If all conditions are met, no need to check further
            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return true;
            }
        }

        if (!(hasUpper && hasLower && hasDigit && hasSpecial)) {
            showAlert("Invalid Password", "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static boolean isValidEmail(String email) {
        if (email.isBlank()) {
            showAlert("Error", "You must enter an email");
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            showAlert("Error", "Improper email format");
            return false;
        }
        return true;
    }

    public static void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }

    public static void showAlert(String title, String content, Alert.AlertType alertType ) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void setupDatePicker(DatePicker datePicker) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null ? formatter.format(date) : "");
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string != null ? LocalDate.parse(string, formatter) : null);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });
    }

}
