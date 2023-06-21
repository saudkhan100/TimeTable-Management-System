package com.example.tts4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class StudentLoginController {
    @FXML
    private ChoiceBox<String> yearChoiceBox;



    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField regNoTextField;



    private TTSApplication ttsApplication;

    @FXML
    public void handleLogin() {
        String selectedYear = yearChoiceBox.getValue();
        String selectedDepartment = departmentChoiceBox.getValue();
        String enteredPassword = passwordField.getText();
        int enteredRegNo = Integer.parseInt(regNoTextField.getText());



        if (validateStudentLogin(selectedYear, selectedDepartment, enteredPassword, enteredRegNo)) {
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


            Scene scene = new Scene(root);
            Stage stage = (Stage) yearChoiceBox.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean validateStudentLogin(String year, String department, String password, int regNo) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM student WHERE start = ? AND department = ? AND password = ? AND reg_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, year);
            stmt.setString(2, department);
            stmt.setString(3, password);
            stmt.setInt(4, regNo);
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

    public void populateChoiceBoxes() {
        // Retrieve years and departments from the database
        ObservableList<String> years = retrieveYears();
        ObservableList<String> departments = retrieveDepartments();

        // Populate the ChoiceBoxes with the retrieved data
        yearChoiceBox.setItems(years);
        departmentChoiceBox.setItems(departments);
    }

    private ObservableList<String> retrieveYears() {
        ObservableList<String> years = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT year_code FROM years")) {

            while (rs.next()) {
                String year = rs.getString("year_code");
                years.add(year);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return years;
    }

    private ObservableList<String> retrieveDepartments() {
        ObservableList<String> departments = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT department FROM student")) {

            while (rs.next()) {
                String department = rs.getString("department");
                departments.add(department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }
}


