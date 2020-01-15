package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.model.Filter;
import com.cinema.presenter.PlayerPresentable;
import com.cinema.presenter.PlayerPresenter;
import com.cinema.presenter.UIPresentable;
import com.cinema.presenter.UIPresenter;
import com.cinema.view.Anchorable;
import com.cinema.view.Playable;
import com.cinema.view.UIBuilder;
import com.cinema.view.Viewable;
import com.cinema.view.player.FxPlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Set;

import static com.cinema.config.Config.getPreference;

@Singleton
public class RootContainer extends StackPane implements Viewable, Playable, Anchorable {
    private final UIPresentable uiPresenter;
    private final PlayerPresentable playerPresentable;
    SplitContainer splitContainer;
    FxPlayer fxPlayer;
    private final VBox loadingView = new VBox();
    private final AnchorPane wrapper;

    @Inject
    public RootContainer(SplitContainer splitContainer, FxPlayer fxPlayer) {
        super(splitContainer);
        this.splitContainer = splitContainer;
        this.fxPlayer = fxPlayer;
        uiPresenter = new UIPresenter(this);
        playerPresentable = new PlayerPresenter(this);
        initLoadingView();
        wrapper = UIBuilder.wrapNodeToAnchor(this);
    }

    public void setGenres() {
        uiPresenter.setGenres();
    }

    public void setMovies() {
        uiPresenter.setMovies(new Filter());
    }

    @Override
    public void setMovies(Set<Movie> movies) {
        splitContainer.listContentContainer.scrollCardsContainer.cardsContainer.resetCards(movies);
    }

    @Override
    public void setGenres(Map<String, Integer> genres) {
        for (String genre : genres.keySet()) {
            splitContainer.sideMenuContainer.root.getChildren().add(new SideMenuContainer.GenreItem(genre, genres.get(genre)));
        }
        splitContainer.sideMenuContainer.getSelectionModel().selectedItemProperty().addListener(e -> {
            Filter filter = new Filter();
            filter.setGenresIncl(new String[] {splitContainer.sideMenuContainer.getSelectionModel().getSelectedItem().getValue().split(" ")[0]});
            uiPresenter.setMovies(filter);
        });
    }

    private void initLoadingView() {
        UIBuilder.setAnchor(loadingView, 0d, 0d, 0d, 0d);
        HBox hBox = new HBox();
        loadingView.getChildren().add(hBox);
        loadingView.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        final ProgressIndicator progressIndicator = new ProgressIndicator();
        hBox.getChildren().add(progressIndicator);
    }

    @Override
    public void showLoadingView() {
        Platform.runLater(() -> {
            if (!getChildren().contains(loadingView)) {
                getChildren().add(loadingView);
            }
        });
    }

    @Override
    public void hideLoadingView() {
        getChildren().remove(loadingView);
    }

    @Override
    public void showPlayer() {
        Platform.runLater(() -> {
            hideLoadingView();
            if (!getChildren().contains(fxPlayer)) {
                getChildren().add(fxPlayer);
            }
        });
    }

    @Override
    public void hidePlayer() {
        getChildren().remove(fxPlayer);
    }

    @Override
    public void play(String file) {
        fxPlayer.getEmbeddedMediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
