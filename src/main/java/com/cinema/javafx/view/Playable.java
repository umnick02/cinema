package com.cinema.javafx.view;

public interface Playable {
    void showLoadingView();
    void hideLoadingView();
    void showPlayer();
    void hidePlayer();
    void play(String file);
}
