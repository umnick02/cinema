package com.cinema.presenter;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.service.bt.BtService;
import com.cinema.view.Playable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.cinema.config.Config.EXECUTORS;
import static com.cinema.config.Config.getPreference;

public class PlayerPresenter implements PlayerPresentable {

    private static final Logger logger = LogManager.getLogger(PlayerPresenter.class);

    private final Playable playable;
    private final BtService btService = INJECTOR.getInstance(BtService.class);

    public PlayerPresenter(Playable playable) {
        this.playable = playable;
    }

    @Override
    public void play(Movie movie) {
        playable.play(movie.getFile());
    }

    @Override
    public void tryPlay(Movie movie) {
        playable.showPlayer();
        if (movie.getFile() == null
                || !new File(getPreference(Config.PrefKey.STORAGE) + movie.getFile()).exists()
                || !Movie.FileStatus.canPlay(movie.getFileStatus())) {
            playable.showLoadingView();
            try {
                EXECUTORS.submit(() -> btService.start(movie));
            } catch (NullPointerException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            playable.play(movie.getFile());
        }
    }
}
