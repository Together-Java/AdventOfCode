package org.togetherjava.aoc;

import org.togetherjava.aoc.core.puzzle.PuzzleInputFactory;
import org.togetherjava.aoc.solutions.Day01;

public class Aoc {

    public static void main(String[] args) {
        String sessionCookie = System.getenv("AOC_SESSION_COOKIE");
        if (sessionCookie == null) {
            throw new IllegalStateException("You must set your AOC_SESSION_COOKIE environment variable in your gradle settings.");
        }
        var day = new Day01();
        System.out.println("#".repeat(50));
        var input = PuzzleInputFactory.of(2024, 1);
        System.out.println("Part 1 solution: %s".formatted(day.part1(input)));
        System.out.println("Part 2 solution: %s".formatted(day.part2(input)));
        System.out.println("#".repeat(50));
    }
}
