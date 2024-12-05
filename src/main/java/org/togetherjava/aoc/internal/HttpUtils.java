package org.togetherjava.aoc.internal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpUtils {

    private static final HttpClient client = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(5)) // infinite by default
            .build();

    public static String get(String url) {
        return get(url, HttpResponse.BodyHandlers.ofString());
    }

    public static <T> T get(String url, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(3))
                .headers("User-Agent", "https://github.com/Together-Java/AdventOfCode")
                .headers("Cookie", "session=" + System.getenv("AOC_SESSION_COOKIE"))
                .build();
        try {
            return client.send(request, bodyHandler).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
