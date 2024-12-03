package org.togetherjava.aoc.api;

import lombok.Getter;

import java.net.URI;
import java.nio.file.Paths;

@Getter
public abstract class AbstractDay {

    private final int year;
    private final int day;
    private final PuzzleInput input;

    public AbstractDay(int year, int day) {
        this.year = year;
        this.day = day;
        this.input = new PuzzleInput(
                URI.create(String.format("https://adventofcode.com/%d/day/%d/input", year, day)),
                Paths.get("inputs", String.valueOf(year), String.format("%02d", day))
        );
    }


    /**
     *
     * @return The object in which the toString() function will be called to display the solution of the first half
     */
    public abstract Object part1Solution();

    /**
     *
     * @return The object in which the toString() function will be called to display the solution of the second half
     */
    public abstract Object part2Solution();

    //TODO: add benchmarking with JMH
    public void benchmarkPart1() {
        part1Solution();
    }

    public void benchmarkPart2() {
        part2Solution();
    }
}
