package com.cinema.ui.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class ScrollCardsContainer extends ScrollPane implements Anchorable {
    private final CardsContainer cardsContainer;
    private final AnchorPane wrapper;

    public ScrollCardsContainer() {
        super();
        cardsContainer = new CardsContainer();
        wrapper = SceneBuilder.wrapNodeToAnchor(this);
        getChildren().addAll(cardsContainer.getWrapper());
    }

    public CardsContainer getCardsContainer() {
        return cardsContainer;
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
