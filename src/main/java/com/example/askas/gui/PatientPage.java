package com.example.askas.gui;

import com.example.askas.DataManager;
import com.example.askas.Patient;
import com.example.askas.UserRecord;
import com.example.askas.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientPage {

    private NavigationController navController;
    private static Path recordPath;

    public PatientPage(NavigationController navController, Path recordPath) {
        this.navController = navController;
        this.recordPath = recordPath;
    }

    public VBox createPatientSignUpLayout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("root");

        Text title = new Text("Account Creation");
        title.getStyleClass().add("title");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.getStyleClass().add("text-field");

        TextField homeAddressField = new TextField();
        homeAddressField.setPromptText("Home Address");
        homeAddressField.getStyleClass().add("text-field");

        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        phoneNumberField.getStyleClass().add("text-field");

        ToggleGroup genderGroup = new ToggleGroup();

        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderGroup);
        maleButton.setUserData("male");

        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderGroup);
        femaleButton.setUserData("Female");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.getStyleClass().add("text-field");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (you will login with this)");
        usernameField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("dd/MM/yyyy");
        Utils.setupDatePicker(datePicker);  // Assuming Utils.setupDatePicker correctly initializes the DatePicker

        // Additional fields for medical history and current medications
        TextArea medicalHistoryField = new TextArea();
        medicalHistoryField.setPromptText("Enter your medical history details");
        medicalHistoryField.getStyleClass().add("text-area");

        TextArea currentMedicationsField = new TextArea();
        currentMedicationsField.setPromptText("Enter your current medications");
        currentMedicationsField.getStyleClass().add("text-area");

        Button createAccountButton = new Button("Create Account");
        createAccountButton.getStyleClass().add("button");
        createAccountButton.setOnAction(e -> {
            boolean success = AuthenticateFields(fullNameField.getText(), emailField.getText(), usernameField.getText(),
                    passwordField.getText(), datePicker.getValue());
            if (!success)
                return;
            if (genderGroup.getSelectedToggle() == null) {
                Utils.showAlert("Validation Error", "Please select a gender.");
                return;
            }
            String selectedGender = genderGroup.getSelectedToggle().getUserData().toString();
            if (success) {
                Path filePath = DataManager.findUserFilePath(usernameField.getText(), "Patient");
                if (filePath != null) {
                    Utils.showAlert("Warning", "The following username has already been taken: " + usernameField.getText());
                    return;
                }
                try {
                    DataManager.createUserRecord(usernameField.getText(), fullNameField.getText(), homeAddressField.getText(), phoneNumberField.getText(),
                            emailField.getText(), passwordField.getText(), selectedGender, datePicker.getValue(), medicalHistoryField.getText(),
                            currentMedicationsField.getText(), "Patient");
                    Utils.showAlert("Record Created", "Thank you for signing up for AKSAS! Your account has been successfully created.", Alert.AlertType.INFORMATION);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Utils.showAlert("Error", "There has been a problem with creating the record. Aborting.", Alert.AlertType.ERROR);
                }
            }
        });

        Hyperlink loginLink = new Hyperlink("You have an account already? Log in");
        loginLink.getStyleClass().add("hyperlink");
        loginLink.setOnAction(e -> navController.showLoginPage());

        // Add all elements to the layout
        layout.getChildren().addAll(title, fullNameField, homeAddressField, phoneNumberField, emailField, usernameField, passwordField, maleButton, femaleButton,
                datePicker, medicalHistoryField, currentMedicationsField,
                createAccountButton, loginLink);

        return layout;
    }

    public static boolean AuthenticateFields(String nameFieldText, String emailFieldText, String usernameFieldText, String passwordFieldText, LocalDate datePickerValue) {
        if (nameFieldText.isBlank()) {
            Utils.showAlert("Error", "You must enter your name");
            return false;
        } else if (!Utils.isValidEmail(emailFieldText)) {
            return false;
        } else if (usernameFieldText.isBlank() || usernameFieldText.length() < 4) {
            Utils.showAlert("Error", "Username must be filled and at least 4 characters in length");
            return false;
        } else if (!Utils.isValidPassword(passwordFieldText)) {
            return false;
        } else if (datePickerValue == null) {
            Utils.showAlert("Missing Information", "Please select your date of birth.");
            return false;
        }
        return true;
    }

    public VBox createPatientPortalLayout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        // Section for Profile Management
        Label sectionTitleProfile = new Label("Profile Management");
        sectionTitleProfile.getStyleClass().add("section-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        Button saveChangesButton = new Button("Save Changes");
        saveChangesButton.getStyleClass().add("button");

        VBox profileManagementSection = new VBox(10, sectionTitleProfile, nameField, addressField, phoneField, emailField, saveChangesButton);
        profileManagementSection.getStyleClass().add("section");

        // Section for Past Visits Summaries
        Label sectionTitleVisits = new Label("Past Visits Summaries");
        sectionTitleVisits.getStyleClass().add("section-title");
        ListView<String> visitsListView = new ListView<>();
        visitsListView.getItems().addAll("Visit on 10/25/2021", "Visit on 09/15/2021"); // Placeholder for example
        visitsListView.getStyleClass().add("list-view");

        VBox pastVisitsSection = new VBox(10, sectionTitleVisits, visitsListView);
        pastVisitsSection.getStyleClass().add("section");

        // Section for Messaging System
        Label sectionTitleMessaging = new Label("Messaging System");
        sectionTitleMessaging.getStyleClass().add("section-title");
        ComboBox<String> recipientsComboBox = new ComboBox<>();
        recipientsComboBox.setPromptText("Select Recipient");
        recipientsComboBox.getStyleClass().add("combo-box");

        TextArea messageTextArea = new TextArea();
        messageTextArea.setPromptText("Compose your message...");
        messageTextArea.getStyleClass().add("text-area");

        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.getStyleClass().add("button");

        VBox messagingSection = new VBox(10, sectionTitleMessaging, recipientsComboBox, messageTextArea, sendMessageButton);
        messagingSection.getStyleClass().add("section");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> navController.showLoginPage());
        logoutButton.getStyleClass().add("logout-button");

        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(logoutButton, 10.0);
        AnchorPane.setRightAnchor(logoutButton, 10.0);
        anchorPane.getChildren().add(logoutButton);

        layout.getChildren().addAll(anchorPane, profileManagementSection, pastVisitsSection, messagingSection);

        return layout;
    }

    public VBox createPatientDashboardLayout(UserRecord userRecord) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("dashboard");

        // Display patient's name
        Text patientName = new Text("Patient: " + userRecord.getName());
        patientName.getStyleClass().add("dashboard-title");

        // Create interactive cards for each section as per the requirements

        Button profileManagementCard = new Button("Profile Management");
        profileManagementCard.getStyleClass().add("dashboard-card");
        profileManagementCard.setOnAction(e -> profileManagement(userRecord));

        Button visitSummariesCard = new Button("Visit Summaries");
        visitSummariesCard.getStyleClass().add("dashboard-card");
        visitSummariesCard.setOnAction(e -> showVisitSummariesPage(userRecord));

        Button myMessagesCard = new Button("My Inbox");
        myMessagesCard.getStyleClass().add("dashboard-card");
        myMessagesCard.setOnAction(e -> displayMessages(recordPath));

        Button messagingSystemCard = new Button("Messaging System");
        messagingSystemCard.getStyleClass().add("dashboard-card");
        messagingSystemCard.setOnAction(e -> sendMessageToUser(userRecord)); //showMessagingSystem(userRecord));
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setOnAction(e -> navController.showLoginPage());

        // Add elements to layout
        layout.getChildren().addAll(patientName, profileManagementCard, visitSummariesCard, myMessagesCard, messagingSystemCard, logoutButton);

        // Add padding around the layout
        layout.setPadding(new Insets(20));

        return layout;
    }

    private void showVisitSummariesPage(UserRecord userRecord) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Visit Summaries for " + userRecord.getName());
        ListView<String> visitSummariesList = new ListView<>();

        // Read the file and extract the visit summaries
        try (BufferedReader reader = new BufferedReader(new FileReader(recordPath.toFile()))) {
            String line;
            boolean inSummariesSection = false;
            StringBuilder summariesBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.contains("<!SMRY_VST:BGN>")) {
                    inSummariesSection = true;
                } else if (line.contains("<!SMRY_VST:END>")) {
                    break;
                }

                if (inSummariesSection) {
                    summariesBuilder.append(line).append("\n");
                }
            }

            // Add "Visit #" prefix to each summary
            String[] summaries = summariesBuilder.toString().split(System.lineSeparator());
            for (int i = 0; i < summaries.length; i++) {
                if (!summaries[i].trim().isEmpty() && !summaries[i].contains("<!")) {
                    String prefixedSummary = "Visit #" + (i + 1) + ": " + summaries[i].trim();
                    visitSummariesList.getItems().add(prefixedSummary);
                }
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while reading the visit summaries: " + e.getMessage());
        }

        layout.getChildren().addAll(title, visitSummariesList);

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Visit Summaries");
        stage.setScene(scene);
        stage.show();
    }

    private void savePatientProfile(Path recordPath, String patientInfo, ListView<String> medicalHistoryList, ListView<String> medicationList, String everythingElse) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recordPath.toFile()))) {
            // Write the patient info section
            writer.write(patientInfo);
            writer.newLine();

            // Write the medical history section
            writer.write("Medical History:");
            writer.newLine();
            for (String history : medicalHistoryList.getItems()) {
                writer.write(history);
                writer.newLine();
            }

            // Write the current medications section
            writer.write("Current Medications:");
            writer.newLine();
            for (String medication : medicationList.getItems()) {
                writer.write(medication);
                writer.newLine();
            }

            writer.write(everythingElse);

            updateRecordUsername(patientInfo);

            showAlert("Information", "Profile updated successfully.");
        } catch (IOException e) {
            showAlert("Error", "An error occurred while saving the profile: " + e.getMessage());
        }
    }

    public static void updateRecordUsername(String patientInfo) {
        // Extract username from the patientInfo using a regex pattern
        Matcher matcher = Pattern.compile("Username:\\s*(\\S+)").matcher(patientInfo);
        String newUsername = "";
        if (matcher.find()) {
            newUsername = matcher.group(1);
        } else {
            System.out.println("Username not found in the provided patient info.");
            return; // Exit if no username is found
        }

        // Extract the current username from the file's name
        String filename = recordPath.getFileName().toString();
        String currentUsername = filename.substring(0, filename.indexOf('_'));

        if (!newUsername.equals(currentUsername)) {
            // Construct new file path with the new username
            String newFilename = newUsername + filename.substring(filename.indexOf('_'));
            Path newFilePath = recordPath.resolveSibling(newFilename);

            try {
                // Rename the file
                Files.move(recordPath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                recordPath = newFilePath; // Update the static path reference to the new path
                System.out.println("File renamed successfully to: " + newFilePath);
            } catch (IOException e) {
                System.err.println("Error updating the file name: " + e.getMessage());
            }
        } else {
            System.out.println("No update needed. Username is the same.");
        }
    }

    private String readMessagesToEndOfFile(Path filePath) {
        StringBuilder messages = new StringBuilder();
        boolean startAppending = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (startAppending) {
                    messages.append(line).append(System.lineSeparator());
                } else if (line.startsWith("Messages:")) {
                    startAppending = true;
                    messages.append(line).append(System.lineSeparator()); // include "Messages:" section title
                }
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while reading messages: " + e.getMessage());
            return ""; // Return an empty string because an error occurred
        }

        return messages.toString();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void profileManagement(UserRecord userRecord) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        TextArea infoArea = new TextArea();
        infoArea.setEditable(true);

        ListView<String> medicalHistoryList = new ListView<>();
        ListView<String> medicationList = new ListView<>();

        // Read and display the file content
        try (BufferedReader reader = new BufferedReader(new FileReader(recordPath.toFile()))) {
            String line;
            boolean isMedicalHistorySection = false;
            boolean isCurrentMedicationSection = false;
            StringBuilder infoBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Medical History:")) {
                    isMedicalHistorySection = true; // Start of Medical History section
                    continue; // Skip the title line
                } else if (line.startsWith("Current Medications:")) {
                    isMedicalHistorySection = false; // End of Medical History section
                    isCurrentMedicationSection = true; // Start of Medications section
                    continue; // Skip the title line
                } else if (line.startsWith("Messages:")) {
                    isCurrentMedicationSection = false; // End of Medications section
                    break; // We don't want to read messages for this section
                }

                // Depending on the current section, add to appropriate list or info area
                if (isMedicalHistorySection) {
                    medicalHistoryList.getItems().add(line.trim());
                } else if (isCurrentMedicationSection) {
                    medicationList.getItems().add(line.trim());
                } else {
                    infoBuilder.append(line).append(System.lineSeparator());
                }
            }

            infoArea.setText(infoBuilder.toString().trim()); // Patient information

        } catch (IOException e) {
            infoArea.setText("Failed to read file: " + e.getMessage());
        }

        Button saveButton = new Button("Save Changes");
        String messagesToEndOfFile = readMessagesToEndOfFile(recordPath);

        saveButton.setOnAction(e -> {
            savePatientProfile(recordPath, infoArea.getText(), medicalHistoryList, medicationList, messagesToEndOfFile);
            stage.close(); // Close the stage after saving
        });

        layout.getChildren().addAll(
                new Label("Patient Information"), infoArea,
                new Label("Medical History"), medicalHistoryList,
                new Label("Current Medications"), medicationList,
                saveButton
        );

        Scene scene = new Scene(layout, 600, 800);
        stage.setTitle("Patient Profile Management");
        stage.setScene(scene);
        stage.show();
    }
