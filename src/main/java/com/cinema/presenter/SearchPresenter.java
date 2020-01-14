package com.cinema.presenter;

import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.view.Searchable;
import com.google.inject.Inject;

import java.util.Set;

import static com.cinema.CinemaApplication.INJECTOR;

public class SearchPresenter implements SearchPresentable {

    private final Searchable searchable;
    private final MovieModel movieModel = INJECTOR.getInstance(MovieModel.class);

    @Inject
    public SearchPresenter(Searchable searchable) {
        this.searchable = searchable;
    }

    @Override
    public void search() {
        String text = searchable.getText();
        Set<Movie> movies = movieModel.findByTitleLike(text);
        searchable.showResults(movies);
    }
}
