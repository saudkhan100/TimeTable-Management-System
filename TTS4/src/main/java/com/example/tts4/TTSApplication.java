package com.example.tts4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class TTSApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initRootLayout();
        showLoginOptions();
    }

    private void initRootLayout() {
        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Time Table Tracking System");
        primaryStage.show();
    }

    private void showLoginOptions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/LoginOptions.fxml"));
        Pane loginOptionsPane = fxmlLoader.load();

        LoginOptionsController controller = fxmlLoader.getController();
        controller.setApplication(this);

        rootLayout.setCenter(loginOptionsPane);
    }

    public void showAdminLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/AdminLogin.fxml"));
        Pane adminLoginPane = fxmlLoader.load();

        AdminLoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        rootLayout.setCenter(adminLoginPane);
    }

    public void showStudentLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/StudentLogin.fxml"));
        Pane studentLoginPane = fxmlLoader.load();

        StudentLoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        rootLayout.setCenter(studentLoginPane);
    }

    public void showFacultyLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/FacultyLogin.fxml"));
        Pane facultyLoginPane = fxmlLoader.load();

        FacultyLoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        rootLayout.setCenter(facultyLoginPane);
    }

    // Other methods and main() as before
}
