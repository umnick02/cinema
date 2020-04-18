//package com.cinema.javafx.view.components;
//
//import com.cinema.javafx.controller.MoviesController;
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.layout.AnchorPane;
//
//@Singleton
//public class ScrollMoviesContainer {
//    ScrollPane scrollPane;
//    final MoviesController moviesController;
//
//    @Inject
//    public ScrollMoviesContainer(MoviesController moviesController) {
//        super();
//        this.moviesController = moviesController;
//        scrollPane = buildScrollPane();
////        scrollPane.setContent(moviesContainer.anchorPane);
//    }
//
//    private ScrollPane buildScrollPane() {
//        ScrollPane scrollPane = new ScrollPane();
//        AnchorPane.setTopAnchor(scrollPane, 30d);
//        AnchorPane.setLeftAnchor(scrollPane, 0d);
//        AnchorPane.setRightAnchor(scrollPane, 0d);
//        AnchorPane.setBottomAnchor(scrollPane, 0d);
//        return scrollPane;
//    }
//}
