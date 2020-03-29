package com.cinema.core.helper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHelper {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String[] DEFAULT_HEADERS = {
            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36",
            "Accept-Language", "en-US"
    };

    public static String requestAndGetBody(String url, String... headers) throws IOException, InterruptedException {
        if (url == null) throw new IOException("URL is null!");
        HttpRequest request = buildRequest(url, headers);
        HttpResponse<String> response = sendRequest(request);
        return fetchResponseBody(response);
    }

    public static String requestAndGetBody(String url) throws IOException, InterruptedException {
        HttpResponse<String> response = requestAndGetResponse(url);
        return fetchResponseBody(response);
    }

    public static HttpResponse<String> requestAndGetResponse(String url) throws IOException, InterruptedException {
        if (url == null) throw new IOException("URL is null!");
        HttpRequest request = buildRequest(url);
        return sendRequest(request);
    }

    private static HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url))
                .headers(DEFAULT_HEADERS)
                .build();
    }

    private static HttpRequest buildRequest(String url, String... headers) {
        return headers.length == 0 ? buildRequest(url) :
                HttpRequest.newBuilder(URI.create(url))
                        .headers(DEFAULT_HEADERS)
                        .headers(headers)
                        .build();
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String fetchResponseBody(HttpResponse<String> response) {
        return response.body();
    }
}
