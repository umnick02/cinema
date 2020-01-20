package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

@Singleton
public class RootContainer {
    private StackPane stackPane = buildStackPane();
    private AnchorPane anchorPane = buildAnchorPane(stackPane);

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
    //    private final UIPresentable uiPresenter;
//    private final PlayerPresentable playerPresentable;
//    SplitContainer splitContainer;
//    FxPlayer fxPlayer;
//    private final VBox loadingView = new VBox();

    @Inject
    public RootContainer(SplitContainer splitContainer) {
        stackPane.getChildren().add(splitContainer.splitPane);
//        this.splitContainer = splitContainer;
//        this.fxPlayer = fxPlayer;
//        uiPresenter = new UIPresenter(this);
//        playerPresentable = new PlayerPresenter(this);
    }

    private StackPane buildStackPane() {
        StackPane stackPane = new StackPane();
        return stackPane;
    }

    private AnchorPane buildAnchorPane(StackPane stackPane) {
        AnchorPane anchorPane = new AnchorPane(stackPane);
        AnchorPane.setBottomAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setTopAnchor(stackPane, 0d);
        return anchorPane;
    }

//    public void setGenres() {
//        uiPresenter.setGenres();
//    }
//
//    public void setMovies() {
//        uiPresenter.setMovies(new Filter());
//    }
//
//    @Override
//    public void setMovies(Set<Movie> movies) {
//        splitContainer.listContentContainer.scrollCardsContainer.cardsContainer.resetCards(movies);
//    }
//
//    @Override
//    public void setGenres(Map<String, Integer> genres) {
//        for (String genre : genres.keySet()) {
//            splitContainer.sideMenuContainer.root.getChildren().add(new SideMenuContainer.GenreItem(genre, genres.get(genre)));
//        }
//        splitContainer.sideMenuContainer.getSelectionModel().selectedItemProperty().addListener(e -> {
//            Filter filter = new Filter();
//            filter.setGenresIncl(new String[] {splitContainer.sideMenuContainer.getSelectionModel().getSelectedItem().getValue().split(" ")[0]});
//            uiPresenter.setMovies(filter);
//        });
//    }
//
//    private void initLoadingView() {
//        UIBuilder.setAnchor(loadingView, 0d, 0d, 0d, 0d);
//        HBox hBox = new HBox();
//        loadingView.getChildren().add(hBox);
//        loadingView.setAlignment(Pos.CENTER);
//        hBox.setAlignment(Pos.CENTER);
//        final ProgressIndicator progressIndicator = new ProgressIndicator();
//        hBox.getChildren().add(progressIndicator);
//    }
//
//    @Override
//    public void showLoadingView() {
//        Platform.runLater(() -> {
//            if (!stackPane.getChildren().contains(loadingView)) {
//                stackPane.getChildren().add(loadingView);
//            }
//        });
//    }
//
//    @Override
//    public void hideLoadingView() {
//        stackPane.getChildren().remove(loadingView);
//    }
//
//    @Override
//    public void showPlayer() {
//        Platform.runLater(() -> {
//            hideLoadingView();
//            if (!stackPane.getChildren().contains(fxPlayer)) {
//                stackPane.getChildren().add(fxPlayer);
//            }
//        });
//    }
//
//    @Override
//    public void hidePlayer() {
//        stackPane.getChildren().remove(fxPlayer);
//    }
//
//    @Override
//    public void play(String file) {
//        fxPlayer.getEmbeddedMediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);
//    }
}
