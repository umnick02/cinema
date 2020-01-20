package com.cinema.presenter;

import bt.metainfo.Torrent;
import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.service.bt.BtService;
import com.cinema.service.parser.MagnetParser;
import com.cinema.view.Searchable;
import com.google.inject.Inject;
import javafx.application.Platform;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cinema.CinemaApplication.INJECTOR;

public class SearchPresenter implements SearchPresentable {

    private final Searchable searchable;
    private final BtService btService;
    private final MagnetParser magnetParser;
    private final MovieModel movieModel = INJECTOR.getInstance(MovieModel.class);
    private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(1);

    @Inject
    public SearchPresenter(Searchable searchable, BtService btService, MagnetParser magnetParser) {
        this.searchable = searchable;
        this.btService = btService;
        this.magnetParser = magnetParser;
    }

    @Override
    public void search() {
        String text = searchable.getText().strip();
        Set<Movie> movies;
        if (text.startsWith("magnet:")) {
            searchable.showLoading();
            EXECUTORS.submit(() -> {
                Movie movie = new Movie();
                movie.setMagnet(text);
                Torrent torrent = btService.fillMovie(movie);
                movie = magnetParser.parse(movie.getMagnet(), torrent);
                Movie m = movieModel.processMovieFromMagnet(movie);
                Platform.runLater(() -> {
                    searchable.hideLoading();
                    searchable.showMovies(Set.of(m));
                });
            });
        } else {
            movies = movieModel.findByTitleLike(text);
            searchable.showMovies(movies);
        }
    }

    @Override
    public void cancelSearch() {
        if (btService.stop()) {
            Platform.runLater(searchable::hideLoading);
        }
    }
}
