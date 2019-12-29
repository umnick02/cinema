package com.cinema.ui.components;

import com.cinema.entity.Movie;
import com.cinema.service.MovieService;
import com.cinema.ui.Anchorable;
import com.cinema.ui.UIBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.util.Set;

@Singleton
public class ScrollCardsContainer extends ScrollPane implements Anchorable {
    private final AnchorPane wrapper;
    private int page = 0;
    private final int cardsPerPage = 10;

    @Inject
    public ScrollCardsContainer(CardsContainer cardsContainer, MovieService movieService) {
        super();
        setContent(cardsContainer.getWrapper());
        Set<Movie> movies = movieService.getMovies(page++, cardsPerPage);
        cardsContainer.addCards(movies);
        wrapper = UIBuilder.wrapNodeToAnchor(this);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
