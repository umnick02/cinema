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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class MagnetParser {

    private static final Logger logger = LogManager.getLogger(MagnetParser.class);

    private final ImdbParser imdbParser;

    @Inject
    public MagnetParser(ImdbParser imdbParser) {
        this.imdbParser = imdbParser;
    }

    public Movie parse(String magnet, Torrent torrent) {
        Movie movie = new Movie();
        movie.setCustom(false);
        movie.setMagnet(magnet);
        movie.setMovieEn(new MovieEn());
        try {
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return movie;
    }

    private String fetchFileName(String torrentName) {
        torrentName = torrentName.substring(0, torrentName.indexOf('.'));
        if (torrentName.contains("(")) {
            String[] parts = torrentName.split(" ");
            if (parts.length > 1) {
                torrentName = String.join("+", Arrays.copyOf(parts, parts.length - 1));
            }
        }
        return torrentName;
    }
}
