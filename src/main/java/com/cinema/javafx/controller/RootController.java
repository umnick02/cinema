package com.cinema.javafx.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import static com.cinema.core.model.ModelEventType.SHUTDOWN;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class RootController {

    @FXML
    public StackPane contentPane;

    @FXML
    public Button backButton;

    @FXML
    void initialize() {
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
