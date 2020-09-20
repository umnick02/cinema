package com.cinema.core.entity;

import com.cinema.core.converter.SubtitleConverter;
import com.cinema.core.dto.SubtitleFile;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.util.Set;

@Embeddable
public class Subtitle {

    @Convert(converter = SubtitleConverter.class)
    @Column(name = "subtitle_files", columnDefinition = "text")
    private Set<SubtitleFile> subtitleFiles;

    public Set<SubtitleFile> getSubtitles() {
        return subtitleFiles;
    }

    public void setSubtitles(Set<SubtitleFile> subtitles) {
        this.subtitleFiles = subtitles;
    }
}
