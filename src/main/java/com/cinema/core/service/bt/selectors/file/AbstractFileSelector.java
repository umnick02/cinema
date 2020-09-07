package com.cinema.core.service.bt.selectors.file;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.core.config.Preferences;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.entity.Source;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.model.impl.SceneModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFileSelector extends TorrentFileSelector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFileSelector.class);

    protected Source source;

    protected AbstractFileSelector(Source source) {
        this.source = source;
    }

    private boolean isVideoFile(TorrentFile file) {
        return file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".avi") ||
                file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".mkv");
    }

    private boolean isVideoFile(Source source, TorrentFile file) {
        return source != null && isVideoFile(file);
    }

    protected abstract void update(Source source);
    protected abstract boolean isValidFile(TorrentFile file);

    @Override
    public List<SelectionResult> selectFiles(List<TorrentFile> files) {
        File onDiskFile = new File(Preferences.getPreference(Preferences.PrefKey.STORAGE) + source.getFile());
        if (source.getFile() == null || !onDiskFile.exists() || source.getFileSize() != onDiskFile.length()) {
//            files.sort(Comparator.comparingLong(TorrentFile::getSize));
            List<SelectionResult> selectionResults = new ArrayList<>();
            for (TorrentFile file : files) {
                selectionResults.add(select(file));
            }
            return selectionResults;
        }
        logger.info("Find downloaded file [{}] for [{}]. Skip all files", source.getFile(), source);
        return files.stream().map(f -> SelectionResult.skip()).collect(Collectors.toList());
    }

    @Override
    protected SelectionResult select(TorrentFile file) {
        if (!isValidFile(file)) {
            return SelectionResult.skip();
        }
        if (Objects.isNull(source.getFile()) || (isVideoFile(source, file) && !SceneModel.INSTANCE.getActiveMovieModel().isPlayable())) {
            source.setFile(MovieModel.getFolder(source) + "\\" + String.join("/", file.getPathElements()));
            source.setFileSize(file.getSize());
            logger.info("Set file [{}] for [{}]", source.getFile(), source);
            update(source);
        }
        return SelectionResult.select().build();
    }
}
