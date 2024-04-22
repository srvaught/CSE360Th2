package com.example.askas.gui;


import com.example.askas.DataManager;
import com.example.askas.Patient;
import com.example.askas.UserRecord;
import com.example.askas.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NursePage {

    private NavigationController navController;
    private UserRecord userRecord;
    private Path recordFilePath;
    private HashMap<String, Path> patientMap = new HashMap<>();
    private HashMap<String, Path> doctorMap = new HashMap<>();
    private static Path recordHandler; // Member field to hold the selected record path
    private static UserRecord userHandler;

    public NursePage(NavigationController navController, UserRecord userRecord) {
        this.navController = navController;
        this.userRecord = userRecord;
        recordHandler = recordHandler != null ? recordHandler : null;
        userHandler = userHandler != null ? userHandler : new UserRecord("", "", "", null);

    }

    public VBox createNurseSignUpLayout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("root");

        Text signUpTitle = new Text("Nurse Account Creation");
        signUpTitle.getStyleClass().add("title");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.getStyleClass().add("text-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.getStyleClass().add("text-field");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (you will use this to log in)");
        usernameField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");

        ToggleGroup genderGroup = new ToggleGroup();

        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderGroup);
        maleButton.setUserData("male");

        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderGroup);
        femaleButton.setUserData("Female");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("dd/MM/yyyy");
        Utils.setupDatePicker(datePicker);

        Button createAccountButton = new Button("Create Account");
        createAccountButton.getStyleClass().add("button");
        createAccountButton.setOnAction(e -> {
            boolean success = PatientPage.AuthenticateFields(fullNameField.getText(), emailField.getText(), usernameField.getText(),
                    passwordField.getText(), datePicker.getValue());
            if (!success)
                return;
            if (genderGroup.getSelectedToggle() == null) {
                Utils.showAlert("Validation Error", "Please select a gender.");
                return;
            }
            String selectedGender = genderGroup.getSelectedToggle().getUserData().toString();
            if (success) {
                Path filePath = DataManager.findUserFilePath(usernameField.getText(), "Nurse");
                if (filePath != null) {
                    Utils.showAlert("Warning", "The following username has already been taken: " + usernameField.getText());
                    return;
                }
                try {
                    DataManager.createUserRecord(usernameField.getText(),
                            fullNameField.getText(), "Unspecified", "Unspecified", emailField.getText(),
                            passwordField.getText(), selectedGender, datePicker.getValue(), "N/A", "N/A", "Nurse");
                    Utils.showAlert("Record Created", "Thank you for signing up for AKSAS! Your account has been successfully created.", Alert.AlertType.INFORMATION);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Utils.showAlert("Error", "There has been a problem with creating the record. Aborting.", Alert.AlertType.ERROR);
                }
            }
        });

        Hyperlink loginLink = new Hyperlink("Already have an account? Log in");
        loginLink.getStyleClass().add("hyperlink");
        loginLink.setOnAction(e -> navController.showLoginPage());

        layout.getChildren().addAll(signUpTitle, fullNameField, emailField, usernameField, passwordField, maleButton, femaleButton, datePicker, createAccountButton, loginLink);

        return layout;
    }

    public VBox createNurseDashboardLayout(UserRecord userRecord, Path filePath) {
        this.recordFilePath = filePath;

        VBox dashboardLayout = new VBox(10);
        dashboardLayout.setAlignment(Pos.TOP_CENTER);
        dashboardLayout.getStyleClass().add("nurse-dashboard");

        Text nurseTitle = new Text("Nurse: " + userRecord.getName());
        nurseTitle.getStyleClass().add("dashboard-title");

        String name = userHandler.getName();
        name = name.isBlank() ? "N/A" : name;
        Text handlerTitle = new Text("Selected User: " + name);
        handlerTitle.getStyleClass().add("dashboard-title");

        // Create the cards for the dashboard
        Button createNewPatientCard = new Button("Create a New Patient Record");
        createNewPatientCard.getStyleClass().add("dashboard-card");
        createNewPatientCard.setOnAction(e -> navController.showPatientSignUpPage());

        Button vitalsCard = new Button("Record Patient Vitals");
        vitalsCard.getStyleClass().add("dashboard-card");
        vitalsCard.setOnAction(e -> recordPatientVitals());

        Button preVisitQuestionsCard = new Button("Pre-Visit Questions");
        preVisitQuestionsCard.getStyleClass().add("dashboard-card");
        preVisitQuestionsCard.setOnAction(e -> askPreVisitQuestions());

        Button patientHistoryCard = new Button("Patient History Record");
        patientHistoryCard.getStyleClass().add("dashboard-card");
        patientHistoryCard.setOnAction(e -> viewPatientHistory());

        Button assignPatientButton = new Button("Assign Patient");
        assignPatientButton.getStyleClass().add("dashboard-card");
        assignPatientButton.setOnAction(e -> {
            showAssignPatientInterface();
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("dashboard-card");
        refreshButton.setOnAction( e -> refreshDashboard());

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction( e -> navController.showLoginPage());

        // Add cards to layout
        dashboardLayout.getChildren().addAll(nurseTitle, handlerTitle, createNewPatientCard,
                vitalsCard, preVisitQuestionsCard, patientHistoryCard, assignPatientButton,
                refreshButton, logoutButton);

        // Assume these methods exist and will be implemented
        // Each method will handle the creation of scenes for each task
        return dashboardLayout;
    }

    private void refreshDashboard() {
        navController.showNurseDashboard(userRecord, recordFilePath);
    }

    private void fillListWithData(ListView<String> listView, HashMap<String, Path> map, String role) {
        String dataPath = DataManager.getDataManagerDirPath() + "/data/" + role + "/";
        File folder = new File(dataPath);

        if (folder.exists() && folder.isDirectory()) {
            for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.isFile()) {
                    Path filePath = fileEntry.toPath();
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileEntry))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("Name: ")) {
                                String name = line.substring(6).trim(); // Assuming the format is "Name: John Doe"
                                listView.getItems().add(name);
                                map.put(name, filePath); // Map the name to the record path
                                break;
                            }
                        }
                    } catch (IOException e) {
                        // Handle exceptions here
                    }
                }
            }
        }
    }

    private void showAssignPatientInterface() {
        Stage assignStage = new Stage();
        assignStage.setTitle("Assign Patient");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        ListView<String> patientListView = new ListView<>();
        ListView<String> doctorListView = new ListView<>();

        // Fill the lists with patient and doctor names
        fillListWithData(patientListView, patientMap, "patient");
        fillListWithData(doctorListView, doctorMap, "doctor");

        // Handle the selection of a patient or doctor
        patientListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) { // Double-click detection
                String selectedPatient = patientListView.getSelectionModel().getSelectedItem();
                if (selectedPatient != null) {
                    recordHandler = patientMap.get(selectedPatient); // Assign the record path to the handler
                    userHandler = DataManager.updatedUserHandler(recordHandler);
                    showAlertSelection("Patient", selectedPatient);
                    assignStage.close();
                }
            }
        });

        doctorListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) { // Double-click detection
                String selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
                if (selectedDoctor != null) {
                    recordHandler = doctorMap.get(selectedDoctor); // Assign the record path to the handler
                    showAlertSelection("Doctor", selectedDoctor);
                    assignStage.close();
                }
            }
        });

        // Add list views to layout
        layout.getChildren().addAll(
                new Label("Select a Patient"),
                patientListView,
                new Label("Select a Doctor"),
                doctorListView
        );

        assignStage.setScene(new Scene(layout, 300, 400));
        assignStage.show();
    }

    private void showAlertSelection(String role, String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selection Success");
        alert.setHeaderText(null);
        alert.setContentText("Success! You've selected " + role + ": " + name + ".");
        alert.showAndWait();
    }

    private void recordPatientVitals() {
        if (recordHandler == null || userHandler == null) {
            showAlert("Error", "No patient has been assigned. Please assign a patient first.");
            return;
        }

        Stage stage = new Stage();
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Record Patient Vitals");

        // Vitals text fields setup
        Map<String, TextField> vitalsFields = new LinkedHashMap<>();
        String[] vitals = {"Weight (kg)", "Height (cm)", "Body Temperature (°C)", "Blood Pressure (mmHg)",
                "Heart Rate (bpm)", "Respiratory Rate (breaths/min)", "Oxygen Saturation (%)"};
        for (String vital : vitals) {
            TextField textField = new TextField();
            textField.setPromptText("Enter " + vital);
            vitalsFields.put(vital, textField);
            layout.add(new Label(vital), 0, vitalsFields.size() - 1);
            layout.add(textField, 1, vitalsFields.size() - 1);
        }

        Button saveButton = new Button("Save Vitals");
        saveButton.setOnAction(e -> {
            try {
                String content = Files.readString(recordHandler);
                StringBuilder vitalsBuilder = new StringBuilder();
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                vitalsBuilder.append("<").append(date.format(formatter)).append(" ----------- VITALS TAKEN BY ")
                        .append(userRecord.getName()).append(" ----------- >\n");
                vitalsFields.forEach((key, field) -> {
                    if (!field.getText().isEmpty()) {
                        vitalsBuilder.append(key).append(": ").append(field.getText()).append("\n");
                    }
                });
                vitalsBuilder.append("<").append(date.format(formatter)).append(" ----------- END OF VITALS ----------- >\n");

                // Insert vitals in the right place
                int start = content.indexOf("<!VTL_SMRY:BGN>") + "<!VTL_SMRY:BGN>".length();
                int end = content.indexOf("<!VTL_SMRY:END>");
                content = content.substring(0, start) + "\n" + vitalsBuilder.toString() + content.substring(end);

                Files.writeString(recordHandler, content);
                Utils.showAlert("Success", "Vitals recorded successfully.", Alert.AlertType.INFORMATION);
                stage.close();
            } catch (IOException ex) {
                showAlert("Error", "Failed to record vitals: " + ex.getMessage());
            }
        });

        layout.add(saveButton, 0, vitalsFields.size(), 2, 1);

        stage.show();
    }

    private void askPreVisitQuestions() {
        if (recordHandler == null || userHandler == null) {
            showAlert("Error", "No patient has been assigned. Please assign a patient first.");
            return;
        }

        Stage stage = new Stage();
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 450, 300);
        stage.setScene(scene);
        stage.setTitle("Pre-Visit Questions");

        // Pre-visit questions setup
        TextArea questionField = new TextArea();
        questionField.setPromptText("Enter pre-visit questions here...");
        questionField.setWrapText(true);
        layout.add(new Label("Pre-Visit Questions:"), 0, 0);
        layout.add(questionField, 0, 1);

        Button saveButton = new Button("Save Questions");
        saveButton.setOnAction(e -> {
            try {
                String content = new String(Files.readAllBytes(recordHandler), StandardCharsets.UTF_8);
                StringBuilder questionsBuilder = new StringBuilder();
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                questionsBuilder.append("<").append(date.format(formatter)).append(" ----------- QUESTIONS BY ")
                        .append(userHandler.getName()).append(" ----------- >\n");
                questionsBuilder.append(questionField.getText().trim()).append("\n");
                questionsBuilder.append("<").append(date.format(formatter)).append(" ----------- END OF QUESTIONS ----------- >\n");

                // Insert questions in the right place
                int start = content.indexOf("<!SMRY_VST:BGN>") + "<!SMRY_VST:BGN>".length();
                int end = content.indexOf("<!SMRY_VST:END>");
                content = content.substring(0, start) + "\n" + questionsBuilder.toString() + content.substring(end);

                Files.write(recordHandler, content.getBytes(StandardCharsets.UTF_8));
                showAlert("Success", "Questions recorded successfully.");
                stage.close();
            } catch (IOException ex) {
                showAlert("Error", "Failed to record pre-visit questions: " + ex.getMessage());
            }
        });

        layout.add(saveButton, 0, 2);

        stage.show();
    }

    public void viewPatientHistory() {
        if (recordHandler == null) {
            showAlert("Error", "No patient record is selected.");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("patient-history-layout");

        Text title = new Text("Patient History Record for " + userHandler.getName());
        title.getStyleClass().add("title");

        TextArea generalInfoArea = new TextArea();
        TextArea medicalHistoryArea = new TextArea();
        TextArea currentMedicationsArea = new TextArea();
        TextArea summaryVisitsArea = new TextArea();
        TextArea vitalsArea = new TextArea();

        generalInfoArea.setEditable(false);
        medicalHistoryArea.setEditable(false);
        currentMedicationsArea.setEditable(false);
        summaryVisitsArea.setEditable(false);
        vitalsArea.setEditable(false);

        // Delimiters for sections
        String genInfoStart = "<!GEN_INFO:BGN>";
        String genInfoEnd = "<!GEN_INFO:END>";
        String medHistStart = "<!MED_HST:BGN>";
        String medHistEnd = "<!MED_HST:END>";
        String curMedStart = "<!CRT_MED:BGN>";
        String curMedEnd = "<!CRT_MED:END>";
        String sumVisStart = "<!SMRY_VST:BGN>";
        String sumVisEnd = "<!SMRY_VST:END>";
        String vitalsStart = "<!VTL_SMRY:BGN>";
        String vitalsEnd = "<!VTL_SMRY:END>";

        try (BufferedReader reader = Files.newBufferedReader(recordHandler)) {
            String content = reader.lines().collect(Collectors.joining("\n"));
            generalInfoArea.setText(extractSection(content, genInfoStart, genInfoEnd).trim());
            medicalHistoryArea.setText(extractSection(content, medHistStart, medHistEnd).trim());
            currentMedicationsArea.setText(extractSection(content, curMedStart, curMedEnd).trim());
            summaryVisitsArea.setText(extractSection(content, sumVisStart, sumVisEnd).trim());
            vitalsArea.setText(extractSection(content, vitalsStart, vitalsEnd).trim()); // New code for vitals
        } catch (IOException e) {
            showAlert("Error", "Failed to read patient history: " + e.getMessage());
        }

        layout.getChildren().addAll(
                title,
                new Label("General Information"), generalInfoArea,
                new Label("Medical History"), medicalHistoryArea,
                new Label("Current Medications"), currentMedicationsArea,
                new Label("Summary Visits"), summaryVisitsArea,
                new Label("Vitals"), vitalsArea
        );

        Scene scene = new Scene(layout, 600, 800);
        stage.setTitle("Patient History Record");
        stage.setScene(scene);
        stage.show();
    }

    private String extractSection(String content, String startDelimiter, String endDelimiter) {
        Pattern pattern = Pattern.compile(Pattern.quote(startDelimiter) + "(.*?)" + Pattern.quote(endDelimiter), Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
