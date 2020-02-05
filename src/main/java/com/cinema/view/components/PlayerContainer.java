package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.presenter.PlayerPresentable;
import com.cinema.view.Playable;
import com.cinema.view.player.FxPlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.cinema.config.Config.getPreference;
import static com.cinema.view.components.RootContainer.buildAnchorPane;

@Singleton
public class PlayerContainer implements Playable {

    StackPane stackPane = buildStackPane();
    AnchorPane anchorPane = buildAnchorPane(stackPane);
    private final VBox loadingView = buildLoadingView();
    private FxPlayer fxPlayer = new FxPlayer();

    PlayerPresentable playerPresentable;

    @Inject
    PlayerContainer(PlayerPresentable playerPresentable) {
        this.playerPresentable = playerPresentable;
    }

    private StackPane buildStackPane() {
        StackPane stackPane = new StackPane();
        return stackPane;
    }

    @Override
    public void showLoadingView() {
        Platform.runLater(() -> {
            if (!stackPane.getChildren().contains(loadingView)) {
                stackPane.getChildren().add(loadingView);
            }
        });
    }

    private VBox buildLoadingView() {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        final ProgressIndicator progressIndicator = new ProgressIndicator();
        hBox.getChildren().add(progressIndicator);
        vBox.getChildren().add(hBox);
        return vBox;
    }

    @Override
    public void hideLoadingView() {
        stackPane.getChildren().remove(loadingView);
    }

    @Override
    public void showPlayer() {
        Platform.runLater(() -> {
            hideLoadingView();
            if (!stackPane.getChildren().contains(fxPlayer.stackPane)) {
                stackPane.getChildren().add(fxPlayer.stackPane);
            }
        });
    }

    @Override
    public void hidePlayer() {
        stackPane.getChildren().remove(fxPlayer.stackPane);
    }

    @Override
    public void play(String file) {
        fxPlayer.getEmbeddedMediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);
    }
}
