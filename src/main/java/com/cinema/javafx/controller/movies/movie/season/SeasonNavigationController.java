package com.cinema.javafx.controller.movies.movie.season;

import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SeasonModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SeasonNavigationController {

    @FXML
    private HBox prev;
    @FXML
    private HBox next;
    @FXML
    private Label prevLabel;
    @FXML
    private Label nextLabel;

    private final MovieModel movieModel;

    public SeasonNavigationController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        SeasonModel nextSeasonModel = movieModel.getNextSeasonModel();
        if (nextSeasonModel != null) {
            nextLabel.setText("Сезон " + nextSeasonModel.getSeason());
        } else {
            next.getStyleClass().add("hide");
        }
        SeasonModel prevSeasonModel = movieModel.getPrevSeasonModel();
        if (prevSeasonModel != null) {
            prevLabel.setText("Сезон " + prevSeasonModel.getSeason());
        } else {
            prev.getStyleClass().add("hide");
        }
    }

    @FXML
    public void up() {
        movieModel.setActiveSeasonModel(null);
    }

    @FXML
    public void nextSeason() {
        movieModel.nextSeasonModel();
    }

    @FXML
    public void prevSeason() {
        movieModel.prevSeasonModel();
    }
}
