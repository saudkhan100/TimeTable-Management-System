package com.example.tts4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
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
                String section = resultSet.getString("section_names");
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
        try {

            Connection connection = Database.getConnection();


            String query = "SELECT type FROM slot";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();


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
             PreparedStatement statement = connection.prepareStatement("SELECT interval FROM slot WHERE type = ?");
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
        timetableGrid.getChildren().removeIf(node -> {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            return columnIndex != null && columnIndex > 0 && rowIndex != null && rowIndex > 0;
        });

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
        populateTimetableGrid();
    }



    private void populateTimetableGrid() {

        List<ClassInfo> classInfoList = fetchClassInfoFromDatabase();


        int totalRows = timetableGrid.getRowCount();
        int totalColumns = timetableGrid.getColumnCount();

        int classesAdded = 0;

        while (classesAdded < classInfoList.size()) {
            int row = (int) (Math.random() * (totalRows - 1)) + 1;
            int column = (int) (Math.random() * (totalColumns - 1)) + 1;


            boolean cellOccupied = timetableGrid.getChildren().stream()
                    .anyMatch(node -> {
                        Integer rowIndex = GridPane.getRowIndex(node);
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        return rowIndex != null && columnIndex != null && rowIndex.intValue() == row && columnIndex.intValue() == column;
                    });

            if (!cellOccupied) {
                ClassInfo classInfo = classInfoList.get(classesAdded);
                String courseName = classInfo.getCourseName();
                String roomNumber = classInfo.getRoomNumber();
                String teacherName = classInfo.getTeacherName();
                String block = classInfo.getBlock();

                VBox classInfoBox = new VBox();
                Label courseLabel = new Label(courseName);
                Label roomLabel = new Label(roomNumber + " - " + block);
                Label teacherLabel = new Label(teacherName);
                classInfoBox.getChildren().addAll(courseLabel, roomLabel, teacherLabel);
                classInfoBox.getStyleClass().add("class-info-box");
                timetableGrid.add(classInfoBox, column, row);

                classesAdded++;
            }
        }
    }

    private List<ClassInfo> fetchClassInfoFromDatabase() {
        List<ClassInfo> classInfoList = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT course_name, room_no, block, name FROM courses, room, faculty ORDER BY RANDOM() LIMIT 17";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String roomNumber = resultSet.getString("room_no");
                String block = resultSet.getString("block");
                String teacherName = resultSet.getString("name");

                ClassInfo classInfo = new ClassInfo(courseName, roomNumber, block, teacherName);
                classInfoList.add(classInfo);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classInfoList;
    }



}
