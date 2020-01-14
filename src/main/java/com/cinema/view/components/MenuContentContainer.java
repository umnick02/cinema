package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.presenter.SearchPresentable;
import com.cinema.presenter.SearchPresenter;
import com.cinema.view.Searchable;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Set;

import static com.cinema.CinemaApplication.INJECTOR;

@Singleton
public class MenuContentContainer extends HBox implements Searchable {

    private final TextField searchField;
    private final SearchPresentable searchPresentable;

    public MenuContentContainer() {
        super();
        searchPresentable = new SearchPresenter(this);
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        setMaxHeight(30);
        setMinHeight(30);
        setFillHeight(true);
        searchField = buildSearch();
        getChildren().addAll(searchField, buildSubmitButton());
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    private TextField buildSearch() {
        TextField textField = new TextField();
        textField.setMinWidth(200);
        textField.setPrefWidth(500);
//        textField.setOnKeyPressed(event -> searchPresentable.search());
        return textField;
    }

    private Button buildSubmitButton() {
        int width = 24;
        Button button = new Button();
        button.setMaxWidth(width);
        button.setMinWidth(width);
        button.setMinHeight(width);
        button.setMaxHeight(width);
        ImageView imageView = new ImageView("/icons/icons8-search-32.png");
        imageView.setFitWidth(width*0.9);
        imageView.setFitHeight(width*0.9);
        button.setGraphic(imageView);
        button.setDefaultButton(true);
        button.setOnAction(event -> searchPresentable.search());
        return button;
    }

    @Override
    public String getText() {
        return searchField.getText();
    }

    @Override
    public void showResults(Set<Movie> movies) {
        INJECTOR.getInstance(RootContainer.class).setMovies(movies);
    }
}
