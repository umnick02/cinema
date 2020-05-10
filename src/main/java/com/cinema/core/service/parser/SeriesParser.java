package com.cinema.core.service.parser;

import com.cinema.core.entity.Movie;
import com.cinema.core.service.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public enum SeriesParser implements Parser {
    INSTANCE;

    @Override
    public void parse(WebDriver webDriver, Movie movie) {
        WebElement fullTitleElement = webDriver.findElement(By.tagName("h1"));
        WebElement titleElement = fullTitleElement.findElement(By.tagName("span"));
        movie.setTitle(titleElement.getText());



        WebElement originalTitleElement = fullTitleElement.findElement(By.xpath("./..")).findElement(By.cssSelector("div > span"));
        movie.setOriginalTitle(originalTitleElement.getText());
    }
}
