package com.example.askas.main_components;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class HospitalManagementSystem extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private String userRole = "";

    private Map<String, User> users = new HashMap<>(); 

    public HospitalManagementSystem() {

        users.put("patient1", new User("patient1", "password123", "Patient"));
        users.put("doctor1", new User("doctor1", "securepassword", "Doctor"));
        users.put("nurse1", new User("nurse1", "medicalpass", "Nurse"));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Hospital Management System");

        initRootLayout();
        showLogin();
    }

    private void initRootLayout() {
        rootLayout = new BorderPane();
        primaryStage.setScene(new Scene(rootLayout, 1024, 768));
        primaryStage.show();
    }

    private void showLogin() {
        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("AKSAS");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

        ToggleGroup roleGroup = new ToggleGroup();
        HBox roleButtons = new HBox(10);
        roleButtons.setAlignment(Pos.CENTER);
        RadioButton patientBtn = new RadioButton("Patient");
        patientBtn.setToggleGroup(roleGroup);
        patientBtn.setSelected(true);
        RadioButton doctorBtn = new RadioButton("Doctor");
        doctorBtn.setToggleGroup(roleGroup);
        RadioButton nurseBtn = new RadioButton("Nurse");
        nurseBtn.setToggleGroup(roleGroup);

        roleButtons.getChildren().addAll(patientBtn, doctorBtn, nurseBtn);

        TextField usernameTextField = new TextField();
        usernameTextField.setMaxWidth(200);
        usernameTextField.setPromptText("Enter User name or ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameTextField.getText(), passwordField.getText(), roleGroup));

        loginBox.getChildren().addAll(titleLabel, roleButtons, usernameTextField, passwordField, loginButton);
        loginBox.setPadding(new Insets(20));

        rootLayout.setCenter(loginBox);
    }

    private void showVitalsView() {
        GridPane vitalsGrid = new GridPane();
        vitalsGrid.setAlignment(Pos.CENTER);
        vitalsGrid.setPadding(new Insets(20));
        vitalsGrid.setVgap(10);
        vitalsGrid.setHgap(10);

        Label titleLabel = new Label("Enter Patient Vitals");
        vitalsGrid.add(titleLabel, 0, 0, 2, 1);

        TextField temperatureField = new TextField();
        TextField bloodPressureField = new TextField();
        TextField heartRateField = new TextField();
        TextField respiratoryRateField = new TextField();
        TextField oxygenSaturationField = new TextField();
        TextField heightField = new TextField();
        TextField weightField = new TextField();

        vitalsGrid.addRow(1, new Label("Temperature (°F):"), temperatureField);
        vitalsGrid.addRow(2, new Label("Blood Pressure (mmHg):"), bloodPressureField);
        vitalsGrid.addRow(3, new Label("Heart Rate (bpm):"), heartRateField);
        vitalsGrid.addRow(4, new Label("Respiratory Rate (breaths/min):"), respiratoryRateField);
        vitalsGrid.addRow(5, new Label("Oxygen Saturation (%):"), oxygenSaturationField);
        vitalsGrid.addRow(6, new Label("Height (ft):"), heightField);
        vitalsGrid.addRow(7, new Label("Weight (lb):"), weightField);

        Button saveButton = new Button("Save");
        vitalsGrid.add(saveButton, 1, 8);
        saveButton.setOnAction(e -> saveVitals(
            temperatureField.getText(),
            bloodPressureField.getText(),
            heartRateField.getText(),
            respiratoryRateField.getText(),
            oxygenSaturationField.getText(),
            heightField.getText(),
            weightField.getText()
        ));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showNurseDashboard());
        vitalsGrid.add(backButton, 1, 9);

        rootLayout.setCenter(vitalsGrid);
    }

    private void saveVitals(String temperature, String bloodPressure, String heartRate, String respiratoryRate, String oxygenSaturation, String height, String weight) {

        String patientId = "patient123"; 

        Vitals vitals = new Vitals();

        PatientRecord record = PatientRecord.loadFromFile(patientId); 
        if (record != null) {
            record.updateVitals(vitals); 
            record.saveToFile(); 
            System.out.println("Vitals saved for patient ID: " + patientId);
        } else {
            System.err.println("Patient record not found for ID: " + patientId);
        }
    }

    private void handleLogin(String username, String password, ToggleGroup roleGroup) {
        User user = users.get(username); 
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        String userRole = selectedRole.getText();

        if (user != null && user.authenticate(password)) {

            switch (userRole) {
                case "Patient":
                    if ("Patient".equals(user.role)) {
                        showPatientPortal(null);
                    } else {
                        showError("Login Failed", "You do not have Patient access.");
                    }
                    break;
                case "Doctor":
                    if ("Doctor".equals(user.role)) {
                        showDoctorDashboard();
                    } else {
                        showError("Login Failed", "You do not have Doctor access.");
                    }
                    break;
                case "Nurse":

                    if ("Nurse".equals(user.role)) {
                        showNurseVerification();
                    } else {
                        showError("Login Failed", "You do not have Nurse access.");
                    }
                    break;
                default:
                    showError("Login Failed", "Unknown role selected.");
                    break;
            }
        } else {

            showError("Login Failed", "Invalid username or password.");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red");

        Button tryAgainButton = new Button("Try Again");
        tryAgainButton.setOnAction(e -> {
            rootLayout.setCenter(null); 
            showLogin();                
        });

        VBox errorBox = new VBox(10, errorLabel, tryAgainButton);
        errorBox.setAlignment(Pos.CENTER);
        rootLayout.setCenter(errorBox);
    }

    private void showNurseVerification()
    {
        Label staffLabel = new Label("Nurse Verification");
        staffLabel.getStyleClass().add("title-label");
        Label userIDLabel = new Label("ID Number");
        Label passwordLabel = new Label("Password");

        TextField userIDField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Enter");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(5);

        grid.add(staffLabel, 1, 0);
        grid.add(userIDLabel, 0, 1);
        grid.add(userIDField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String entereduserID = userIDField.getText();
                String enteredPassword = passwordField.getText();

                String userID = "Abdullah";
                String password = "123";

                if (entereduserID.equals(userID) && enteredPassword.equals(password)) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Login Successful");
                    successAlert.setHeaderText("Welcome!");
                    successAlert.setContentText("You have successfully logged in.");
                    successAlert.showAndWait();
                } else {
                    Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                    failureAlert.setHeaderText("Invalid Credentials");
                    failureAlert.setContentText("Please check The ID Number and Password and try again.");
                    failureAlert.showAndWait();
                }
            }
        });

        Scene scene = new Scene(grid, 350, 350);

        primaryStage.setTitle("Nurse Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPatientPortal(Runnable cb) {
        VBox patientPortal = new VBox(15); 
        patientPortal.setPadding(new Insets(20));
        patientPortal.setStyle("-fx-background-color: #f0f0f0"); 

        HBox profileBar = new HBox();
        profileBar.setStyle("-fx-padding: 10px 0px 10px 10px; -fx-background-color: #e0e0e0; -fx-border-radius: 5px 0px 0px 5px;");

        Label profileTitle = new Label("Profile Management");
        profileTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090"); 

        Line separatorLine = new Line();
        separatorLine.setStroke(Color.color(0.8, 0.8, 0.8));
        separatorLine.setStrokeWidth(1);
        separatorLine.setStartY(0);  
        separatorLine.setEndY(40);   

        profileBar.getChildren().addAll(profileTitle, separatorLine);

        HBox profileDetails = new HBox(10); 
        Label nameLabel = new Label("Name: ");
        nameLabel.setPrefWidth(80); 

        String patientInfo = "Jofin Doe\n" +
                "123 Main St, Cityville\n" +
                "123-456-7890\n" +
                "johndoe@example.com";
        Label nameAndInfo = new Label(patientInfo);
        nameAndInfo.setWrapText(true); 

        profileDetails.getChildren().addAll(nameLabel, nameAndInfo);

        Button saveChangesButton = new Button("Save Changes");

        saveChangesButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white; -fx-cursor: default;");
        saveChangesButton.setDisable(true); 

        Button backButton = new Button("Back");
        if (cb != null) {

            backButton.setOnAction(e -> cb.run());
        } else {

            backButton.setOnAction(e -> showLogin());
        }

        patientPortal.getChildren().add(backButton);

        VBox visitSummariesSection = new VBox(10);
        visitSummariesSection.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px;");

        Label visitHistoryTitle = new Label("Past Visits Summaries");
        visitHistoryTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090;");

        visitSummariesSection.getChildren().addAll(visitHistoryTitle, createVisitSummary("10/25/2021", "Flu", "Rest and fluids"),
                createVisitSummary("09/15/2021", "Allergies", "Antihistamines, Panadol Night"));

        Label messagingTitle = new Label("Messaging System");
        messagingTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090; -fx-margin-top: 20px;");

        HBox messageControls = new HBox(10);
        ComboBox<String> recipientComboBox = new ComboBox<>(); 
        recipientComboBox.setDisable(true);
        TextField messageField = new TextField();
        messageField.setDisable(true);
        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setDisable(true);

        messageControls.getChildren().addAll(recipientComboBox, messageField, sendMessageButton);

        patientPortal.getChildren().addAll(profileBar, profileDetails, saveChangesButton, visitSummariesSection, messagingTitle, messageControls);
        rootLayout.setCenter(patientPortal);
    }

    private VBox createVisitSummary(String visitDate, String diagnosis, String treatment) {
        VBox visitSummary = new VBox(5);
        visitSummary.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10px; -fx-border-radius: 5px;"); 

        Label dateLabel = new Label("Visit Date: " + visitDate);
        Label diagnosisLabel = new Label("Diagnosis: " + diagnosis);
        Label treatmentLabel = new Label("Treatment: " + treatment);

        visitSummary.getChildren().addAll(dateLabel, diagnosisLabel, treatmentLabel);
        return visitSummary;
    }

    private void showDoctorVerification()
    {
        Label staffLabel = new Label("Doctor Verification");
        staffLabel.getStyleClass().add("title-label");
        Label userIDLabel = new Label("ID Number");
        Label passwordLabel = new Label("Password");

        TextField userIDField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Enter");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(5);

        grid.add(staffLabel, 1, 0);
        grid.add(userIDLabel, 0, 1);
        grid.add(userIDField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String entereduserID = userIDField.getText();
                String enteredPassword = passwordField.getText();

                String userID = "Abdullah";
                String password = "123";

                if (entereduserID.equals(userID) && enteredPassword.equals(password)) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Login Successful");
                    successAlert.setHeaderText("Welcome!");
                    successAlert.setContentText("You have successfully logged in.");
                    successAlert.showAndWait();
                } else {
                    Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                    failureAlert.setHeaderText("Invalid Credentials");
                    failureAlert.setContentText("Please check The ID Number and Password and try again.");
                    failureAlert.showAndWait();
                }
            }
        });

        Scene scene = new Scene(grid, 350, 350);

        primaryStage.setTitle("Doctor Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static class User {
        private final String username;
        private final String password; 
        private final String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public boolean authenticate(String password) {
            return this.password.equals(password);
        }
    }

    private void showAccessHistoryView() {
        VBox accessHistoryView = new VBox(10);
        accessHistoryView.setAlignment(Pos.CENTER);
        accessHistoryView.setPadding(new Insets(15));

        TextField patientIdLookupField = new TextField();
        patientIdLookupField.setPromptText("Enter Patient ID for History");
        Button lookupButton = new Button("Lookup History");

        TextArea historyTextArea = new TextArea();
        historyTextArea.setEditable(false);

        lookupButton.setOnAction(e -> {
            String patientId = patientIdLookupField.getText();
            PatientRecord record = PatientRecord.loadFromFile(patientId);

        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDoctorDashboard());
        accessHistoryView.getChildren().addAll(new Label("Access Patient History"), patientIdLookupField, lookupButton, historyTextArea, backButton);
        rootLayout.setCenter(accessHistoryView);
    }

    private void showRegisterRecordView() {
        GridPane registerRecordView = new GridPane();
        registerRecordView.setAlignment(Pos.CENTER);
        registerRecordView.setVgap(10);
        registerRecordView.setHgap(10);
        registerRecordView.setPadding(new Insets(10));

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");
        TextField patientNameField = new TextField();
        patientNameField.setPromptText("Patient Name");

        Button saveRecordButton = new Button("Save Record");
        saveRecordButton.setOnAction(e -> {
            String patientId = patientIdField.getText().trim();
            String patientName = patientNameField.getText().trim();

            if (!patientId.isEmpty() && !patientName.isEmpty()) {
                PatientRecord patientRecord = new PatientRecord(patientId);
                patientRecord.setPatientName(patientName);

                patientRecord.saveToFile(); 

                System.out.println("Saved/Updated Record for: " + patientName);

            } else {
                System.err.println("Patient ID and name must not be empty.");

            }
        });

        registerRecordView.add(new Label("Patient ID:"), 0, 0);
        registerRecordView.add(patientIdField, 1, 0);
        registerRecordView.add(new Label("Patient Name:"), 0, 1);
        registerRecordView.add(patientNameField, 1, 1);

        registerRecordView.add(saveRecordButton, 1, 3);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showNurseDashboard()); 
        registerRecordView.add(backButton, 1, 4); 

        rootLayout.setCenter(registerRecordView);

    }

    private void savePatientCheckIn(String name, String id) {

        String filePath = "patient_check_ins.txt";

        String patientInfo = name + "," + id + "\n";

        try {

            Files.write(Paths.get(filePath), patientInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            System.out.println("Patient Checked-In: " + name);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void showGreetPatientView() {

    	    VBox checkInView = new VBox(10);
    	    checkInView.setAlignment(Pos.CENTER);
    	    checkInView.setPadding(new Insets(15));

    	    Label checkInLabel = new Label("Patient Check-In");
    	    checkInLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    	    TextField patientNameField = new TextField();
    	    patientNameField.setPromptText("Enter Patient's Name");

    	    TextField patientIDField = new TextField();
    	    patientIDField.setPromptText("Enter Patient's ID");

    	    Button checkInButton = new Button("Check-In");
    	    checkInButton.setOnAction(e -> savePatientCheckIn(patientNameField.getText(), patientIDField.getText()));

    	    Button backButton = new Button("Back");
    	    backButton.setOnAction(e -> showNurseDashboard());

    	    checkInView.getChildren().addAll(checkInLabel, patientNameField, patientIDField, checkInButton, backButton);
    	    rootLayout.setCenter(checkInView);

    }

    private void showNurseDashboard() {
        VBox nurseDashboard = new VBox(10);
        nurseDashboard.setAlignment(Pos.CENTER);
        nurseDashboard.setPadding(new Insets(15));

        Label dashboardLabel = new Label("Nurse Dashboard");
        dashboardLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button greetPatientButton = new Button("Meet and Greet Patient");
        greetPatientButton.setOnAction(e -> showGreetPatientView());

        Button registerRecordButton = new Button("Register New Record");
        registerRecordButton.setOnAction(e -> showRegisterRecordView());

        Button takeVitalsButton = new Button("Take Vitals");
        takeVitalsButton.setOnAction(e -> showVitalsView());

        Button accessHistoryButton = new Button("Access Patient History");
        accessHistoryButton.setOnAction(e -> showAccessHistoryView());

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> showLogin());

        nurseDashboard.getChildren().addAll(dashboardLabel, greetPatientButton, registerRecordButton, takeVitalsButton, accessHistoryButton, logoutButton);
        rootLayout.setCenter(nurseDashboard);
    }

    private void showDoctorDashboard() {
        VBox doctorDashboard = new VBox(10);
        doctorDashboard.setAlignment(Pos.CENTER);
        doctorDashboard.setPadding(new Insets(15));

        Label dashboardLabel = new Label("Doctor Dashboard");
        dashboardLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button accessHistoryButton = new Button("Access Patient History");
        accessHistoryButton.setOnAction(e -> showAccessHistoryView()); 

        Button listMedicationsButton = new Button("List Prescribed Medications");
        listMedicationsButton.setOnAction(e -> listPrescribedMedications()); 

        Button sendPrescriptionButton = new Button("Send Prescription to Pharmacy");
        sendPrescriptionButton.setOnAction(e -> sendPrescriptionToPharmacy()); 

        Button accessRecordsButton = new Button("Access Patient Records");
        accessRecordsButton.setOnAction(e -> accessPatientRecords()); 

        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> showDoctorDashboard()); 

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> showLogin()); 

        doctorDashboard.getChildren().addAll(dashboardLabel, accessHistoryButton, listMedicationsButton, sendPrescriptionButton, accessRecordsButton, homeButton, logoutButton);
        rootLayout.setCenter(doctorDashboard);
    }

    private void listPrescribedMedications() {

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f0f0f0;");

        Label patientInfoLabel = new Label("Patient: John Doe");
        patientInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #206090; -fx-padding: 5px;");
        HBox patientInfoBox = new HBox(patientInfoLabel);
        patientInfoBox.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #c0c0c0; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white;");
        backButton.setOnAction(e -> showDoctorDashboard()); 

        Label title = new Label("Prescribed Medications");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #206090;");

        Label medicationDetails = new Label(
                "Medication: Amoxicillin\n" +
                        "Dosage: 500mg\n" +
                        "Frequency: Three times a day\n" +
                        "Prescribed by: Dr. Smith\n" +
                        "Date: 01/01/2024\n\n" +
                        "Medication: Ibuprofen\n" +
                        "Dosage: 400mg\n" +
                        "Frequency: As needed for pain\n" +
                        "Prescribed by: Dr. Smith\n" +
                        "Date: 15/01/2024"
        );
        medicationDetails.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        mainContainer.getChildren().addAll(backButton, patientInfoBox, title, medicationDetails);

        rootLayout.setCenter(mainContainer); 
    }

    private void sendPrescriptionToPharmacy() {
        VBox prescriptionForm = new VBox(10);
        prescriptionForm.setPadding(new Insets(20));
        prescriptionForm.setStyle("-fx-background-color: #f0f0f0;");

        Label formTitle = new Label("Send Prescription to Pharmacy");
        formTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #206090;");

        Label patientInfoLabel = new Label("Patient: John Doe");
        patientInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #206090; -fx-padding: 5px;");

        TextField medicationField = new TextField();
        medicationField.setPromptText("Medication");

        TextField dosageField = new TextField();
        dosageField.setPromptText("Dosage");

        TextField frequencyField = new TextField();
        frequencyField.setPromptText("Frequency");

        ComboBox<String> pharmacyComboBox = new ComboBox<>();
        pharmacyComboBox.setPromptText("Select Pharmacy");

        pharmacyComboBox.getItems().addAll("Pharmacy A", "Pharmacy B", "Pharmacy C");

        Button sendButton = new Button("Send to Pharmacy");
        sendButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
        sendButton.setOnAction(e -> {

            System.out.println("Sending prescription: " + medicationField.getText() + ", " +
                    dosageField.getText() + ", " +
                    frequencyField.getText() + " to " + pharmacyComboBox.getValue());
            showAlert("Success", "The prescription has been sent to the pharmacy.");

        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white;");
        backButton.setOnAction(e -> showDoctorDashboard()); 

        prescriptionForm.getChildren().addAll(formTitle, patientInfoLabel, medicationField, dosageField, frequencyField, pharmacyComboBox, sendButton, backButton);

        rootLayout.setCenter(prescriptionForm); 
    }

    private void accessPatientRecords() {
        VBox patientRecordsForm = new VBox(10);
        patientRecordsForm.setPadding(new Insets(20));
        patientRecordsForm.setStyle("-fx-background-color: #f0f0f0;");

        Label formTitle = new Label("Access Patient Records");
        formTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #206090;");

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");

        Button submitButton = new Button("Access Records");
        submitButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            String patientId = patientIdField.getText();

            if (patientId.equals("1234")) { 
                showPatientPortal(this::showDoctorDashboard); 
            } else {
                showAlert("Error", "Patient ID not found. Please try again.");
            }
        });

        patientRecordsForm.getChildren().addAll(formTitle, patientIdField, submitButton);
        rootLayout.setCenter(patientRecordsForm); 
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
