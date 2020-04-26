package com.cinema.javafx.controller.movies.movie.season;

import com.cinema.core.model.impl.EpisodeModel;
import com.cinema.core.utils.StringUtils;
import com.cinema.javafx.controller.movies.MovieController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EpisodeController {

    @FXML
    private BorderPane episodePane;
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label release;
    @FXML
    private Label rating;

    private final EpisodeModel episodeModel;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public EpisodeController(EpisodeModel episodeModel) {
        this.episodeModel = episodeModel;
    }

    @FXML
    public void initialize() {
        episodePane.setMinWidth(MovieController.width*(MovieController.ratio - 1) - 40);
        episodePane.setMaxWidth(MovieController.width*(MovieController.ratio - 1) - 40);
        Image image = new Image(episodeModel.getEpisode().getPoster(),
                episodePane.getMaxWidth()/2.5, -1, true, true);
        executorService.submit(() -> poster.setImage(image));
        title.setText(episodeModel.getEpisode().getEpisode() + ". " + episodeModel.getEpisode().getTitle());
        release.setText(episodeModel.getEpisode().getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        rating.setText(String.format("%.1f (%s)", episodeModel.getEpisode().getRating(), StringUtils.longToString(episodeModel.getEpisode().getRatingVotes())));
    }
}
