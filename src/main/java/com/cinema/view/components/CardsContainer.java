package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.service.bt.BtService;
import com.cinema.view.Anchorable;
import com.cinema.view.UIBuilder;
import com.google.inject.Inject;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.*;

public class CardsContainer extends GridPane implements Anchorable {

    private final AnchorPane wrapper;
    private final Set<CardContainer> cards = new HashSet<>();
    private final BtService btService;

    @Inject
    public CardsContainer(BtService btService) {
        super();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(20);
        getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints, columnConstraints, columnConstraints);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        this.btService = btService;
        wrapper = UIBuilder.wrapNodeToAnchor(this);
    }

    public void addCards(Set<Movie> movies) {
        int fromCard = cards.size();
        movies.forEach(m -> cards.add(new CardContainer(m, btService)));
        showCards(fromCard);
    }

    public Set<CardContainer> getCards() {
        return cards;
    }

    public void resetCards(Set<Movie> movies) {
        Iterator<CardContainer> cardsIterator = cards.iterator();
        Iterator<Movie> movieIterator = movies.iterator();
        Set<Movie> newMovies = null;
        Set<CardContainer> staleCards = null;
        while (movieIterator.hasNext()) {
            Movie movie = movieIterator.next();
            if (cardsIterator.hasNext()) {
                CardContainer card = cardsIterator.next();
                if (!movie.equals(card.getMovie())) {
                    updateCard(card, movie);
                }
            } else {
                if (newMovies == null) newMovies = new HashSet<>();
                newMovies.add(movie);
            }
        }
        if (newMovies != null && newMovies.size() > 0) {
            addCards(newMovies);
        } else {
            while (cardsIterator.hasNext()) {
                if (staleCards == null) staleCards = new HashSet<>();
                staleCards.add(cardsIterator.next());
            }
            if (staleCards != null && staleCards.size() > 0) {
                removeCards(staleCards);
            }
        }
    }

    private void removeCards(Set<CardContainer> cards) {
        for (CardContainer card : cards) {
            this.cards.remove(card);
            getChildren().remove(card);
        }
    }

    private void updateCard(CardContainer card, Movie movie) {
        card.setMovie(movie);
    }

    private void showCards(int fromCard) {
        int cardWidth = Integer.parseInt(Config.getPreference(Config.PrefKey.CARD_WIDTH));
        int itemsInRow = 1000 / cardWidth;
        Iterator<CardContainer> iterator = cards.iterator();
        int i = fromCard;
        int j = 0;
        while (iterator.hasNext()) {
            if (i < j++) continue;
            add(iterator.next(), i % itemsInRow, i / itemsInRow);
            i++;
        }
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
