package com.cinema.javafx.controller.movies.movie.season;

import com.cinema.core.model.impl.SeasonModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SeasonController {

    @FXML
    public VBox episodesPane;

    private SeasonModel seasonModel;

    public SeasonController(SeasonModel seasonModel) {
        this.seasonModel = seasonModel;
    }

    @FXML
    public void initialize() {
        seasonModel.getEpisodeModels()
                .forEach(episodeModel -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie/season/episode.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(EpisodeController.class)) {
                            return new EpisodeController(episodeModel);
                        }
                        return null;
                    });
                    try {
                        episodesPane.getChildren().add(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
//    private BorderPane buildControls(SeasonsController seasonsController) {
//        BorderPane controlsContainer = new BorderPane();
//        if (seasonStat.getSeason() < seasonsController.getMaxSeason()) {
//            Button next = ComponentBuilder.buildIcon("icon-right_arrow");
//            next.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.changeEpisode((short) (seasonStat.getSeason() + 1)));
//            controlsContainer.setRight(next);
//        }
//        if (seasonStat.getSeason() > 1) {
//            Button prev = ComponentBuilder.buildIcon("icon-left_arrow");
//            prev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.changeEpisode((short) (seasonStat.getSeason() - 1)));
//            controlsContainer.setLeft(prev);
//        }
//        Button top = ComponentBuilder.buildIcon("icon-upwards_arrow");
//        top.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> seasonsController.hideEpisodes());
//        controlsContainer.setCenter(top);
//        return controlsContainer;
//    }
//
//    private List<BorderPane> buildEpisodes() {
//        List<BorderPane> episodesContainer = new ArrayList<>();
//        seasonStat.getEpisodes().stream().sorted(Comparator.comparingInt(Episode::getEpisode)).forEach(e -> {
////            BorderPane borderPane = new BorderPane();
////            borderPane.setMinWidth(MovieBuilder.width*(MovieBuilder.ratio - 1) - 40);
////            borderPane.setMaxWidth(MovieBuilder.width*(MovieBuilder.ratio - 1) - 40);
////            Platform.runLater(() -> borderPane.setLeft(ComponentBuilder.buildImage(e.getPoster())));
//            borderPane.setLeft(ComponentBuilder.buildImage(null));
////            VBox box = new VBox();
////            box.setPadding(new Insets(0, 5, 0, 5));
////            box.getChildren().add(ComponentBuilder.multilineText(e.getEpisode() + ". " + e.getTitle()));
////            box.getChildren().add(ComponentBuilder.regularText(
////                    e.getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))));
////            box.getChildren().add(ComponentBuilder.regularText(
////                    String.format("%.1f (%s)", e.getRating(), StringUtils.longToString(e.getRatingVotes()))));
////            borderPane.setCenter(box);
//            HBox hBox = new HBox(ComponentBuilder.buildIcon("icon-play"));
////            hBox.setAlignment(Pos.CENTER);
////            borderPane.setRight(hBox);
//            episodesContainer.add(borderPane);
//        });
//        return episodesContainer;
//    }
}
