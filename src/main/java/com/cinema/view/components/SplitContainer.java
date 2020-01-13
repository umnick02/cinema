package com.cinema.view.components;

import com.cinema.view.UIBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;

@Singleton
public class SplitContainer extends SplitPane {
    SideMenuContainer sideMenuContainer;
    ListContentContainer listContentContainer;

    @Inject
    SplitContainer(SideMenuContainer sideMenuContainer, ListContentContainer listContentContainer) {
        super(sideMenuContainer.getWrapper(), listContentContainer);
        this.sideMenuContainer = sideMenuContainer;
        this.listContentContainer = listContentContainer;
        setDividerPosition(0, 0.15);
        UIBuilder.setAnchor(this, 0d, 0d, 0d, 0d);
    }
}
