package com.cinema.javafx.controller.player;

import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.model.impl.TorrentModel;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cinema.core.model.ModelEventType.SHUTDOWN;
import static com.cinema.core.model.ModelEventType.TORRENT_UPDATE;

public class PosterController {

    private static final Logger logger = LoggerFactory.getLogger(PosterController.class);

    @FXML
    private StackPane torrentStatistic;
    @FXML
    private ImageView posterView;
    @FXML
    private Label statusLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Label peersLabel;

    private final BorderPane menuPane;

    public PosterController(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

    @FXML
    public void closePlayer() {
        menuPane.fireEvent(new Event(SHUTDOWN.getEventType()));
    }

    @FXML
    public void initialize() {
        initializePoster();

        TorrentModel.INSTANCE.registerEventTarget(torrentStatistic);

        torrentStatistic.addEventHandler(TORRENT_UPDATE.getEventType(), event ->
                Platform.runLater(() -> {
                    logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());

                    int statusVal = TorrentModel.INSTANCE.getStatus();
                    if (statusVal >= 100) {
                        TorrentModel.INSTANCE.unRegisterEventTarget(torrentStatistic);
                    }
                    String status = String.format("Загрузка: %d %%", statusVal);
                    String peers = String.format("Пиров: %d", TorrentModel.INSTANCE.getPeers());
                    String speed = String.format("Скорость: %d kb/s", TorrentModel.INSTANCE.getAvgSpeed());

                    logger.info("{} {} {}", status, peers, speed);

                    statusLabel.setText(status);
                    peersLabel.setText(peers);
                    speedLabel.setText(speed);
                })
        );

        torrentStatistic.addEventHandler(SHUTDOWN.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            TorrentModel.INSTANCE.unRegisterEventTarget(torrentStatistic);
        });
    }

    private void initializePoster() {
        if (SceneModel.INSTANCE.getActiveMovieModel().getMovie().getPosters() == null) {
            return;
        }
        JsonArray posters = Json.parse(SceneModel.INSTANCE.getActiveMovieModel().getMovie().getPosters()).asArray();
        if (posters.size() > 0) {
            posterView.setImage(new Image(posters.get(0).asString(),
                    0, 0, true, false));
            setPosterDimension();

            FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), posterView);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), posterView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<>() {
                int i = 1;
                @Override
                public void handle(ActionEvent event) {
                    fadeOut.setOnFinished(fadeOutEvent -> {
                        posterView.setImage(new Image(posters.get(i++ % posters.size()).asString(),
                                0, 0, true, false));
                        setPosterDimension();
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            menuPane.widthProperty().addListener((observable, oldValue, newValue) -> setPosterDimension());
            menuPane.heightProperty().addListener((observable, oldValue, newValue) -> setPosterDimension());
        }
    }

    private void setPosterDimension() {
        double imageWidthToHeight = posterView.getImage().getWidth() / posterView.getImage().getHeight();
        double sceneWidthToHeight = menuPane.getScene().getWidth() / (menuPane.getScene().getHeight() - menuPane.getHeight());
        if (imageWidthToHeight < sceneWidthToHeight) {
            posterView.setFitHeight(menuPane.getScene().getHeight() - menuPane.getHeight());
        } else {
            posterView.setFitWidth(menuPane.getWidth());
        }
    }
}
