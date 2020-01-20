package com.cinema.service.parser;

import com.cinema.entity.*;
import com.cinema.helper.HttpHelper;
import com.cinema.helper.YoutubeHelper;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cinema.helper.HttpHelper.requestAndGetBody;

@Singleton
public class ImdbParser {

    private static final Logger logger = LogManager.getLogger(ImdbParser.class);

    private static final String HOST = "https://www.imdb.com";

    public void parse(String url, Movie movie) throws Exception {
        logger.info("Trying to fetch IMDB data from url=[{}]", url);
        String body = requestAndGetBody(HOST + url + "/");
        try {
            Document html = Jsoup.parse(body);
            Parser.parse(html, movie);
            logger.info("Successfully fetching IMDB data for movie=[{}]", movie.getMovieEn().getTitle());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static final class Parser {

        private static void parse(Document html, Movie movie) {
            JsonObject json = fetchJsonFromHtml(html);
            movie.setTitle(getOriginalTitle(json));
            movie.getMovieEn().setTitle(getTitle(html));

            movie.setPosterThumbnail(getPosterThumbnail(html));
            movie.setCompany(getCompany(html));
            movie.setRatingImdb(getRating(json));
            movie.setRatingImdbVotes(getRatingVotes(json));
            movie.setSeries(isSeries(json));
            movie.setReleaseDate(getReleaseDate(json));
            fillGenres(json, movie.getMovieEn());
            movie.setDuration(getDuration(html));
            movie.setBudget(getBudget(html));
            movie.setCurrency(getCurrency(html));
            movie.setGross(getGross(html));

            movie.getMovieEn().setMovie(movie);
            movie.getMovieEn().setUrl(getUrl(json));
            movie.getMovieEn().setTrailer(getTrailer(movie.getMovieEn().getTitle()));
            movie.getMovieEn().setTrailerThumbnail(getTrailerThumbnail(movie.getMovieEn().getTrailer()));
            movie.getMovieEn().setDescription(getDescription(html));
            movie.getMovieEn().setCountry(getCountry(html));
            fillPosters(html, movie);
            fillCasts(json, movie.getMovieEn());
        }

        private static void fillCasts(JsonObject json, MovieEn movie) {
            List<CastEn> casts = new ArrayList<>();
            JsonArray actors = json.get("actor").asArray();
            for (int i = 0; i < actors.size(); i++) {
                JsonObject actor = actors.get(i).asObject();
                if (actor.getString("@type", "").equals("Person")) {
                    CastEn cast = new CastEn();
                    cast.setName(actor.getString("name", null));
                    cast.setMovie(movie);
                    cast.setPriority((short) i);
                    cast.setRole(Role.ACTOR);
                    casts.add(cast);
                }
            }
            movie.setCasts(casts);
        }

        private static String getUrl(JsonObject json) {
            return json.getString("url", null);
        }

        private static String getTrailer(String name) {
            try {
                return YoutubeHelper.getTrailer(name);
            } catch (InterruptedException | IOException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        private static String getTrailerThumbnail(String trailerUrl) {
            return YoutubeHelper.getTrailerThumbnail(trailerUrl);
        }

        private static JsonObject fetchJsonFromHtml(Document html) {
            String json = html.selectFirst("script[type=\"application/ld+json\"]").html();
            return Json.parse(json).asObject();
        }

        private static String getPosterThumbnail(Document html) {
            return html.select(".poster img").attr("src");
        }

        private static void fillPosters(Document html, Movie movie) {
            try {
                String url = html.select(".poster > a").attr("href");
                String body = HttpHelper.requestAndGetBody(HOST + url);
                html = Jsoup.parse(body);
                Elements scripts = html.select("script:not([type],[id],[src])");
                for (Element element : scripts) {
                    String script = element.html();
                    if (script.contains("IMDbMediaViewerInitialState")) {
                        String s = script.substring(script.indexOf('{') + 1, script.lastIndexOf('}'));
                        s = s.substring(s.indexOf('{'));
                        JsonObject json = Json.parse(s).asObject().get("galleries").asObject().iterator().next().getValue().asObject();
                        JsonArray images = json.get("allImages").asArray();
                        List<String> postersUrl = new ArrayList<>();
                        for (int i = 0; i < images.size(); i++) {
                            JsonObject image = images.get(i).asObject();
                            if (image.get("relatedLanguages") == null && image.get("relatedCountries") == null && image.get("copyright") != null) {
                                String src = image.getString("src", null);
                                if (src != null) {
                                    postersUrl.add(src);
                                }
                            } else if (image.get("relatedLanguages") != null && image.get("relatedCountries") != null) {
                                boolean countryUs = image.get("relatedCountries").asArray().values().stream()
                                        .map(JsonValue::asString)
                                        .anyMatch(c -> c.equals("US"));
                                boolean langEn = image.get("relatedLanguages").asArray().values().stream()
                                        .map(JsonValue::asString)
                                        .anyMatch(c -> c.equals("en"));
                                if (countryUs && langEn) {
                                    if (movie.getPoster() == null) {
                                        movie.setPoster(image.getString("src", null));
                                    }
                                }
                            }
                        }
                        String postersUrlStr = postersUrl.stream()
                                .map(c -> String.format("\"%s\"", c))
                                .collect(Collectors.joining(", "));
                        movie.setPosters(String.format("[%s]", postersUrlStr));
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        private static String getOriginalTitle(JsonObject json) {
            return json.getString("name", null);
        }

        private static String getTitle(Document html) {
            return html.selectFirst(".title_wrapper > h1").ownText();
        }

        private static String getDescription(Document html) {
            return html.select("#titleStoryLine > div > p > span").text();
        }

        private static String getCountry(Document html) {
            Elements countries = getElementsWithTagA(html, "Country");
            if (countries == null) return null;
            String country = countries.stream()
                    .map(c -> String.format("\"%s\"", c.text().trim()))
                    .collect(Collectors.joining(", "));
            return String.format("[%s]", country);
        }

        private static String getCompany(Document html) {
            Elements companies = getElementsWithTagA(html, "Production Co");
            if (companies == null) return null;
            String company = companies.stream()
                    .map(c -> String.format("\"%s\"", c.text().trim()))
                    .collect(Collectors.joining(", "));
            return String.format("[%s]", company);
        }

        private static Float getRating(JsonObject json) {
            try {
                String val = json.get("aggregateRating").asObject().getString("ratingValue", null);
                if (val == null) return null;
                return Float.parseFloat(val);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static Integer getRatingVotes(JsonObject json) {
            try {
                return json.get("aggregateRating").asObject().get("ratingCount").asInt();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static boolean isSeries(JsonObject json) {
            return json.getString("@type", "").equals("TVSeries");
        }

        private static short getDuration(Document html) {
            return Short.parseShort(html.select("div.subtext time").attr("datetime").replaceAll("[^0-9]+", ""));
        }

        private static void fillGenres(JsonObject json, MovieEn movie) {
            if (json.get("genre").isString()) {
                movie.setGenre1(json.get("genre").asString());
                return;
            }
            JsonArray genres = json.get("genre").asArray();
            for (int i = 0; i < genres.size() && i < 3; i++) {
                switch (i) {
                    case 0:
                        movie.setGenre1(genres.get(i).asString());
                        break;
                    case 1:
                        movie.setGenre2(genres.get(i).asString());
                        break;
                    case 2:
                        movie.setGenre3(genres.get(i).asString());
                        break;
                    default:
                        break;
                }
            }
        }

        private static LocalDate getReleaseDate(JsonObject json) {
            try {
                return LocalDate.parse(json.getString("datePublished", null), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        private static Long getBudget(Document html) {
            String value = getFieldValue(html, "Budget");
            if (value == null) return null;
            return Long.parseLong(value.replaceAll("[^0-9]+", ""));
        }

        private static Long getGross(Document html) {
            String value = getFieldValue(html, "Worldwide Gross");
            if (value == null) return null;
            return Long.parseLong(value.replaceAll("[^0-9]+", ""));
        }

        private static Currency getCurrency(Document html) {
            String value = getFieldValue(html, "Budget");
            if (value == null) return null;
            return Currency.getCurrencyFromChar(value.charAt(0));
        }

        private static String getFieldValue(Document html, String fieldName) {
            for (Element element : html.select("#titleDetails div.txt-block")) {
                if (element.select("h4").text().contains(fieldName)) {
                    return element.ownText().trim();
                }
            }
            return null;
        }

        private static Elements getElementsWithTagA(Document html, String fieldName) {
            for (Element element : html.select("#titleDetails div.txt-block")) {
                if (element.select("h4").text().contains(fieldName)) {
                    return element.select(">a");
                }
            }
            return null;
        }
    }
}
