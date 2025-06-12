package org.togetherjava.aoc.internal.puzzle;

import org.togetherjava.aoc.core.utils.Regex;
import org.togetherjava.aoc.core.math.matrix.Matrix;

import java.util.ArrayList;
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
     * Get a stream of all input lines
     * @return Stream of input file lines
     */
    public Stream<String> stream() {
        return Stream.of(Regex.EOL.split(rawInput));
    }

    /**
     * Get a list of all input lines
     * @return Immutable list of input file lines
     */
    public List<String> getLines() {
        return stream().toList();
    }

    public List<List<String>> getColumns() {
        // Get rows and transpose
        List<List<String>> originalLines = splitLines();
        List<List<String>> outputCols = new ArrayList<>();
        final int columnCount = originalLines.getFirst().size();
        for (int i = 0; i < columnCount; ++i) {
            outputCols.add(new ArrayList<>(originalLines.size()));
        }
        for (List<String> originalLine : originalLines) {
            for (int col = 0; col < columnCount; ++col) {
                outputCols.get(col).add(originalLine.get(col));
            }
        }
        return outputCols;
    }


    public List<List<Long>> getColumnsAsLongs() {
        // Get rows and transpose
        List<List<Long>> originalLines = parseNumbers();
        List<List<Long>> outputCols = new ArrayList<>();
        final int columnCount = originalLines.getFirst().size();
        for (int i = 0; i < columnCount; ++i) {
            outputCols.add(new ArrayList<>(originalLines.size()));
        }
        for (List<Long> originalLine : originalLines) {
            for (int col = 0; col < columnCount; ++col) {
                outputCols.get(col).add(originalLine.get(col));
            }
        }
        return outputCols;
    }

    /**
     * Get a List of longs, one per input line.
     * @return List of longs
     */
    public List<Long> parseLongPerLine() {
        return stream().map(Long::parseLong).toList();
    }

    /**
     * Split each line by the given pattern.
     * <br>
     * For example, with this puzzle input:
     * <pre>
     * ABC: 123
     * IJK: 456
     * XYZ: 789
     * </pre>
     * Calling <code>input.splitLines(": ")</code> would result in the following List of Lists:
     * <pre>
     * [
     *     ["ABC", "123"],
     *     ["IJK", "456"],
     *     ["XYZ", "789"]
     * ]
     * </pre>
     * @param regex pattern to split each line by
     * @return List of line splits
     */
    public List<List<String>> splitLines(String regex) {
        return stream().map(line -> Regex.split(regex, line)).toList();
    }

    /**
     * Split each line by whitespaces
     * @return List of line splits
     * @see PuzzleInput#splitLines(String)
     */
    public List<List<String>> splitLines() {
        return stream().map(Regex::split).toList();
    }

    /**
     * Get a list of each input line with a list of parsed longs
     * @return Stream of a list of longs
     * @see Regex#parseLongs(String)
     */
    public List<List<Long>> parseNumbers() {
        return stream().map(Regex::parseLongs).toList();
    }

    /**
     * Get a stream of each input line with a list of parsed string words
     * @return Stream of a list of strings
     * @see Regex#parseStrings(String)
     */
    public List<List<String>> parseStrings() {
        return stream().map(Regex::parseStrings).toList();
    }

    /**
     * Get the clustered input separated by at least one blank line,
     * as a list of new puzzle inputs (one for each cluster).
     */
    public List<PuzzleInput> getClusters() {
        String[] rawClusters = Regex.BLANK_LINES.split(rawInput);
        return Stream.of(rawClusters).map(PuzzleInput::new).toList();
    }

    /**
     * Convert the raw input into a jagged 2D char array.
     * <br>
     * If the input is rectangular, so too will this array be.
     * <br>
     * Outer array is array of lines. Inner array is a line data.
     * Thus, <code>result[row][col]</code> is correct
     * @return <code>char[][]</code> of the input
     */
    public char[][] toCharGrid() {
        List<String> lines = getLines();
        final int lineCount = lines.size();
        char[][] output = new char[lineCount][];
        for (int i = 0; i < lineCount; i++) {
            output[i] = lines.get(i).toCharArray();
        }
        return output;
    }

    /**
     * Turn the input into a matrix of characters
     */
    public Matrix<Character> toCharMatrix() {
        List<String> lines = getLines();
        final int lineCount = lines.size();
        Character[][] output = new Character[lineCount][];
        for (int i = 0; i < lineCount; i++) {
            String sourceLine = lines.get(i);
            Character[] row = new Character[sourceLine.length()];
            for (int j = 0; j < row.length; ++j) {
                row[j] = sourceLine.charAt(j);
            }
            output[i] = row;
        }
        return new Matrix<>(output);
    }

    /**
     * Turn the input into a matrix of individual digits 0-9
     */
    public Matrix<Integer> toDigitMatrix() {
        List<String> lines = getLines();
        final int lineCount = lines.size();
        Integer[][] output = new Integer[lineCount][];
        for (int i = 0; i < lineCount; i++) {
            String sourceLine = lines.get(i);
            Integer[] row = new Integer[sourceLine.length()];
            for (int j = 0; j < row.length; ++j) {
                row[j] = sourceLine.charAt(j) - 48; // Ascii offset
            }
            output[i] = row;
        }
        return new Matrix<>(output);
    }
}
