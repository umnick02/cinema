package com.cinema.core.service;

import com.cinema.core.entity.Movie;
import org.openqa.selenium.WebDriver;

public interface Parser {
    void parse(WebDriver webDriver, Movie movie);
}
