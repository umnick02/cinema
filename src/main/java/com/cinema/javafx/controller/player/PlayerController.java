package com.cinema.javafx.controller.player;

import com.cinema.javafx.model.PlayerModel;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cinema.core.model.ModelEventType.SHUTDOWN;

public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @FXML
    private StackPane playerPane;
    @FXML
    private ImageView videoImageView;

    @FXML
    public void initialize() {
        PlayerModel.INSTANCE.setVideoImageView(videoImageView);
        PlayerModel.INSTANCE.play();

        videoImageView.fitWidthProperty().bind(playerPane.widthProperty());
        videoImageView.fitHeightProperty().bind(playerPane.heightProperty());

        PlayerModel.INSTANCE.registerEventTarget(playerPane);
        playerPane.addEventHandler(SHUTDOWN.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            PlayerModel.INSTANCE.unRegisterEventTarget(playerPane);
            PlayerModel.INSTANCE.stop();
        });
    }
}
