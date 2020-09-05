package com.cinema.core.service.parser;

import com.cinema.core.config.Lang;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.entity.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.cinema.core.helper.HttpHelper.*;

public class SubtitleParser {

    public static void main(String[] args) {
        parseSubtitle("Guns Akimbo", 2019, Lang.EN, null, null);
    }

    public static Magnet.Subtitle getSubtitleFile(Source source, Lang lang) {
        Magnet.Subtitle subtitle = getSubtitle(source.getSubtitles(), lang);
        if (Objects.nonNull(subtitle)) {
            return subtitle;
        }
        if (source instanceof Movie) {
            Movie movie = (Movie) source;
            return parseSubtitle(movie.getTitle(), movie.getReleaseDate().getYear(), lang, null, null);
        } else {
            Episode episode = (Episode) source;
            return parseSubtitle(episode.getTitle(), episode.getReleaseDate().getYear(), lang, episode.getSeason(), episode.getEpisode());
        }
    }

    private static Magnet.Subtitle getSubtitle(List<Magnet.Subtitle> subtitles, Lang lang) {
        if (Objects.isNull(subtitles)) {
            return null;
        }
        Optional<Magnet.Subtitle> optionalSubtitle = subtitles.stream().filter(subtitle -> subtitle.getLang() == lang).findFirst();
        if (optionalSubtitle.isEmpty()) {
            return null;
        }
        Magnet.Subtitle subtitle = optionalSubtitle.get();
        return Files.exists(Paths.get(subtitle.getFile())) ? subtitle : null;
    }

    private static Magnet.Subtitle parseSubtitle(String movieName, int movieYear, Lang lang, Short season, Short episode) {
        String host = "https://www.opensubtitles.org";
        String path = String.format("/en/search2?MovieName=%s&MovieYear=%d&SubLanguageID=%s&Season=%s&Episode=%s&id=8&action=search&SubFormat=srt&MovieImdbRatingSign=1&MovieYearSign=1",
                URLEncoder.encode(movieName, StandardCharsets.UTF_8), movieYear, lang, Objects.isNull(season) ? "" : season, Objects.isNull(episode) ? "" : episode);
        HttpRequest request = buildRequest(host + path);
        try {
            HttpResponse<InputStream> response = sendRequest(request);
            Elements elements = Jsoup.parse(fetchResponseBody(response)).select("table#search_results tr");
            Optional<Element> optional = elements.stream()
                    .filter(row -> row.select("td a").stream()
                            .anyMatch(col -> col.text().matches("\\d+x")))
                    .findFirst();
            if (optional.isPresent()) {
                String subtitlePath = optional.get().select("td a").stream()
                        .filter(col -> col.text().matches("\\d+x"))
                        .findFirst().get()
                        .attr("href");
                HttpRequest subtitleRequest = buildRequest(host + subtitlePath);
                HttpResponse<InputStream> subtitleResponse = sendRequest(subtitleRequest);
                String subtitleFilePath = fetchResponseFile(subtitleResponse, movieName);
                Magnet.Subtitle subtitle = new Magnet.Subtitle();
                subtitle.setLang(lang);
                subtitle.setFile(subtitleFilePath);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
