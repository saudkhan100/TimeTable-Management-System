package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CreateTimeTableController {
    @FXML
    private GridPane timetableGrid;


    private TextField[][] timetableFields;

    @FXML
    public void initialize() {

        timetableFields = new TextField[7][6];


        for (int day = 0; day < 7; day++) {
            for (int period = 0; period < 6; period++) {
                TextField textField = new TextField();
                timetableFields[day][period] = textField;
                timetableGrid.add(textField, day + 1, period + 1);
            }
        }
    }

    @FXML
    private void saveButtonClicked() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Timetable saved successfully.");
        alert.showAndWait();
    }
}
