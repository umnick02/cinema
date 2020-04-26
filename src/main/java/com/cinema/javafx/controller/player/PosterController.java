package com.cinema.javafx.controller.player;

import com.cinema.core.model.impl.SceneModel;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PosterController {

    @FXML
    private ImageView posterView;

    private final Scene scene;

    public PosterController(Scene scene) {
        this.scene = scene;
    }

    @FXML
    public void initialize() {
        JsonArray posters = Json.parse(SceneModel.INSTANCE.getActiveMovieModel().getMovie().getPosters()).asArray();
        if (posters.size() > 0) {
            posterView.setImage(new Image(posters.get(0).asString(),
                    -1, -1, true, true));
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
                                -1, -1, true, true));
                        setPosterDimension();
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            scene.widthProperty().addListener((observable, oldValue, newValue) -> setPosterDimension());
            scene.heightProperty().addListener((observable, oldValue, newValue) -> setPosterDimension());
        }
    }

    private void setPosterDimension() {
        double byWidth = scene.getHeight() / posterView.getImage().getHeight() * posterView.getImage().getWidth();
        double byHeight = scene.getWidth() / posterView.getImage().getWidth() * posterView.getImage().getHeight();
        if (byWidth > scene.getWidth()) {
            posterView.setFitHeight(byHeight);
            posterView.setFitWidth(posterView.getFitHeight() / posterView.getImage().getHeight() * posterView.getImage().getWidth());
        } else {
            posterView.setFitWidth(scene.getWidth());
            posterView.setFitHeight(posterView.getFitWidth() / posterView.getImage().getWidth() * posterView.getImage().getHeight());
        }
    }
}
