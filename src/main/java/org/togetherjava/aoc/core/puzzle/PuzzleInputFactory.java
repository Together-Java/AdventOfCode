package org.togetherjava.aoc.core.puzzle;

import org.togetherjava.aoc.internal.HttpUtils;
import org.togetherjava.aoc.internal.PuzzleDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class PuzzleInputFactory {

    /**
     * Get your puzzle input for the given year and day.
     * <br>
     * Internally caches to the {@code user.home} directory
     * after fetching from the API.
     * @param year Advent of Code year
     * @param day Advent of Code day
     * @return PuzzleInput object representing your puzzle input text
     */
    public static PuzzleInput of(int year, int day) {
        // Use the cached file if possible
        Optional<PuzzleInput> cachedInput = getCachedInput(year, day);
        if (cachedInput.isPresent()) {
            return cachedInput.get();
        }

        // Fetch from API
        String rawInput = fetchInput(year, day);

        final String dayNotAvailable = "Please don't repeatedly request this endpoint before it unlocks!";
        if(rawInput.startsWith(dayNotAvailable)) {
            throw new RuntimeException("Year %d day %02d isn't available yet.".formatted(year, day));
        }

        cacheInput(year, day, rawInput);
        return PuzzleInput.of(rawInput);
    }

    /**
     * Get your puzzle input for the given date.
     * <br>
     * Internally caches to the {@code user.home} directory
     * after fetching from the API.
     * @param date Advent of Code date
     * @return PuzzleInput object representing your puzzle input text
     */
    public static PuzzleInput of(PuzzleDate date) {
        return of(date.year(), date.day());
    }

    /**
     * Return the local, platform-independent input directory.
     * @return the full {@link Path} to the input directory
     */
    private static Path getInputDir() {
        String home = System.getProperty("user.home");
        if (home == null) {
            throw new IllegalStateException("user.home property cannot be found.");
        }
        return Paths.get(home, ".together-java", "aoc", "inputs");
    }

    /**
     * Get the full path to the input text file, which may or may not exist.
     * @param year Advent of Code year
     * @param day Advent of Code day
     * @return the full {@link Path} to the input file.
     */
    private static Path getInputFilePath(int year, int day) {
        return getInputDir().resolve("%d-%02d-puzzle-input.txt".formatted(year, day));
    }

    /**
     * Attempt to find the puzzle input from local file cache.
     * @param year Advent of Code year
     * @param day Advent of Code day
     * @return An optional {@link PuzzleInput}, which is present if cached.
     */
    private static Optional<PuzzleInput> getCachedInput(int year, int day) {
        Path filePath = getInputFilePath(year, day);
        try {
            String input = Files.readString(filePath);
            return Optional.of(PuzzleInput.of(input));
        } catch (IOException ioException) {
            return Optional.empty();
        }
    }

    /**
     * Fetch your input file from the AOC servers.
     * @param year Advent of Code year
     * @param day Advent of Code day
     * @return {@link String} server response
     */
    private static String fetchInput(int year, int day) {
        return HttpUtils.get("https://adventofcode.com/%d/day/%d/input".formatted(year, day));
    }

    /**
     * Write a puzzle input to the file cache
     * @param year Advent of Code year
     * @param day Advent of Code day
     * @param input puzzle input text
     * @throws RuntimeException if insufficient file permissions.
     */
    private static void cacheInput(int year, int day, String input) {
        Path filePath = getInputFilePath(year, day);
        try {
            Files.createDirectories(getInputDir());
            Files.writeString(filePath, input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to input file cache", e);
        }
    }

}
