package com.cinema.core.service.bt.selectors.impl;

import bt.metainfo.TorrentFile;
import com.cinema.core.entity.Magnetize;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.service.bt.selectors.AbstractFileSelector;

public class MovieFileSelector extends AbstractFileSelector {

    private final MovieModel movieModel = MovieModel.INSTANCE;

    public MovieFileSelector(Movie movie) {
        super(movie);
    }

    @Override
    protected void update(Magnetize magnetize) {
        movieModel.update((Movie) magnetize);
    }

    @Override
    protected boolean isValidFile(TorrentFile file) {
        return true;
    }
}
