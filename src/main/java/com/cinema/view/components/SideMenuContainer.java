package com.cinema.view.components;

import com.cinema.config.Config;
import com.google.inject.Singleton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

@Singleton
public class SideMenuContainer {

    TreeItem<String> treeItem = buildTreeItem();
    TreeView<String> treeView = buildTreeView(treeItem);
    AnchorPane anchorPane = buildAnchorPane(treeView);

    public SideMenuContainer() {
        SplitPane.setResizableWithParent(anchorPane, Boolean.FALSE);
    }

    private TreeItem<String> buildTreeItem() {
        TreeItem<String> treeItem = new TreeItem<>(Config.getGenre());
        treeItem.setExpanded(true);
        return treeItem;
    }

    private TreeView<String> buildTreeView(TreeItem<String> treeItem) {
        TreeView<String> treeView = new TreeView<>();
        treeView.getStyleClass().add("side-menu_item");
        treeView.setRoot(treeItem);
        treeView.setShowRoot(true);
        return treeView;
    }

    private AnchorPane buildAnchorPane(TreeView<String> treeView) {
        AnchorPane anchorPane = new AnchorPane(treeView);
        anchorPane.setStyle("-fx-focus-color: transparent;");
        AnchorPane.setBottomAnchor(treeView, 0d);
        AnchorPane.setRightAnchor(treeView, 0d);
        AnchorPane.setLeftAnchor(treeView, 0d);
        AnchorPane.setTopAnchor(treeView, 0d);
        return anchorPane;
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
