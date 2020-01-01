package com.cinema.service.parser;

import com.cinema.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

import static com.cinema.helper.HttpHelper.requestAndGetBody;

public class KpParser implements Callable<Movie> {

    private final String url;
    private final Movie movie;

    private static final String HOST = "https://www.kinopoisk.ru";

    public KpParser(String url, Movie movie) {
        this.url = url;
        this.movie = movie;
    }

    @Override
    public Movie call() throws Exception {
        String body = requestAndGetBody(url,
                "accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "accept-encoding", "gzip, deflate, br",
                "accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
                "sec-fetch-mode", "navigate",
                "sec-fetch-site", "none",
                "sec-fetch-user", "?1",
                "upgrade-insecure-requests", "1",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        Document html = Jsoup.parse(body);
        Parser.parse(html, movie);
        return movie;
    }

    private static final class Parser {
        private static void parse(Document html, Movie movie) {
            movie.setRatingKp(getRating(html));
            movie.setRatingKpVotes(getRatingVotes(html));
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
