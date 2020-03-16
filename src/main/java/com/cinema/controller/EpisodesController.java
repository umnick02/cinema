package com.cinema.controller;

import com.cinema.entity.Episode;
import com.cinema.view.builder.ComponentBuilder;
import com.cinema.view.components.CardContainer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.cinema.utils.StringUtils.longToString;

public class EpisodesController {

    private ScrollPane rootContainer;
    private SeasonsController.SeasonStat seasonStat;

    public EpisodesController(SeasonsController seasonsController, SeasonsController.SeasonStat seasonStat) {
        this.seasonStat = seasonStat;
        BorderPane contentContainer = new BorderPane();
        VBox episodesContainer = new VBox();
        episodesContainer.setPadding(new Insets(0, 5, 0, 5));
        episodesContainer.setSpacing(5);
        episodesContainer.getChildren().addAll(buildEpisodes());
        contentContainer.setCenter(episodesContainer);
        contentContainer.setTop(buildControls(seasonsController));
        contentContainer.setBottom(buildControls(seasonsController));
        rootContainer = new ScrollPane();
        rootContainer.setContent(contentContainer);
    }

    public ScrollPane getRootContainer() {
        return rootContainer;
    }

    private BorderPane buildControls(SeasonsController seasonsController) {
        BorderPane controlsContainer = new BorderPane();
        if (seasonStat.getSeason() < seasonsController.getMaxSeason()) {
            Button next = ComponentBuilder.INSTANCE.buildIcon("icon-right_arrow");
            next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.changeEpisode((short) (seasonStat.getSeason() + 1)));
            controlsContainer.setRight(next);
        }
        if (seasonStat.getSeason() > 1) {
            Button prev = ComponentBuilder.INSTANCE.buildIcon("icon-left_arrow");
            prev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.changeEpisode((short) (seasonStat.getSeason() - 1)));
            controlsContainer.setLeft(prev);
        }
        Button top = ComponentBuilder.INSTANCE.buildIcon("icon-upwards_arrow");
        top.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.hideEpisodes());
        controlsContainer.setCenter(top);
        return controlsContainer;
    }

    private List<BorderPane> buildEpisodes() {
        List<BorderPane> episodesContainer = new ArrayList<>();
        seasonStat.getEpisodes().stream().sorted(Comparator.comparingInt(Episode::getEpisode)).forEach(e -> {
            BorderPane borderPane = new BorderPane();
            borderPane.setMinWidth(CardContainer.width*(CardContainer.ratio - 1) - 40);
            borderPane.setMaxWidth(CardContainer.width*(CardContainer.ratio - 1) - 40);
            borderPane.setLeft(ComponentBuilder.INSTANCE.buildImage(e.getPoster()));
            VBox box = new VBox();
            box.setPadding(new Insets(0, 5, 0, 5));
            box.getChildren().add(ComponentBuilder.INSTANCE.multilineText(e.getEpisode() + ". " + e.getTitle()));
            box.getChildren().add(ComponentBuilder.INSTANCE.regularText(
                    e.getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))));
            box.getChildren().add(ComponentBuilder.INSTANCE.regularText(
                    String.format("%.1f (%s)", e.getRating(), longToString(e.getRatingVotes()))));
            borderPane.setCenter(box);
            HBox hBox = new HBox(ComponentBuilder.INSTANCE.buildIcon("icon-play"));
            hBox.setAlignment(Pos.CENTER);
            borderPane.setRight(hBox);
            episodesContainer.add(borderPane);
        });
        return episodesContainer;
    }
}
