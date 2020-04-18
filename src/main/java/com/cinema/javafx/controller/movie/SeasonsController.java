package com.cinema.javafx.controller.movie;

import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SeasonModel;
import com.cinema.javafx.controller.movie.season.SeasonController;
import com.cinema.javafx.controller.movie.season.SeasonNavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
                seasonPane.setOnMouseClicked(event -> showSeason(seasonModel));
                seasonsPane.getChildren().add(seasonPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public List<Button> buildEpisodes() {
//        List<Button> buttons = new ArrayList<>();
//        seasonStats.stream().sorted(Comparator.comparingInt(Movie.SeasonStat::getSeason)).forEach(stat -> {
////            Button button = buildSeasonMovie(stat);
////            button.setOnMouseClicked(event -> changeEpisode(stat));
////            buttons.add(button);
//        });
//        return buttons;
//    }

    public void showSeason(SeasonModel seasonModel) {
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
            ScrollPane seasonPane = loader.load();
            movieModel.registerEventTarget(seasonPane);
            seasonPane.addEventHandler(ModelEventType.SEASON_CHANGE.getEventType(),
                    event -> showSeason(movieModel.getActiveSeasonModel()));
            seriesPane.getChildren().add(seasonPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        seasonStat.getEpisodes().stream().filter(s -> s.getSeason() == seasonStat.getSeason()).findFirst().ifPresent(this::changeEpisode);
    }
//
//    public void changeEpisode(Movie.SeasonStat stat) {
//        if (container.getChildren().size() > 1) {
//            container.getChildren().remove(1);
//        }
//        EpisodesController episodesController = new EpisodesController(this, stat);
//        container.getChildren().add(episodesController.getRootContainer());
//    }
//
//    public void hideEpisodes() {
//        if (container.getChildren().size() > 1) {
//            container.getChildren().remove(1);
//        }
//    }
}
