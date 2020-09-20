package com.cinema.core.service.parser;

import bt.metainfo.Torrent;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.helper.HttpHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum MagnetParser {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(MagnetParser.class);

    public static void main(String[] args) {
        MagnetParser.INSTANCE.parse("Пушки акимбо");
    }

    public Movie parse(Torrent torrent) {
        Movie movie = parse(torrent.getName());
        movie.setMagnet(new Magnet());
        movie.getMagnet().setHash(torrent.getTorrentId().toString());
        movie.getMagnet().setFileSize(torrent.getSize());
        return movie;
    }

    public Movie parse(String movieName) {
        try {
            Document html = Jsoup.parse(HttpHelper.requestAndGetBody(buildSearchUrl(movieName)));
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
        String[] parts = torrentName.replaceAll("[_.()\\[\\]]", " ").split(" ");
        List<String> result = new ArrayList<>();
        switch (parts.length) {
            case 2:
                result.add(parts[1]);
            case 1:
                result.add(parts[0]);
                break;
            default:
                result.add(parts[2]);
                result.add(parts[1]);
                result.add(parts[0]);
                break;
        }
        Collections.reverse(result);
        return String.join("+", result);
    }
}
