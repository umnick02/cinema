package com.cinema.core.service.parser;

import com.cinema.core.entity.Cast;
import com.cinema.core.entity.Movie;
import com.cinema.core.service.InfoField;
import com.cinema.core.service.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cinema.core.entity.Cast.Role.*;

public enum MovieParser implements Parser {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(MovieParser.class);

    private static final String HOST = "https://www.kinopoisk.ru";

    @Override
    public void parse(WebDriver webDriver, final Movie movie) {
        WebElement info = webDriver.findElement(By.className("movie-info"));
        WebElement content = info.findElement(By.className("movie-info__content"));
        movie.setTitle(content.findElement(By.className("moviename-title-wrapper")).getText());
        try {
            movie.setOriginalTitle(content.findElement(By.className("alternativeHeadline")).getText());
        } catch (Exception e) {
            logger.info("", e);
        }
        List<WebElement> infos = content.findElements(By.cssSelector(".info tr"));
        infos.forEach(infoContent -> handleInfo(infoContent, movie));

        movie.setPoster(HOST + info.findElement(By.className("popupBigImage")).getAttribute("onclick").split("'")[1]);

        AtomicInteger i = new AtomicInteger(1);
        if (i.get() <= 10) {
            info.findElements(By.cssSelector("#actorList li")).forEach(actor ->
                    movie.addCasts(buildCast(actor, ACTOR, (short) i.getAndIncrement())));
        }
        logger.info("{}", movie);
    }

    private void handleInfo(WebElement info, Movie movie) {
        String value = info.findElement(By.cssSelector("td.type")).getText();
        InfoField infoField = InfoField.valueFrom(value);
        if (infoField != null) {
            switch (infoField) {
                case COUNTRY:
                    List<WebElement> countries = info.findElements(By.tagName("a"));
                    movie.setCountry1(countries.get(0).getText());
                    if (countries.size() > 1) movie.setCountry2(countries.get(1).getText());
                    if (countries.size() > 2) movie.setCountry3(countries.get(2).getText());
                    break;
                case GENRE:
                    List<WebElement> genres = info.findElements(By.tagName("a"));
                    movie.setGenre1(genres.get(0).getText());
                    movie.setType(movie.getGenre1().equals("мультфильм") ? Movie.Type.CARTOON : Movie.Type.MOVIE);
                    if (genres.size() > 1) movie.setGenre2(genres.get(1).getText());
                    if (genres.size() > 2) movie.setGenre3(genres.get(2).getText());
                    break;
                case DURATION:
                    String duration = info.findElement(By.className("time")).getText().split(" мин")[0];
                    movie.setDuration(Short.parseShort(duration));
                    break;
                case RELEASE:
                    String release = info.findElement(By.className("prem_ical")).getAttribute("data-date-premier-start-link");
                    LocalDate.parse(release, DateTimeFormatter.BASIC_ISO_DATE);
                    movie.setReleaseDate(LocalDate.parse(release, DateTimeFormatter.BASIC_ISO_DATE));
                    break;
                case DIRECTOR:
                    movie.addCasts(buildCasts(info, DIRECTOR));
                    break;
                case WRITER:
                    movie.addCasts(buildCasts(info, WRITER));
                    break;
                case COMPOSER:
                    movie.addCasts(buildCasts(info, COMPOSER));
                    break;
                default:
                    break;
            }
        }
    }

    private Cast[] buildCasts(WebElement webElement, Cast.Role role) {
        AtomicInteger i = new AtomicInteger(1);
        return webElement.findElements(By.tagName("a")).stream().map(writerElement ->
                buildCast(writerElement, role, (short) i.getAndIncrement())).toArray(Cast[]::new);
    }

    private Cast buildCast(WebElement webElement, Cast.Role role, short priority) {
        Cast cast = new Cast();
        cast.setPriority(priority);
        cast.setName(webElement.getText());
        cast.setRole(role);
        return cast;
    }
}
