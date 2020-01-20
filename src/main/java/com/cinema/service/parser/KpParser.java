package com.cinema.service.parser;

import com.cinema.entity.*;
import com.cinema.helper.YoutubeHelper;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cinema.helper.HttpHelper.requestAndGetBody;

@Singleton
public class KpParser {

    private static final Logger logger = LogManager.getLogger(KpParser.class);

    private static final String HOST = "https://www.kinopoisk.ru";

    public void parse(String url, Movie movie) throws Exception {
        logger.info("Trying to fetch KP data from url=[{}]", url);
        String body = requestAndGetBody(HOST + url,
                "accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "accept-encoding", "deflate",
                "accept-language", "ru-RU,ru",
                "sec-fetch-mode", "navigate",
                "sec-fetch-site", "none",
                "sec-fetch-user", "?1",
                "upgrade-insecure-requests", "1",
                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        try {
            Document html = Jsoup.parse(body);
            Parser.parse(html, movie);
            logger.info("Successfully fetching KP data for movie=[{}]", movie.getMovieEn().getTitle());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static final class Parser {
        private static void parse(Document html, Movie movie) {
            movie.setRatingKp(getRating(html));
            movie.setRatingKpVotes(getRatingVotes(html));
            movie.setMovieRu(new MovieRu());
            movie.getMovieRu().setMovie(movie);

            movie.getMovieRu().setTitle(getTitle(html));
            movie.getMovieRu().setUrl(getUrl(html));
            movie.getMovieRu().setTrailer(getTrailer(movie.getMovieRu().getTitle()));
            movie.getMovieRu().setTrailerThumbnail(getTrailerThumbnail(movie.getMovieRu().getTrailer()));
            movie.getMovieRu().setDescription(getDescription(html));
            movie.getMovieRu().setCountry(getCountry(html));
            fillGenres(html, movie.getMovieRu());
            fillCasts(html, movie.getMovieRu().getUrl(), movie.getMovieRu());
        }

        private static String getTrailer(String name) {
            try {
                return YoutubeHelper.getTrailer(name);
            } catch (InterruptedException | IOException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        private static void fillGenres(Document html, MovieRu movie) {
            Elements genres = html.select("span[itemprop=\"genre\"] a");
            for (int i = 0; i < genres.size() && i < 3; i++) {
                switch (i) {
                    case 0:
                        movie.setGenre1(genres.get(i).text());
                        break;
                    case 1:
                        movie.setGenre2(genres.get(i).text());
                        break;
                    case 2:
                        movie.setGenre3(genres.get(i).text());
                        break;
                    default:
                        break;
                }
            }
        }

        private static void fillCasts(Document document, String url, MovieRu movie) {
            try {
                String body = requestAndGetBody(HOST + url + "cast/",
                        "accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                        "accept-encoding", "deflate",
                        "accept-language", "ru-RU,ru",
                        "sec-fetch-mode", "navigate",
                        "sec-fetch-site", "none",
                        "sec-fetch-user", "?1",
                        "upgrade-insecure-requests", "1",
                        "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
                List<CastRu> casts = new ArrayList<>();
                Document html = Jsoup.parse(body);
                Elements actors = html.select(".actorInfo > .info");
                for (int i = 0; i < actors.size() && casts.size() < 8; i++) {
                    if (actors.get(i).selectFirst(".role").text().length() > 0) {
                        CastRu cast = new CastRu();
                        cast.setName(actors.get(i).select(".name > a").text());
                        cast.setMovie(movie);
                        cast.setPriority((short) i);
                        cast.setQua(actors.get(i).selectFirst(".role").text());
                        cast.setRole(Role.ACTOR);
                        casts.add(cast);
                    }
                }
                Elements employers = document.select("table.info tr");
                for (Element employer : employers) {
                    switch (employer.select("td.type").text()) {
                        case "режиссер":
                            Elements directors = employer.select("a");
                            for (short i = 0; i < Math.min(directors.size(), 3); i++) {
                                casts.add(createCast(directors.get(i).text(), Role.DIRECTOR, i, movie));
                            }
                            break;
                        case "сценарий":
                            Elements screenwriters = employer.select("a");
                            for (short i = 0; i < Math.min(screenwriters.size(), 3); i++) {
                                casts.add(createCast(screenwriters.get(i).text(), Role.SCREENWRITER, i, movie));
                            }
                            break;
                        case "композитор":
                            Elements composers = employer.select("a");
                            for (short i = 0; i < Math.min(composers.size(), 3); i++) {
                                casts.add(createCast(composers.get(i).text(), Role.COMPOSER, i, movie));
                            }
                            break;
                        default:
                            break;
                    }
                }
                movie.setCasts(casts);
            } catch (IOException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        private static CastRu createCast(String name, Role role, short priority, MovieRu movie) {
            CastRu cast = new CastRu();
            cast.setName(name);
            cast.setMovie(movie);
            cast.setPriority(priority);
            cast.setRole(role);
            return cast;
        }

        private static String getTrailerThumbnail(String trailerUrl) {
            return YoutubeHelper.getTrailerThumbnail(trailerUrl);
        }

        private static String getTitle(Document html) {
            return html.selectFirst(".moviename-title-wrapper").ownText().strip();
        }

        private static String getUrl(Document html) {
            return html.selectFirst("meta[property=\"og:url\"]").attr("content").strip();
        }

        private static String getCountry(Document html) {
            return html.selectFirst(".movie-info__flag.flag.flag1").attr("title").strip();
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
