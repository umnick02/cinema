package com.cinema.core.converter;

import com.cinema.core.entity.Magnet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Type;
import java.util.List;

public class SubtitleConverter implements AttributeConverter<List<Magnet.Subtitle>, String> {

    private final Gson gson = new Gson();
    private final Type type = new TypeToken<List<Magnet.Subtitle>>(){}.getType();

    @Override
    public String convertToDatabaseColumn(List<Magnet.Subtitle> attribute) {
        return gson.toJson(attribute);
    }

    @Override
    public List<Magnet.Subtitle> convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, type);
    }
}
