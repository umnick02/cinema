package com.cinema.javafx.controller;

import com.cinema.javafx.view.components.CardsContainer;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.MovieModel;
import javafx.scene.layout.TilePane;

import java.util.*;

public class CardsController {

    public CardsContainer cardsContainer;
    private final MovieModel movieModel = MovieModel.INSTANCE;
    private RootController rootController;

    public CardsController(RootController rootController, TilePane container) {
        this.rootController = rootController;
        this.cardsContainer = new CardsContainer(this, container);
    }

    public RootController getRootController() {
        return rootController;
    }

    public void resetMovies(Set<Movie> movies) {
        cardsContainer.resetCards(movies);
    }
}
