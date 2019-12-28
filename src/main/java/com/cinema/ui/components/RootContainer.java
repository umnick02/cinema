package com.cinema.ui.components;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class RootContainer extends AnchorPane {
    @Autowired
    private TopMenuContainer topMenuContainer;
    @Autowired
    private ContentContainer contentContainer;

    @PostConstruct
    public void postConstruct() {
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
