package com.example.tts4;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label helloLabel;

    public void initialize() {
        helloLabel.setText("Hello, World!");
    }
}
