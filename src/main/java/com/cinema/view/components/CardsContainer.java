package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.google.inject.Singleton;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.util.*;

@Singleton
public class CardsContainer {

    TilePane tilePane;
    AnchorPane anchorPane;
    private final Set<CardContainer> cards = new HashSet<>();

    public CardsContainer() {
        tilePane = buildTilePane();
        anchorPane = new AnchorPane(tilePane);
    }

    private TilePane buildTilePane() {
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPadding(new Insets(25, 25, 25, 25));
        AnchorPane.setTopAnchor(tilePane, 0d);
        AnchorPane.setBottomAnchor(tilePane, 0d);
        AnchorPane.setRightAnchor(tilePane, 0d);
        AnchorPane.setLeftAnchor(tilePane, 0d);
        return tilePane;
    }

    public void addCards(Set<Movie> movies) {
        int fromCard = cards.size();
        movies.forEach(m -> cards.add(new CardContainer(m)));
        showCards(fromCard);
    }

    private void removeCards() {
        tilePane.getChildren().clear();
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
            tilePane.getChildren().add(iterator.next().stackPane);
            i++;
        }
    }
}
