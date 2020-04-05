//package com.cinema.core.presenter;
//
//import com.cinema.core.entity.Movie;
//import com.cinema.core.model.Filter;
//import com.cinema.core.model.MovieModel;
//import com.cinema.core.view.Viewable;
//import com.google.inject.Inject;
//
//import java.util.Map;
//import java.util.Set;
//
//public class ContentPresenter implements ContentPresentable {
//    private final Viewable view;
//    private final MovieModel movieModel = INJECTOR.getInstance(MovieModel.class);
//
//    @Inject
//    public ContentPresenter(Viewable view) {
//        this.view = view;
//    }
//
//    @Override
//    public void setGenres() {
//        Map<String, Integer> genres = movieModel.getGenres();
//        view.showGenres(genres);
//    }
//
//    @Override
//    public void setMovies() {
//        Filter filter = view.getFilter();
//        Set<Movie> movies = movieModel.getMovies(filter);
//        view.showMovies(movies);
//    }
//}
