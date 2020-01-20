package com.cinema.service.parser;

import bt.metainfo.Torrent;
import com.cinema.entity.Episode;
import com.cinema.entity.Movie;
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
import java.util.Locale;

import static com.cinema.helper.HttpHelper.requestAndGetBody;

@Singleton
public class EpisodeParser {

    private static final Logger logger = LogManager.getLogger(EpisodeParser.class);

    public static void parse(Movie movie, String magnet) {
        try {
            String body = requestAndGetBody(movie.getMovieEn().getUrl() + "/episodes");
            Document html = Jsoup.parse(body);
            movie.setEpisodes(new ArrayList<>());
            for (Element season : html.select("#bySeason option")) {
                fillEpisodesForSeason(movie, Short.parseShort(season.attr("value")), magnet);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void fillEpisodesForSeason(Movie movie, short season, String magnet) {
        try {
            String body = requestAndGetBody(movie.getMovieEn().getUrl() + "/episodes?season=" + season);
            Elements episodes = Jsoup.parse(body).select(".eplist .list_item");
            for (short i = 0; i < episodes.size(); i++) {
                Element e = episodes.get(i);
                Episode episode = new Episode();
                String date = e.select(".airdate").text().replaceAll("\\.", "");
                if (date.length() < 11) date = "0" + date;
                episode.setReleaseDate(
                        LocalDate.parse(date,
                                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US))
                );
                episode.setRating(Float.parseFloat(e.selectFirst(".ipl-rating-star__rating").text()));
                episode.setRatingVotes(Integer.parseInt(
                        e.selectFirst(".ipl-rating-star__total-votes").text().replaceAll("[^0-9]+", "")
                ));
                episode.setPoster(e.select(".image img").attr("src"));
                episode.setMagnet(magnet);
                episode.setSeason(season);
                episode.setTitle(e.select("strong a").attr("title"));
                episode.setSeries(movie);
                episode.setEpisode((short) (i + 1));
                movie.getEpisodes().add(episode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
