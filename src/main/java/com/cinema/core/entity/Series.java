package com.cinema.core.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;

public class Series extends Movie {

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Episode> episodes;

    @Override
    public boolean isSeries() {
        return true;
    }

    public Set<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Set<Episode> episodes) {
        this.episodes = episodes;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public short getMaxSeason() {
        return episodes.stream().map(Episode::getSeason).max(Short::compareTo).orElseGet(() -> (short) 0);
    }

    @Override
    public String fetchType() {
        return episodes.stream()
                .max(Comparator.comparing(Episode::getSeason))
                .map(value -> String.format("Сериал (%d сезонов)", value.getSeason()))
                .orElse(null);
    }

    @Override
    public String fetchTitle() {
        if (finishDate == null) {
            return String.format("%s (%d — %s)", getTitle(), getReleaseDate().getYear(),  ". . .");
        } else {
            return String.format("%s (%d — %d)", getTitle(), getReleaseDate().getYear(),  finishDate.getYear());
        }
    }
}
