package org.togetherjava.aoc.core.puzzle;

import org.togetherjava.aoc.core.utils.Matrix2D;

import java.util.List;
import java.util.stream.Stream;

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

    public Character[][] toGrid() {
        List<String> input = getLines();
        Character[][] matrix = new Character[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            matrix[i] = input.get(i).chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        }
        return matrix;
    }

    /**
     *
     * @return Wrapped in {@link Matrix2D}
     * to perform matrix operations on
     */
    public Matrix2D<Character> toMatrix() {
        return new Matrix2D<>(toGrid());
    }

    /**
     *
     * @return If the input has more than 1 region of inputs delimited by empty lines, return all the groups of inputs.
     */
    public List<List<String>> toMultipleInputs() {
        String[] parts = rawInput.split("\\R{2,}");
        return Stream.of(parts).map(lines -> List.of(lines.split("\\R"))).toList();
    }
}
