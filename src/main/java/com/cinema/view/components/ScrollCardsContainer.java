package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

@Singleton
public class ScrollCardsContainer {
    ScrollPane scrollPane;
    final CardsContainer cardsContainer;

    @Inject
    public ScrollCardsContainer(CardsContainer cardsContainer) {
        super();
        this.cardsContainer = cardsContainer;
        scrollPane = buildScrollPane();
        scrollPane.setContent(cardsContainer.anchorPane);
    }

    private ScrollPane buildScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        AnchorPane.setTopAnchor(scrollPane, 30d);
        AnchorPane.setLeftAnchor(scrollPane, 0d);
        AnchorPane.setRightAnchor(scrollPane, 0d);
        AnchorPane.setBottomAnchor(scrollPane, 0d);
        return scrollPane;
    }
}
