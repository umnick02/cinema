package com.cinema.core.service.parser;

import bt.metainfo.Torrent;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.helper.HttpHelper;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagnetParser {

    private static final Logger logger = LoggerFactory.getLogger(MagnetParser.class);

    private final ImdbParser imdbParser = new ImdbParser();
    private final KpParser kpParser = new KpParser();
    private final EpisodeParser episodeParser = new EpisodeParser();

    public Movie parse(String magnet, Torrent torrent) {
        Movie movie = new Movie();
        movie.setCustom(false);
        movie.setMagnet(new Magnet(magnet));
        try {
            parseImdb(movie, torrent);
            parseKp(movie, torrent);
            if (movie.getType() == Movie.Type.SERIES) {
                episodeParser.parse(movie, torrent);
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
        String url = String.format("https://www.google.com/search?q=site:kinopoisk.ru+%s", movieName);
        String body = HttpHelper.requestAndGetBody(url);
        String cachedUrl = Jsoup.parse(body).selectFirst(".g>div").getAllElements().get(0).select("a").get(0).attr("href");
        kpParser.parse(cachedUrl, movie);
    }

    private String fetchFileName(String torrentName) {
        if (torrentName.contains(".")) {
            torrentName = torrentName.substring(0, torrentName.indexOf('.'));
        }
        if (torrentName.contains("(")) {
            torrentName = torrentName.substring(0, torrentName.indexOf('(')).strip();
        }
        return torrentName.replaceAll(" ", "+");
    }
}
