package org.togetherjava.aoc.core.puzzle;

import java.util.List;

/**
 * Represents the puzzle input text
 * @param rawInput
 */
public record PuzzleInput(String rawInput) {

    public static PuzzleInput of(String input) {
        return new PuzzleInput(input);
    }

    /**
     * Immutable input lines
     * @return Immutable list of input file lines
     */
    public List<String> getLines() {
        return List.of(rawInput.split("\\R"));
    }

}
