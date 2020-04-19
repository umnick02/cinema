package com.cinema.javafx.controller.movies.movie;

import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SeasonModel;
import com.cinema.javafx.controller.movies.movie.season.SeasonController;
import com.cinema.javafx.controller.movies.movie.season.SeasonNavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.cinema.core.utils.StringUtils.longToString;

public class SeasonsController {

    @FXML
    public StackPane seriesPane;
    @FXML
    public TilePane seasonsPane;

    private final MovieModel movieModel;

    public SeasonsController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        List<SeasonModel> seasonModelsList = movieModel.getSeasonModels().stream()
                .sorted(Comparator.comparingInt(SeasonModel::getSeason))
                .collect(Collectors.toList());
        for (SeasonModel seasonModel : seasonModelsList) {
            try {
                Button seasonPane = FXMLLoader.load(getClass().getResource("/view/movie/season/season.fxml"));
                seasonPane.setText(String.format("Сезон %d (%d эп.)\n%s\n%.1f (%s)",
                        seasonModel.getSeason(), seasonModel.getEpisodeModels().size(),
                        seasonModel.getRelease().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                        seasonModel.getAvgRating(), longToString(seasonModel.getAvgVotes()))
                );
                seasonPane.setOnMouseClicked(event -> {
                    if (seriesPane.getChildren().size() > 1) {
                        seriesPane.getChildren().remove(seriesPane.getChildren().size() - 1);
                    }
                    movieModel.setActiveSeasonModel(seasonModel);
                });
                seasonsPane.getChildren().add(seasonPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        seriesPane.addEventHandler(ModelEventType.SEASON_CHANGE.getEventType(),
                event -> showSeason(movieModel.getActiveSeasonModel()));
        movieModel.registerEventTarget(seriesPane);
    }

    public void showSeason(SeasonModel seasonModel) {
        if (seriesPane.getChildren().size() > 1) {
            seriesPane.getChildren().remove(seriesPane.getChildren().size() - 1);
        }
        if (seasonModel == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie/season/episodes.fxml"));
        loader.setControllerFactory(param -> {
            if (param.isAssignableFrom(SeasonController.class)) {
                return new SeasonController(seasonModel);
            } else if (param.isAssignableFrom(SeasonNavigationController.class)) {
                return new SeasonNavigationController(movieModel);
            }
            return null;
        });
        try {
            seriesPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
