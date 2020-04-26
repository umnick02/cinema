package com.cinema.javafx.controller.movies.movie;

import com.cinema.core.entity.Movie;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SceneModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DetailsController {

    private final MovieModel movieModel;

    @FXML
    public Label title;
    @FXML
    public Label originalTitle;
    @FXML
    public Label type;
    @FXML
    public Label genres;
    @FXML
    public Label country;
    @FXML
    public Label duration;
    @FXML
    public Label rating;

    @FXML
    public Button trailerButton;
    @FXML
    public Button playButton;

    public DetailsController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        Movie movie = movieModel.getMovie();
        title.setText(movie.fetchTitle());
        if (movie.fetchAdditionalTitle() != null) {
            originalTitle.setText(movie.fetchAdditionalTitle());
        }
        type.setText(movie.fetchType());
        genres.setText(movie.fetchGenres());
        country.setText(movie.fetchCountry());
        duration.setText(movie.fetchDuration());
        rating.setText(movie.fetchRating());
    }

    @FXML
    public void playTrailer() {
        SceneModel.INSTANCE.setActiveMovieModel(movieModel);
        SceneModel.INSTANCE.fireEvent(new Event(ModelEventType.TRAILER_PLAY.getEventType()));
    }

    @FXML
    public void playMovie() {
        if (movieModel.isSeries()) {
            movieModel.fireEvent(new Event(ModelEventType.SEASONS_SHOW.getEventType()));
        } else {
            SceneModel.INSTANCE.setActiveMovieModel(movieModel);
            SceneModel.INSTANCE.fireEvent(new Event(ModelEventType.MOVIE_PLAY.getEventType()));
        }
    }
}
