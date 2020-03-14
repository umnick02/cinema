package com.cinema;

import com.cinema.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main_scene.fxml"));
        loader.setController(new MainController());
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.setTitle("Cinema");
        stage.show();
    }
}
