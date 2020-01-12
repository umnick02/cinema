package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.model.Filter;
import com.cinema.presenter.Presentable;
import com.cinema.presenter.Presenter;
import com.cinema.view.Viewable;
import com.cinema.view.UIBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;

import java.util.Map;
import java.util.Set;

@Singleton
public class RootContainer extends SplitPane implements Viewable {
    private final Presentable presenter;
    SideMenuContainer sideMenuContainer;
    ListContentContainer listContentContainer;

    @Inject
    public RootContainer(SideMenuContainer sideMenuContainer, ListContentContainer listContentContainer) {
        super(sideMenuContainer.getWrapper(), listContentContainer);
        this.sideMenuContainer = sideMenuContainer;
        this.listContentContainer = listContentContainer;
        this.presenter = new Presenter(this);
        setDividerPosition(0, 0.15);
        UIBuilder.setAnchor(this, 0d, 0d, 0d, 0d);
        presenter.setGenres();
        presenter.setMovies(new Filter());
        sideMenuContainer.getSelectionModel().selectedItemProperty().addListener(e -> {
            Filter filter = new Filter();
            filter.setGenresIncl(new String[] {sideMenuContainer.getSelectionModel().getSelectedItem().getValue().split(" ")[0]});
            presenter.setMovies(filter);
        });
    }

    @Override
    public void setMovies(Set<Movie> movies) {
        listContentContainer.scrollCardsContainer.cardsContainer.resetCards(movies);
    }

    @Override
    public void setGenres(Map<String, Integer> genres) {
        for (String genre : genres.keySet()) {
            sideMenuContainer.root.getChildren().add(new SideMenuContainer.GenreItem(genre, genres.get(genre)));
        }
    }
}
