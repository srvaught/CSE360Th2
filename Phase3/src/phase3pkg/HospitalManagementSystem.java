package evenRecognizerTestbed;
//////
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private Map<String, User> users = new HashMap<>(); // Simulate a simple user store

    public HospitalManagementSystem() {
        // Populate with sample users for demonstration
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

        // Define labels and text fields for vitals information
        Label titleLabel = new Label("Enter Patient Vitals");
        vitalsGrid.add(titleLabel, 0, 0, 2, 1);

        TextField temperatureField = new TextField();
        TextField bloodPressureField = new TextField();
        TextField heartRateField = new TextField();
        TextField respiratoryRateField = new TextField();
        TextField oxygenSaturationField = new TextField();
        TextField heightField = new TextField();
        TextField weightField = new TextField();

        // Add fields and labels to the grid
        vitalsGrid.addRow(1, new Label("Temperature (°F):"), temperatureField);
        vitalsGrid.addRow(2, new Label("Blood Pressure (mmHg):"), bloodPressureField);
        vitalsGrid.addRow(3, new Label("Heart Rate (bpm):"), heartRateField);
        vitalsGrid.addRow(4, new Label("Respiratory Rate (breaths/min):"), respiratoryRateField);
        vitalsGrid.addRow(5, new Label("Oxygen Saturation (%):"), oxygenSaturationField);
        vitalsGrid.addRow(6, new Label("Height (ft):"), heightField);
        vitalsGrid.addRow(7, new Label("Weight (lb):"), weightField);

        // Save Button with action to save vitals
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

        // Back Button to navigate back to the Nurse Dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showNurseDashboard());
        vitalsGrid.add(backButton, 1, 9);

        // Setting the vitals grid to the center of the root layout
        rootLayout.setCenter(vitalsGrid);
    }

    private void saveVitals(String temperature, String bloodPressure, String heartRate, String respiratoryRate, String oxygenSaturation, String height, String weight) {
        // Here you will implement the logic to create a Vitals object and save it to the PatientRecord.
        // For demonstration, let's assume we're dealing with a hardcoded patient ID.
        String patientId = "patient123"; // This should be dynamically determined based on your application's context

        Vitals vitals = new Vitals();

        // Now, fetch the patient record, update it with new vitals, and save
        PatientRecord record = PatientRecord.loadFromFile(patientId); // Implement this method to load a patient record
        if (record != null) {
            record.updateVitals(vitals); // Make sure you have this method in your PatientRecord class
            record.saveToFile(); // And this one to save the record back to file
            System.out.println("Vitals saved for patient ID: " + patientId);
        } else {
            System.err.println("Patient record not found for ID: " + patientId);
        }
    }

    
    private void handleLogin(String username, String password, ToggleGroup roleGroup) {
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        String userRole = selectedRole.getText();

        switch (userRole) {
            case "Patient":
                showPatientPortal();
                break;
            case "Doctor":
                showDoctorDashboard();
                break;
            case "Nurse":
                showNurseDashboard();
                break;
            default:
                System.out.println("Error: Unknown role selected.");
                break;
        }
    }

    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red");

        Button tryAgainButton = new Button("Try Again");
        tryAgainButton.setOnAction(e -> {
            rootLayout.setCenter(null); // Clear the existing error message
            showLogin();                // Display the login form again
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
      //  scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setTitle("Nurse Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showPatientPortal() {
        VBox patientPortal = new VBox(15); // Increased spacing between elements
        patientPortal.setPadding(new Insets(20));
        patientPortal.setStyle("-fx-background-color: #f0f0f0"); // Light gray background

        // Patient Profile Section
        HBox profileBar = new HBox();
        profileBar.setStyle("-fx-padding: 10px 0px 10px 10px; -fx-background-color: #e0e0e0; -fx-border-radius: 5px 0px 0px 5px;");

        Label profileTitle = new Label("Profile Management");
        profileTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090"); // Dark blue title, bold

        Line separatorLine = new Line();
        separatorLine.setStroke(Color.color(0.8, 0.8, 0.8));
        separatorLine.setStrokeWidth(1);
        separatorLine.setStartY(0);  // Start the line at the top
        separatorLine.setEndY(40);   // Set the line's height

        profileBar.getChildren().addAll(profileTitle, separatorLine);

        HBox profileDetails = new HBox(10); // Left-align labels
        Label nameLabel = new Label("Name: ");
        nameLabel.setPrefWidth(80); // Adjust width if needed

        String patientInfo = "Jofin Doe\n" +
                "123 Main St, Cityville\n" +
                "123-456-7890\n" +
                "johndoe@example.com";
        Label nameAndInfo = new Label(patientInfo);
        nameAndInfo.setWrapText(true); // Enable line breaks for long content

        profileDetails.getChildren().addAll(nameLabel, nameAndInfo);

        // Save Changes Button (Assuming non-functional in this example)
      



       
        Button saveChangesButton = new Button("Save Changes");
        //
        saveChangesButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white; -fx-cursor: default;");
        saveChangesButton.setDisable(true); // Assuming saving is disabled for now

        // Add the Back Button here, right after its declaration and before the visit summaries section
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Navigate back to the desired screen
            showLogin();
        });
        // Make sure to add the backButton to the VBox
        patientPortal.getChildren().add(backButton);

        // Past Visits Summaries Section
        VBox visitSummariesSection = new VBox(10);
        visitSummariesSection.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-radius: 5px;");

        Label visitHistoryTitle = new Label("Past Visits Summaries");
        visitHistoryTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090;");

        visitSummariesSection.getChildren().addAll(visitHistoryTitle, createVisitSummary("10/25/2021", "Flu", "Rest and fluids"),
                createVisitSummary("09/15/2021", "Allergies", "Antihistamines, Panadol Night"));

        // Messaging System (Assuming non-functional in this example)
        Label messagingTitle = new Label("Messaging System");
        messagingTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #206090; -fx-margin-top: 20px;");

        HBox messageControls = new HBox(10);
        ComboBox<String> recipientComboBox = new ComboBox<>(); // Placeholder for recipient list
        recipientComboBox.setDisable(true);
        TextField messageField = new TextField();
        messageField.setDisable(true);
        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setDisable(true);

        messageControls.getChildren().addAll(recipientComboBox, messageField, sendMessageButton);

        // Assemble the UI
        patientPortal.getChildren().addAll(profileBar, profileDetails, saveChangesButton, visitSummariesSection, messagingTitle, messageControls);
        rootLayout.setCenter(patientPortal);
    }

    // Helper method to create a visit summary tile
    private VBox createVisitSummary(String visitDate, String diagnosis, String treatment) {
        VBox visitSummary = new VBox(5);
        visitSummary.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10px; -fx-border-radius: 5px;"); // Light gray background with rounded corners

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
       // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setTitle("Doctor Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Helper class to represent a User
    private static class User {
        private final String username;
        private final String password; // This should be hashed in a real application
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
            //String history = (record != null) ? record.getFormattedHistory() : "History not available for Patient ID: " + patientId;
           // historyTextArea.setText(history);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showNurseDashboard());
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
        // Add more fields as necessary for registration

        Button saveRecordButton = new Button("Save Record");
        saveRecordButton.setOnAction(e -> {
            String patientId = patientIdField.getText().trim();
            String patientName = patientNameField.getText().trim();
            // Validate input as necessary
            
            if (!patientId.isEmpty() && !patientName.isEmpty()) {
                PatientRecord patientRecord = new PatientRecord(patientId);
                patientRecord.setPatientName(patientName);
                // Set other details on the patientRecord as needed
                
                patientRecord.saveToFile(); // Saves the record to a file
                
                System.out.println("Saved/Updated Record for: " + patientName);
                // Optionally, clear the fields or show a confirmation message
            } else {
                System.err.println("Patient ID and name must not be empty.");
                // Show an error message to the user
            }
        });


        registerRecordView.add(new Label("Patient ID:"), 0, 0);
        registerRecordView.add(patientIdField, 1, 0);
        registerRecordView.add(new Label("Patient Name:"), 0, 1);
        registerRecordView.add(patientNameField, 1, 1);
        // Add more fields to the grid as needed
        registerRecordView.add(saveRecordButton, 1, 3);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showNurseDashboard()); // Navigate back to the dashboard.
        registerRecordView.add(backButton, 1, 4); // Adjust grid positioning as needed.
        
        rootLayout.setCenter(registerRecordView);
        
    }

    private void savePatientCheckIn(String name, String id) {
        // File path where the check-ins will be saved
        String filePath = "patient_check_ins.txt";
        
        // Format the patient information as a string to be written to the file
        String patientInfo = name + "," + id + "\n";

        try {
            // Java NIO package offers a simple way to append text to a file
            Files.write(Paths.get(filePath), patientInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            
            // Confirmation message (could be replaced with a more user-friendly notification)
            System.out.println("Patient Checked-In: " + name);
        } catch (IOException e) {
            e.printStackTrace();
            // Error handling here (e.g., show an error dialog to the user)
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

        // Access Patient History
        Button accessHistoryButton = new Button("Access Patient History");
        accessHistoryButton.setOnAction(e -> showAccessHistoryView()); // You'll need to implement this to match your data model

        // List Prescribed Medications
        Button listMedicationsButton = new Button("List Prescribed Medications");
        listMedicationsButton.setOnAction(e -> listPrescribedMedications()); // You'll need to create this method

        // Send Prescription to Pharmacy
        Button sendPrescriptionButton = new Button("Send Prescription to Pharmacy");
        sendPrescriptionButton.setOnAction(e -> sendPrescriptionToPharmacy()); // You'll need to create this method

        // Access Patient Records
        Button accessRecordsButton = new Button("Access Patient Records");
        accessRecordsButton.setOnAction(e -> accessPatientRecords()); // You'll need to create this method

        // Back to Home Dashboard or Log Out
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> showDoctorDashboard()); // Refreshes the Doctor Dashboard

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> showLogin()); // Returns to the Login Screen

        doctorDashboard.getChildren().addAll(dashboardLabel, accessHistoryButton, listMedicationsButton, sendPrescriptionButton, accessRecordsButton, homeButton, logoutButton);
        rootLayout.setCenter(doctorDashboard);
    }

   

    private void listPrescribedMedications() {
        // Placeholder method for listing prescribed medications.
        System.out.println("Listing prescribed medications...");
        // Implement functionality based on your application's requirements
    }

    private void sendPrescriptionToPharmacy() {
        // Placeholder method for sending prescriptions to a pharmacy.
        System.out.println("Sending prescription to the pharmacy...");
        // Implement functionality based on your application's requirements
    }

    private void accessPatientRecords() {
        // Placeholder method for accessing patient records.
        System.out.println("Accessing patient records...");
        // Implement functionality based on your application's requirements
    }

    public static void main(String[] args) {
        launch(args);
    }
}
