package org.togetherjava.aoc.api;

import lombok.RequiredArgsConstructor;
import org.togetherjava.aoc.util.Matrix2D;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PuzzleInput {

    private final URI puzzleInputURI;
    private final Path puzzleInputPath;

    public Stream<String> asStream() {
        Path parent = puzzleInputPath.getParent();
        if (!Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new RuntimeException("Could not create input directory", e);
            }
        }

        if (!Files.exists(puzzleInputPath)) {
            try {
                // this is fine, since we only need to iterate once, this cast suffices
                Files.write(puzzleInputPath, (Iterable<String>) HttpUtils.getEndpoint(puzzleInputURI)::iterator);
            } catch (IOException e) {
                throw new RuntimeException("Could not write puzzle input to disk", e);
            }
        }

        try {
            return Files.lines(puzzleInputPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file from disk", e);
        }
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
     * @return Wrapped in {@link Matrix2D}
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
