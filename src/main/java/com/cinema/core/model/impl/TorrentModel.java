package com.cinema.core.model.impl;

import bt.torrent.TorrentSessionState;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import com.cinema.core.service.Stoppable;
import com.cinema.core.service.bt.BtClientService;
import javafx.event.Event;

import java.time.LocalDateTime;

import static com.cinema.core.config.Preferences.PRELOAD_MIN;
import static java.time.temporal.ChronoUnit.SECONDS;

public class TorrentModel extends ObservableModel implements Stoppable {

    public static final TorrentModel INSTANCE = new TorrentModel();

    private TorrentModel() {}

    private LocalDateTime start;

    private TorrentSessionState torrentSessionState;

    public void setTorrentSessionState(TorrentSessionState torrentSessionState) {
        this.torrentSessionState = torrentSessionState;
        if (torrentSessionState.getDownloaded() == 0) {
            start = LocalDateTime.now();
        }
        fireEvent(new Event(ModelEventType.TORRENT_UPDATE.getEventType()));
    }

    public long getAvgSpeed() {
        if (start != null && SECONDS.between(start, LocalDateTime.now()) > 0) {
            return torrentSessionState.getDownloaded() / 1024 / SECONDS.between(start, LocalDateTime.now());
        } else {
            return 0;
        }
    }

    public int getStatus() {
        Movie movie = SceneModel.INSTANCE.getActiveMovieModel().getMovie();
        if (movie.getDuration() == 0) {
            return 0;
        }
        long piecesDownloaded = torrentSessionState.getPiecesNotSkipped() - torrentSessionState.getPiecesRemaining();
        double totalPiecesForDownload = torrentSessionState.getPiecesNotSkipped() * PRELOAD_MIN / movie.getDuration();
        if (totalPiecesForDownload == 0) {
            return 0;
        }
        double downloadedPart = piecesDownloaded / totalPiecesForDownload;
        if (downloadedPart >= 1) {
            movie.setStatus(Magnet.Status.PLAYABLE);
            MovieModel.update(movie);
//            fireEvent(new Event(MOVIE_PLAY.getEventType()));
        }
        return (int) (downloadedPart * 100);
    }

    public int getPeers() {
        return torrentSessionState.getConnectedPeers().size();
    }

    @Override
    public void stop() {
        BtClientService.INSTANCE.stop();
    }
}
