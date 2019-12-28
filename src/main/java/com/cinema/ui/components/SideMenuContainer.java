package com.cinema.ui.components;

import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class SideMenuContainer extends ListView implements Anchorable {
    private final AnchorPane wrapper;
    public SideMenuContainer() {
        super();
        wrapper = SceneBuilder.wrapNodeToAnchor(this);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
