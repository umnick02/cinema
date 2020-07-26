package com.cinema.core.service;

import com.cinema.core.dto.Award;
import com.cinema.core.dto.Cast;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.List;

public interface MovieFields {
    String fetchTitle(Document html);
    String fetchOriginalTitle(Document html);
    String fetchPoster(Document html);
    List<String> fetchPosters(Document html);
    String fetchTrailer(String title);
    String fetchDescription(Document html);
    Float fetchRatingKp(Document html);
    Integer fetchRatingKpVotes(Document html);
    Float fetchRatingImdb(Document html);
    Integer fetchRatingImdbVotes(Document html);
    Award fetchAward(Document html);
    List<Cast> fetchCasts(Document html);
    LocalDate fetchReleaseDate(Document html);
    Short fetchDuration(Document html);
    List<String> fetchGenres(Document html);
    List<String> fetchCountries(Document html);
}
