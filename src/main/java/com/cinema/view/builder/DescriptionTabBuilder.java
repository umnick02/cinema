package com.cinema.view.builder;

import com.cinema.controller.SeasonsController;
import com.cinema.entity.Cast;
import com.cinema.entity.Episode;
import com.cinema.entity.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.Collectors;

import static com.cinema.utils.StringUtils.longToString;

public enum DescriptionTabBuilder {
    INSTANCE;

    public void renderDetails(Tab tab, Movie movie) {
        VBox vBox = (VBox) ((BorderPane) tab.getContent()).getCenter();
        vBox.getChildren().add(buildTitle(movie));
        Text additionalTitle = buildAdditionalTitle(movie);
        if (additionalTitle != null) {
            vBox.getChildren().add(additionalTitle);
        }
        vBox.getChildren().add(buildType(movie));
        vBox.getChildren().add(buildGenres(movie));
        vBox.getChildren().add(buildCountry(movie));
        vBox.getChildren().add(buildDuration(movie));
        vBox.getChildren().add(buildRating(movie));
    }

    public void renderDescription(Tab tab, Movie movie) {
        VBox vBox = (VBox) ((ScrollPane) tab.getContent()).getContent();
        vBox.getChildren().add(buildDescription(movie));
        vBox.getChildren().add(buildCast(movie.getCasts()));
    }

    public void renderSeasons(Tab tab, Movie movie) {
        TilePane tilePane = (TilePane)((ScrollPane)((StackPane) tab.getContent()).getChildren().get(0)).getContent();
        SeasonsController seasonsController = new SeasonsController((StackPane) tab.getContent(), movie);
        tilePane.getChildren().addAll(seasonsController.buildEpisodes());
    }

    private Text buildTitle(Movie movie) {
        if (movie.getType() != Movie.Type.SERIES) {
            return ComponentBuilder.INSTANCE.mainText(String.format("%s (%d)", movie.getTitleRu(), movie.getReleaseDate().getYear()));
        } else if (movie.getFinishDate() == null) {
            return ComponentBuilder.INSTANCE.mainText(String.format("%s (%d — %s)", movie.getTitleRu(), movie.getReleaseDate().getYear(),  ". . ."));
        } else {
            return ComponentBuilder.INSTANCE.mainText(String.format("%s (%d — %d)", movie.getTitleRu(), movie.getReleaseDate().getYear(),  movie.getFinishDate().getYear()));
        }
    }

    private Text buildAdditionalTitle(Movie movie) {
        if (!movie.getTitleRu().equals(movie.getOriginalTitle()) && movie.getOriginalTitle() != null) {
            return ComponentBuilder.INSTANCE.italicText(movie.getOriginalTitle());
        } else if (!movie.getTitleRu().equals(movie.getTitle()) && movie.getTitle() != null) {
            return ComponentBuilder.INSTANCE.italicText(movie.getTitle());
        }
        return null;
    }

    private Text buildGenres(Movie movie) {
        StringBuilder sb = new StringBuilder();
        if (movie.getGenre1() != null) {
            sb.append(movie.getGenre1());
        }
        if (movie.getGenre2() != null) {
            sb.append(" / ");
            sb.append(movie.getGenre2());
        }
        if (movie.getGenre3() != null) {
            sb.append(" / ");
            sb.append(movie.getGenre3());
        }
        return ComponentBuilder.INSTANCE.regularText("Жанры: " + sb.toString());
    }

    private Text buildCountry(Movie movie) {
        String value = movie.getCountry().substring(1, movie.getCountry().length() - 1)
                .replaceAll("\"", "")
                .replaceAll(",", " /");
        return ComponentBuilder.INSTANCE.regularText("Страна: " + value);
    }

    private Text buildDuration(Movie movie) {
        if (movie.getDuration() >= 60) {
            return ComponentBuilder.INSTANCE.regularText(
                    String.format("Длительность: %dч %dм", movie.getDuration() / 60, movie.getDuration() % 60));
        } else {
            return ComponentBuilder.INSTANCE.regularText(String.format("Длительность: %dм", movie.getDuration()));
        }
    }

    private Text buildType(Movie movie) {
        if (movie.getType() != Movie.Type.SERIES) {
            return ComponentBuilder.INSTANCE.regularText(movie.getType().toString());
        }
        return movie.getEpisodes().stream()
                .max(Comparator.comparing(Episode::getSeason))
                .map(value -> ComponentBuilder.INSTANCE.regularText(String.format("Сериал (%d сезонов)", value.getSeason())))
                .orElse(null);
    }

    private Text buildRating(Movie movie) {
        return ComponentBuilder.INSTANCE.regularText(String.format("IMDB %.1f (%s) / KP %.1f (%s)",
                movie.getRatingImdb(), longToString(movie.getRatingImdbVotes()), movie.getRatingKp(), longToString(movie.getRatingKpVotes())));
    }

    private Label buildDescription(Movie movie) {
        return ComponentBuilder.INSTANCE.multilineText(movie.getDescriptionRu());
    }

    private VBox buildCast(Set<Cast> casts) {
        VBox vBox = new VBox();
        vBox.getChildren().add(ComponentBuilder.INSTANCE.multilineText("Режиссёр: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.DIRECTOR).map(Cast::getName).collect(Collectors.joining(", "))));
        vBox.getChildren().add(ComponentBuilder.INSTANCE.multilineText("Сценарист: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.WRITER).map(Cast::getName).collect(Collectors.joining(", "))));
        vBox.getChildren().add(ComponentBuilder.INSTANCE.multilineText("Композитор: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.COMPOSER).map(Cast::getName).collect(Collectors.joining(", "))));
        vBox.getChildren().add(ComponentBuilder.INSTANCE.regularText("В главных ролях:"));
        casts.stream()
                .filter(c -> c.getRole() == Cast.Role.ACTOR)
                .sorted(Comparator.comparing(Cast::getPriority))
                .forEach(c -> {
                    HBox box = new HBox();
                    box.setPadding(new Insets(0, 0, 0, 20));
                    box.getChildren().add(ComponentBuilder.INSTANCE.regularText(c.getName()));
                    if (c.getQua() != null) {
                        box.getChildren().add(ComponentBuilder.INSTANCE.italicText(" ... as " + c.getQua()));
                    }
                    vBox.getChildren().add(box);
                });
        return vBox;
    }
}
