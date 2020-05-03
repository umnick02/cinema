package com.cinema.javafx.controller.player;

import com.cinema.javafx.model.PlayerModel;
import com.cinema.javafx.player.Time;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerControlsController {

    @FXML
    private Label currentTimeLabel;
    @FXML
    private Slider timelineSlider;
    @FXML
    private Label durationLabel;

    private final MediaPlayer mediaPlayer;

    private final AtomicBoolean tracking = new AtomicBoolean();

    private Timer clockTimer = new Timer();

    public PlayerControlsController() {
        this.mediaPlayer = PlayerModel.INSTANCE.getEmbeddedMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                startTimer();
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                stopTimer();
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                stopTimer();
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                stopTimer();
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                stopTimer();
            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                Platform.runLater(() -> updateDuration(newLength));
            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                Platform.runLater(() -> updateSliderPosition(newPosition));
            }
        });
    }

    @FXML
    public void initialize() {
        timelineSlider.valueProperty().addListener((obs, oldValue, newValue) -> updateMediaPlayerPosition(newValue.floatValue() / 100));
    }

    @FXML
    public void play() {
        mediaPlayer.controls().play();
    }

    @FXML
    public void pause() {
        mediaPlayer.controls().pause();
    }

    @FXML
    public void stop() {
        mediaPlayer.controls().stop();
    }

    private void startTimer() {
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> currentTimeLabel.setText(Time.formatTime(mediaPlayer.status().time())));
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        clockTimer.cancel();
    }

    private void updateDuration(long newValue) {
        durationLabel.setText(Time.formatTime(newValue));
    }

    private synchronized void updateMediaPlayerPosition(float newValue) {
        if (tracking.get()) {
            mediaPlayer.controls().setPosition(newValue);
        }
    }

    @FXML
    private synchronized void beginTracking() {
        tracking.set(true);
    }

    @FXML
    private synchronized void endTracking() {
        tracking.set(false);
        // This deals with the case where there was an absolute click in the timeline rather than a drag
        mediaPlayer.controls().setPosition((float) timelineSlider.getValue() / 100);
    }

    private synchronized void updateSliderPosition(float newValue) {
        if (!tracking.get()) {
            timelineSlider.setValue(newValue * 100);
        }
    }
}
