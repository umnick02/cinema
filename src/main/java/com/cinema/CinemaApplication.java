package com.cinema;

import com.cinema.config.Config;
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

    private RootContainer root;
    private MovieModel movieModel;

    @Override
    public void init() {
//        run("magnet:?xt=urn:btih:4B9E634A87B853C54AD9F13F7B9E119A3BF13328&tr=http%3A%2F%2Fbt3.t-ru.org%2Fann%3Fmagnet&dn=%D0%9E%D0%B4%D0%BD%D0%B0%D0%B6%D0%B4%D1%8B%20%D0%B2%E2%80%A6%20%D0%93%D0%BE%D0%BB%D0%BB%D0%B8%D0%B2%D1%83%D0%B4%D0%B5%20%2F%20Once%20Upon%20a%20Time%20...%20in%20Hollywood%20(%D0%9A%D0%B2%D0%B5%D0%BD%D1%82%D0%B8%D0%BD%20%D0%A2%D0%B0%D1%80%D0%B0%D0%BD%D1%82%D0%B8%D0%BD%D0%BE%20%2F%20Quentin%20Tarantino)%20%5B2019%2C%20%D0%A1%D0%A8%D0%90%2C%20%D0%92%D0%B5%D0%BB%D0%B8%D0%BA%D0%BE%D0%B1%D1%80%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D1%8F%2C%20%D0%9A%D0%B8%D1%82%D0%B0%D0%B9%2C%20%D0%B4%D1%80%D0%B0%D0%BC%D0%B0%2C%20%D0%BA%D0%BE%D0%BC%D0%B5%D0%B4%D0%B8%D1%8F%2C%20BD");
        Injector injector = Guice.createInjector(new GuiceModule());
        root = injector.getInstance(RootContainer.class);
        movieModel = injector.getInstance(MovieModel.class);
        movieModel.getGenres();
    }

    @Override
    public void start(Stage stage) {
        logger.info("start");
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.SCREEN_WIDTH));
        Scene scene = new Scene(root, width, width * 9 / 16d);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

//    public void run(String magnet) {
//        try {
//            Movie movie = new MagnetParser().parse(magnet);
//            movieService.saveMovie(movie);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
