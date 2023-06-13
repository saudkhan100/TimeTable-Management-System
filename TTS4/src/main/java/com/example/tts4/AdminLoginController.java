package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController {

    private TTSApplication application;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    public void setApplication(TTSApplication application) {
        this.application = application;
    }

    @FXML
    private void loginButtonClicked() {
        String email = emailField.getText();
        String password = passwordField.getText();

        System.out.println("Admin logged in successfully");

        
    }
}
