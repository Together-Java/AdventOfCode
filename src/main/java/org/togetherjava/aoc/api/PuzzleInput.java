package org.togetherjava.aoc.api;

import lombok.RequiredArgsConstructor;
import org.togetherjava.aoc.util.Matrix2D;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PuzzleInput {

    private final URI puzzleInputURI;

    public Stream<String> asStream() {
        return HttpUtils.getEndpoint(puzzleInputURI);
    }

    public String asString() {
        return asStream().collect(Collectors.joining("\n"));
    }

    public Character[][] as2DArray() {
        List<String> input = asStream().collect(Collectors.toList());
        Character[][] matrix = new Character[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            matrix[i] = input.get(i).chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        }
        return matrix;
    }

    /**
     *
     * @return Wrapped in {@link org.togetherjava.aoc.util.Matrix2D}
     * to perform matrix operations on
     */
    public Matrix2D<Character> asMatrix() {
        return new Matrix2D<>(as2DArray());
    }

    /**
     *
     * @return the input as comma seperated values
     */
    public String[] asCSV() {
        return asString().split(",");
    }
}
