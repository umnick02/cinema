package com.cinema.core.service.bt.selectors;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.core.config.Config;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.impl.MovieModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class DraftFilesSelector extends TorrentFileSelector {

    private static final Logger logger = LogManager.getLogger(DraftFilesSelector.class);

    private final MovieModel movieModel;
    private Movie movie;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Inject
    public DraftFilesSelector(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @Override
    public List<SelectionResult> selectFiles(List<TorrentFile> files) {
        if (movie != null) {
            if (movie.getType() != Movie.Type.SERIES) {
                if (movie.getFile() == null) {
                    logger.info("File not set. Load all files for movie=[{}]", movie);
                    return files.stream().sorted(Comparator.comparingLong(TorrentFile::getSize)).map(this::select).collect(Collectors.toList());
                } else {
                    File file = new File(Config.getPreference(Config.PrefKey.STORAGE) + movie.getFile());
                    if (!file.exists()) {
                        logger.info("File not exists. Load all files for movie=[{}]", movie);
                        return files.stream().sorted(Comparator.comparingLong(TorrentFile::getSize)).map(this::select).collect(Collectors.toList());
                    }
                    logger.info("Find file=[{}] for movie=[{}]", movie.getFile(), movie);
                }
            } else {
                // TODO first or last episode
            }
        }
        logger.info("Skip all files for movie=[{}]", movie);
        return files.stream().map(f -> SelectionResult.skip()).collect(Collectors.toList());
    }

    @Override
    protected SelectionResult select(TorrentFile file) {
        if (movie != null && movie.getFile() == null) {
            movie.setFile(String.join("/", file.getPathElements()));
            movie.setFileStatus(Movie.FileStatus.UNPLAYABLE);
            logger.info("Set file=[{}] for movie=[{}]", movie.getFile(), movie);
            movieModel.updateMovie(movie);
        }
        return SelectionResult.select().build();
    }
}
