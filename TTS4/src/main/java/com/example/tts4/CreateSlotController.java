package com.example.tts4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateSlotController {

    @FXML
    private TextField typeField;

    @FXML
    private TextField intervalField;

    @FXML
    private void createSlotHandler(ActionEvent event) {
        String type = typeField.getText();
        String interval = intervalField.getText();


        insertSlotRecord(type, interval);


        typeField.clear();
        intervalField.clear();
    }

    private void insertSlotRecord(String type, String interval) {
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO slot (type, \"interval\") VALUES (?, ?::integer)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, type);
            statement.setString(2, interval);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