////////////////////////////


    private void sendMessageToUser(UserRecord userRecord) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Send Message");
        ComboBox<String> userComboBox = new ComboBox<>();
        TextArea messageBox = new TextArea();
        Button sendButton = new Button("Send");

        Map<String, Path> userSelectionMap = new HashMap<>(); // Map to track the selected user's record

        // Populate the ComboBox with user data
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

    private void appendMessageToFile(UserRecord userRecord, Path filePath, String senderName, String senderRole, String message) {
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
    ////////////////////


    private void showMessagingSystem(UserRecord userRecord) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Messaging System");
        ComboBox<String> doctorComboBox = new ComboBox<>();
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Type your message here...");

        // Retrieve doctor names and IDs
        Map<String, String> doctorNamesToIDs = new HashMap<>();
        File doctorDirectory = new File(DataManager.getDataManagerDirPath() + "/data/doctor/");
        File[] doctorFiles = doctorDirectory.listFiles();
        String selectedDoctorID2 = "";
        if (doctorFiles != null) {
            for (File file : doctorFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String name = "";
                    String id = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Name:")) {
                            name = line.substring(line.indexOf(":") + 1).trim();
                        } else if (line.startsWith("Username:")) {
                            id = line.substring(line.indexOf(":") + 1).trim();
                            selectedDoctorID2 = id;
                        }
                    }
                    if (!name.isEmpty() && !id.isEmpty()) {
                        doctorNamesToIDs.put(name, id);
                        doctorComboBox.getItems().add(name);
                    }
                } catch (IOException e) {
                    showAlert("Error", "An error occurred while reading doctor information: " + e.getMessage());
                }
            }
        }

        Button sendButton = new Button("Send");
        String finalSelectedDoctorID = selectedDoctorID2;
        sendButton.setOnAction(e -> {
            String selectedDoctorName = doctorComboBox.getValue();
            String selectedDoctorID = finalSelectedDoctorID; // doctorNamesToIDs.get(selectedDoctorName);
            String message = messageArea.getText();

            if (selectedDoctorID != null && !message.isEmpty()) {
                // Find the doctor's file based on the selectedDoctorID
                File doctorDirectory2 = new File(DataManager.getDataManagerDirPath() + "/data/doctor/");
                File[] matchingFiles = doctorDirectory2.listFiles((dir, name) -> name.startsWith(selectedDoctorID + "_") && name.endsWith("_info.txt"));

                if (matchingFiles != null && matchingFiles.length == 1) {
                    // If exactly one matching file is found, use it
                    Path doctorFilePath = matchingFiles[0].toPath();
                    sendMessageToDoctor(doctorFilePath, userRecord.getUsername(), message);
                } else {
                    // Handle the case where no matching file is found, or multiple matching files are found
                    showAlert("Error", "Failed to find a unique doctor file for ID: " + selectedDoctorID);
                }
            }
            stage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        HBox buttonLayout = new HBox(10, sendButton, cancelButton);
        buttonLayout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, doctorComboBox, messageArea, buttonLayout);

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Send Message to Doctor");
        stage.setScene(scene);
        stage.show();
    }

    private void sendMessageToDoctor(Path doctorFilePath, String patientUsername, String message) {
        appendMessageToFile(doctorFilePath, patientUsername, "patient", message);
        System.out.println(doctorFilePath.toAbsolutePath());
        Utils.showAlert("Success", "Message Sent!", Alert.AlertType.INFORMATION);
    }

    // *********************

    private void displayMessages(Path patientFilePath) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Messages");
        ListView<String> messagesList = new ListView<>();

        // Read the messages section from the patient's file
        try (BufferedReader patientFileReader = new BufferedReader(new FileReader(patientFilePath.toFile()))) {
            String line;
            boolean inMessagesSection = false;
            while ((line = patientFileReader.readLine()) != null) {
                if (inMessagesSection) {
                    if (line.startsWith("From:")) {
                        String senderId = line.substring(line.indexOf(':') + 1, line.indexOf(';')).trim();
                        String messageContent = line.substring(line.indexOf(';') + 1).trim();
                        String senderName = getStaffNameById(senderId);
                        messagesList.getItems().add(senderName + ": " + messageContent);
                    }
                } else if (line.startsWith("Messages:")) {
                    inMessagesSection = true;
                }
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while reading messages: " + e.getMessage());
            return;
        }

        layout.getChildren().addAll(title, messagesList);

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }

    private String getStaffNameById(String id) {
        String[] staffDirectories = { "/data/doctor/", "/data/nurse/" };
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

    private void appendMessageToFile(Path filePath, String patientUsername, String senderRole, String message) {
        String formattedDate = "";
        String messageEntry = "From: " + patientUsername + "; " + " (" + senderRole + "): " + message + "\n";

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
}
