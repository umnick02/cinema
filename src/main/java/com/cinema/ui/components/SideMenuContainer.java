package com.cinema.ui.components;

import com.cinema.config.Config;
import com.cinema.helper.UIHelper;
import com.cinema.service.MovieService;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;

@Component
public class SideMenuContainer extends TreeView<String> implements Anchorable {
    @Autowired
    private MovieService movieService;

    private final AnchorPane wrapper;
    public SideMenuContainer() {
        super();
        wrapper = UIHelper.wrapNodeToAnchor(this);
    }

    @PostConstruct
    public void postConstruct() {
        Config.PrefKey.Language lang = Config.getLang();
        Set<GenreItem> genreItems = movieService.getGenres(lang);
        TreeItem<String> root = new TreeItem<>(Config.getGenre());
        root.setExpanded(true);
        setShowRoot(true);
        root.getChildren().addAll(genreItems);
        setRoot(root);
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }

    public static class GenreItem extends TreeItem<String> {
        private String genre;
        private Integer cnt;
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
