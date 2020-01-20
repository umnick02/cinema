package com.cinema.presenter;

import com.cinema.entity.Movie;
import com.cinema.model.Filter;
import com.cinema.model.MovieModel;
import com.cinema.view.Viewable;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Set;

import static com.cinema.CinemaApplication.INJECTOR;

public class ContentPresenter implements ContentPresentable {
    private final Viewable view;
    private final MovieModel movieModel = INJECTOR.getInstance(MovieModel.class);

    @Inject
    public ContentPresenter(Viewable view) {
        this.view = view;
    }

    @Override
    public void setGenres() {
        Map<String, Integer> genres = movieModel.getGenres();
        view.showGenres(genres);
    }

    @Override
    public void setMovies() {
        Filter filter = view.getFilter();
        Set<Movie> movies = movieModel.getMovies(filter);
        view.showMovies(movies);
    }
}
