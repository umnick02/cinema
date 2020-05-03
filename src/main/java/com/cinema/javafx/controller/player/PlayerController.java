package com.cinema.javafx.controller.player;

import com.cinema.javafx.model.PlayerModel;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class PlayerController {

    @FXML
    private BorderPane playerPane;
    @FXML
    private ImageView videoImageView;

    @FXML
    public void initialize() {
        PlayerModel.INSTANCE.setVideoImageView(videoImageView);
        PlayerModel.INSTANCE.play();
        videoImageView.fitWidthProperty().bind(playerPane.widthProperty());
        videoImageView.fitHeightProperty().bind(playerPane.heightProperty());
    }
}
