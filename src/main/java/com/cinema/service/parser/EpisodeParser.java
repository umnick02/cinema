package com.cinema.service.parser;

import bt.metainfo.Torrent;
import bt.metainfo.TorrentFile;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cinema.helper.HttpHelper.requestAndGetBody;

@Singleton
public class EpisodeParser {

    private static final Logger logger = LogManager.getLogger(EpisodeParser.class);

    public void parse(Movie movie, Torrent torrent) {
        try {
            String body = requestAndGetBody(movie.getUrl() + "episodes");
            Document html = Jsoup.parse(body);
            movie.setEpisodes(new HashSet<>());
            Set<String> availableEpisodes = getAvailableEpisodes(torrent.getFiles());
            removeExistedEpisodes(movie.getEpisodes(), availableEpisodes);
            for (Element season : html.select("#bySeason option")) {
                fillEpisodesForSeason(movie, Short.parseShort(season.attr("value")), movie.getMagnet(), availableEpisodes);
            }
            movie.setMagnet(null);
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Set<String> getAvailableEpisodes(List<TorrentFile> torrentFiles) {
        HashSet<String> episodes = new HashSet<>();
        for (TorrentFile torrentFile : torrentFiles) {
            String se = fetchSeasonAndEpisode(torrentFile.getPathElements().get(torrentFile.getPathElements().size() - 1));
            if (se != null) {
                episodes.add(se);
            }
        }
        return episodes;
    }

    private void removeExistedEpisodes(Set<Episode> episodes, Set<String> availableEpisodes) {
        Set<String> existedEpisodes = new HashSet<>();
        for (Episode episode : episodes) {
            existedEpisodes.add(String.format("S%02dE%02d", episode.getSeason(), episode.getEpisode()));
        }
        availableEpisodes.removeAll(existedEpisodes);
    }

    private String fetchSeasonAndEpisode(String path) {
        Pattern pattern = Pattern.compile("[s|S][\\d]{2}[e|E][\\d]{2}");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group().toUpperCase();
        }
        return null;
    }

    private void fillEpisodesForSeason(Movie movie, short season, String magnet, Set<String> availableEpisodes) {
        try {
            String body = requestAndGetBody(movie.getUrl() + "episodes?season=" + season);
            Elements episodes = Jsoup.parse(body).select(".eplist .list_item");
            for (short i = 0; i < episodes.size(); i++) {
                if (availableEpisodes.contains(String.format("S%02dE%02d", season, i + 1))) {
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
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
