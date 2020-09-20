package com.cinema.javafx.controller.movies;

import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.javafx.controller.movies.movie.CastsController;
import com.cinema.javafx.controller.movies.movie.DescriptionController;
import com.cinema.javafx.controller.movies.movie.DetailsController;
import com.cinema.javafx.controller.movies.movie.SeasonsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.TilePane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoviesController {

    private static final Logger logger = LoggerFactory.getLogger(MoviesController.class);

    @FXML
    public TilePane moviesContainer;

    @FXML
    public void initialize() {
        moviesContainer.addEventHandler(ModelEventType.MOVIES_UPDATE.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            updateMovies();
        });
        SceneModel.INSTANCE.registerEventTarget(moviesContainer);
        SceneModel.INSTANCE.setMovies(MovieModel.getMovies());
    }

    private void updateMovies() {
        moviesContainer.getChildren().clear();
        for (MovieModel movieModel : SceneModel.INSTANCE.getMovieModels()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie.fxml"));
                loader.setControllerFactory(param -> {
                    if (param.isAssignableFrom(MovieController.class)) {
                        return new MovieController(movieModel);
                    } else if (param.isAssignableFrom(DescriptionController.class)) {
                        return new DescriptionController(movieModel.getMovie());
                    } else if (param.isAssignableFrom(CastsController.class)) {
                        return new CastsController(movieModel.getMovie());
                    } else if (param.isAssignableFrom(DetailsController.class)) {
                        return new DetailsController(movieModel);
                    } else if (param.isAssignableFrom(SeasonsController.class) && movieModel.getMovie().isSeries()) {
                        return new SeasonsController(movieModel);
                    }
                    return null;
                });
                moviesContainer.getChildren().add(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
