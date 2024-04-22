package com.example.askas.gui;

import com.example.askas.UserRecord;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Objects;

public class NavigationController {

    private final Stage primaryStage;
    private Path recordFilePath;

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.recordFilePath = null;
    }

    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(this);
        VBox loginLayout = loginPage.createLoginLayout();
        Scene scene = new Scene(loginLayout, 800, 800); // You can adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/LoginStyles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
    }

    public void navigateToSignUpPage(String role) {
        switch (role) {
            case "Patient" -> showPatientSignUpPage();
            case "Doctor" -> showDoctorSignUpPage();
            case "Nurse" -> showNurseSignUpPage();
            default -> showLoginPage(); // or show an error message or default page
        }
    }

    public void showPatientPage() {
        PatientPage patientPage = new PatientPage(this, null);
        VBox portalLayout = patientPage.createPatientPortalLayout();
        Scene scene = new Scene(portalLayout, 400, 600); // Adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/PatientPortalStyles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Portal");
    }

    public void showPatientDashboard(UserRecord userRecord, Path filePath) {
        // Assuming userRecord is retrieved and available here
        recordFilePath = filePath;
        PatientPage patientDashboardPage = new PatientPage(this, filePath);
        VBox dashboardLayout = patientDashboardPage.createPatientDashboardLayout(userRecord);
        Scene scene = new Scene(dashboardLayout, 800, 600); // Adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dashboard.css")).toExternalForm()); // Link the CSS file
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Dashboard");
    }

    public void showPatientSignUpPage() {
        PatientPage patientSignUpPage = new PatientPage(this, null);
        VBox signUpLayout = patientSignUpPage.createPatientSignUpLayout();
        Scene scene = new Scene(signUpLayout, 450, 800); // Adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/form-styles.css")).toExternalForm()); // Link the universal stylesheet
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Portal - Sign Up");
    }

    public void showDoctorSignUpPage() {
        DoctorPage doctorPage = new DoctorPage(this, null);
        VBox signUpLayout = doctorPage.createDoctorSignUpLayout();
        setupScene(signUpLayout, "Doctor Sign Up");
    }

    public void showDoctorDashboard(UserRecord userRecord, Path filePath) {
        DoctorPage doctorDashboardPage = new DoctorPage(this, userRecord);
        VBox dashboardLayout = doctorDashboardPage.createDoctorDashboardLayout(userRecord, filePath);
        Scene scene = new Scene(dashboardLayout, 700, 900); // Adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dashboard.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Doctor Dashboard");
    }

    public void showNurseSignUpPage() {
        NursePage nursePage = new NursePage(this, null);
        VBox signUpLayout = nursePage.createNurseSignUpLayout();
        Scene scene = new Scene(signUpLayout, 450, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/form-styles.css")).toExternalForm()); // Link the universal stylesheet
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nurse Sign Up");
    }

    private void setupScene(VBox layout, String title) {
        Scene scene = new Scene(layout, 400, 500); // Adjust the size as needed
        scene.getStylesheets().add(getClass().getResource("/UniversalStyles.css").toExternalForm()); // Link the universal stylesheet
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }

    public void showNurseDashboard(UserRecord userRecord, Path filePath) {
        // Assuming userRecord is retrieved and available here
        NursePage nurseDashboardPage = new NursePage(this, userRecord);
        VBox dashboardLayout = nurseDashboardPage.createNurseDashboardLayout(userRecord, filePath);
        Scene scene = new Scene(dashboardLayout, 800, 600); // Adjust the size as needed
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dashboard.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nurse Dashboard");
    }
}
