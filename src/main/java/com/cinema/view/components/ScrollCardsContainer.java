package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.view.Anchorable;
import com.cinema.view.UIBuilder;
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
    public ScrollCardsContainer(CardsContainer cardsContainer, MovieModel movieModel) {
        super();
        setContent(cardsContainer.getWrapper());
        Set<Movie> movies = movieModel.getMovies(page++, cardsPerPage);
        cardsContainer.addCards(movies);
        wrapper = UIBuilder.wrapNodeToAnchor(this, 30d);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
