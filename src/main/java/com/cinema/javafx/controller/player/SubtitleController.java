package com.cinema.javafx.controller.player;

import com.cinema.core.config.Lang;
import com.cinema.core.dto.SubtitleFileEntry;
import com.cinema.core.model.impl.PlayerModel;
import com.cinema.core.model.impl.SubtitleModel;
import com.cinema.core.service.Stoppable;
import com.cinema.core.service.player.subtitle.SubtitleService;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cinema.core.model.ModelEventType.*;

public class SubtitleController implements Stoppable {

    private static final String HOST = "https://translate.google.com";

    enum Style {
        ITALIC, BOLD, UNDERLINE, NONE
    }

    private EmbeddedMediaPlayer mediaPlayer;
    private MediaPlayerEventAdapter eventAdapter;

    @FXML
    private VBox subVBox;
    @FXML
    private VBox subTextVBox;

    private VBox playerBottom;
    private WebView subTranslateWebView;


    private AtomicBoolean playerWasPaused = new AtomicBoolean(false);
    private long subLastRenderTime = 0;
    private long subRenderPeriod = 100;
    private String lastClickedWord = null;

    public SubtitleController(EmbeddedMediaPlayer mediaPlayer, VBox playerBottom) {
        this.mediaPlayer = mediaPlayer;
        this.playerBottom = playerBottom;
        initTranslateWebView();
        eventAdapter = buildEventAdapter();
        mediaPlayer.events().addMediaPlayerEventListener(eventAdapter);
    }

    @FXML
    public void initialize() {
        subVBox.addEventFilter(SUBTITLE_UPDATE.getEventType(), event -> {
            if (SubtitleModel.INSTANCE.getSubtitleFileEntry() == null) {
                hideSub();
                return;
            }
            renderSubElements(SubtitleModel.INSTANCE.getSubtitleFileEntry());
        });
        SubtitleModel.INSTANCE.registerEventTarget(subVBox);
        subTextVBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                if (mediaPlayer.status().isPlaying()) {
                    playerWasPaused.set(false);
                    mediaPlayer.controls().pause();
                } else {
                    playerWasPaused.set(true);
                }
            } else if (oldValue && !newValue) {
                if (!playerWasPaused.get() && !mediaPlayer.status().isPlaying()) {
                    mediaPlayer.controls().play();
                }
            }
        });
        playerBottom.getChildren().add(0, subVBox);
    }

    private MediaPlayerEventAdapter buildEventAdapter() {
        return new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> removeSubTranslate());
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                if (Math.abs(newTime - subLastRenderTime) < subRenderPeriod) {
                    return;
                }
                subLastRenderTime = newTime;
                SubtitleModel.INSTANCE.actualSubtitleFileEntry(newTime);
            }
        };
    }

    private void renderSubElements(SubtitleFileEntry subtitle) {
        List<HBox> subtitleTextContainer = new ArrayList<>();
        List<Label> elements = new ArrayList<>();
        Style style = Style.NONE;
        for (String element : subtitle.getElements()) {
            if (element.equals("\n")) {
                subtitleTextContainer.add(buildSubtitleRow(elements));
                elements = new ArrayList<>();
                continue;
            }
            if (element.equals("<i>")) {
                style = Style.ITALIC;
                continue;
            }
            if (element.equals("</i>")) {
                style = Style.NONE;
                continue;
            }
            if (element.equals("<b>")) {
                style = Style.BOLD;
                continue;
            }
            if (element.equals("</b>")) {
                style = Style.NONE;
                continue;
            }
            if (element.equals("<u>")) {
                style = Style.UNDERLINE;
                continue;
            }
            if (element.equals("</u>")) {
                style = Style.NONE;
                continue;
            }
            elements.add(buildLabel(element, style));
        }
        if (!elements.isEmpty()) {
            subtitleTextContainer.add(buildSubtitleRow(elements));
            Platform.runLater(() -> {
                subTextVBox.getChildren().clear();
                subTextVBox.getChildren().addAll(subtitleTextContainer);
                showSub();
            });
        }
    }

    private Label buildLabel(String element, Style style) {
        Label label = new Label();
        label.setText(element);
        label.getStyleClass().addAll("subtitle-element", PlayerModel.INSTANCE.isFullScreen() ? "sub-big" : "sub-small");
        switch (style) {
            case ITALIC:
                label.getStyleClass().add("text-italic");
                break;
            case BOLD:
                label.getStyleClass().add("text-bold");
                break;
            case UNDERLINE:
                label.getStyleClass().add("text-underline");
                break;
            default:
                break;
        }
        if (SubtitleService.isWord(element)) {
            label.setCursor(Cursor.HAND);
            label.setOnMouseClicked((event) -> {
                if ((lastClickedWord != null && lastClickedWord.equals(label.getText())) || !subVBox.getChildren().contains(subTranslateWebView)) {
                    if (subVBox.getChildren().contains(subTranslateWebView)) {
                        removeSubTranslate();
                    } else {
                        addSubTranslate();
                    }
                }
                if (subVBox.getChildren().contains(subTranslateWebView)) {
                    playerWasPaused.set(true);
                    if (mediaPlayer.status().isPlaying()) {
                        mediaPlayer.controls().pause();
                    }
                    lastClickedWord = label.getText();
                    translate(label.getText(), SubtitleModel.INSTANCE.getLang(), Lang.RU);
                }
            });
        }
        return label;
    }

    private HBox buildSubtitleRow(List<Label> elements) {
        HBox subtitleRow = new HBox();
        subtitleRow.setAlignment(Pos.CENTER);
        subtitleRow.getChildren().addAll(elements);
        return subtitleRow;
    }

    private void showSub() {
        if (!subTextVBox.isVisible()) {
            subTextVBox.setVisible(true);
        }
    }

    private void hideSub() {
        if (subTextVBox.isVisible()) {
            Platform.runLater(() -> {
                subTextVBox.setVisible(false);
                subTextVBox.getChildren().clear();
            });
        }
    }

    private void removeSub() {
        playerBottom.getChildren().remove(subVBox);
    }

    private void addSubTranslate() {
        if (!subVBox.getChildren().contains(subTranslateWebView)) {
            subVBox.getChildren().add(0, subTranslateWebView);
        }
    }

    private void removeSubTranslate() {
        subVBox.getChildren().remove(subTranslateWebView);
    }

    private void initTranslateWebView() {
        subTranslateWebView = new WebView();
        subTranslateWebView.setPrefHeight(200);
        subTranslateWebView.setPrefWidth(Double.MAX_VALUE);
        subTranslateWebView.getEngine().setJavaScriptEnabled(true);
        subTranslateWebView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                subTranslateWebView.getEngine().executeScript("javascript:(function() { " +
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
    }

    public void translate(String word, Lang from, Lang to) {
        String url = String.format("%s/#view=home&op=translate&sl=%s&tl=%s&text=%s", HOST, from.name().toLowerCase(), to.name().toLowerCase(), word);
        subTranslateWebView.getEngine().load(url);
    }

    @Override
    public void stop() {
        mediaPlayer.events().removeMediaPlayerEventListener(eventAdapter);
        SubtitleModel.INSTANCE.unRegisterEventTarget(subVBox);
        removeSub();
    }
}
