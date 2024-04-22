package com.example.askas.gui;

import com.example.askas.DataManager;
import com.example.askas.UserRecord;
import com.example.askas.Utils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
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

import static com.example.askas.DataManager.DELIMITERS;

public class DoctorPage {

    private NavigationController navController;
    private UserRecord userRecord;
    private Path recordFilePath;
    private HashMap<String, Path> patientMap = new HashMap<>();
    private HashMap<String, Path> NurseMap = new HashMap<>();
    private static Path recordHandler; // Member field to hold the selected record path
    private static UserRecord userHandler;

    public DoctorPage(NavigationController navController, UserRecord userRecord) {
        this.navController = navController;
        this.userRecord = userRecord;
        recordHandler = recordHandler != null ? recordHandler : null;
        userHandler = userHandler != null ? userHandler : new UserRecord("", "", "", null);

    }

    public VBox createDoctorSignUpLayout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("root");

        Text signUpTitle = new Text("Doctor Account Creation");
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
                Path filePath = DataManager.findUserFilePath(usernameField.getText(), "Doctor");
                if (filePath != null) {
                    Utils.showAlert("Warning", "The following username has already been taken: " + usernameField.getText());
                    return;
                }
                try {
                    DataManager.createUserRecord(usernameField.getText(),
                            fullNameField.getText(), "Unspecified", "Unspecified", emailField.getText(),
                            passwordField.getText(), selectedGender, datePicker.getValue(), "N/A", "N/A", "Doctor");
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

    public VBox createDoctorDashboardLayout(UserRecord userRecord, Path filePath) {
        this.recordFilePath = filePath; // Assuming this is the path to the directory containing patient records

        VBox dashboardLayout = new VBox(10);
        dashboardLayout.setAlignment(Pos.TOP_CENTER);
        dashboardLayout.getStyleClass().add("doctor-dashboard");

        Text doctorTitle = new Text("Doctor: " + userRecord.getName());
        doctorTitle.getStyleClass().add("dashboard-title");

        String name = userHandler.getName();
        name = name.isBlank() ? "N/A" : name;
        Text handlerTitle = new Text("Selected User: " + name);
        handlerTitle.getStyleClass().add("dashboard-title");

        // Cards for the doctor's dashboard
        Button inputSystemDataCard = new Button("Input System Data");
        inputSystemDataCard.getStyleClass().add("dashboard-card");
        inputSystemDataCard.setOnAction(e -> inputSystemData());

        Button accessPatientHistoryCard = new Button("Access Patient History");
        accessPatientHistoryCard.getStyleClass().add("dashboard-card");
        accessPatientHistoryCard.setOnAction(e -> viewPatientHistory());

        Button inboxCard = new Button("View Inbox");
        inboxCard.getStyleClass().add("dashboard-card");
        inboxCard.setOnAction(e -> viewInbox());

        Button messageCard = new Button("Send Messages");
        messageCard.getStyleClass().add("dashboard-card");
        messageCard.setOnAction(e -> sendMessageToUser());

        Button assignPatientButton = new Button("Assign Patient");
        assignPatientButton.getStyleClass().add("dashboard-card");
        assignPatientButton.setOnAction(e -> showAssignPatientInterface());

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("dashboard-card");
        refreshButton.setOnAction(e -> refreshDashboard());

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> navController.showLoginPage());

        // Add cards to layout
        dashboardLayout.getChildren().addAll(
                doctorTitle,
                handlerTitle,
                inputSystemDataCard,
                accessPatientHistoryCard,
                inboxCard,
                messageCard,
                assignPatientButton,
                refreshButton,
                logoutButton
        );

        // Return the complete layout
        return dashboardLayout;
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
        fillListWithData(doctorListView, NurseMap, "Nurse");

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
                    recordHandler = NurseMap.get(selectedDoctor); // Assign the record path to the handler
                    showAlertSelection("Doctor", selectedDoctor);
                    assignStage.close();
                }
            }
        });

        // Add list views to layout
        layout.getChildren().addAll(
                new Label("Select a Patient"),
                patientListView,
                new Label("Select a Nurse"),
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

    private void refreshDashboard() {
        navController.showDoctorDashboard(userRecord, recordFilePath);
    }

    private void sendMessageToUser() {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Send Message");
        ComboBox<String> userComboBox = new ComboBox<>();
        TextArea messageBox = new TextArea();
        Button sendButton = new Button("Send");

        Map<String, Path> userSelectionMap = new HashMap<>(); // Map to track the selected user's record

        // Populate the ComboBox with user data
        loadUsersToComboBox(userComboBox, userSelectionMap, "patient");
        loadUsersToComboBox(userComboBox, userSelectionMap, "nurse");
        loadUsersToComboBox(userComboBox, userSelectionMap, "doctor");

        sendButton.setOnAction(e -> {
            String selectedUser = userComboBox.getValue();
            Path selectedPath = userSelectionMap.get(selectedUser);

            if (selectedUser != null && selectedPath != null) {
                String message = messageBox.getText();
                if (!message.isEmpty()) {
                    appendMessageToFile(selectedPath, userRecord.getName(), userRecord.getRole(), message);
                    showAlert("Success", "Message sent successfully.");
                    stage.close();
                } else {
                    showAlert("Error", "Message cannot be empty.");
                }
            } else {
                showAlert("Error", "No user selected or user not found.");
            }
        });

        layout.getChildren().addAll(title, userComboBox, messageBox, sendButton);

        Scene scene = new Scene(layout, 400, 500);
        stage.setTitle("Send Message to User");
        stage.setScene(scene);
        stage.show();
    }

    private void appendMessageToFile(Path filePath, String senderName, String senderRole, String message) {
        String messageEntry = "From: " + userRecord.getUsername() + "; " + " (" + senderRole + "): " + message + "\n";

        try {
            // Read the entire content of the file
            String content = Files.readString(filePath, StandardCharsets.UTF_8);

            // Find the index to insert the message
            String insertPoint = "Messages:";
            int insertIndex = content.indexOf(insertPoint);
            if (insertIndex != -1) {
                // Calculate the index after the insertPoint and the newline
                insertIndex += insertPoint.length() + 1;

                // Insert the new message into the content
                String updatedContent = new StringBuilder(content)
                        .insert(insertIndex, messageEntry)
                        .toString();

                // Write the updated content back to the file
                Files.writeString(filePath, updatedContent, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                throw new IOException("Message delimiter not found in the file.");
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to append message to the file: " + e.getMessage());
        }
    }

    private void loadUsersToComboBox(ComboBox<String> userComboBox, Map<String, Path> userSelectionMap, String role) {
        String dataPath = DataManager.getDataManagerDirPath() + "/data/" + role + "/";
        File folder = new File(dataPath);

        if (folder.exists() && folder.isDirectory()) {
            for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.isFile()) {
                    Path filePath = fileEntry.toPath();
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileEntry))) {
                        String line;
                        String name = "";
                        LocalDate dob = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("Name:")) {
                                name = line.substring(6).trim(); // Assuming the format is "Name: John Doe"
                            } else if (line.startsWith("DOB:")) {
                            }
                        }
                        if (!name.isEmpty()) {
                            String displayString = name + "_" + "" + "_" + role;
                            userComboBox.getItems().add(displayString);
                            userSelectionMap.put(displayString, filePath); // Map the display string to the record path
                        }
                    } catch (IOException e) {
                        showAlert("Error", "Failed to read user information: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void viewInbox() {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Messages");
        ListView<String> messagesList = new ListView<>();

        // Read the messages section from the patient's file
        try (BufferedReader patientFileReader = new BufferedReader(new FileReader(recordFilePath.toFile()))) {
            String line;
            boolean inMessagesSection = false;
            while ((line = patientFileReader.readLine()) != null) {
                if (inMessagesSection) {
                    if (line.startsWith("From:")) {
                        String[] parts = line.split(";"); // Split by ";"
                        if (parts.length >= 2) {
                            String senderId = parts[0].substring(6).trim(); // Extract the username
                            String messageContent = parts[1].trim();
                            String senderName = getStaffNameById(senderId);
                            messagesList.getItems().add(senderName + ": " + messageContent);
                        }
                    }
                } else if (line.startsWith("<!MSG_IBX:BGN>")) {
                    inMessagesSection = true;
                } else if (line.startsWith("<!MSG_IBX:END>")) {
                    inMessagesSection = false;
                }
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while reading messages: " + e.getMessage());
        }

        layout.getChildren().addAll(title, messagesList);

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }

    private String getStaffNameById(String id) {
        String[] staffDirectories = { "/data/doctor/", "/data/nurse/", "/data/patient/" };
        for (String directory : staffDirectories) {
            File dir = new File(DataManager.getDataManagerDirPath() + directory);
            File[] files = dir.listFiles((d, name) -> name.startsWith(id + "_"));
            if (files != null && files.length > 0) {
                // Since ID is unique, there should be only one file starting with that ID.
                File file = files[0];
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Name:")) {
                            return line.substring(line.indexOf(':') + 1).trim();
                        }
                    }
                } catch (IOException e) {
                    // Handle exception or log an error message
                }
            }
        }
        return "Unknown"; // Return a default name if no match is found
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void viewPatientHistory() {
        if (recordHandler == null) {
            Utils.showAlert("Error", "No patient record is selected.");
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
            Utils.showAlert("Error", "Failed to read patient history: " + e.getMessage());
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

    public void inputSystemData() {
        if (recordHandler == null) {
            showAlert("Error", "No record file selected.");
            return;
        }

        // Creating the stage and layout for the user interface
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // Fetch initial content from the file for each section
        String content = null;
        try {
            content = DataManager.readContentFromFile(recordHandler);
        } catch (IOException e) {
            Utils.showAlert("Error", e.getMessage());
        }
        TextArea generalInfoArea = createEditableArea("General Information", extractSectionForEdit(content, DELIMITERS.get("GEN_INFO_START"), DELIMITERS.get("GEN_INFO_END")));
        TextArea medicalHistoryArea = createEditableArea("Medical History", extractSection(content, DELIMITERS.get("MED_HST_START"), DELIMITERS.get("MED_HST_END")));
        TextArea currentMedicationsArea = createEditableArea("Current Medications", extractSection(content, DELIMITERS.get("CRT_MED_START"), DELIMITERS.get("CRT_MED_END")));

        // Button to save changes
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            try {
                DataManager.updateSection(recordHandler, generalInfoArea.getText(), DELIMITERS.get("GEN_INFO_START"), DELIMITERS.get("GEN_INFO_END"));
                DataManager.updateSection(recordHandler, medicalHistoryArea.getText(), DELIMITERS.get("MED_HST_START"), DELIMITERS.get("MED_HST_END"));
                DataManager.updateSection(recordHandler, currentMedicationsArea.getText(), DELIMITERS.get("CRT_MED_START"), DELIMITERS.get("CRT_MED_END"));
                showAlert("Success", "Data updated successfully.");
                stage.close();
            } catch (IOException ex) {
                showAlert("Error", "Failed to update file: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(generalInfoArea, medicalHistoryArea, currentMedicationsArea, saveButton);
        Scene scene = new Scene(layout, 600, 800);
        stage.setTitle("Edit System Data");
        stage.setScene(scene);
        stage.show();
    }

    private TextArea createEditableArea(String title, String initialContent) {
        TextArea textArea = new TextArea(initialContent);
        textArea.setPromptText("Edit " + title);
        textArea.setMaxHeight(200);
        return textArea;
    }

    private String extractSectionForEdit(String content, String startDelimiter, String endDelimiter) {
        int startIndex = content.indexOf(startDelimiter) + startDelimiter.length();
        int endIndex = content.indexOf(endDelimiter);
        if (startIndex < endIndex && startIndex != -1) {
            return content.substring(startIndex, endIndex).trim();
        }
        return "";
    }

    private String extractSection(String content, String startDelimiter, String endDelimiter) {
        Pattern pattern = Pattern.compile(Pattern.quote(startDelimiter) + "(.*?)" + Pattern.quote(endDelimiter), Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
