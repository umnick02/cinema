package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.AnchorPane;

@Singleton
public class RootContainer extends AnchorPane {
    @Inject
    public RootContainer(ContentContainer contentContainer) {
        super(contentContainer);
    }
}
