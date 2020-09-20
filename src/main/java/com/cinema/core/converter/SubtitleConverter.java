package com.cinema.core.converter;

import com.cinema.core.dto.SubtitleFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Type;
import java.util.Set;

public class SubtitleConverter implements AttributeConverter<Set<SubtitleFile>, String> {

    private final Gson gson = new Gson();
    private final Type type = new TypeToken<Set<SubtitleFile>>(){}.getType();

    @Override
    public String convertToDatabaseColumn(Set<SubtitleFile> attribute) {
        return gson.toJson(attribute);
    }

    @Override
    public Set<SubtitleFile> convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, type);
    }
}
