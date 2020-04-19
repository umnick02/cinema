package com.cinema.javafx.controller.movies.movie.casts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CastController {

    private final String name;
    private String qua;

    @FXML
    public Label castName;

    @FXML
    public Label castQua;

    public CastController(String name) {
        this.name = name;
    }

    public CastController(String name, String qua) {
        this(name);
        this.qua = " ... as " + qua;
    }

    @FXML
    public void initialize() {
        castName.setText(name);
        if (qua != null) {
            castQua.setText(qua);
        }
    }
}
