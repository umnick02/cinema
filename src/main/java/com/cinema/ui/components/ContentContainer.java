package com.cinema.ui.components;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ContentContainer extends SplitPane {
    @Autowired
    private SideMenuContainer sideMenuContainer;
    @Autowired
    private ScrollCardsContainer scrollCardsContainer;

    public ContentContainer() {
        super();
        setDividerPosition(0, 0.15);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 25d);
        AnchorPane.setBottomAnchor(this, 0d);
    }

    @PostConstruct
    public void postConstruct() {
        getItems().addAll(sideMenuContainer.getWrapper(), scrollCardsContainer.getWrapper());
    }

    public SideMenuContainer getSideMenuContainer() {
        return sideMenuContainer;
    }

    public ScrollCardsContainer getScrollCardsContainer() {
        return scrollCardsContainer;
    }
}
