package com.cinema.ui.components;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class ContentContainer extends SplitPane {
    private final SideMenuContainer sideMenuContainer;
    private final ScrollCardsContainer scrollCardsContainer;

    public ContentContainer() {
        super();
        this.sideMenuContainer = new SideMenuContainer();
        this.scrollCardsContainer = new ScrollCardsContainer();
        setDividerPosition(0, 0.15);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 25d);
        AnchorPane.setBottomAnchor(this, 0d);
        getChildren().addAll(sideMenuContainer.getWrapper(), scrollCardsContainer.getWrapper());
    }

    public SideMenuContainer getSideMenuContainer() {
        return sideMenuContainer;
    }

    public ScrollCardsContainer getScrollCardsContainer() {
        return scrollCardsContainer;
    }
}
