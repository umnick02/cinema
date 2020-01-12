package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

@Singleton
public class ScrollCardsContainer extends ScrollPane {
    CardsContainer cardsContainer;

    @Inject
    public ScrollCardsContainer(CardsContainer cardsContainer) {
        super();
        this.cardsContainer = cardsContainer;
        AnchorPane.setTopAnchor(this, 30d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        setContent(cardsContainer.getWrapper());
    }
}
