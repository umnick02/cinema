package com.cinema.presenter;

import com.cinema.model.Filter;

public interface Presentable {
    void setGenres();
    void setMovies(Filter filter);
}
