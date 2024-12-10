package org.togetherjava.aoc.core.math.matrix;

import org.togetherjava.aoc.core.math.Direction;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface IMatrix<T> {
    int getRows();
    int getCols();

    void set(int row, int col, T value);

    default void set(MatrixPosition pos, T value) {
        set(pos.row(), pos.col(), value);
    }

    T get(int row, int col);

    default T get(MatrixPosition pos) {
        return get(pos.row(), pos.col());
    }

    default MatrixRegion getFullRegion() {
        return new MatrixRegion(new MatrixPosition(0, 0), new MatrixPosition(getRows() - 1, getCols() - 1));
    }

    default MatrixBorder<T> getInnerBorder(MatrixRegion region) {
        return MatrixBorder.ofInside(this, region);
    }

    default MatrixBorder<T> getInnerBorder(int row, int col) {
        return getInnerBorder(new MatrixRegion(row, col));
    }

    default MatrixBorder<T> getInnerBorder(MatrixPosition position) {
        return getInnerBorder(new MatrixRegion(position));
    }

    default MatrixBorder<T> getInnerBorder() {
        return getInnerBorder(getFullRegion());
    }

    default MatrixBorder<T> getOuterBorder(MatrixRegion region) {
        return MatrixBorder.ofOutside(this, region);
    }

    default MatrixBorder<T> getOuterBorder(int row, int col) {
        return getOuterBorder(new MatrixRegion(row, col));
    }

    default MatrixBorder<T> getOuterBorder(MatrixPosition position) {
        return getOuterBorder(new MatrixRegion(position));
    }

    default MatrixBorder<T> getOuterBorder() {
        return getOuterBorder(getFullRegion());
    }

    default Optional<T> tryGet(int row, int col) {
        if (outOfBounds(row, col)) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(get(row, col));
        }
    }

    default Optional<T> tryGet(MatrixPosition pos) {
        return tryGet(pos.row(), pos.col());
    }

    default boolean inBounds(MatrixPosition position) {
        return inBounds(position.row(), position.col());
    }

    default boolean inBounds(int row, int col) {
        return 0 <= row && row < getRows() && 0 <= col && col < getCols();
    }

    default boolean outOfBounds(int row, int col) {
        return !inBounds(row, col);
    }

    default boolean outOfBounds(MatrixPosition position) {
        return outOfBounds(position.row(), position.col());
    }

    default void setAll(T value) {
        final int rows = getRows();
        final int cols = getCols();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                set(row, col, value);
            }
        }
    }

    default List<Matrix.Entry<T>> rayCast(MatrixPosition position, Direction direction) {
        return rayCast(position, direction, Integer.MAX_VALUE);
    }

    /**
     * Inclusive ray cast from the given position
     */
    default List<Matrix.Entry<T>> rayCast(MatrixPosition position, Direction direction, int limit) {
        MatrixPosition current = position;
        List<Matrix.Entry<T>> elements = new ArrayList<>(limit);
        while (inBounds(current) && elements.size() < limit) {
            var entry = Matrix.Entry.of(current, this.get(current));
            elements.add(entry);
            current = current.move(direction);
        }
        return elements;
    }

    default List<Matrix.Entry<T>> rayCastWhile(MatrixPosition position, Direction direction, Predicate<? super Matrix.Entry<T>> predicate) {
        MatrixPosition current = position;
        List<Matrix.Entry<T>> elements = new ArrayList<>();
        while (inBounds(current)) {
            T currentElement = this.get(current);
            var entry = Matrix.Entry.of(current, currentElement);
            if (!predicate.test(entry)) {
                break;
            }
            elements.add(entry);
            current = current.move(direction);
        }
        return elements;
    }

    default List<T> getNeighbors(MatrixPosition position) {
        return this.getOuterBorder(position).getAllNonNull();
    }

    static <T> boolean equals(IMatrix<T> a, IMatrix<T> b) {
        boolean sameDim = a.getRows() == b.getRows() && a.getCols() == b.getCols();
        if (!sameDim) {
            return false;
        }
        int rows = a.getRows();
        int cols = a.getCols();
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                T aElement = a.get(row, col);
                T bElement = b.get(row, col);
                if (!Objects.equals(aElement, bElement)) {
                    return false;
                }
            }
        }
        return true;
    }


    static <T> String toString(IMatrix<T> matrix) {
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        if (rows == 0 || cols == 0) {
            return "[]";
        }
        String[][] strings = new String[rows][cols];
        int[] maxColumnWidths = new int[cols];

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                String cellString = String.valueOf(matrix.get(row, col));
                strings[row][col] = cellString;
                maxColumnWidths[col] = Math.max(maxColumnWidths[col], cellString.length());
            }
        }

        int innerWidth = IntStream.of(maxColumnWidths).sum() + cols - 1 ;
        StringJoiner rowJoiner = new StringJoiner(
                "\n",
                "┌ " + " ".repeat(innerWidth) + " ┐\n",
                "\n└ " + " ".repeat(innerWidth) + " ┘"
        );

        for (int row = 0; row < rows; ++row) {
            StringJoiner rowBuilder = new StringJoiner(" ", "│ ", " │");
            for (int col = 0; col < cols; ++col) {
                String cell = strings[row][col];
                int paddingSize = maxColumnWidths[col] - cell.length();
                if (paddingSize > 0) {
                    cell = " ".repeat(paddingSize) + cell;
                }
                rowBuilder.add(cell);
            }
            rowJoiner.add(rowBuilder.toString());
        }

        return rowJoiner.toString();
    }
}
