package com.example.tts4;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FacultyLoginController {

    private TTSApplication application;

    @FXML
    private TextField facultyIdField;

    @FXML
    private PasswordField passwordField;

    public void setApplication(TTSApplication application) {
        this.application = application;
    }

    @FXML
    private void loginButtonClicked() {
        String facultyId = facultyIdField.getText();
        String password = passwordField.getText();


        boolean loginSuccessful = validateFacultyLogin(facultyId, password);

        if (loginSuccessful) {
            System.out.println("Faculty logged in successfully");


            loadFacultyDashboard();
        } else {
            System.out.println("Faculty login failed. Invalid credentials.");

        }
    }

    private boolean validateFacultyLogin(String facultyId, String password) {

        return true;
    }

    private void loadFacultyDashboard() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/FacultyDashboard.fxml"));
            AnchorPane facultyDashboard = fxmlLoader.load();


            FacultyDashboardController facultyDashboardController = fxmlLoader.getController();
            facultyDashboardController.setApplication(application);

            Scene scene = new Scene(facultyDashboard);
            Stage primaryStage = (Stage) facultyIdField.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Faculty Dashboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}


