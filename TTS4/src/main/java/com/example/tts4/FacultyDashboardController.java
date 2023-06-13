package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FacultyDashboardController {

    private TTSApplication application;

    @FXML
    private Label welcomeLabel;

    public void setApplication(TTSApplication application) {
        this.application = application;
    }

    public void initialize() {

        String facultyName = application.getFacultyName();

        welcomeLabel.setText("Welcome, " + facultyName + "!");
    }

    @FXML
    private void handleLogout() {

        application.showLoginScreen();
    }



}

