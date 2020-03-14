package com.cinema.controller;

import com.cinema.view.components.SearchContainer;
import javafx.scene.layout.AnchorPane;

public class SearchController {

    private SearchContainer searchContainer;

    public SearchController(AnchorPane searchPane) {
        searchContainer = new SearchContainer(this, searchPane);
    }

//    public void search() {
//        String text = searchable.getText().strip();
//        Set<Movie> movies;
//        if (text.startsWith("magnet:")) {
//            searchable.showLoading();
//            EXECUTORS.submit(() -> {
//                Movie movie = new Movie();
//                movie.setMagnet(text);
//                Torrent torrent = btService.fillMovie(movie);
//                movie = magnetParser.parse(movie.getMagnet(), torrent);
//                Movie m = movieModel.processMovieFromMagnet(movie);
//                Platform.runLater(() -> {
//                    searchable.hideLoading();
//                    searchable.showMovies(Set.of(m));
//                });
//            });
//        } else {
//            movies = movieModel.findByTitleLike(text);
//            searchable.showMovies(movies);
//        }
//    }
//
//    public void cancelSearch() {
//        if (btService.stop()) {
//            Platform.runLater(searchable::hideLoading);
//        }
//    }
}
