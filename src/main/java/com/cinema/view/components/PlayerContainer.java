package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.presenter.PlayerPresentable;
import com.cinema.presenter.PlayerPresenter;
import com.cinema.view.Playable;
import com.cinema.view.player.FxPlayer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.cinema.CinemaApplication.INJECTOR;
import static com.cinema.config.Config.getPreference;
import static com.cinema.view.components.RootContainer.buildAnchorPane;

public class PlayerContainer implements Playable {

    private StackPane parent;
    private StackPane child;
    AnchorPane anchorPane;
    private final VBox loadingView = buildLoadingView();
    private FxPlayer fxPlayer;

    PlayerPresentable playerPresentable;

    PlayerContainer(StackPane stackPane) {
        parent = stackPane;
        child = buildStackPane();
        anchorPane = buildAnchorPane(child);
        fxPlayer = new FxPlayer(child);
        playerPresentable = new PlayerPresenter(this);
    }

    private StackPane buildStackPane() {
        StackPane stackPane = new StackPane();
        return stackPane;
    }

    @Override
    public void showLoadingView() {
        Platform.runLater(() -> {
            if (!parent.getChildren().contains(loadingView)) {
                parent.getChildren().add(loadingView);
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
        parent.getChildren().remove(loadingView);
    }

    @Override
    public void showPlayer() {
        Platform.runLater(() -> {
            hideLoadingView();
            if (!parent.getChildren().contains(fxPlayer.stackPane)) {
                parent.getChildren().add(fxPlayer.stackPane);
            }
        });
    }

    @Override
    public void hidePlayer() {
        parent.getChildren().remove(fxPlayer.stackPane);
    }

    @Override
    public void play(String file) {
        fxPlayer.getEmbeddedMediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);
    }
}
