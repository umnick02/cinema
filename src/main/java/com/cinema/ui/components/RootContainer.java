package com.cinema.ui.components;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

import java.util.Set;

public class RootContainer extends AnchorPane {
    private final TopMenuContainer topMenuContainer;
    private final ContentContainer contentContainer;

    public RootContainer() {
        super();
        topMenuContainer = new TopMenuContainer();
        contentContainer = new ContentContainer();
        getChildren().addAll(topMenuContainer, contentContainer);
    }

    public MenuBar getTopMenuContainer() {
        return topMenuContainer;
    }

    public ContentContainer getContentContainer() {
        return contentContainer;
    }

    public SideMenuContainer getSideMenuContainer() {
        return contentContainer.getSideMenuContainer();
    }

    public ScrollCardsContainer getScrollCardsContainer() {
        return contentContainer.getScrollCardsContainer();
    }

    public CardsContainer getCardsContainer() {
        return getScrollCardsContainer().getCardsContainer();
    }

    public Set<CardContainer> getCardContainers() {
        return getCardsContainer().getCards();
    }
}
