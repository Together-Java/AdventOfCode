package org.togetherjava.aoc.core.math.matrix;

import org.togetherjava.aoc.core.math.Direction;

public record MatrixPosition(int row, int col) {

    public MatrixPosition move(int rows, int cols) {
        return new MatrixPosition(row + rows, col + cols);
    }

    public MatrixPosition move(Direction direction) {
        return new MatrixPosition(row - direction.getY(), col + direction.getX());
    }

    public MatrixPosition move(Direction direction, int magnitude) {
        return new MatrixPosition(row - (direction.getY() * magnitude), col + (direction.getX() * magnitude));
    }
}
