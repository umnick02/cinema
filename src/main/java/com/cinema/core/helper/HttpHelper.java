package com.cinema.core.helper;

import org.brotli.dec.BrotliInputStream;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.GZIPInputStream;

public class HttpHelper {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final String[] DEFAULT_HEADERS = {
            "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36",
            "accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,de;q=0.6",
            "accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "dnt", "1",
            "accept-encoding", "gzip, deflate, br",
            "sec-fetch-dest", "document",
            "sec-fetch-mode", "navigate",
            "sec-fetch-site", "none",
            "sec-fetch-user", "?1",
            "upgrade-insecure-requests", "1",
    };

    public static String requestAndGetBody(String url, String... headers) throws IOException, InterruptedException {
        if (url == null) throw new IOException("URL is null!");
        HttpRequest request = buildRequest(url, headers);
        HttpResponse<InputStream> response = sendRequest(request);
        return fetchResponseBody(response);
    }

    public static String requestAndGetBody(String url) throws IOException, InterruptedException {
        HttpResponse<InputStream> response = requestAndGetResponse(url);
        return fetchResponseBody(response);
    }

    public static HttpResponse<InputStream> requestAndGetResponse(String url) throws IOException, InterruptedException {
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

    private static HttpResponse<InputStream> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }

    private static String fetchResponseBody(HttpResponse<InputStream> response) throws IOException {
        String encoding = fetchContentEncoding(response);
        InputStream inputStream;
        switch (encoding) {
            case "br":
                inputStream = new BrotliInputStream(response.body());
                break;
            case "gzip":
                inputStream = new GZIPInputStream(response.body());
                break;
            default:
                inputStream = response.body();
                break;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    private static String fetchContentEncoding(HttpResponse<?> response) {
        return response.headers().firstValue("Content-Encoding").orElse("");
    }
}
