package com.cinema.ui.components;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.helper.UIHelper;
import com.cinema.service.MovieService;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class ScrollCardsContainer extends ScrollPane implements Anchorable {
    @Autowired
    public MovieService movieService;
    @Autowired
    private CardsContainer cardsContainer;
    private int page = 0;
    private final AnchorPane wrapper;

    public ScrollCardsContainer() {
        super();
        wrapper = UIHelper.wrapNodeToAnchor(this);
    }

    @PostConstruct
    public void postConstruct() {
        setContent(cardsContainer.getWrapper());
        Set<Movie> movies = movieService.getMovies(PageRequest.of(page++, 10), Config.PrefKey.Language.EN);
        cardsContainer.addCards(movies);
    }

    public CardsContainer getCardsContainer() {
        return cardsContainer;
    }

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
