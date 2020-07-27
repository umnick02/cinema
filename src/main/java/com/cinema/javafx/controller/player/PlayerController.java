package com.cinema.javafx.controller.player;

import com.cinema.javafx.CinemaApplication;
import com.cinema.javafx.model.PlayerModel;
import com.cinema.javafx.player.Time;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cinema.core.model.ModelEventType.SHUTDOWN;

public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @FXML
    private StackPane playerPane;
    @FXML
    private ImageView videoImageView;
    @FXML
    private BorderPane controlPane;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Slider timelineSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label durationLabel;
    @FXML
    private Button pauseButton;
    @FXML
    private Button playButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button volumeButton;

    private int lastVolume = 50;

    private final MediaPlayer mediaPlayer;

    private final AtomicBoolean tracking = new AtomicBoolean();
    private final AtomicBoolean volumeTracking = new AtomicBoolean();

    private Timer clockTimer = new Timer();

    public PlayerController() {
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

            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
                Platform.runLater(() -> updateVolumeSliderPosition(volume * 100));
            }
        });
    }

    @FXML
    public void initialize() {
        PlayerModel.INSTANCE.setVideoImageView(videoImageView);
        PlayerModel.INSTANCE.play();

        videoImageView.fitWidthProperty().bind(playerPane.widthProperty());
        videoImageView.fitHeightProperty().bind(playerPane.heightProperty());

        controlPane.prefWidthProperty().bind(playerPane.widthProperty());

        PlayerModel.INSTANCE.registerEventTarget(playerPane);
        playerPane.addEventHandler(SHUTDOWN.getEventType(), event -> {
            logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
            PlayerModel.INSTANCE.unRegisterEventTarget(playerPane);
            PlayerModel.INSTANCE.stop();
        });
        timelineSlider.valueProperty().addListener((obs, oldValue, newValue) -> updateMediaPlayerPosition(newValue.floatValue() / 100));
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> updateMediaPlayerVolumePosition(newValue.intValue()));
        playButton.setVisible(false);
        pauseButton.setVisible(true);

        muteButton.setVisible(false);
        volumeButton.setVisible(true);
    }

    @FXML
    public void play() {
        mediaPlayer.controls().play();
        pauseButton.setVisible(true);
        playButton.setVisible(false);
    }

    @FXML
    public void pause() {
        mediaPlayer.controls().pause();
        playButton.setVisible(true);
        pauseButton.setVisible(false);
    }

    @FXML
    public void settings() {

    }

    @FXML
    public void fullscreen() {
        Stage stage = ((Stage) videoImageView.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    public void mute() {
        mediaPlayer.audio().setMute(true);
        if (mediaPlayer.audio().volume() > 0) {
            lastVolume = mediaPlayer.audio().volume();
        }
        mediaPlayer.audio().setVolume(0);
        muteButton.setVisible(true);
        volumeButton.setVisible(false);
    }

    @FXML
    public void volume() {
        mediaPlayer.audio().setMute(false);
        if (mediaPlayer.audio().volume() == 0) {
            mediaPlayer.audio().setVolume(lastVolume);
        }
        volumeButton.setVisible(true);
        muteButton.setVisible(false);
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

    private synchronized void updateMediaPlayerVolumePosition(int newValue) {
        if (volumeTracking.get()) {
            mediaPlayer.audio().setVolume(newValue);
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

    @FXML
    private synchronized void beginVolumeTracking() {
        volumeTracking.set(true);
    }

    @FXML
    private synchronized void endVolumeTracking() {
        volumeTracking.set(false);
        // This deals with the case where there was an absolute click in the timeline rather than a drag
        mediaPlayer.audio().setVolume((int) volumeSlider.getValue());
    }

    private synchronized void updateSliderPosition(float newValue) {
        if (!tracking.get()) {
            timelineSlider.setValue(newValue * 100);
        }
    }

    private synchronized void updateVolumeSliderPosition(float newValue) {
        if (!volumeTracking.get()) {
            volumeSlider.setValue(Math.max(0, newValue));
            if (!mediaPlayer.audio().isMute()) {
                if (volumeSlider.getValue() > 0) {
                    lastVolume = (int) volumeSlider.getValue();
                }
            } else if (volumeSlider.getValue() > 0) {
                lastVolume = (int) volumeSlider.getValue();
                volume();
            }
        }
    }
}
