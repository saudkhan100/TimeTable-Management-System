package com.example.tts4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TTSApplication extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginOptions();
    }

    private void showLoginOptions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/hello.fxml"));
        rootLayout = fxmlLoader.load();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Time Table Tracking System");
        primaryStage.show();

        Button adminButton = (Button) rootLayout.lookup("#adminButton");
        Button facultyButton = (Button) rootLayout.lookup("#facultyButton");
        Button studentButton = (Button) rootLayout.lookup("#studentButton");

        adminButton.setOnAction(event -> {
            showAdminLogin();
        });

        facultyButton.setOnAction(event -> {
            try {
                showFacultyLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        studentButton.setOnAction(event -> {
            showStudentLogin();
        });
    }

    public void showAdminLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tts4/AdminLogin.fxml"));
            Parent adminLogin = loader.load();


            AdminLoginController adminLoginController = loader.getController();


            adminLoginController.setTTSApplication(this);

            Scene scene = new Scene(adminLogin);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudentLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TTSApplication.class.getResource("studentLogin.fxml"));
            Pane studentLogin = loader.load();


            StudentLoginController controller = loader.getController();
            controller.setTTSApplication(this);


            Scene scene = new Scene(studentLogin);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFacultyLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tts4/FacultyLogin.fxml"));
        VBox facultyLoginPane = fxmlLoader.load();
        rootLayout.getChildren().setAll(facultyLoginPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
