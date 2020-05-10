package com.cinema.core.service.parser;

import bt.metainfo.Torrent;
import com.cinema.core.entity.Movie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MagnetParser {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(MagnetParser.class);
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\umnick\\IdeaProjects\\cinema\\chromedriver.exe");
    }

    public static void main(String[] args) {
        WebDriver driver = MagnetParser.INSTANCE.buildWebDriver();
        MagnetParser.INSTANCE.parse(driver, "Отель «Гранд Будапешт»");
    }

    public Movie parse(Torrent torrent) {
        WebDriver driver = buildWebDriver();
        try {
            Movie movie = parse(driver, torrent.getName());
            movie.getMagnet().setHash(torrent.getTorrentId().toString());
            return movie;
        } finally {
            driver.close();
        }
    }

    public Movie parse(WebDriver driver, String torrentName) {
        try {
            driver.get(buildSearchUrl(torrentName));
            driver.findElement(By.cssSelector("#search a")).click();
            Movie movie = new Movie();
            movie.setUrl(driver.getCurrentUrl());
            if (movie.getUrl().contains("series")) {
                movie.setType(Movie.Type.SERIES);
                SeriesParser.INSTANCE.parse(driver, movie);
            } else {
                MovieParser.INSTANCE.parse(driver, movie);
            }
            return movie;
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    private WebDriver buildWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    private String buildSearchUrl(String torrentName) {
        String movieName = fetchFileName(torrentName);
        logger.info("Trying to find KP url for movie=[{}]", movieName);
        return "https://www.google.com/search?q=site:kinopoisk.ru+" + movieName;
    }

    private String fetchFileName(String torrentName) {
        if (torrentName.contains(".")) {
            torrentName = torrentName.substring(0, torrentName.indexOf('.'));
        }
        if (torrentName.contains("(")) {
            torrentName = torrentName.substring(0, torrentName.indexOf('(')).strip();
        }
        return torrentName.replaceAll(" ", "+");
    }
}
