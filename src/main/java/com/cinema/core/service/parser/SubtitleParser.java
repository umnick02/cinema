package com.cinema.core.service.parser;

import com.cinema.core.config.Lang;
import com.cinema.core.dto.SubtitleFile;
import com.cinema.core.entity.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.cinema.core.helper.HttpHelper.*;

public class SubtitleParser {

    public static void main(String[] args) {
        parseSubtitle("Guns Akimbo", 2019, Lang.EN, null, null);
    }

    public static Set<SubtitleFile> buildSubtitles(SubtitleHolder subtitleHolder, Lang... langs) {
        return Arrays.stream(langs)
                .map(lang -> SubtitleParser.getSubtitleFile(subtitleHolder, lang))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static SubtitleFile getSubtitleFile(SubtitleHolder subtitleHolder, Lang lang) {
        SubtitleFile subtitleFile = getSubtitleFile(subtitleHolder.getSubtitle().getSubtitles(), lang);
        if (subtitleFile != null) {
            return subtitleFile;
        }
        if (subtitleHolder instanceof Movie) {
            Movie movie = (Movie) subtitleHolder;
            return parseSubtitle(movie.getOriginalTitle(), movie.getReleaseDate().getYear(), lang, null, null);
        } else {
            Episode episode = (Episode) subtitleHolder;
            return parseSubtitle(episode.getSeries().getOriginalTitle(), episode.getReleaseDate().getYear(), lang, episode.getSeason(), episode.getEpisode());
        }
    }

    private static SubtitleFile getSubtitleFile(Set<SubtitleFile> subtitles, Lang lang) {
        if (subtitles == null) {
            return null;
        }
        Optional<SubtitleFile> optionalSubtitle = subtitles.stream().filter(subtitle -> subtitle.getLang() == lang).findFirst();
        if (optionalSubtitle.isEmpty()) {
            return null;
        }
        SubtitleFile subtitleFile = optionalSubtitle.get();
        return Files.exists(Paths.get(subtitleFile.getFile())) ? subtitleFile : null;
    }

    private static SubtitleFile parseSubtitle(String movieName, int movieYear, Lang lang, Short season, Short episode) {
        String host = "https://www.opensubtitles.org";
        String path = String.format("/en/search2?MovieName=%s&MovieYear=%d&SubLanguageID=%s&Season=%s&Episode=%s&id=8&action=search&SubFormat=srt&MovieImdbRatingSign=1&MovieYearSign=1",
                URLEncoder.encode(movieName, StandardCharsets.UTF_8), movieYear, lang, Objects.isNull(season) ? "" : season, Objects.isNull(episode) ? "" : episode);
        HttpRequest request = buildRequest(host + path);
        try {
            HttpResponse<InputStream> response = sendRequest(request);
            Document document = Jsoup.parse(fetchResponseBody(response));
            Elements elements = document.select("table#search_results tr");
            String subtitlePath = null;
            if (!elements.isEmpty()) {
                Optional<Element> optional = elements.stream()
                        .filter(row -> row.select("td a").stream()
                                .anyMatch(col -> col.text().matches("\\d+x")))
                        .findFirst();
                if (optional.isPresent()) {
                    subtitlePath = optional.get().select("td a").stream()
                            .filter(col -> col.text().matches("\\d+x"))
                            .findFirst().get()
                            .attr("href");
                }
            } else {
                subtitlePath = document.select("#bt-dwl-bt").attr("href");
            }
            if (Objects.nonNull(subtitlePath) && subtitlePath.length() > 0) {
                HttpRequest subtitleRequest = buildRequest(host + subtitlePath);
                HttpResponse<InputStream> subtitleResponse = sendRequest(subtitleRequest);
                String subtitleFilePath = fetchResponseFile(subtitleResponse, movieName);
                SubtitleFile subtitleFile = new SubtitleFile();
                subtitleFile.setLang(lang);
                subtitleFile.setFile(subtitleFilePath);
                return subtitleFile;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
