package com.cinema.presenter;

import com.cinema.entity.Movie;

public interface PlayerPresentable {
    void play(Movie movie);
    void tryPlay(Movie movie);
}
