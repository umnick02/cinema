package com.cinema.core.service.parser;

import com.cinema.core.dto.Award;
import com.cinema.core.dto.Cast;
import com.cinema.core.entity.Movie;
import com.cinema.core.helper.HttpHelper;
import com.cinema.core.helper.YoutubeHelper;
import com.cinema.core.model.impl.MovieModel;
import com.cinema.core.service.MovieFields;
import com.cinema.core.service.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

public class MovieParser implements Parser, MovieFields {

    public static MovieParser INSTANCE = new MovieParser();

    MovieParser() {}

    private static final Logger logger = LoggerFactory.getLogger(MovieParser.class);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern(("dd MMMM yyyy")))
            .toFormatter();

    @Override
    public void parse(final Movie movie) throws IOException, InterruptedException {
        Document html = Jsoup.parse(HttpHelper.requestAndGetBody(movie.getUrl()));

//        AtomicInteger i = new AtomicInteger(1);
//        if (i.get() <= 10) {
//            info.select("#actorList li").stream().filter(actor -> !actor.text().equals("...")).forEach(actor ->
//                    movie.addCasts(buildCast(actor, ACTOR, (short) i.getAndIncrement())));
//        }
//
//        Elements photoElements = Jsoup.parse(HttpHelper.requestAndGetBody(movie.getUrl() + "stills/"))
//                .select(".fotos a");
//        JsonArray photos = new JsonArray();
//        for (Element photoElement : photoElements) {
//            if (photos.size() > 12) {
//                break;
//            }
//            try {
//                Document doc = Jsoup.parse(HttpHelper.requestAndGetBody(HOST + photoElement.attr("href")));
//                String url = doc.select("#image").attr("src");
//                if (url.length() > 0) {
//                    photos.add(doc.select("#image").attr("src"));
//                }
//            } catch (InterruptedException | IOException e) {
//                logger.info("", e);
//            }
//        }
//        movie.setPosters(photoElements.toString());
        movie.setTitle(fetchTitle(html));
        movie.setOriginalTitle(fetchOriginalTitle(html));
        movie.setPoster(fetchPoster(html));
        movie.setTrailer(fetchTrailer(movie.getTitle()));
        movie.setDescription(fetchDescription(html));
        movie.setRatingKp(fetchRatingKp(html));
        movie.setRatingKpVotes(fetchRatingKpVotes(html));
        movie.setRatingImdb(fetchRatingImdb(html));
        movie.setRatingImdbVotes(fetchRatingImdbVotes(html));
        movie.setAward(fetchAward(html));
        movie.setReleaseDate(fetchReleaseDate(html));
        movie.setDuration(fetchDuration(html));
        movie.setCasts(fetchCasts(html));

        List<String> genres = fetchGenres(html);
        movie.setGenre1(genres.get(0));
        if (genres.size() > 1) movie.setGenre2(genres.get(1));
        if (genres.size() > 2) movie.setGenre3(genres.get(2));

        movie.setType(movie.getGenre1().equals("мультфильм") ? Movie.Type.CARTOON : Movie.Type.MOVIE);

        List<String> countries = fetchCountries(html);
        movie.setCountry1(countries.get(0));
        if (countries.size() > 1) movie.setCountry2(countries.get(1));
        if (countries.size() > 2) movie.setCountry3(countries.get(2));

        logger.info("{}", movie);
        MovieModel.save(movie);
    }

    private String fetchByField(Document html, String field) {
        for (Element element : html.select("div[data-tid='2e44ef7d'] > div")) {
            if (element.selectFirst("div").text().contains(field)) {
                return element.select("> div").get(1).text();
            }
        }
        logger.error("Not found field: {}", field);
        throw new RuntimeException("Not found field: " + field);
    }

    @Override
    public String fetchTitle(Document html) {
        return html.select("span[class^=styles_title]").text();
    }

    @Override
    public String fetchOriginalTitle(Document html) {
        try {
            return html.select("span[class^=styles_originalTitle]").text();
        } catch (Exception e) {
            logger.info("", e);
            return null;
        }
    }

    @Override
    public String fetchPoster(Document html) {
        String[] srcset = html.select(".film-poster").attr("srcset").split(",");
        return "https:" + srcset[srcset.length - 1].trim().split(" ")[0];
    }

    @Override
    public List<String> fetchPosters(Document html) {
        return null;
    }

    @Override
    public String fetchTrailer(String title) {
        try {
            return YoutubeHelper.getTrailer(title);
        } catch (Exception e) {
            logger.warn("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String fetchDescription(Document html) {
        return html.select(".styles_filmSynopsis__zLClu").text();
    }

    @Override
    public Float fetchRatingKp(Document html) {
        return Float.parseFloat(html.select("a.film-rating-value").text());
    }

    @Override
    public Integer fetchRatingKpVotes(Document html) {
        return Integer.parseInt(html.select(".styles_ratingContainer__24Wyy .styles_count__3hSWL").text().replaceAll(" ", ""));
    }

    @Override
    public Float fetchRatingImdb(Document html) {
        return Float.parseFloat(html.select(".styles_valueSection__19woS").text().replaceAll("[^0-9.]", ""));
    }

    @Override
    public Integer fetchRatingImdbVotes(Document html) {
        String ratingStr = html.select(".styles_count__gelnz").text().replaceAll(" ", "");
        ratingStr = ratingStr.replace("K", "000");
        return Integer.parseInt(ratingStr);
    }

    @Override
    public Award fetchAward(Document html) {
        try {
            String awardImg = html.select(".styles_link__3wjyE img").attr("src");
            return Award.buildAward(Award.AwardType.awardTypeOf(awardImg),
                    Short.parseShort(html.select(".styles_mainAward__27dye .styles_nominationCount__1Z7w_").text()),
                    !awardImg.contains("Gray")
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Cast> fetchCasts(Document html) {
        List<Cast> casts = new ArrayList<>();
        casts.addAll(findCasts(html, "Режиссер"));
        casts.addAll(findCasts(html, "Композитор"));
        casts.addAll(findCasts(html, "Сценарий"));
        casts.addAll(findActors(html));
        return casts;
    }

    private List<Cast> findCasts(Document html, String roleStr) {
        Cast.Role role = Cast.Role.roleOf(roleStr);
        List<String> names = limitThree(fetchByField(html, role.toString()).split(","));
        List<Cast> casts = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            casts.add(Cast.buildCast(names.get(i), role, (short) (i + 1)));
        }
        return casts;
    }

    private List<Cast> findActors(Document html) {
        Elements elements = html.select(".styles_list__I97eu li");
        List<Cast> casts = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            String name = elements.get(i).text().trim();
            if (name.contains("...")) {
                break;
            }
            casts.add(Cast.buildCast(name, Cast.Role.ACTOR, (short) (i + 1)));
        }
        return casts;
    }

    @Override
    public LocalDate fetchReleaseDate(Document html) {
        return LocalDate.parse(fetchByField(html, "Премьера в мире").split(",")[0], DATE_TIME_FORMATTER);
    }

    @Override
    public Short fetchDuration(Document html) {
        return Short.parseShort(fetchByField(html, "Время").split(" мин")[0]);
    }

    @Override
    public List<String> fetchGenres(Document html) {
        return limitThree(fetchByField(html, "Жанр").split(","));
    }

    @Override
    public List<String> fetchCountries(Document html) {
        return limitThree(fetchByField(html, "Страна").split(","));
    }

    private List<String> limitThree(String[] values) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if (values[i].contains("...") || i == 3) {
                break;
            }
            list.add(values[i].trim());
        }
        return list;
    }
}
