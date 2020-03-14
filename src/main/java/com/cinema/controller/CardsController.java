package com.cinema.controller;

import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.view.components.CardsContainer;
import javafx.scene.layout.TilePane;

import java.util.*;

public class CardsController {

    public CardsContainer cardsContainer;
    private final MovieModel movieModel = MovieModel.INSTANCE;

    public CardsController(TilePane container) {
        this.cardsContainer = new CardsContainer(this, container);
    }

    public void resetMovies(Set<Movie> movies) {
        cardsContainer.resetCards(movies);
    }
}
