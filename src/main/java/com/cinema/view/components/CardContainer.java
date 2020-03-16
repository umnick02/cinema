package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.view.builder.DescriptionTabBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;

public class CardContainer {

    private HBox card;

    private Movie movie;

    public static final Double width = 1920/9-5d;
    public static final Double ratio = 3d;

    public CardContainer(Movie movie) throws IOException {
        this.movie = movie;
        card = FXMLLoader.load(getClass().getResource("/card.fxml"));
        init();
    }

    public Movie getMovie() {
        return movie;
    }

    public HBox getCard() {
        return card;
    }

    private void init() {
        setDimension();
        setPoster();
        if (movie.getType() != Movie.Type.SERIES) {
            ((TabPane)((AnchorPane) card.getChildren().get(1)).getChildren().get(0)).getTabs().get(2).setStyle("visibility: hidden;");
        }
        TabPane tabPane = (TabPane)((AnchorPane) card.getChildren().get(1)).getChildren().get(0);
        tabPane.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, oldValue, newValue) -> selectTab(Tabs.values()[newValue.intValue()]));
        selectTab(Tabs.DETAILS);
    }

    private void selectTab(Tabs tab) {
        TabPane tabPane = (TabPane) ((AnchorPane) card.getChildren().get(1)).getChildren().get(0);
        tabPane.getSelectionModel().select(tab.position);
        switch (tab) {
            case DETAILS:
                if (((VBox)((BorderPane) tabPane.getTabs().get(tab.position).getContent()).getCenter()).getChildren().size() == 0) {
                    DescriptionTabBuilder.INSTANCE.renderDetails(tabPane.getTabs().get(tab.position), movie);
                }
                break;
            case DESCRIPTION:
                if (((VBox)((ScrollPane) tabPane.getTabs().get(tab.position).getContent()).getContent()).getChildren().size() == 0) {
                    DescriptionTabBuilder.INSTANCE.renderDescription(tabPane.getTabs().get(tab.position), movie);
                }
                break;
            case EPISODES:
                StackPane stackPane = (StackPane) tabPane.getTabs().get(tab.position).getContent();
                if (stackPane.getChildren().size() > 1) {
                    stackPane.getChildren().remove(1);
                }
                if (((TilePane)((ScrollPane)((StackPane) tabPane.getTabs().get(tab.position).getContent()).getChildren().get(0)).getContent()).getChildren().size() == 0) {
                    DescriptionTabBuilder.INSTANCE.renderSeasons(tabPane.getTabs().get(tab.position), movie);
                }
                break;
            default:
                break;
        }
    }

    private void setDimension() {
        card.setMinSize(width * ratio, width * 1.4);
        card.setMaxSize(width * ratio, width * 1.4);
        StackPane posterPane = (StackPane) card.getChildren().get(0);
        posterPane.setMinSize(width, card.getMinHeight());
        posterPane.setMaxSize(width, card.getMaxHeight());
        AnchorPane detailsPane = (AnchorPane) card.getChildren().get(1);
        detailsPane.setMinSize(card.getMinWidth() - width, card.getMinHeight());
        detailsPane.setMaxSize(card.getMaxWidth() - width, card.getMaxHeight());
    }

    private void setPoster() {
        StackPane posterPane = (StackPane) card.getChildren().get(0);
        Image image = new Image(movie.getPoster(), width, -1, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        posterPane.setBackground(new Background(backgroundImage));
    }

    private enum Tabs {
        DETAILS(0, "Детали"), DESCRIPTION(1, "Описание"), EPISODES(2, "Эпизоды");

        private int position;
        private String value;

        Tabs(int position, String value) {
            this.value = value;
            this.position = position;
        }

        @Override
        public String toString() {
            return "Tab{" +
                    "position=" + position +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
