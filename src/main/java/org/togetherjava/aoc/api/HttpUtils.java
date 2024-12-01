package org.togetherjava.aoc.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

public class HttpUtils {

    private static final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    public static Stream<String> getEndpoint(final URI URI) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI)
                .timeout(Duration.ofSeconds(3))
                .headers("User-Agent", "https://github.com/Together-Java/AdventOfCode")
                .headers("Cookie", "session=" + System.getenv("session_cookie"))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofLines()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
