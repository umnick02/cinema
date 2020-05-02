package com.cinema.javafx.controller.movies.movie.season;

import com.cinema.core.model.impl.SeasonModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SeasonController {

    @FXML
    public VBox episodesPane;

    private SeasonModel seasonModel;

    public SeasonController(SeasonModel seasonModel) {
        this.seasonModel = seasonModel;
    }

    @FXML
    public void initialize() {
        seasonModel.getEpisodeModels()
                .forEach(episodeModel -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie/season/episode.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(EpisodeController.class)) {
                            return new EpisodeController(episodeModel);
                        }
                        return null;
                    });
                    try {
                        episodesPane.getChildren().add(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
