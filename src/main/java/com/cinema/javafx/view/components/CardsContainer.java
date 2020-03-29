package com.cinema.javafx.view.components;

import com.cinema.javafx.controller.CardsController;
import com.cinema.core.entity.Movie;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CardsContainer {

    private CardsController controller;
    private TilePane container;
    private final Set<CardContainer> cards = new HashSet<>();

    public CardsContainer(CardsController controller, TilePane container) {
        this.controller = controller;
        this.container = container;
    }

    public void addCards(Set<Movie> movies) {
        Set<Movie> oldMovies = cards.stream().map(CardContainer::getMovie).collect(Collectors.toSet());
        for (Movie movie : movies) {
            if (!oldMovies.contains(movie)) {
                try {
                    CardContainer cardContainer = new CardContainer(controller, movie);
                    cards.add(cardContainer);
                    container.getChildren().add(cardContainer.getCard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resetCards(Set<Movie> movies) {
        container.getChildren().clear();
        cards.clear();
        addCards(movies);
    }
}
