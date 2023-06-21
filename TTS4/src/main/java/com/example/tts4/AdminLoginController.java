package com.example.tts4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private TTSApplication ttsApplication;

    public void setTTSApplication(TTSApplication ttsApplication) {
        this.ttsApplication = ttsApplication;
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (validateAdminLogin(email, password)) {
            navigateToAdminDashboard();
            System.out.println("Login successful. Navigating to AdminDashboard...");
        } else {
            System.out.println("Invalid login credentials. Please try again.");
        }
    }

    private void navigateToAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController dashboardController = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAdminLogin(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
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
}
