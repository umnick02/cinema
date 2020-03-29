//package com.cinema.service.bt;
//
//import bt.Bt;
//import bt.data.Storage;
//import bt.data.file.FileSystemStorage;
//import bt.dht.DHTConfig;
//import bt.dht.DHTModule;
//import bt.metainfo.Torrent;
//import bt.runtime.BtClient;
//import bt.runtime.Config;
//import bt.torrent.fileselector.TorrentFileSelector;
//import bt.torrent.selector.SequentialSelector;
//import com.cinema.entity.Movie;
//import com.cinema.model.MovieModel;
//import com.cinema.presenter.PlayerPresentable;
//import com.cinema.service.bt.selectors.DraftFilesSelector;
//import com.google.inject.Inject;
//import com.google.inject.Module;
//import com.google.inject.Singleton;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.nio.file.Paths;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import static com.cinema.CinemaApplication.INJECTOR;
//
//@Singleton
//public class BtService {
//
//    private static final Logger logger = LogManager.getLogger(BtService.class);
//
//    private final TorrentFileSelector fileSelector;
//    private final MovieModel movieModel;
//    private static final double preloadDuration = 0.5;
//    private final Module dhtModule = new DHTModule(new DHTConfig() {
//        @Override
//        public boolean shouldUseRouterBootstrap() {
//            return true;
//        }
//    });
//    private final Config config = new Config() {
//        @Override
//        public int getNumOfHashingThreads() {
//            return Runtime.getRuntime().availableProcessors() * 2;
//        }
//    };
//    private final Storage storage = new FileSystemStorage(Paths.get(com.cinema.config.Config.getPreference(com.cinema.config.Config.PrefKey.STORAGE)));
//    private BtClient btClient;
//
//    @Inject
//    public BtService(TorrentFileSelector fileSelector, MovieModel movieModel) {
//        this.fileSelector = fileSelector;
//        this.movieModel = movieModel;
//    }
//
//    public boolean stop() {
//        if (btClient != null && btClient.isStarted()) {
//            btClient.stop();
//            return true;
//        }
//        return false;
//    }
//
//    public Torrent fillMovie(Movie movie) {
//        TorrentHolder torrentHolder = new TorrentHolder();
//        ((DraftFilesSelector) fileSelector).setMovie(movie);
//        btClient = Bt.client()
//                .config(config)
//                .module(dhtModule)
//                .storage(storage)
//                .magnet(movie.getMagnet())
//                .autoLoadModules()
//                .afterTorrentFetched(t -> torrentHolder.torrent = t)
//                .initEagerly()
//                .fileSelector(fileSelector)
//                .selector(SequentialSelector.sequential())
//                .stopWhenDownloaded()
//                .build();
//        CompletableFuture<?> future = btClient.startAsync(torrentSessionState -> {
//            if (torrentHolder.torrent != null) {
//                try {
//                    btClient.stop();
//                } catch (RuntimeException e) {
//                    logger.info("BtClient was stopped!");
//                }
//            }
//        }, 1000);
//        try {
//            future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            logger.error(e.getMessage(), e);
//        }
//        return torrentHolder.torrent;
//    }
//
//    public void start(Movie movie) {
//        ((DraftFilesSelector) fileSelector).setMovie(movie);
//        BtClient btClient = Bt.client()
//                .config(config)
//                .module(dhtModule)
//                .storage(storage)
//                .magnet(movie.getMagnet())
//                .autoLoadModules()
//                .initEagerly()
//                .fileSelector(fileSelector)
//                .selector(SequentialSelector.sequential())
//                .stopWhenDownloaded()
//                .build();
//        logger.info("Preloading movie [{}]", movie.getTitle());
//        Preloader preloader = new Preloader();
//        btClient.startAsync(state -> {
//            double loadDuration = (double) state.getPiecesComplete() * movie.getDuration() / state.getPiecesTotal();
//            preloader.check(loadDuration, movie, movieModel);
//            logger.info(String.format("Preloaded [%.1f / {}] min of movie [{}]", loadDuration),
//                    movie.getDuration(), movie.getTitle());
//        }, 1000).join();
//        logger.info("Movie [{}] downloaded!", movie.getTitle());
//        movie.setFileStatus(Movie.FileStatus.DOWNLOADED);
//        movieModel.updateMovie(movie);
//    }
//
//    private static final class TorrentHolder {
//        private Torrent torrent;
//    }
//
//    private static final class Preloader {
//        boolean preloaded = false;
//        private void check(double loadDuration, Movie movie, MovieModel movieModel) {
//            if (!preloaded && loadDuration >= preloadDuration) {
//                logger.info("Movie [{}] preloaded!", movie.getTitle());
//                movie.setFileStatus(Movie.FileStatus.PLAYABLE);
//                movieModel.updateMovie(movie);
//                INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie);
//                preloaded = true;
//            }
//        }
//    }
//}
