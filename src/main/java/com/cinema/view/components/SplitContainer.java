package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.model.Filter;
import com.cinema.presenter.ContentPresentable;
import com.cinema.presenter.ContentPresenter;
import com.cinema.view.Viewable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.util.Map;
import java.util.Set;

@Singleton
public class SplitContainer implements Viewable {

    private ContentPresentable contentPresentable;
    final SplitPane splitPane = buildSplitPane();
    final SideMenuContainer sideMenuContainer;
    final ListContentContainer listContentContainer;

    @Inject
    SplitContainer(SideMenuContainer sideMenuContainer, ListContentContainer listContentContainer) {
        this.sideMenuContainer = sideMenuContainer;
        this.listContentContainer = listContentContainer;
        contentPresentable = new ContentPresenter(this);
        splitPane.getItems().addAll(sideMenuContainer.anchorPane, listContentContainer.anchorPane);
        sideMenuContainer.treeView.getSelectionModel().selectedItemProperty()
                .addListener(e -> contentPresentable.setMovies());
        contentPresentable.setGenres();
        contentPresentable.setMovies();
    }

    private SplitPane buildSplitPane() {
        SplitPane splitPane = new SplitPane();
        AnchorPane.setBottomAnchor(splitPane, 0d);
        AnchorPane.setRightAnchor(splitPane, 0d);
        AnchorPane.setLeftAnchor(splitPane, 0d);
        AnchorPane.setTopAnchor(splitPane, 0d);
        splitPane.setDividerPosition(0, 0.15);
        return splitPane;
    }

    @Override
    public void showGenres(Map<String, Integer> genres) {
        for (String genre : genres.keySet()) {
            sideMenuContainer.treeItem.getChildren().add(new SideMenuContainer.GenreItem(genre, genres.get(genre)));
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter();
        if (sideMenuContainer.treeView.getSelectionModel().getSelectedItem() != null) {
            filter.setGenresIncl(new String[]{
                    sideMenuContainer.treeView.getSelectionModel().getSelectedItem().getValue().split(" ")[0]
            });
        }
        return filter;
    }

    @Override
    public void showMovies(Set<Movie> movies) {
        listContentContainer.scrollCardsContainer.cardsContainer.resetCards(movies);
    }
}
