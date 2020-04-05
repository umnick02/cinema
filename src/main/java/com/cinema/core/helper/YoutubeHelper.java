package com.cinema.core.helper;

import com.cinema.core.config.Preferences;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.http.HttpResponse;

public class YoutubeHelper {

    public static String getTrailer(String name, Preferences.PrefKey.Language lang) throws IOException, InterruptedException {
        String url = String.format("https://www.google.com/search?q=site:youtube.com+%s+%s",
                name.replaceAll(" ", "+"), lang == Preferences.PrefKey.Language.RU ? "официальный+трейлер" : "official+trailer");
        HttpResponse<String> response = HttpHelper.requestAndGetResponse(url);
        Document html = Jsoup.parse(response.body());
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
