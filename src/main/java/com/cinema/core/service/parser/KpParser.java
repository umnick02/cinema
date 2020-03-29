package com.cinema.core.service.parser;

import com.cinema.core.config.Config;
import com.cinema.core.entity.Movie;
import com.cinema.core.helper.HttpHelper;
import com.cinema.core.helper.YoutubeHelper;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Singleton
public class KpParser {

    private static final Logger logger = LogManager.getLogger(KpParser.class);

    public void parse(String url, Movie movie) throws Exception {
        logger.info("Trying to fetch KP data from url=[{}]", url);
        String body = HttpHelper.requestAndGetBody(url);
        try {
            Document html = Jsoup.parse(body.substring(body.indexOf("<!")));
            Parser.parse(html, movie);
            logger.info("Successfully fetching KP data for movie=[{}]", movie.getTitle());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static final class Parser {
        private static void parse(Document html, Movie movie) {
            movie.setRatingKp(getRating(html));
            movie.setRatingKpVotes(getRatingVotes(html));
            movie.setTitleRu(getTitle(html));
            movie.setTrailerRu(getTrailer(movie.getTitle()));
            movie.setDescriptionRu(getDescription(html));
        }

        private static String getTrailer(String name) {
            try {
                return YoutubeHelper.getTrailer(name, Config.PrefKey.Language.RU);
            } catch (InterruptedException | IOException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        private static String getTitle(Document html) {
            return html.selectFirst(".moviename-title-wrapper").ownText().strip();
        }

        private static String getDescription(Document html) {
            return html.selectFirst("meta[name=\"mrc__share_description\"]").attr("content").strip();
        }

        private static Float getRating(Document html) {
            try {
                return Float.parseFloat(html.select(".rating_link > .rating_ball").text());
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static Integer getRatingVotes(Document html) {
            try {
                return Integer.parseInt(html.select(".rating_link > .ratingCount").text().replaceAll("[^0-9.]+", ""));
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }
}
