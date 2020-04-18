package com.cinema.javafx.controller.movie.season;

import com.cinema.core.model.impl.MovieModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SeasonNavigationController {

    @FXML
    public VBox episodesPane;
    @FXML
    private Button prev;
    @FXML
    private Button up;
    @FXML
    private Button next;

    private final MovieModel movieModel;

    public SeasonNavigationController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        prev.setOnMouseClicked(event -> movieModel.setActiveSeasonModel(movieModel.prevSeasonModel()));
        next.setOnMouseClicked(event -> movieModel.setActiveSeasonModel(movieModel.nextSeasonModel()));
        up.setOnMouseClicked(event -> movieModel.setActiveSeasonModel(null));
    }
}
