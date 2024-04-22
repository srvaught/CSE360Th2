package com.example.askas.gui;

import com.example.askas.DataManager;
import com.example.askas.UserRecord;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.util.Locale;

public class LoginPage {

    private NavigationController navController;

    public LoginPage(NavigationController navController) {
        this.navController = navController;
    }

    public VBox createLoginLayout() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        Label headerLabel = new Label("AKSAS");
        headerLabel.getStyleClass().add("label-header");

        HBox roleButtons = new HBox(10);
        roleButtons.setAlignment(Pos.CENTER);
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton rbPatient = new RadioButton("Patient");
        rbPatient.setToggleGroup(roleGroup);
        RadioButton rbDoctor = new RadioButton("Doctor");
        rbDoctor.setToggleGroup(roleGroup);
        RadioButton rbNurse = new RadioButton("Nurse");
        rbNurse.setToggleGroup(roleGroup);
        roleButtons.getChildren().addAll(rbPatient, rbDoctor, rbNurse);
        roleButtons.getStyleClass().add("role-buttons");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter User name or ID");
        usernameField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("login-button");
        Button signUpButton = new Button("Sign Up");
        signUpButton.getStyleClass().add("login-button"); // Reuse login button styles or define new ones

        HBox buttons = new HBox(10, loginButton, signUpButton);
        buttons.setAlignment(Pos.CENTER);
        loginButton.setOnAction(e -> {
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            if (selectedRole == null) {
                showAlert("Login Failed", "You must select role");
            } else {
                String role = selectedRole.getText();
                String username = usernameField.getText();
                if (username.isBlank()) {
                    showAlert("Login Failed", "You must enter a username");
                    return;
                }
                String password = passwordField.getText();
                if (password.isBlank()) {
                    showAlert("Login Failed", "You must enter a password");
                    return;
                }
                authenticateAndNavigate(username, password, role);
            }
        });

        signUpButton.setOnAction(e -> {
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            if (selectedRole != null) {
                String role = selectedRole.getText();
                navController.navigateToSignUpPage(role); // Navigate to sign up page based on the selected role
            } else {
                showAlert("Sign Up Failed", "You must select role");
            }
        });

        root.getChildren().addAll(headerLabel, roleButtons, usernameField, passwordField, buttons);

        return root;
    }

    public void  authenticateAndNavigate(String username, String password, String role) {
        boolean recordExists = DataManager.doesUsernameFileExist(username, role.toLowerCase());
        Path filePath1 = DataManager.findUserFilePath(username, role.toLowerCase());
        String filePath;
        if (recordExists) {
            filePath  = filePath1.toString();
            UserRecord userRecord = DataManager.authenticateUser(filePath, username, password);
            if (userRecord != null) {
                switch (role) {
                    case "Doctor":
                        navController.showDoctorDashboard(userRecord, filePath1);
                        break;
                    case "Nurse":
                        navController.showNurseDashboard(userRecord, filePath1);
                        break;
                    case "Patient":
                        navController.showPatientDashboard(userRecord, filePath1);
                        break;
                    default:
                        showAlert("Login Failed", "Invalid role selected!");
                        break;
                }
            } else {
                showAlert("Login Failed", "Invalid username or password!");
            }
        } else {
            showAlert("Login Failed", "No record found!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
