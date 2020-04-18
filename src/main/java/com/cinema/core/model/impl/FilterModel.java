package com.cinema.core.model.impl;

import com.cinema.core.model.Filter;

public class FilterModel {

    private static Filter filter;

    public static Filter getFilter() {
        if (filter == null) {
            filter = new Filter.Builder().build();
        }
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
