package com.example.tts4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.TimetableModel;

import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class CreateTimetableController implements Initializable {
    @FXML
    private GridPane timetableGrid;

    @FXML
    private ChoiceBox<String> slotChoiceBox;

    @FXML
    private ChoiceBox<String> yearChoiceBox;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private ChoiceBox<String> sectionChoiceBox;

    private int interval;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSlotChoiceBox();
        populateYearChoiceBox();
        populateDepartmentChoiceBox();
        populateSectionChoiceBox();
        addDayTitle();
        addDaysOfWeek();
        setupSlotChoiceBoxListener();
    }

    private void populateSectionChoiceBox() {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT section_names FROM section";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String section = resultSet.getString("section.section_name");
                sectionChoiceBox.getItems().add(section);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateDepartmentChoiceBox() {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT DISTINCT department FROM student";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String department = resultSet.getString("department");
                departmentChoiceBox.getItems().add(department);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateYearChoiceBox() {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT year_code FROM years";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String yearCode = resultSet.getString("year_code");
                yearChoiceBox.getItems().add(yearCode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateSlotChoiceBox() {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT type FROM slot";
            ResultSet resultSet = statement.executeQuery(query);

            List<String> slots = new ArrayList<>();
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                slots.add(type);
            }

            slotChoiceBox.getItems().addAll(slots);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDayTitle() {
        Label dayTitleLabel = new Label("Day Title");
        dayTitleLabel.setAlignment(Pos.CENTER);
        dayTitleLabel.getStyleClass().add("day-title");
        timetableGrid.add(dayTitleLabel, 0, 0);
    }

    private void addDaysOfWeek() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            String day = daysOfWeek[i];
            Label dayLabel = new Label(day);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.getStyleClass().add("day-label");
            timetableGrid.add(dayLabel, 0, i + 1);
        }
    }

    private void setupSlotChoiceBoxListener() {
        slotChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedSlot = newValue;
                interval = getIntervalFromDatabase(selectedSlot);
                updateTimetableGrid();
            }
        });
    }

    private int getIntervalFromDatabase(String selectedSlot) {
        int interval = 0;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT interval FROM slot WHERE type = ?")
        ) {
            statement.setString(1, selectedSlot);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                interval = resultSet.getInt("interval");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interval;
    }

    private void updateTimetableGrid() {
        // Clear existing time intervals
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : timetableGrid.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (columnIndex != null && rowIndex != null && columnIndex > 0 && rowIndex == 0) {
                nodesToRemove.add(node);
            }
        }
        timetableGrid.getChildren().removeAll(nodesToRemove);

        // Add new time intervals based on the selected slot
        int numTimeSlots = (21 * 60 - 8 * 60 + 30) / interval;

        int breakIndex = -1;

        for (int i = 0; i < numTimeSlots; i++) {
            int startHour = 8 + (interval * i) / 60;
            int startMinute = (interval * i) % 60;

            if (startHour == 12 && startMinute == 50) {
                breakIndex = i;
                int breakEndHour = startHour + 1;
                int breakEndMinute = startMinute + 50;
                String breakInterval = String.format("%02d:%02d - %02d:%02d", startHour, startMinute, breakEndHour, breakEndMinute);
                Label breakLabel = new Label(breakInterval);
                breakLabel.setAlignment(Pos.CENTER);
                breakLabel.getStyleClass().add("break-label");
                timetableGrid.add(breakLabel, i + 1, 0);
            }

            int column = i;
            if (i >= breakIndex) {
                column++;
            }

            int endHour = 8 + (interval * (i + 1)) / 60;
            int endMinute = (interval * (i + 1)) % 60;

            String timeInterval = String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute);
            Label timeIntervalLabel = new Label(timeInterval);
            timeIntervalLabel.setAlignment(Pos.CENTER);
            timeIntervalLabel.getStyleClass().add("time-interval-label");
            timetableGrid.add(timeIntervalLabel, column, 0);
        }
    }

    @FXML
    private void handleGenerateButtonAction(ActionEvent event) {
        // Get the number of columns and rows in the grid
        int columnCount = timetableGrid.getColumnCount();
        int rowCount = timetableGrid.getRowCount();

        // Remove the lecture-related content from the grid
        for (int row = 1; row < rowCount; row++) {
            for (int column = 1; column < columnCount; column++) {
                VBox classInfoBox = getClassInfoBoxFromGrid(column, row);
                if (classInfoBox != null) {
                    classInfoBox.getChildren().clear();
                }
            }
        }
        populateTimetableGrid();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO timetable (year_code, department, section, day_of_week, start_time, end_time, course_name, room_number, teacher_name) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
             )
        ) {
            String yearCode = yearChoiceBox.getValue();
            String department = departmentChoiceBox.getValue();
            String section = sectionChoiceBox.getValue();

            // Get the timetable data from the grid
            for (int row = 1; row < timetableGrid.getRowCount(); row++) {
                for (int column = 1; column < timetableGrid.getColumnCount() - 1; column++) {
                    VBox classInfoBox = getClassInfoBoxFromGrid(column, row);

                    // Extract the class information from the VBox
                    if (classInfoBox != null) {
                        Label courseLabel = (Label) classInfoBox.getChildren().get(0);
                        Label roomLabel = (Label) classInfoBox.getChildren().get(1);
                        Label teacherLabel = (Label) classInfoBox.getChildren().get(2);

                        String dayOfWeek = timetableGrid.getChildren().get(row).getId();

                        // Check if the time strings are non-null before parsing
                        String startTimeString = timetableGrid.getChildren().get((row - 1) * (timetableGrid.getColumnCount() - 1) + (column - 1)).getId();
                        String endTimeString = timetableGrid.getChildren().get(row * (timetableGrid.getColumnCount() - 1) + column).getId();

                        if (startTimeString == null || endTimeString == null) {
                            continue;
                        }

                        LocalTime startTime = LocalTime.parse(startTimeString);
                        LocalTime endTime = LocalTime.parse(endTimeString);
                        String courseName = courseLabel.getText();
                        String roomNumber = roomLabel.getText().split(" - ")[0];
                        String teacherName = teacherLabel.getText();

                        // Set the parameter values in the prepared statement
                        statement.setString(1, yearCode);
                        statement.setString(2, department);
                        statement.setString(3, section);
                        statement.setString(4, dayOfWeek);
                        statement.setTime(5, Time.valueOf(startTime));
                        statement.setTime(6, Time.valueOf(endTime));
                        statement.setString(7, courseName);
                        statement.setString(8, roomNumber);
                        statement.setString(9, teacherName);

                        // Execute the SQL statement
                        statement.executeUpdate();
                    }
                }
            }

            System.out.println("Timetable saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the class info VBox from the timetable grid
    private VBox getClassInfoBoxFromGrid(int column, int row) {
        for (Node node : timetableGrid.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex != null && rowIndex != null && columnIndex == column && rowIndex == row) {
                if (node instanceof VBox) {
                    return (VBox) node;
                }
            }
        }

        return null;
    }

    private void populateTimetableGrid() {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT c.course_name, r.room_no, t.block, f.name " +
                    "FROM courses c " +
                    "JOIN room r ON c.course_id = r.course_id " +
                    "JOIN faculty f ON c.faculty_id = f.faculty_id " +
                    "ORDER BY RANDOM() LIMIT 17";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int totalRows = timetableGrid.getRowCount();
            int totalColumns = timetableGrid.getColumnCount();

            int classesAdded = 0;

            while (resultSet.next() && classesAdded < totalRows * (totalColumns - 1)) {
                int row = (int) (Math.random() * (totalRows - 1)) + 1;
                int column = (int) (Math.random() * (totalColumns - 1)) + 1;

                VBox classInfoBox = getClassInfoBoxFromGrid(column, row);

                // Check if the cell is occupied
                if (classInfoBox == null) {
                    String courseName = resultSet.getString("course_name");
                    String roomNumber = resultSet.getString("room_no");
                    String block = resultSet.getString("block");
                    String teacherName = resultSet.getString("name");

                    classInfoBox = new VBox();
                    Label courseLabel = new Label(courseName);
                    Label roomLabel = new Label(roomNumber + " - " + block);
                    Label teacherLabel = new Label(teacherName);
                    classInfoBox.getChildren().addAll(courseLabel, roomLabel, teacherLabel);
                    classInfoBox.getStyleClass().add("class-info-box");
                    timetableGrid.add(classInfoBox, column, row);

                    classesAdded++;
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
