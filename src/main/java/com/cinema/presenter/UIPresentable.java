package com.cinema.presenter;

import com.cinema.model.Filter;

public interface UIPresentable {
    void setGenres();
    void setMovies(Filter filter);
}
