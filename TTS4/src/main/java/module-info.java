module com.example.tts4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.tts4 to javafx.fxml;
    exports com.example.tts4;
}