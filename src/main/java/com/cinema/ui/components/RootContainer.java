package com.cinema.ui.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.AnchorPane;

@Singleton
public class RootContainer extends AnchorPane {
    @Inject
    public RootContainer(TopMenuContainer topMenuContainer, ContentContainer contentContainer) {
        super();
        getChildren().addAll(topMenuContainer, contentContainer);
    }
}
