package com.cinema.view.components;

import com.cinema.controller.SearchController;
import javafx.scene.layout.AnchorPane;

public class SearchContainer {

    private SearchController controller;
    private AnchorPane container;

    public SearchContainer(SearchController controller, AnchorPane container) {
        this.controller = controller;
        this.container = container;
    }
}
