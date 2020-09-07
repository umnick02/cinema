package com.cinema.core.helper;

import com.cinema.core.config.Preferences;
import org.brotli.dec.BrotliInputStream;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.http.HttpHeaders.LOCATION;

public class HttpHelper {

    private static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
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

    public static Object requestAndGetBody(String url, String... headers) throws IOException, InterruptedException {
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

    public static HttpRequest buildRequest(String url) {
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

    public static HttpResponse<InputStream> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
        Optional<String> optional = response.headers().firstValue(LOCATION);
        if (optional.isPresent()) {
            return sendRequest(buildRequest(optional.get().replaceAll("[+ ]", "%20"),
                    "referer", request.uri().toString()));
        }
        return response;
    }

    public static String fetchResponseBody(HttpResponse<InputStream> response) throws IOException {
        String encoding = fetchContentEncoding(response);
        StringBuilder result = new StringBuilder();
        InputStream inputStream = null;
        try {
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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public static String fetchResponseFile(HttpResponse<InputStream> response, String folderName) throws IOException {
        folderName = folderName.replaceAll(" ", "_");
        try (InputStream inputStream = response.body()) {
            if (response.headers().firstValue("content-disposition").isEmpty()) {
                return null;
            }
            String fileName = response.headers().firstValue("content-disposition").get().split("\"")[1];
            String zipFilePath = String.format("%s%s\\%s", Preferences.getPreference(Preferences.PrefKey.STORAGE), folderName, fileName);
            new File(zipFilePath).getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(zipFilePath)) {
                byte[] b = new byte[1024];
                int count;
                while ((count = inputStream.read(b)) >= 0) {
                    out.write(b, 0, count);
                }
            }
            try (ZipFile zipFile = new ZipFile(zipFilePath)) {
                Enumeration<ZipEntry> zipFileEntries = (Enumeration<ZipEntry>) zipFile.entries();
                while (zipFileEntries.hasMoreElements()) {
                    ZipEntry entry = zipFileEntries.nextElement();
                    String entryName = entry.getName();
                    if (!entryName.matches(".*\\.srt$")) {
                        continue;
                    }
                    String subtitleFilePath = String.format("%s\\%s", folderName, entryName);
                    File destFile = new File(Preferences.getPreference(Preferences.PrefKey.STORAGE) + subtitleFilePath);
                    destFile.getParentFile().mkdirs();
                    if (!entry.isDirectory()) {
                        try (BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry))) {
                            int currentByte;
                            byte[] data = new byte[1024];
                            try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(destFile), 1024)) {
                                while ((currentByte = is.read(data, 0, 1024)) != -1) {
                                    dest.write(data, 0, currentByte);
                                }
                                return subtitleFilePath;
                            }
                        }
                    }
                }
            } finally {
                Files.delete(Path.of(zipFilePath));
            }
        }
        return null;
    }

    private static String fetchContentEncoding(HttpResponse<?> response) {
        return response.headers().firstValue("Content-Encoding").orElse("");
    }
}
