//package com.cinema.javafx.view.builder;
//
//import com.cinema.core.entity.Movie;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TabPane;
//import javafx.scene.image.Image;
//import javafx.scene.layout.*;
//
//public class MovieBuilder {
//
//    public static HBox buildMovie(Movie movie) throws Exception {
//        HBox movie = FXMLLoader.load(MovieBuilder.class.getResource("/view/movie.fxml"));
//
////        setDimension(movie);
////        setPoster(movie, movie.getPoster());
//        if (movie.getType() != Movie.Type.SERIES) {
//            ((TabPane)((AnchorPane) movie.getChildren().get(1)).getChildren().get(0)).getTabs().get(2).setStyle("visibility: hidden;");
//        }
//        Button trailer = (Button)((AnchorPane)((BorderPane)((TabPane)((AnchorPane) movie.getChildren().get(1)).getChildren().get(0)).getTabs().get(0).getContent()).getBottom()).getChildren().get(0);
////        trailer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> moviesController.getRootController().runTrailer(movie.getTrailer()));
//        Button play = (Button)((AnchorPane)((BorderPane)((TabPane)((AnchorPane) movie.getChildren().get(1)).getChildren().get(0)).getTabs().get(0).getContent()).getBottom()).getChildren().get(1);
////        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> moviesController.getRootController().runPlayer(movie));
//        TabPane tabPane = (TabPane)((AnchorPane) movie.getChildren().get(1)).getChildren().get(0);
//        tabPane.getSelectionModel().selectedIndexProperty()
//                .addListener((observableValue, oldValue, newValue) -> selectTab(movie, movie, Tabs.values()[newValue.intValue()]));
//        selectTab(movie, movie, Tabs.DETAILS);
//        return movie;
//    }
//
//    private static void selectTab(HBox movie, Movie movie, Tabs tab) {
//        TabPane tabPane = (TabPane) ((AnchorPane) movie.getChildren().get(1)).getChildren().get(0);
//        tabPane.getSelectionModel().select(tab.position);
//        switch (tab) {
//            case DETAILS:
//                if (((VBox)((BorderPane) tabPane.getTabs().get(tab.position).getContent()).getCenter()).getChildren().size() == 0) {
//                    DescriptionTabBuilder.renderDetails(tabPane.getTabs().get(tab.position), movie);
//                }
//                break;
//            case DESCRIPTION:
//                if (((VBox)((ScrollPane) tabPane.getTabs().get(tab.position).getContent()).getContent()).getChildren().size() == 0) {
////                    DescriptionTabBuilder.renderDescription(tabPane.getTabs().get(tab.position), movie);
//                }
//                break;
//            case EPISODES:
//                StackPane stackPane = (StackPane) tabPane.getTabs().get(tab.position).getContent();
//                if (stackPane.getChildren().size() > 1) {
//                    stackPane.getChildren().remove(1);
//                }
//                if (((TilePane)((ScrollPane)((StackPane) tabPane.getTabs().get(tab.position).getContent()).getChildren().get(0)).getContent()).getChildren().size() == 0) {
//                    DescriptionTabBuilder.renderSeasons(tabPane.getTabs().get(tab.position), movie);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
////    public void startTrailer(String url) {
////        WebView webView = new WebView();
////        webView.getEngine().load(url);
////        content.getChildren().add(webView);
////    }
////
////    public void stopTrailer(WebView webView) {
////        webView.getEngine().load(null);
////    }
////
////    public void runPlayer(Movie movie) {
////        content.getChildren().add(FxPlayer.getBorderPane());
////        FxPlayer.getEmbeddedMediaPlayer().media().play(movie.getFile());
////    }
////
////    public void stopPlayer(Movie movie) {
////        FxPlayer.getEmbeddedMediaPlayer().media().play((String) null);
////        content.getChildren().remove(FxPlayer.getBorderPane());
////    }
//
////    private static void setDimension(HBox movie) {
////        movie.setMinSize(width * ratio, width * 1.4);
////        movie.setMaxSize(width * ratio, width * 1.4);
////        StackPane posterPane = (StackPane) movie.getChildren().get(0);
////        posterPane.setMinSize(width, movie.getMinHeight());
////        posterPane.setMaxSize(width, movie.getMaxHeight());
////        AnchorPane detailsPane = (AnchorPane) movie.getChildren().get(1);
////        detailsPane.setMinSize(movie.getMinWidth() - width, movie.getMinHeight());
////        detailsPane.setMaxSize(movie.getMaxWidth() - width, movie.getMaxHeight());
////    }
////
////    private static void setPoster(HBox movie, String poster) {
////        StackPane posterPane = (StackPane) movie.getChildren().get(0);
////        Image image = new Image(poster, width, -1, true, false);
////        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
////        posterPane.setBackground(new Background(backgroundImage));
////    }
//
//    private enum Tabs {
//        DETAILS(0, "Детали"), DESCRIPTION(1, "Описание"), EPISODES(2, "Эпизоды");
//
//        private int position;
//        private String value;
//
//        Tabs(int position, String value) {
//            this.value = value;
//            this.position = position;
//        }
//
//        @Override
//        public String toString() {
//            return "Tab{" +
//                    "position=" + position +
//                    ", value='" + value + '\'' +
//                    '}';
//        }
//    }
//}
