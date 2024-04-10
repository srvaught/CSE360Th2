package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String imagePath = "/Users/abdullahalghabban/eclipse-workspace/aksas.png";
        File imageFile = new File(imagePath);
        String fileUrl = imageFile.toURI().toString();
        Image logoImage = new Image(fileUrl);

        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(50);
        logoImageView.setPreserveRatio(true);

        Label hospitalNameLabel = new Label("AKSAS");
        hospitalNameLabel.getStyleClass().add("title-label");
        Label userIDLabel = new Label("User ID");
        Label passwordLabel = new Label("Password");

        TextField userIDField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        HBox companyBox = new HBox(10);
        companyBox.setAlignment(Pos.CENTER_LEFT);
        companyBox.getChildren().addAll(hospitalNameLabel, logoImageView);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(5);

        grid.add(companyBox, 1, 0);
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
                    failureAlert.setTitle("Login Failed");
                    failureAlert.setHeaderText("Invalid Credentials");
                    failureAlert.setContentText("Please check your User ID and Password and try again.");
                    failureAlert.showAndWait();
                }
            }
        });

        Scene scene = new Scene(grid, 350, 350);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
