package com.cinema.javafx.controller.movie;

import com.cinema.core.entity.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DetailsController {

    private final Movie movie;

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


    public DetailsController(Movie movie) {
        this.movie = movie;
    }

    @FXML
    public void initialize() {
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
}
