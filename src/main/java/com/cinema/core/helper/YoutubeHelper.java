package com.cinema.core.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class YoutubeHelper {

    public static String getTrailer(String name) throws IOException, InterruptedException {
        String url = String.format("https://www.google.com/search?q=site:youtube.com+%s+%s",
                name.replaceAll(" ", "+"), "официальный+трейлер");
        String responseBody = HttpHelper.requestAndGetBody(url);
        Document html = Jsoup.parse(responseBody);
        String uri = html.selectFirst(".g a").attr("href");
        String id = uri.substring(uri.indexOf('=') + 1);
        return "https://www.youtube.com/embed/" + id;
    }

    public static String getTrailerThumbnail(String trailerUrl) {
        if (trailerUrl.contains("youtube")) {
            String id = trailerUrl.substring(trailerUrl.lastIndexOf("/") + 1);
            return String.format("https://img.youtube.com/vi/%s/hqdefault.jpg", id);
        }
        return null;
    }
}
