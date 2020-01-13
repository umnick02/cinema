package com.cinema.service.bt;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.fileselector.TorrentFileSelector;
import bt.torrent.selector.SequentialSelector;
import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.presenter.PlayerPresentable;
import com.cinema.service.parser.MagnetParser;
import com.cinema.service.bt.selectors.DraftFilesSelector;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;

import static com.cinema.CinemaApplication.INJECTOR;

@Singleton
public class BtService {

    private static final Logger logger = LogManager.getLogger(BtService.class);

    private final TorrentFileSelector fileSelector;
    private final MagnetParser magnetParser;
    private final MovieModel movieModel;
    private static final double preloadDuration = 0.5;

    @Inject
    public BtService(TorrentFileSelector fileSelector, MagnetParser magnetParser, MovieModel movieModel) {
        this.fileSelector = fileSelector;
        this.magnetParser = magnetParser;
        this.movieModel = movieModel;
    }

    public void start(Movie movie) {
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
        Storage storage = new FileSystemStorage(Paths.get(com.cinema.config.Config.getPreference(com.cinema.config.Config.PrefKey.STORAGE)));
        ((DraftFilesSelector) fileSelector).setMovie(movie);
        BtClient btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
                .magnet(movie.getMagnet())
                .autoLoadModules()
                .afterTorrentFetched(torrent -> movieModel.processMovieFromMagnet(magnetParser.parse(movie.getMagnet(), torrent)))
                .initEagerly()
                .fileSelector(fileSelector)
                .selector(SequentialSelector.sequential())
                .stopWhenDownloaded()
                .build();
        logger.info("Preloading movie [{}]", movie.getMovieEn().getTitle());
        Preloader preloader = new Preloader();
        btClient.startAsync(state -> {
            double loadDuration = (double) state.getPiecesComplete() * movie.getDuration() / state.getPiecesTotal();
            preloader.check(loadDuration, movie, movieModel);
            logger.info(String.format("Preloaded [%.1f / {}] min of movie [{}]", loadDuration),
                    movie.getDuration(), movie.getMovieEn().getTitle());
        }, 1000).join();
        logger.info("Movie [{}] downloaded!", movie.getMovieEn().getTitle());
        movie.setFileStatus(Movie.FileStatus.DOWNLOADED);
        movieModel.updateMovie(movie);
    }

    private static final class Preloader {
        boolean preloaded = false;
        private void check(double loadDuration, Movie movie, MovieModel movieModel) {
            if (!preloaded && loadDuration >= preloadDuration) {
                logger.info("Movie [{}] preloaded!", movie.getMovieEn().getTitle());
                movie.setFileStatus(Movie.FileStatus.PLAYABLE);
                movieModel.updateMovie(movie);
                INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie);
                preloaded = true;
            }
        }
    }
}
