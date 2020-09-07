package com.cinema.javafx.controller.subtitle;

import com.cinema.core.model.impl.SubtitleModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

import static com.cinema.core.model.ModelEventType.SUBTITLE_SHOW;

public class SubtitleController {

    @FXML
    private FlowPane subtitlePane;

    private SubtitleModel subtitleModel = SubtitleModel.INSTANCE;

    @FXML
    public void initialize() {
        subtitleModel.registerEventTarget(subtitlePane);
        subtitlePane.addEventHandler(SUBTITLE_SHOW.getEventType(), event -> {
            Platform.runLater(() -> {
                System.out.println(event);
            });
        });
    }
}
