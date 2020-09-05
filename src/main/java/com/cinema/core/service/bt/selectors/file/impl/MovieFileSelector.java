package com.cinema.core.service.bt.selectors.file.impl;

import bt.metainfo.TorrentFile;
import com.cinema.core.entity.Source;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.service.bt.selectors.file.AbstractFileSelector;

public class MovieFileSelector extends AbstractFileSelector {

    public MovieFileSelector(Movie movie) {
        super(movie);
    }

    @Override
    protected void update(Source source) {
        MovieModel.update((Movie) source);
    }

    @Override
    protected boolean isValidFile(TorrentFile file) {
        return true;
    }
}
