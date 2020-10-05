package com.cinema.core.service.parser;

import com.cinema.core.entity.Movie;
import com.cinema.core.entity.Series;
import com.cinema.core.service.EpisodeFields;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class SeriesParser extends MovieParser {

    private static final Logger logger = LoggerFactory.getLogger(SeriesParser.class);

    public static SeriesParser INSTANCE = new SeriesParser();

    private SeriesParser() {}

    @Override
    public void parse(Movie movie) throws IOException, InterruptedException {
        super.parse(movie);
//        ((Series) movie).setEpisodes();
    }

    @Override
    public String fetchTitle(Document html) {
        return html.select("h1[class^=styles_title]").text().split(" \\(")[0].trim();
    }

    public static class EpisodeParser implements EpisodeFields {

        @Override
        public Short fetchSeason(Document html) {
            return null;
        }

        @Override
        public Short fetchEpisode(Document html) {
            return null;
        }

        @Override
        public String fetchEpisodePoster(Document html) {
            return null;
        }

        @Override
        public Float fetchEpisodeRaring(Document html) {
            return null;
        }

        @Override
        public Float fetchEpisodeVotes(Document html) {
            return null;
        }

        @Override
        public LocalDate fetchEpisodeReleaseDate(Document html) {
            return null;
        }

        @Override
        public String fetchEpisodeTitle(Document html) {
            return null;
        }
    }
}
