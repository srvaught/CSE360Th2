package evenRecognizerTestbed;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

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

    private void handleLogin(String username, String password, ToggleGroup roleGroup) {
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        userRole = selectedRole.getText();

        if (true) {
            if (userRole.equals("Patient")) {
                showPatientPortal(); // Only show for patients
            } else {
                // Display a message indicating successful login for non-patients
                Label successLabel = new Label("Login Successful! Please wait for further instructions.");
                rootLayout.setCenter(successLabel);
            }
        } else {
            // Display a more prominent error message for login failures
            Label errorLabel = new Label("Invalid username or password");
            errorLabel.setStyle("-fx-text-fill: red");

            Button tryAgainButton = new Button("Try Again");
            tryAgainButton.setOnAction(e -> {
                rootLayout.setCenter(null); // Clear the existing error message
                showLogin();             // Display the login form again
            });

            VBox errorBox = new VBox(10, errorLabel, tryAgainButton);
            errorBox.setAlignment(Pos.CENTER);
            rootLayout.setCenter(errorBox);
        }
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
        saveChangesButton.setStyle("-fx-background-color: #206090; -fx-text-fill: white; -fx-cursor: default;");
        saveChangesButton.setDisable(true); // Assuming saving is disabled for now






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

    public static void main(String[] args) {
        launch(args);
    }
}