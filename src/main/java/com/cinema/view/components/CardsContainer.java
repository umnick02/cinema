package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.view.Anchorable;
import com.cinema.view.UIBuilder;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.util.*;

@Singleton
public class CardsContainer extends TilePane implements Anchorable {

    private final AnchorPane wrapper;
    private final Set<CardContainer> cards = new HashSet<>();

    public CardsContainer() {
        super();
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        wrapper = UIBuilder.wrapNodeToAnchor(this);
    }

    public void addCards(Set<Movie> movies) {
        int fromCard = cards.size();
        movies.forEach(m -> cards.add(new CardContainer(m)));
        showCards(fromCard);
    }

    private void removeCards() {
        getChildren().clear();
        cards.clear();
    }

    public void resetCards(Set<Movie> movies) {
        removeCards();
        addCards(movies);
    }

    private void showCards(int fromCard) {
        Iterator<CardContainer> iterator = cards.iterator();
        int i = fromCard;
        int j = 0;
        while (iterator.hasNext()) {
            if (i < j++) continue;
            getChildren().add(iterator.next());
            i++;
        }
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
