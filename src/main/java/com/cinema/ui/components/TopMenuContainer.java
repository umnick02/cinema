package com.cinema.ui.components;

import com.google.inject.Singleton;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

@Singleton
public class TopMenuContainer extends MenuBar {
    public TopMenuContainer() {
        super();
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        getMenus().add(new Menu("File"));
    }
}
