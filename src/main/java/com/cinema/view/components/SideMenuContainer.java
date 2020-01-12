package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.view.Anchorable;
import com.cinema.view.UIBuilder;
import com.google.inject.Singleton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

@Singleton
public class SideMenuContainer extends TreeView<String> implements Anchorable {

    private final AnchorPane wrapper;
    TreeItem<String> root = new TreeItem<>(Config.getGenre());

    public SideMenuContainer() {
        super();
        root.setExpanded(true);
        setRoot(root);
        setShowRoot(true);
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
