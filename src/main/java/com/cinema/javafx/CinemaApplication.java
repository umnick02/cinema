package com.cinema.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/root.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Cinema");
        stage.show();
    }
}
