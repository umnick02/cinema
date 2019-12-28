package com.cinema;

import com.cinema.entity.Movie;
import com.cinema.parser.MagnetParser;
import com.cinema.service.Language;
import com.cinema.service.MovieService;
import com.cinema.ui.ApplicationUI;
import com.cinema.ui.components.RootContainer;
import com.cinema.ui.components.SceneBuilder;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;

import java.util.*;

@SpringBootApplication
public class CinemaApplication extends ApplicationUI {

    private static int width = 1024;
    private static final int itemsInRow = 2;
    private static final String title = "Cinema";
    @Autowired
    public MovieService movieService;
    private int page = 0;

    @Override
    public void start(Stage stage) {
////        run("magnet:?xt=urn:btih:AE190D99F38E5A50AAD6A1B20EE5AF6D165FEEBE&tr=http%3A%2F%2Fbt.t-ru.org%2Fann%3Fmagnet");
//        Group root = new Group();
//        GridPane gridPane = UIBuilder.createGridPane();
//        List<Movie> movies = movieService.getMovies();
//        for (int i = 0; i < movies.size(); i++) {
//            gridPane.add(UIBuilder.createCard(movies.get(i).getMovieEn().getPoster()), i % itemsInRow, i / itemsInRow);
//        }
//        root.getChildren().add(gridPane);

        RootContainer root = SceneBuilder.buildRootContainer();
        Scene scene = new Scene(root, width, width * 9 / 16d);
        stage.setScene(scene);
        stage.setTitle(title);
//        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
////            resetWidth(oldVal.intValue(), newVal.intValue());
//            resetCards();
//
//        });
        stage.show();

        Set<Movie> movies = movieService.getMovies(PageRequest.of(page++, 10), Language.EN);
        root.getContentContainer().getScrollCardsContainer().getCardsContainer().addCards(movies);
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
