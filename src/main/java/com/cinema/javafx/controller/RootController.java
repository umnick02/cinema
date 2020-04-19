package com.cinema.javafx.controller;

import com.cinema.core.model.impl.SceneModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;

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
            trailerView.addEventHandler(SHUTDOWN.getEventType(), shutdown -> trailerView.getEngine().load(null));
            contentPane.getChildren().add(trailerView);
        });

        contentPane.addEventHandler(MOVIE_PLAY.getEventType(), play -> {
//            tabPane.getSelectionModel().select(seasonsTab);
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
