package com.cinema.core.model.impl;

import bt.metainfo.Torrent;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.Filter;
import com.cinema.core.service.bt.BtClientService;
import com.cinema.core.service.parser.MagnetParser;
import com.cinema.javafx.controller.RootController;

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
        this.filter = filter;
        if (filter.isMagnet()) {
            RootController.EXECUTOR_SERVICE.submit(() -> {
                Torrent torrent = BtClientService.INSTANCE.downloadTorrent(filter.getTitle());
                Movie movie = MagnetParser.INSTANCE.parse(torrent);
                movie.setMagnet(new Magnet(filter.getTitle()));
                MovieModel.save(movie);
            });
        }
    }
}
