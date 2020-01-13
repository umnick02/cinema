package com.cinema;

import com.cinema.config.Config;
import com.cinema.config.GuiceModule;
import com.cinema.model.MovieModel;
import com.cinema.view.components.RootContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.cinema.config.Config.TITLE;

public class CinemaApplication extends Application {

    private static final Logger logger = LogManager.getLogger(CinemaApplication.class);

    private RootContainer rootContainer;
    private MovieModel movieModel;
    public static final Injector INJECTOR = Guice.createInjector(new GuiceModule());

    @Override
    public void init() {
        rootContainer = INJECTOR.getInstance(RootContainer.class);
        rootContainer.setGenres();
        rootContainer.setMovies();
        movieModel = INJECTOR.getInstance(MovieModel.class);
        movieModel.getGenres();
    }

    @Override
    public void start(Stage stage) {
        logger.info("start");
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.SCREEN_WIDTH));
        Scene scene = new Scene(rootContainer, width, width * 9 / 16d);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }
}
