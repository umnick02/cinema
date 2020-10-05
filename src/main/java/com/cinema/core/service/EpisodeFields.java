package com.cinema.core.service;

import org.jsoup.nodes.Document;

import java.time.LocalDate;

public interface EpisodeFields {
    Short fetchSeason(Document html);
    Short fetchEpisode(Document html);
    String fetchEpisodePoster(Document html);
    Float fetchEpisodeRaring(Document html);
    Float fetchEpisodeVotes(Document html);
    LocalDate fetchEpisodeReleaseDate(Document html);
    String fetchEpisodeTitle(Document html);
}
