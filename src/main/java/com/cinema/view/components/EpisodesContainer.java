//package com.cinema.view.components;
//
//import com.cinema.entity.Episode;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.scene.Node;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Paint;
//import javafx.scene.text.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import static com.cinema.view.components.MovieDetailsContainer.longByGroup;
//
//public class EpisodesContainer {
//
//    private static final Font second = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 14);
//    private static final String fontColor = "#ffffff";
//
//    VBox serialContainer;
//    private VBox episodeContainer;
//    private List<Episode> episodes;
//
//    EpisodesContainer(List<Episode> episodes) {
//        this.episodes = episodes;
//        episodeContainer = buildEpisodesBox();
//        Set<Short> seasons = new HashSet<>();
//        for (Episode episode : episodes) {
//            seasons.add(episode.getSeason());
//        }
//        List<Short> sortedSeasons = new ArrayList<>(seasons);
//        Collections.sort(sortedSeasons);
//        episodeContainer.getChildren().addAll(buildEpisodes((short) 1));
//        HBox seasonBox = buildSeasonsBox(sortedSeasons);
//        ScrollPane scrollPane = buildScrollPane(episodeContainer);
//        serialContainer = new VBox(seasonBox, scrollPane);
//    }
//
//    private List<HBox> buildEpisodes(short season) {
//        List<HBox> result = new ArrayList<>();
//        for (Episode episode : episodes) {
//            if (episode.getSeason().equals(season)) {
//                result.add(buildEpisodeBox(episode));
//            }
//        }
//        return result;
//    }
//
//    private HBox buildSeasonsBox(List<Short> seasons) {
//        HBox hBox = new HBox();
//        for (short season : seasons) {
//            Text text = new Text("Season " + season);
//            text.setOnMouseClicked(event -> {
//                episodeContainer.getChildren().clear();
//                episodeContainer.getChildren().addAll(buildEpisodes(season));
//            });
//            text.setFont(second);
//            hBox.getChildren().add(text);
//        }
//        return hBox;
//    }
//
//    private ScrollPane buildScrollPane(Pane pane) {
//        ScrollPane scrollPane = new ScrollPane(pane);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setMaxHeight(521);
//        scrollPane.setMinHeight(521);
//        scrollPane.setPadding(new Insets(10, 0, 10, 0));
//        return scrollPane;
//    }
//
//    private VBox buildEpisodesBox() {
//        VBox vBox = new VBox();
//        vBox.setSpacing(10);
//        return vBox;
//    }
//
//    private HBox buildEpisodeBox(Episode episode) {
//        HBox hBox = new HBox();
//        hBox.setSpacing(10);
//        hBox.setMinHeight(63);
//        hBox.setMaxHeight(63);
//        hBox.getChildren().add(buildImageView(null));
//        Platform.runLater(() -> {
//            hBox.getChildren().add(0, buildImageView(episode.getPoster()));
//        });
//        VBox titleBox = buildBox(224, buildTitle(episode.getEpisode(), episode.getTitle()),
//            buildRating(episode.getRating(), episode.getRatingVotes())
//        );
//        VBox dateBox = buildBox(224, buildDate(episode.getReleaseDate()));
//        VBox playBox = buildBox(66, buildPlay(episode.getMagnet(), episode.getFile()));
//        hBox.getChildren().add(titleBox);
//        hBox.getChildren().add(dateBox);
//        hBox.getChildren().add(playBox);
//        return hBox;
//    }
//
//    private ImageView buildImageView(String url) {
//        ImageView imageView;
//        if (url != null) {
//            Image image = new Image(url);
//            imageView = new ImageView(image);
//        } else {
//            imageView = new ImageView();
//        }
//        imageView.setFitWidth(112);
//        imageView.setFitHeight(63);
//        return imageView;
//    }
//
//    private VBox buildBox(int width, Node... nodes) {
//        VBox vBox = new VBox();
//        vBox.setPadding(new Insets(4, 0, 4, 0));
//        vBox.setMinWidth(width);
//        vBox.setMaxWidth(width);
//        vBox.getChildren().addAll(nodes);
//        return vBox;
//    }
//
//    private Text buildTitle(short episode, String text) {
//        Text t = new Text(episode + ". " + text);
//        t.setFont(second);
//        t.setFill(Paint.valueOf(fontColor));
//        return t;
//    }
//
//    private Text buildRating(float rating, int votes) {
//        Text t = new Text(String.format("%.01f (%s)", rating, longByGroup(votes)));
//        t.setFont(second);
//        t.setFill(Paint.valueOf(fontColor));
//        return t;
//    }
//
//    private Text buildDate(LocalDate date) {
//        Text t = new Text(date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)));
//        t.setFont(second);
//        t.setFill(Paint.valueOf(fontColor));
//        return t;
//    }
//
//    private Button buildPlay(String magnet, String file) {
//        Button button = new Button("Play");
//        return button;
//    }
//}
