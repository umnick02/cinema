package com.cinema.javafx.controller;

import com.cinema.core.entity.Movie;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;

import java.util.Set;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class RootController {

    @FXML
    public StackPane content;

    @FXML
    public TilePane cardsContainer;

    @FXML
    public AnchorPane searchPane;

    @FXML
    public Button back;

    private CardsController cardsController;
    private SearchController searchController;
    private FilterController filterController;

    @FXML
    void initialize() {
        cardsController = new CardsController(this, cardsContainer);
        searchController = new SearchController(searchPane);
        filterController = new FilterController(null);
        back.addEventHandler(MOUSE_CLICKED, event -> {
            int size = content.getChildren().size();
            if (size > 1) {
                Node node = content.getChildren().get(size - 1);
                if (node instanceof WebView) {
                    stopTrailer((WebView) node);
                }
                content.getChildren().remove(size - 1);
            }
        });
        resetMovies();
    }

    private void resetMovies() {
        Set<Movie> movies = filterController.getMovies();
        cardsController.resetMovies(movies);
    }

    public void runTrailer(String url) {
        WebView webView = new WebView();
        webView.getEngine().load(url);
        content.getChildren().add(webView);
    }

    public void runPlayer(Movie movie) {
        WebView webView = new WebView();
//        webView.getEngine().load(url);
        content.getChildren().add(webView);
    }

    public void stopTrailer(WebView webView) {
        webView.getEngine().load(null);
    }
}
