package com.example.tts4;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {
    @FXML
    private Label nameLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label sectionLabel;

    @FXML
    private GridPane timetableGrid;

    private String studentName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the student details
        populateStudentDetails();

        // Fetch and display the student's timetable
        String department = departmentLabel.getText();
        String section = sectionLabel.getText();
        fetchAndDisplayTimetable();
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
        nameLabel.setText(studentName);
    }

    private void populateStudentDetails() {
        // Fetch the student details from the database using the student's name
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT name, email, department, section FROM student WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String department = resultSet.getString("department");
                String section = resultSet.getString("section");

                // Populate the labels with student details
                nameLabel.setText(name);
                departmentLabel.setText(department);
                sectionLabel.setText(section);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchAndDisplayTimetable() {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT * FROM timetable ORDER BY RANDOM() LIMIT 5";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear the existing timetable grid
            timetableGrid.getChildren().clear();

            // Define the starting time for the first column
            LocalTime startTime = LocalTime.of(8, 0);

            // Populate the timetable grid with the fetched timetable data
            int row = 0;
            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("day_of_week");
                String startTimeString = resultSet.getTime("start_time").toString();
                String endTimeString = resultSet.getTime("end_time").toString();
                String courseName = resultSet.getString("course_name");
                String roomNumber = resultSet.getString("room_number");
                String teacherName = resultSet.getString("teacher_name");

                // Create labels to display the timetable information
                Label dayLabel = new Label(dayOfWeek);
                Label timeLabel = new Label(startTimeString + " - " + endTimeString);
                Label courseLabel = new Label(courseName);
                Label roomLabel = new Label(roomNumber);
                Label teacherLabel = new Label(teacherName);

                // Add the labels to the timetable grid
                timetableGrid.add(dayLabel, 0, row);
                timetableGrid.add(timeLabel, 1, row);
                timetableGrid.add(courseLabel, 2, row);
                timetableGrid.add(roomLabel, 3, row);
                timetableGrid.add(teacherLabel, 4, row);

                // Update the starting time for the next column
                startTime = LocalTime.parse(endTimeString).plusMinutes(10);

                row++;
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String getDayOfWeek(int row) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        if (row >= 0 && row < daysOfWeek.length) {
            return daysOfWeek[row];
        }
        return "";
    }


    private String getColumnTimeInterval(int column) {
        // Calculate the start time for the column
        int startHour = 8 + (column - 1) / 2;
        int startMinute = (column - 1) % 2 * 30;

        // Calculate the end time for the column
        int endHour = startHour;
        int endMinute = startMinute + 30;

        // Format the time interval string
        String startTimeString = String.format("%02d:%02d", startHour, startMinute);
        String endTimeString = String.format("%02d:%02d", endHour, endMinute);
        return startTimeString + " - " + endTimeString;
    }



}
