package com.cinema.javafx.controller.movie;

import com.cinema.core.entity.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DescriptionController {

    private final Movie movie;

    @FXML
    public Label description;

    public DescriptionController(Movie movie) {
        this.movie = movie;
    }

    @FXML
    public void initialize() {
        description.setText(movie.getDescriptionRu());
    }
}
