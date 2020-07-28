package com.cinema.core.service.parser;

import bt.metainfo.Torrent;
import com.cinema.core.entity.Movie;
import com.cinema.core.helper.HttpHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MagnetParser {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(MagnetParser.class);

    public static void main(String[] args) {
        MagnetParser.INSTANCE.parse("Пушки акимбо");
    }

    public Movie parse(Torrent torrent) {
        Movie movie = parse(torrent.getName());
        movie.getMagnet().setHash(torrent.getTorrentId().toString());
        return movie;
    }

    public Movie parse(String torrentName) {
        try {
            Document html = Jsoup.parse(HttpHelper.requestAndGetBody(buildSearchUrl(torrentName)));
            String kpUrl = html.select("#search a").attr("href");
            Movie movie = new Movie();
            movie.setUrl(kpUrl);
            if (movie.getUrl().contains("series")) {
                movie.setType(Movie.Type.SERIES);
                SeriesParser.INSTANCE.parse(movie);
            } else {
                MovieParser.INSTANCE.parse(movie);
            }
            return movie;
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    private String buildSearchUrl(String torrentName) {
        String movieName = fetchFileName(torrentName);
        logger.info("Trying to find KP url for movie=[{}]", movieName);
        return "https://www.google.com/search?q=site:kinopoisk.ru+" + movieName;
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
