package com.cinema.javafx.controller;

import com.cinema.core.entity.Magnetize;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.service.bt.BtClientService;
import com.cinema.javafx.controller.player.PlayerController;
import com.cinema.javafx.controller.player.PlayerControlsController;
import com.cinema.javafx.controller.player.PosterController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cinema.core.model.ModelEventType.*;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class RootController {

    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(8);

    @FXML
    public StackPane rootPane;

    @FXML
    public StackPane contentPane;

    @FXML
    private BorderPane menuPane;

    @FXML
    public Button backButton;

    private Node pane;

    @FXML
    void initialize() {
        contentPane.addEventHandler(TRAILER_PLAY.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            try {
                WebView trailerView = FXMLLoader.load(getClass().getResource("/view/movie/trailer.fxml"));
                trailerView.getEngine().load(SceneModel.INSTANCE.getActiveMovieModel().getMovie().getTrailer());
                trailerView.addEventHandler(SHUTDOWN.getEventType(), shutdown -> {
                    logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
                    SceneModel.INSTANCE.unRegisterEventTarget(trailerView);
                    trailerView.getEngine().load(null);
                });
                contentPane.getChildren().add(trailerView);
            } catch (IOException e) {
                logger.error("", e);
            }
        });

        rootPane.addEventHandler(MOVIE_PLAY.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            MovieModel movieModel = SceneModel.INSTANCE.getActiveMovieModel();
            try {
                if (pane != null) {
                    rootPane.getChildren().remove(pane);
                }
                if (movieModel.isPlayable()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/player/player.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(PlayerController.class)) {
                            return new PlayerController();
                        } else if (param.isAssignableFrom(PlayerControlsController.class)) {
                            return new PlayerControlsController();
                        }
                        return null;
                    });
                    pane = loader.load();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/player/player-loading.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(PosterController.class)) {
                            return new PosterController(menuPane);
                        }
                        return null;
                    });
                    pane = loader.load();
                }
                pane.addEventHandler(TORRENT_START.getEventType(), start -> {
                    logger.info("Handle event {} from source {} on target {}", start.getEventType(), start.getSource(), start.getTarget());
                    if (!movieModel.isDownloaded()) {
                        Magnetize magnetize = movieModel.isSeries() ? movieModel.getActiveSeasonModel().getActiveEpisodeModel().getEpisode() : movieModel.getMovie();
                        EXECUTOR_SERVICE.submit(() -> BtClientService.INSTANCE.downloadTorrentFiles(magnetize));
                    }
                });
                pane.addEventHandler(SHUTDOWN.getEventType(), shutdown -> {
                    logger.info("Handle event {} from source {} on target {}", shutdown.getEventType(), shutdown.getSource(), shutdown.getTarget());
                    SceneModel.INSTANCE.unRegisterEventTarget(pane);
                    EXECUTOR_SERVICE.submit(BtClientService.INSTANCE::stop);
                });
                SceneModel.INSTANCE.registerEventTarget(pane);
                rootPane.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        backButton.addEventHandler(MOUSE_CLICKED, event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            int size = contentPane.getChildren().size();
            if (size > 1) {
                Node node = contentPane.getChildren().get(size - 1);
                node.fireEvent(new Event(SHUTDOWN.getEventType()));
                contentPane.getChildren().remove(node);
            }
        });
    }
}
