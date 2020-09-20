package com.cinema.core.model.impl;

import bt.metainfo.Torrent;
import com.cinema.core.config.Lang;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.Filter;
import com.cinema.core.service.bt.BtClientService;
import com.cinema.core.service.parser.MagnetParser;
import com.cinema.core.service.parser.SubtitleParser;
import com.cinema.javafx.controller.RootController;

import java.util.Objects;
import java.util.Set;

public class FilterModel {

    public static final FilterModel INSTANCE = new FilterModel();

    private FilterModel() {}

    private static Filter filter;

    public static Filter getFilter() {
        if (filter == null) {
            filter = new Filter.Builder().build();
        }
        return filter;
    }

    public void setFilter(Filter filter) {
        FilterModel.filter = filter;
        if (filter.isMagnet()) {
            RootController.EXECUTOR_SERVICE.submit(() -> {
                Torrent torrent = BtClientService.INSTANCE.downloadTorrent(filter.getTitle());
                Movie movie = MagnetParser.INSTANCE.parse(torrent);
                movie.getSubtitle().setSubtitles(SubtitleParser.buildSubtitles(movie, Lang.EN, Lang.RU, Lang.DE));
                MovieModel.update(movie);
                SceneModel.INSTANCE.setMovies(Set.of(movie));
            });
        } else {
            RootController.EXECUTOR_SERVICE.submit(() -> {
                Movie movie = MovieModel.getMovie(filter.getTitle());
                if (Objects.nonNull(movie)) {
                    movie.getSubtitle().setSubtitles(SubtitleParser.buildSubtitles(movie, Lang.EN, Lang.RU, Lang.DE));
                    MovieModel.update(movie);
                    SceneModel.INSTANCE.setMovies(Set.of(movie));
                }
            });
        }
    }
}
