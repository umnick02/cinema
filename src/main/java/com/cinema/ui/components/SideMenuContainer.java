package com.cinema.ui.components;

import com.cinema.config.Config;
import com.cinema.ui.Anchorable;
import com.cinema.ui.UIBuilder;
import com.cinema.service.MovieService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;
import java.util.Set;

@Singleton
public class SideMenuContainer extends TreeView<String> implements Anchorable {

    private final AnchorPane wrapper;
    @Inject
    public SideMenuContainer(MovieService movieService) {
        super();
        Set<GenreItem> genreItems = movieService.getGenres();
        TreeItem<String> root = new TreeItem<>(Config.getGenre());
        root.setExpanded(true);
        setShowRoot(true);
        root.getChildren().addAll(genreItems);
        setRoot(root);
        wrapper = UIBuilder.wrapNodeToAnchor(this);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }

    public static class GenreItem extends TreeItem<String> {
        private final String genre;
        private final Integer cnt;
        public GenreItem(String genre, Integer cnt) {
            super(String.format("%s (%d)", genre, cnt));
            this.genre = genre;
            this.cnt = cnt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GenreItem genreItem = (GenreItem) o;
            return genre.equals(genreItem.genre) &&
                    cnt.equals(genreItem.cnt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(genre, cnt);
        }
    }
}
