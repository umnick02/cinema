package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.presenter.SearchPresentable;
import com.cinema.view.Searchable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.Set;

@Singleton
public class ListContentContainer implements Searchable {

    final SearchPresentable searchPresentable;

    AnchorPane anchorPane = buildAnchorPane();
    final ScrollCardsContainer scrollCardsContainer;
    final MenuContentContainer menuContentContainer;

    @Inject
    public ListContentContainer(MenuContentContainer menuContentContainer, ScrollCardsContainer scrollCardsContainer,
                                SearchPresentable searchPresentable) {
        this.searchPresentable = searchPresentable;
        this.scrollCardsContainer = scrollCardsContainer;
        this.menuContentContainer = menuContentContainer;
        menuContentContainer.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchPresentable.search();
            } else {
                searchPresentable.cancelSearch();
            }
        });
        menuContentContainer.button.setOnAction(event -> searchPresentable.search());
        anchorPane.getChildren().addAll(menuContentContainer.hBox, scrollCardsContainer.scrollPane);
    }

    private AnchorPane buildAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(anchorPane, 0d);
        AnchorPane.setRightAnchor(anchorPane, 0d);
        AnchorPane.setLeftAnchor(anchorPane, 0d);
        AnchorPane.setTopAnchor(anchorPane, 0d);
        return anchorPane;
    }

    @Override
    public String getText() {
        return menuContentContainer.textField.getText();
    }

    @Override
    public void showMovies(Set<Movie> movies) {
        scrollCardsContainer.cardsContainer.resetCards(movies);
    }

    @Override
    public void showLoading() {
        menuContentContainer.showLoadingIcon();
    }

    @Override
    public void hideLoading() {
        menuContentContainer.hideLoadingIcon();
    }
}
