package com.cinema.javafx.controller;

import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.service.bt.BtClientService;
import com.cinema.javafx.controller.player.PosterController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;

import java.io.IOException;

import static com.cinema.core.model.ModelEventType.*;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class RootController {

    @FXML
    public StackPane contentPane;

    @FXML
    public Button backButton;

    @FXML
    void initialize() {
        contentPane.addEventHandler(TRAILER_PLAY.getEventType(), play -> {
            WebView trailerView = new WebView();
            trailerView.getEngine().load(SceneModel.INSTANCE.getActiveMovieModel().getMovie().getTrailerRu());
            trailerView.addEventHandler(SHUTDOWN.getEventType(), shutdown -> {
                SceneModel.INSTANCE.unRegisterEventTarget(trailerView);
                trailerView.getEngine().load(null);
            });
            contentPane.getChildren().add(trailerView);
        });

        contentPane.addEventHandler(MOVIE_PLAY.getEventType(), play -> {
            MovieModel movieModel = SceneModel.INSTANCE.getActiveMovieModel();
            try {
                Node pane;
                if (movieModel.isPlayable()) {
                    pane = (Node) play.getSource();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/player/player-loading.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(PosterController.class)) {
                            return new PosterController(contentPane.getScene());
                        }
                        return null;
                    });
                    pane = loader.load();
                }
                contentPane.getChildren().add(pane);

                pane.addEventHandler(SHUTDOWN.getEventType(), shutdown -> {
                    SceneModel.INSTANCE.unRegisterEventTarget(pane);
                    BtClientService.INSTANCE.stop();
                });
                SceneModel.INSTANCE.registerEventTarget(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!movieModel.isDownloaded()) {
                BtClientService.INSTANCE.downloadTorrentFiles(movieModel.getMovie());
            }
        });

        backButton.addEventHandler(MOUSE_CLICKED, event -> {
            int size = contentPane.getChildren().size();
            if (size > 1) {
                Node node = contentPane.getChildren().get(size - 1);
                node.fireEvent(new Event(SHUTDOWN.getEventType()));
                contentPane.getChildren().remove(node);
            }
        });
    }
}
