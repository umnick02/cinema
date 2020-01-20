package com.cinema.service.parser;

import bt.metainfo.Torrent;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieEn;
import com.cinema.helper.HttpHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class MagnetParser {

    private static final Logger logger = LogManager.getLogger(MagnetParser.class);

    private final ImdbParser imdbParser;
    private final KpParser kpParser;
    private final EpisodeParser episodeParser;

    @Inject
    public MagnetParser(ImdbParser imdbParser, KpParser kpParser, EpisodeParser episodeParser) {
        this.imdbParser = imdbParser;
        this.kpParser = kpParser;
        this.episodeParser = episodeParser;
    }

    public Movie parse(String magnet, Torrent torrent) {
        Movie movie = new Movie();
        movie.setCustom(false);
        movie.setMagnet(magnet);
        movie.setMovieEn(new MovieEn());
        try {
            parseImdb(movie, torrent);
            parseKp(movie, torrent);
            if (movie.getSeries()) {
//                episodeParser.parse(movie);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return movie;
    }

    private void parseImdb(Movie movie, Torrent torrent) throws Exception {
        String movieName = fetchFileName(torrent.getName());
        logger.info("Trying to find IMDB url for movie=[{}]", movieName);
        String url = String.format("https://www.google.com/search?btnI=1&q=site:imdb.com+%s", movieName);
        HttpResponse<String> response = HttpHelper.requestAndGetResponse(url);
        String location = response.headers().firstValue("location").orElse(null);
        if (location != null) {
            Pattern pattern = Pattern.compile("/title/[^/]+");
            Matcher matcher = pattern.matcher(location);
            if (matcher.find()) {
                imdbParser.parse(matcher.group(), movie);
            }
        } else {
            logger.warn("IMDB url for movie=[{}] not found!", movieName);
        }
    }

    private void parseKp(Movie movie, Torrent torrent) throws Exception {
        String movieName = fetchFileName(torrent.getName());
        logger.info("Trying to find KP url for movie=[{}]", movieName);
        String url = String.format("https://www.google.com/search?btnI=1&q=site:kinopoisk.ru+%s", movieName);
        HttpResponse<String> response = HttpHelper.requestAndGetResponse(url);
        String location = response.headers().firstValue("location").orElse(null);
        if (location != null) {
            Pattern pattern = Pattern.compile("/film/[^/]+");
            Matcher matcher = pattern.matcher(location);
            if (matcher.find()) {
                kpParser.parse(matcher.group(), movie);
            }
        } else {
            logger.warn("KP url for movie=[{}] not found!", movieName);
        }
    }

    private String fetchFileName(String torrentName) {
        if (torrentName.contains(".")) {
            torrentName = torrentName.substring(0, torrentName.indexOf('.'));
        }
        torrentName = torrentName.substring(0, torrentName.indexOf('(')).strip();
        return torrentName.replaceAll(" ", "+");
    }
}
