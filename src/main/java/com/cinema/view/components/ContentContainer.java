package com.cinema.view.components;

import com.cinema.view.UIBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;

@Singleton
public class ContentContainer extends SplitPane {
    @Inject
    public ContentContainer(SideMenuContainer sideMenuContainer, ListContentContainer listContentContainer) {
        super(sideMenuContainer.getWrapper(), listContentContainer);
        setDividerPosition(0, 0.15);
        UIBuilder.setAnchor(this, 0d, 0d, 0d, 0d);
    }
}
