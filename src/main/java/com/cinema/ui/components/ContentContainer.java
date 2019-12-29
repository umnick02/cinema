package com.cinema.ui.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

@Singleton
public class ContentContainer extends SplitPane {
    @Inject
    public ContentContainer(SideMenuContainer sideMenuContainer, ScrollCardsContainer scrollCardsContainer) {
        super();
        setDividerPosition(0, 0.15);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 25d);
        AnchorPane.setBottomAnchor(this, 0d);
        getItems().addAll(sideMenuContainer.getWrapper(), scrollCardsContainer.getWrapper());
    }
}
