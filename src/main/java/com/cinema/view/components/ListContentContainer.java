package com.cinema.view.components;

import com.cinema.view.UIBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.AnchorPane;

@Singleton
public class ListContentContainer extends AnchorPane {
    ScrollCardsContainer scrollCardsContainer;
    @Inject
    public ListContentContainer(MenuContentContainer menuContentContainer, ScrollCardsContainer scrollCardsContainer) {
        super(menuContentContainer, scrollCardsContainer);
        this.scrollCardsContainer = scrollCardsContainer;
        UIBuilder.setAnchor(this, 0d, 0d, 0d, 0d);
    }
}
