package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginOptionsController {

    private TTSApplication application;

    @FXML
    private Button adminButton;

    @FXML
    private Button studentButton;

    @FXML
    private Button facultyButton;

    public void setApplication(TTSApplication application) {
        this.application = application;
    }

    @FXML
    private void handleAdminLogin() {

        application.showAdminLoginScreen();
    }

    @FXML
    private void handleStudentLogin() {

        application.showStudentLoginScreen();
    }

    @FXML
    private void handleFacultyLogin() {

        application.showFacultyLoginScreen();
    }



}

