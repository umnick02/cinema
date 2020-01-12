package com.cinema.view;

import com.cinema.config.Config;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import static com.cinema.config.Config.getPreference;

public class UIComponents {
    public static EmbeddedMediaPlayerComponent mediaPlayerComponent;
    public static void play(String file) {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        mediaPlayerComponent.mediaPlayer().media().play(getPreference(Config.PrefKey.STORAGE) + file);

    }
}
