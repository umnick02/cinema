package com.cinema.javafx.controller.filter;

import com.cinema.core.model.Filter;
import com.cinema.core.model.impl.FilterModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FilterController {

    @FXML
    private TextField queryTextField;

    @FXML
    public void searchByText() {
        String queryText = queryTextField.getText();
        FilterModel.INSTANCE.setFilter(new Filter.Builder().title(queryText).build());
    }
}
