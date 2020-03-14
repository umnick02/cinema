package com.cinema.controller;

import com.cinema.entity.Movie;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.util.Set;

public class MainController {

    @FXML
    public AnchorPane main;

    @FXML
    public TilePane cardsContainer;

    @FXML
    public AnchorPane searchPane;

    private CardsController cardsController;
    private SearchController searchController;
    private FilterController filterController;

    @FXML
    void initialize() {
        cardsController = new CardsController(cardsContainer);
        searchController = new SearchController(searchPane);
        filterController = new FilterController(null);

        resetMovies();
    }

    private void resetMovies() {
        Set<Movie> movies = filterController.getMovies();
        cardsController.resetMovies(movies);
    }
}
