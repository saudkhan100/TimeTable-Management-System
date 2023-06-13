package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StudentLoginController {

    private TTSApplication application;

    @FXML
    private TextField studentIdField;

    @FXML
    private PasswordField passwordField;

    public void setApplication(TTSApplication application) {
        this.application = application;
    }

    @FXML
    private void loginButtonClicked() {
        String studentId = studentIdField.getText();
        String password = passwordField.getText();



        System.out.println("Student logged in successfully");


    }
}
