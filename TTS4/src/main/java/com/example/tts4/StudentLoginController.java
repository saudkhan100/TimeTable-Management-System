package com.example.tts4;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class StudentLoginController {
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    private TTSApplication ttsApplication;

    @FXML
    public void handleLogin() {
        String enteredEmail = emailTextField.getText();
        String enteredPassword = passwordField.getText();

        if (validateEmail(enteredEmail) && validateStudentLogin(enteredEmail, enteredPassword)) {
            navigateToDashboard();
            System.out.println("Login successful. Navigating to StudentDashboard...");
        } else {
            System.out.println("Invalid login credentials. Please try again.");
        }
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));
            Parent root = loader.load();

            StudentDashboardController dashboardController = loader.getController();
            String studentName = getStudentNameFromDatabase(emailTextField.getText());
            dashboardController.setStudentName(studentName);

            Scene scene = new Scene(root);
            Stage stage = (Stage) emailTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStudentNameFromDatabase(String email) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT name FROM student WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@gmail.com$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean validateStudentLogin(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM student WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setTTSApplication(TTSApplication ttsApplication) {
        this.ttsApplication = ttsApplication;
    }
}

