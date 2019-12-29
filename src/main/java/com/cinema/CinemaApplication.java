package com.cinema;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.parser.MagnetParser;
import com.cinema.service.MovieService;
import com.cinema.ui.components.RootContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.cinema.config.Config.TITLE;

public class CinemaApplication extends Application {

    private RootContainer root;
    private MovieService movieService;

    @Override
    public void init() {
        Injector injector = Guice.createInjector();
        root = injector.getInstance(RootContainer.class);
        movieService = injector.getInstance(MovieService.class);
    }

    @Override
    public void start(Stage stage) {
//        run("magnet:?xt=urn:btih:AE190D99F38E5A50AAD6A1B20EE5AF6D165FEEBE&tr=http%3A%2F%2Fbt.t-ru.org%2Fann%3Fmagnet");
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.SCREEN_WIDTH));
        Scene scene = new Scene(root, width, width * 9 / 16d);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    public void run(String magnet) {
        try {
            Movie movie = new MagnetParser().parse(magnet);
            movieService.saveMovie(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
