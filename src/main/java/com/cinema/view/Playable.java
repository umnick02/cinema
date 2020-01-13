package com.cinema.view;

public interface Playable {
    void showLoadingView();
    void hideLoadingView();
    void showPlayer();
    void hidePlayer();
    void play(String file);
}
