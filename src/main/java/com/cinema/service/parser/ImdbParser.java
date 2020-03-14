package com.cinema.service.parser;

import com.cinema.config.Config;
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
import java.util.HashSet;
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
            logger.info("Successfully fetching IMDB data for movie=[{}]", movie.getTitle());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static final class Parser {

        private static void parse(Document html, Movie movie) {
            JsonObject json = fetchJsonFromHtml(html);
            movie.setOriginalTitle(getOriginalTitle(json));
            movie.setTitle(getTitle(html));

            movie.setRatingImdb(getRating(json));
            movie.setRatingImdbVotes(getRatingVotes(json));
//            movie.setSeries(isSeries(json));
            movie.setReleaseDate(getReleaseDate(json));
            fillGenres(json, movie);
            movie.setDuration(getDuration(html));

            movie.setUrl(getUrl(json));
            movie.setTrailer(getTrailer(movie.getTitle()));
            movie.setDescription(getDescription(html));
            movie.setCountry(getCountry(html));
            fillPosters(html, movie);
            fillCasts(movie);
        }

        private static void fillCasts(Movie movie) {
            try {
                String body = requestAndGetBody(movie.getUrl() + "fullcredits");
                Document html = Jsoup.parse(body);
                movie.setCasts(new HashSet<>());
                for (int i = 0; i < html.select("h4").size(); i++) {
                    if (html.select("h4").get(i).text().contains("Directed")) {
                        Elements elements = html.select("table").get(i).select("tr");
                        addCasts(elements, movie, Cast.Role.DIRECTOR, 3);
                    } else if (html.select("h4").get(i).text().contains("Writing")) {
                        Elements elements = html.select("table").get(i).select("tr");
                        addCasts(elements, movie, Cast.Role.WRITER, 3);
                    } else if (html.select("h4").get(i).text().contains("Music")) {
                        Elements elements = html.select("table").get(i).select("tr");
                        addCasts(elements, movie, Cast.Role.COMPOSER, 3);
                    } else if (html.select("h4").get(i).text().contains("Cast")) {
                        Elements elements = html.select("table").get(i).select("tr");
                        addCasts(elements, movie, Cast.Role.ACTOR, 7);
                    }
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        private static void addCasts(Elements elements, Movie movie, Cast.Role role, int limit) {
            String cls = "odd";
            for (int k = 0; k < Math.min(elements.size(), limit); k++) {
                Cast cast = new Cast();
                cast.setMovie(movie);
                cast.setPriority((short) (k + 1));
                cast.setRole(role);
                if (role != Cast.Role.ACTOR) {
                    cast.setName(elements.get(k).select("td.name a").text());
                } else {
                    if (!elements.get(k).hasClass(cls)) {
                        continue;
                    }
                    cast.setName(elements.get(k).select("td img").attr("title"));
                    if (elements.get(k).select("td").size() > 3) {
                        if (elements.get(k).select("td").get(3).selectFirst("a") != null) {
                            cast.setQua(elements.get(k).select("td").get(3).selectFirst("a").ownText().strip());
                        } else {
                            cast.setQua(elements.get(k).select("td").get(3).ownText().strip());
                        }
                    }
                    cls = cls.equals("odd") ? "even" : "odd";
                }
                movie.getCasts().add(cast);
            }
        }

        private static String getUrl(JsonObject json) {
            return HOST + json.getString("url", null);
        }

        private static String getTrailer(String name) {
            try {
                return YoutubeHelper.getTrailer(name, Config.PrefKey.Language.EN);
            } catch (InterruptedException | IOException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        private static JsonObject fetchJsonFromHtml(Document html) {
            String json = html.selectFirst("script[type=\"application/ld+json\"]").html();
            return Json.parse(json).asObject();
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
                                if (src != null && postersUrl.size() < 48) {
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

        private static void fillGenres(JsonObject json, Movie movie) {
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
