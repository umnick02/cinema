package com.cinema.core.service;

import com.cinema.core.entity.Movie;

import java.io.IOException;

public interface Parser {
    void parse(Movie movie) throws IOException, InterruptedException;
}
