//package com.cinema.javafx.view.components;
//
//import com.cinema.javafx.player.FxPlayer;
//import com.cinema.presenter.PlayerPresenter;
//import javafx.application.Platform;
//import javafx.geometry.Pos;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//
//public class PlayerContainer implements Playable {
//
//    private StackPane child;
//    private final VBox loadingView;
//    private FxPlayer fxPlayer;
//
//
//    PlayerContainer(StackPane stackPane) {
//        loadingView = buildLoadingView();
//        child = buildStackPane();
//        fxPlayer = new FxPlayer(child);
//        playerPresentable = new PlayerPresenter(this);
//    }
//
//    private StackPane buildStackPane() {
//        StackPane stackPane = new StackPane();
//        return stackPane;
//    }
//
//    @Override
//    public void showLoadingView() {
//        Platform.runLater(() -> {
//            if (!parent.getChildren().contains(loadingView)) {
//                parent.getChildren().add(loadingView);
//            }
//        });
//    }
//
//    private VBox buildLoadingView() {
//        VBox vBox = new VBox();
//        HBox hBox = new HBox();
//        vBox.setAlignment(Pos.CENTER);
//        hBox.setAlignment(Pos.CENTER);
//        final ProgressIndicator progressIndicator = new ProgressIndicator();
//        hBox.getChildren().add(progressIndicator);
//        vBox.getChildren().add(hBox);
//        return vBox;
//    }
//
//    @Override
//    public void hideLoadingView() {
//        parent.getChildren().remove(loadingView);
//    }
//
////    @Override
////    public void play(String file) {
////        fxPlayer.getEmbeddedMediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);
////    }
//}
