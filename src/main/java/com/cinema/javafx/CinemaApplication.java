package com.cinema.javafx;

import com.cinema.javafx.controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/root.fxml"));
        loader.setController(new RootController());
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Cinema");
        stage.show();
    }
}
