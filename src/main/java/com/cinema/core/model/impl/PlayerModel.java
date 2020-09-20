package com.cinema.core.model.impl;

import com.cinema.core.model.ObservableModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerModel extends ObservableModel {

    public static final PlayerModel INSTANCE = new PlayerModel();

    private PlayerModel() {}

    private AtomicBoolean isFullScreen = new AtomicBoolean(false);

    public boolean isFullScreen() {
        return isFullScreen.get();
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen.set(isFullScreen);
    }
}
