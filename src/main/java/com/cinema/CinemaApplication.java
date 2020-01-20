package com.cinema;

import com.cinema.config.Config;
import com.cinema.config.GuiceModule;
import com.cinema.view.components.RootContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static com.cinema.config.Config.TITLE;

public class CinemaApplication extends Application {

    public static final Injector INJECTOR = Guice.createInjector(new GuiceModule());

    private RootContainer rootContainer;

    @Override
    public void init() {
        rootContainer = INJECTOR.getInstance(RootContainer.class);
    }

    @Override
    public void start(Stage stage) {
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.SCREEN_WIDTH));
        Scene scene = new Scene(rootContainer.getAnchorPane(), width, width * 9 / 16d);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }
}
