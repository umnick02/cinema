package com.cinema.javafx.controller.player;

import com.cinema.core.config.Lang;
import com.cinema.core.config.Preferences;
import com.cinema.core.dto.Subtitle;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.model.impl.SubtitleModel;
import com.cinema.core.service.subtitle.SubtitleService;
import com.cinema.javafx.player.PlayerFactory;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cinema.core.config.Preferences.getPreference;
import static com.cinema.core.model.ModelEventType.*;
import static com.sun.javafx.event.EventUtil.fireEvent;
import static java.time.temporal.ChronoUnit.SECONDS;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.MouseEvent.ANY;

public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    public static EmbeddedMediaPlayer mediaPlayer;
    private static final String HOST = "https://translate.google.com";

    @FXML
    private StackPane playerPane;
    @FXML
    public ImageView videoImageView;
    @FXML
    private AnchorPane controlPane;
    @FXML
    public Label currentTimeLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private ProgressBar timelineBar;
    @FXML
    private ProgressBar downloadBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button playButton;
    @FXML
    private Button volumeButton;
    @FXML
    private Button fullscreenButton;
    @FXML
    private Button closeButton;
    @FXML
    private Menu audioMenu;
    @FXML
    private Menu subtitleMenu;
    @FXML
    private VBox subtitleBox;
    @FXML
    private BorderPane playerControlsPane;
    private WebView subtitleTranslate;
    @FXML
    private VBox subtitleContainer;

    private long subLastUpdate;
    private float sliderLastUpdate;

    private int lastVolume = 50;
    private boolean isAudioMenuSet = false;
    private AtomicBoolean isPaused = new AtomicBoolean(false);

    private final AtomicBoolean timelineTracking = new AtomicBoolean();
    private final AtomicBoolean volumeTracking = new AtomicBoolean();

    private Timer mouseTimer = new Timer();
    private Timer preventSleepTimer = new Timer();
    private LocalDateTime lastMouseMove;
    private String lastClickedWord = null;

    private EventHandler<MouseEvent> mouseMovedEventHandler = event -> {
        lastMouseMove = LocalDateTime.now();
        controls(true);
    };

    private TimerTask mouseHideTask = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                if (SECONDS.between(lastMouseMove, LocalDateTime.now()) > 3) {
                    System.out.println("mouseHideTask");
                    controls(false);
                }
            });
        }
    };

    private Timer timer;

    public void play() {
        mediaPlayer.submit(() ->
                mediaPlayer.media().play(
                        getPreference(Preferences.PrefKey.STORAGE) +
                                SceneModel.INSTANCE.getActiveMovieModel().getMovie().getFile()
                )
        );
    }

    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fireEvent(new Event(PLAYER_TICK.getEventType()));
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void bindVideoImageViewSize() {
        videoImageView.fitWidthProperty().bind(playerPane.getScene().widthProperty());
        videoImageView.fitHeightProperty().bind(playerPane.getScene().heightProperty());
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Movie movie = SceneModel.INSTANCE.getActiveMovieModel().getMovie();
            Long fileSize = movie.getFileSize();
            if (fileSize != null) {
                File file = new File(Preferences.getPreference(Preferences.PrefKey.STORAGE) + movie.getFile());
                if (file.exists()) {
                    downloadBar.setProgress((float) file.length() / fileSize);
                }
            }
            durationLabel.setText(String.format(""));

            subtitleTranslate = new WebView();
            subtitleTranslate.setPrefHeight(200);
            subtitleTranslate.setPrefWidth(Double.MAX_VALUE);
            subtitleTranslate.getEngine().setJavaScriptEnabled(true);
            subtitleTranslate.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    subtitleTranslate.getEngine().executeScript("javascript:(function() { " +
                            "document.getElementsByClassName('input-button-container')[0].style.display='none'; " +
                            "document.getElementsByClassName('tlid-language-bar')[0].style.display='none'; " +
                            "document.getElementsByClassName('tlid-source-target')[0].style.display='none'; " +
                            "document.getElementsByClassName('tlid-source-target')[0].style.paddingTop=0; " +
                            "document.getElementsByClassName('frame')[0].style.height='auto'; " +
                            "document.getElementsByClassName('frame')[0].style.overflowY='auto'; " +
                            "document.getElementsByClassName('notification-area')[0].style.display='none'; " +
                            "document.getElementsByClassName('gp-footer')[0].style.display='none'; " +
                            "document.getElementsByTagName('header')[0].style.display='none'; " +
                            "})()");
                }
            });
            mediaPlayer = PlayerFactory.buildMediaPlayer(videoImageView);
            mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                @Override
                public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {}

                @Override
                public void opening(MediaPlayer mediaPlayer) {
                    System.out.println("opening");
                    Platform.runLater(() -> volumeSlider.setValue(lastVolume));
                }

                @Override
                public void buffering(MediaPlayer mediaPlayer, float newCache) {
                }

                @Override
                public void playing(MediaPlayer mediaPlayer) {
                    System.out.println("playing");
                    Platform.runLater(() -> {
                        subtitleContainer.getChildren().remove(subtitleTranslate);
                        playButton.getStyleClass().remove("icon-play");
                        if (!playButton.getStyleClass().contains("icon-pause")) {
                            playButton.getStyleClass().add("icon-pause");
                        }
                    });
                    if (!isAudioMenuSet) {
                        isAudioMenuSet = true;
                        ToggleGroup toggleGroup = new ToggleGroup();
                        Platform.runLater(() -> mediaPlayer.audio().trackDescriptions().forEach(trackDescription -> {
                            RadioMenuItem menuItem = new RadioMenuItem(trackDescription.description());
                            menuItem.setOnAction(event -> mediaPlayer.audio().setTrack(trackDescription.id()));
                            toggleGroup.getToggles().add(menuItem);
                            if (mediaPlayer.audio().track() == trackDescription.id()) {
                                toggleGroup.selectToggle(menuItem);
                            }
                            audioMenu.getItems().add(menuItem);
                        }));
                    }
                    startTimer();
                }

                @Override
                public void paused(MediaPlayer mediaPlayer) {
                    System.out.println("paused");
                    Platform.runLater(() -> {
                        playButton.getStyleClass().remove("icon-pause");
                        playButton.getStyleClass().add("icon-play");
                    });
                    stopTimer();
                }

                @Override
                public void stopped(MediaPlayer mediaPlayer) {
                    System.out.println("stopped");
                    stopTimer();
                }

                @Override
                public void forward(MediaPlayer mediaPlayer) {
                    System.out.println("forward");
                }

                @Override
                public void backward(MediaPlayer mediaPlayer) {
                    System.out.println("backward");
                }

                @Override
                public void finished(MediaPlayer mediaPlayer) {
                    System.out.println("finished");
                    stopTimer();
                }

                @Override
                public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                    if (!SubtitleModel.INSTANCE.isShowSubtitles() || newTime - subLastUpdate < 100) {
                        return;
                    }
                    subLastUpdate = newTime;
                    Subtitle subtitle = SubtitleModel.INSTANCE.actualSubtitle(newTime);
                    if (subtitle != null) {
                        if (subtitle.equals(SubtitleModel.INSTANCE.getActiveSubtitle())) {
                            return;
                        }
                        if (!subtitleBox.isVisible()) {
                            Platform.runLater(() -> {
                                subtitleBox.getChildren().clear();
                                subtitleBox.setVisible(true);
                            });
                        }

                        List<HBox> subtitleTextContainer = new ArrayList<>();
                        List<Label> elements = new ArrayList<>();
                        Stage stage = ((Stage) videoImageView.getScene().getWindow());
                        boolean italic = false;
                        for (String element : subtitle.getElements()) {
                            if (element.equals("\n")) {
                                subtitleTextContainer.add(buildSubtitleRow(elements));
                                elements = new ArrayList<>();
                                continue;
                            }
                            if (element.equals("<i>")) {
                                italic = true;
                                continue;
                            }
                            if (element.equals("</i>")) {
                                italic = false;
                                continue;
                            }

                            Label text = new Label();
                            text.setText(element);
                            text.getStyleClass().addAll("subtitle-element", stage.isFullScreen() ? "sub-big" : "sub-small");
                            if (italic) {
                                text.getStyleClass().add("text-italic");
                            }
                            if (SubtitleService.isWord(element)) {
                                text.setCursor(Cursor.HAND);
                                text.setOnMouseClicked((event) -> {
                                    if ((lastClickedWord != null && lastClickedWord.equals(text.getText())) || !subtitleContainer.getChildren().contains(subtitleTranslate)) {
                                        if (subtitleContainer.getChildren().contains(subtitleTranslate)) {
                                            subtitleContainer.getChildren().remove(subtitleTranslate);
                                        } else {
                                            subtitleContainer.getChildren().add(0, subtitleTranslate);
                                        }
                                    }
                                    if (subtitleContainer.getChildren().contains(subtitleTranslate)) {
                                        isPaused = new AtomicBoolean(true);
                                        paused(mediaPlayer);
                                        lastClickedWord = text.getText();
                                        translate(text.getText(), SubtitleModel.INSTANCE.getLang(), Lang.RU);
                                    }
                                });
                            }
                            elements.add(text);
                        }
                        if (!elements.isEmpty()) {
                            subtitleTextContainer.add(buildSubtitleRow(elements));
                            Platform.runLater(() -> {
                                subtitleBox.getChildren().clear();
                                subtitleBox.getChildren().addAll(subtitleTextContainer);
                            });
                        }
                    } else {
                        if (subtitleBox.isVisible()) {
                            Platform.runLater(() -> {
                                subtitleBox.setVisible(false);
                                subtitleBox.getChildren().clear();
                            });
                        }
                    }
                }

                @Override
                public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                    if (newPosition - sliderLastUpdate > 0.001) {
                        sliderLastUpdate = newPosition;
                        updateMovieTrackingPosition(newPosition);
                    }
                }

                @Override
                public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
                    System.out.printf("seekableChanged: %s\n", newSeekable);
                    super.seekableChanged(mediaPlayer, newSeekable);
                }

                @Override
                public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
                    super.pausableChanged(mediaPlayer, newPausable);
                }

                @Override
                public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
                    super.titleChanged(mediaPlayer, newTitle);
                }

                @Override
                public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                    super.snapshotTaken(mediaPlayer, filename);
                }

                @Override
                public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                    super.lengthChanged(mediaPlayer, newLength);
                }

                @Override
                public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                    super.videoOutput(mediaPlayer, newCount);
                }

                @Override
                public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
                    super.scrambledChanged(mediaPlayer, newScrambled);
                }

                @Override
                public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id) {
                    super.elementaryStreamAdded(mediaPlayer, type, id);
                }

                @Override
                public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id) {
                    super.elementaryStreamDeleted(mediaPlayer, type, id);
                }

                @Override
                public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id) {
                    super.elementaryStreamSelected(mediaPlayer, type, id);
                }

                @Override
                public void corked(MediaPlayer mediaPlayer, boolean corked) {
                    System.out.format("corked: %s\n", corked);
                }

                @Override
                public void muted(MediaPlayer mediaPlayer, boolean muted) {
                    System.out.format("muted: %s\n", muted);
                    Platform.runLater(() -> {
                        if (muted) {
                            if (mediaPlayer.audio().volume() > 0) {
                                lastVolume = mediaPlayer.audio().volume();
                            }
                            volumeSlider.setValue(0);
                            volumeButton.getStyleClass().remove("icon-volume");
                            volumeButton.getStyleClass().add("icon-mute");

                        } else {
                            volumeSlider.setValue(lastVolume);
                            if (!volumeButton.getStyleClass().contains("icon-volume")) {
                                volumeButton.getStyleClass().add("icon-volume");
                            }
                        }
                    });
                }

                @Override
                public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
                    System.out.format("volumeChanged: %s\n", volume);
                }

                @Override
                public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
                    super.audioDeviceChanged(mediaPlayer, audioDevice);
                }

                @Override
                public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
                    super.chapterChanged(mediaPlayer, newChapter);
                }

                @Override
                public void error(MediaPlayer mediaPlayer) {
                    System.out.println("error");
                }

                @Override
                public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                    System.out.println("mediaPlayerReady");
                }
            });

            play();
            bindVideoImageViewSize();
            ToggleGroup toggleGroup = new ToggleGroup();
            buildSubtitleMenuItems().forEach(menuItem -> {
                toggleGroup.getToggles().add(menuItem);
                subtitleMenu.getItems().add(menuItem);
            });
        });

        subtitleBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                if (mediaPlayer.status().isPlaying()) {
                    mediaPlayer.controls().pause();
                }
            } else if (oldValue && !newValue) {
                if (!isPaused.get() && !mediaPlayer.status().isPlaying()) {
                    mediaPlayer.controls().play();
                }
            }
        });
        controlPane.prefWidthProperty().bind(playerPane.widthProperty());
        timelineBar.progressProperty().addListener((obs, oldValue, newValue) -> updateMediaPlayerPosition(newValue.floatValue()));
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> updateMediaPlayerVolumePosition(newValue.intValue()));
    }

    public void translate(String word, Lang from, Lang to) {
        String url = String.format("%s/#view=home&op=translate&sl=%s&tl=%s&text=%s", HOST, from.name().toLowerCase(), to.name().toLowerCase(), word);
        subtitleTranslate.getEngine().load(url);
    }

    private HBox buildSubtitleRow(List<Label> elements) {
        HBox subtitleRow = new HBox();
        subtitleRow.setAlignment(Pos.CENTER);
        subtitleRow.getChildren().addAll(elements);
        return subtitleRow;
    }

    private List<RadioMenuItem> buildSubtitleMenuItems() {
        Set<Magnet.Subtitle> subtitles;
        if (!SceneModel.INSTANCE.getActiveMovieModel().isSeries()) {
            subtitles = SceneModel.INSTANCE.getActiveMovieModel().getMovie().getSubtitles();
        } else {
            subtitles = SceneModel.INSTANCE.getActiveMovieModel().getActiveSeasonModel().getActiveEpisodeModel().getEpisode().getSubtitles();
        }
        List<RadioMenuItem> subtitleItems = new ArrayList<>();
        RadioMenuItem offMenuItem = new RadioMenuItem("Off");
        offMenuItem.setId("Off");
        offMenuItem.setOnAction(event -> Platform.runLater(() -> SubtitleModel.INSTANCE.setShowSubtitles(false)));
        subtitleItems.add(offMenuItem);
        for (Magnet.Subtitle subtitle : subtitles) {
            RadioMenuItem menuItem = new RadioMenuItem(subtitle.getLang().getFullName());
            menuItem.setId(subtitle.getLang().name());
            ImageView imageView = new ImageView("/icons/" + subtitle.getLang().getIcon());
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            menuItem.setGraphic(imageView);
            menuItem.setOnAction(event -> {
                Set<Subtitle> subtitlesDto = SubtitleService.buildSubtitles(Lang.valueOf(((RadioMenuItem) event.getTarget()).getId()));
                if (subtitlesDto != null) {
                    Platform.runLater(() -> {
                        SubtitleModel.INSTANCE.setLang(subtitle.getLang());
                        SubtitleModel.INSTANCE.setSubtitles(subtitlesDto);
                    });
                }
            });
            subtitleItems.add(menuItem);
        }
        return subtitleItems;
    }

    @FXML
    public void closePlayer() {
        Stage stage = ((Stage) videoImageView.getScene().getWindow());
        if (stage.isFullScreen()) {
            changeFullscreenStatus();
        }
        stop();
        playerPane.fireEvent(new Event(SHUTDOWN.getEventType()));
    }

    private static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.controls().stop();
            mediaPlayer.release();
        }
    }

    @FXML
    public void changePlayingShortcut(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                changeFullscreenStatus();
            }
            changePlaying();
        }
    }

    @FXML
    public void changePlaying() {
        isPaused = new AtomicBoolean(mediaPlayer.status().isPlaying());
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().pause();
        } else {
            mediaPlayer.controls().play();
        }
    }

    @FXML
    public void settings() {

    }

    @FXML
    public void changeFullscreenStatus() {
        Stage stage = ((Stage) videoImageView.getScene().getWindow());
        if (stage.isFullScreen()) {
            stage.getScene().removeEventHandler(ANY, mouseMovedEventHandler);
            stage.setFullScreen(false);
            fullscreenButton.getStyleClass().remove("icon-fullscreen-out");
            fullscreenButton.getStyleClass().add("icon-fullscreen-in");
            closeButton.setVisible(true);
//            mouseTimer.cancel();
//            preventSleepTimer.cancel();
        } else {
            stage.getScene().setOnMouseMoved(mouseMovedEventHandler);
            stage.setFullScreen(true);
            fullscreenButton.getStyleClass().remove("icon-fullscreen-in");
            fullscreenButton.getStyleClass().add("icon-fullscreen-out");
            closeButton.setVisible(false);
//            mouseTimer = new Timer();
//            mouseTimer.scheduleAtFixedRate(mouseHideTask, 0, 3000);
//            preventSleepTimer = new Timer();
//            preventSleepTimer.scheduleAtFixedRate(mouseMoveTask, 0, 600000);
        }
    }

    private void controls(boolean visible) {
        playerControlsPane.setVisible(visible);
        closeButton.setVisible(visible);
        videoImageView.getScene().setCursor(visible ? Cursor.DEFAULT : Cursor.NONE);
    }

    @FXML
    public void volume() {
        mediaPlayer.audio().setMute(!mediaPlayer.audio().isMute());
    }

    private synchronized void updateMediaPlayerPosition(float newValue) {
        System.out.println("updateMediaPlayerPosition: " + newValue);
        if (timelineTracking.get()) {
            mediaPlayer.controls().setPosition(newValue);
        }
    }

    private synchronized void updateMediaPlayerVolumePosition(int newValue) {
        if (volumeTracking.get()) {
            mediaPlayer.audio().setVolume(newValue);
        }
    }

    @FXML
    private synchronized void beginMovieTracking() {
        System.out.println("beginMovieTracking");
        timelineTracking.set(true);
    }

    @FXML
    private synchronized void endMovieTracking(Event event) {
        System.out.println("endMovieTracking");

        timelineTracking.set(false);
        subLastUpdate = 0;
        float ratio = (float) (((MouseEvent) event).getX() / timelineBar.getWidth());
        Movie movie = SceneModel.INSTANCE.getActiveMovieModel().getMovie();
        Long fileSize = movie.getFileSize();
        if (fileSize != null) {
            File file = new File(Preferences.getPreference(Preferences.PrefKey.STORAGE) + movie.getFile());
            if (file.exists() && file.length() > fileSize * ratio) {
                timelineBar.setProgress(ratio);
                mediaPlayer.controls().setPosition(ratio);
            }
        }
    }

    @FXML
    private synchronized void beginVolumeTracking() {
        volumeTracking.set(true);
    }

    @FXML
    private synchronized void endVolumeTracking() {
        volumeTracking.set(false);
        mediaPlayer.audio().setVolume((int) volumeSlider.getValue());
        lastVolume = (int) volumeSlider.getValue();
        if (mediaPlayer.audio().isMute()) {
            volume();
        }
    }

    private synchronized void updateMovieTrackingPosition(float newValue) {
        if (!timelineTracking.get()) {
            System.out.println("updateMovieTrackingPosition: " + newValue);
            timelineBar.setProgress(newValue);
        }
    }

    private void hotKeys() {
        Scene scene = videoImageView.getScene();
        KeyCombination kc = new KeyCodeCombination(SPACE);
        scene.getAccelerators().put(kc, this::changePlaying);
    }
}
