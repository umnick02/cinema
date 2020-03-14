package com.cinema.view.components;

import com.cinema.controller.FilterController;
import com.cinema.model.Filter;
import javafx.scene.layout.Pane;

public class FilterContainer {

    private FilterController controller;
    private Pane container;

    public FilterContainer(FilterController controller, Pane container) {
        this.container = container;
        this.controller = controller;
    }

    public Filter getFilter() {
        Filter filter = new Filter();
        return filter;
    }
}
