package com.example.tts4;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private Button createTimetableButton;

    @FXML
    public void initialize() {
        createTimetableButton.setOnAction(event -> navigateToCreateTimetable());
    }

    @FXML
    private void handleCreateSlotButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateSlot.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToCreateTimetable() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateTimetable.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            Stage stage = (Stage) createTimetableButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

