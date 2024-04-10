package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Enter Patient Vitals");
        Label temperatureLabel = new Label("Temperature (°F):");
        Label bloodPressureLabel = new Label("Blood Pressure (mmHg):");
        Label heartRateLabel = new Label("Heart Rate (bpm):");
        Label respiratoryRateLabel = new Label("Respiratory Rate (breaths/min):");
        Label oxygenSaturationLabel = new Label("Oxygen Saturation (%):");
        Label heightLabel = new Label("Height (ft):");
        Label weightLabel = new Label("Weight (lb):");

        TextField temperatureField = new TextField();
        TextField bloodPressureField = new TextField();
        TextField heartRateField = new TextField();
        TextField respiratoryRateField = new TextField();
        TextField oxygenSaturationField = new TextField();
        TextField heightField = new TextField();
        TextField weightField = new TextField();

        Button saveButton = new Button("Save");

        Arc arc = new Arc(15, 15, 10, 10, 180, 90);
        arc.setType(ArcType.OPEN);
        arc.setStroke(Color.LIGHTBLUE);
        arc.setStrokeWidth(2);

        Rectangle patientNameRect = new Rectangle(320, 30);
        patientNameRect.setFill(Color.LIGHTBLUE);
        patientNameRect.setArcHeight(10);
        patientNameRect.setArcWidth(10);

        Label patientNameLabel = new Label(" Patient: Mr. Ronaldo");

        Button historyRecordButton = new Button("Patient History Record");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(arc, 0, 0);
        grid.add(patientNameRect, 0, 0, 2, 1);
        grid.add(patientNameLabel, 0, 0, 2, 1);
        grid.add(historyRecordButton, 1, 0, 4, 1);
        grid.add(titleLabel, 0, 1, 2, 1);
        grid.add(temperatureLabel, 0, 2);
        grid.add(temperatureField, 1, 2);
        grid.add(bloodPressureLabel, 0, 3);
        grid.add(bloodPressureField, 1, 3);
        grid.add(heartRateLabel, 0, 4);
        grid.add(heartRateField, 1, 4);
        grid.add(respiratoryRateLabel, 0, 5);
        grid.add(respiratoryRateField, 1, 5);
        grid.add(oxygenSaturationLabel, 0, 6);
        grid.add(oxygenSaturationField, 1, 6);
        grid.add(heightLabel, 0, 7);
        grid.add(heightField, 1, 7);
        grid.add(weightLabel, 0, 8);
        grid.add(weightField, 1, 8);
        grid.add(saveButton, 1, 9);

        saveButton.setOnAction(e -> saveVitals());

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setTitle("Vitals");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveVitals() {
        System.out.println("Saving Vitals...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
