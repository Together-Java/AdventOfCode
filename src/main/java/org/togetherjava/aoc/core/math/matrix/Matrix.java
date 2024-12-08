package org.togetherjava.aoc.core.math.matrix;


import org.togetherjava.aoc.core.utils.AocUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Matrix<T> implements Iterable<T>, IMatrix<T> {
    private final int rows;
    private final int cols;
    private final T[][] matrix;

    @SuppressWarnings("unchecked")
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = (T[][]) new Object[rows][cols];
    }

    public Matrix() {
        this(0, 0);
    }

    public Matrix(T[][] array) {
        T[][] copy = AocUtils.deepCopy(array);
        if (copy == null) {
            throw new NullPointerException("Matrix cannot be null");
        }
        this.matrix = copy;
        if (copy.length == 0) {
            this.rows = 0;
            this.cols = 0;
        } else {
            this.rows = copy.length;
            T[] firstRow = matrix[0];
            if (firstRow == null) {
                throw new NullPointerException("Matrix row 0 cannot be null");
            }
            this.cols = firstRow.length;
            for (int row = 1; row < rows; ++row) {
                T[] tRow = matrix[row];
                if (tRow == null) {
                    throw new NullPointerException("Matrix row " + row + " cannot be null");
                }
                if (tRow.length != cols) {
                    throw new IllegalArgumentException("Non-rectangular matrix given. Expected width " + cols + ", got width " + tRow.length);
                }
            }
        }
    }

    /**
     * Get a mutable <strong>reference</strong> to the underlying 2D array as {@code array[row][col]}
     * <br>
     * Usage of this return value will mutate the state of {@code this}.
     */
    public T[][] toArray() {
        return matrix;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public void set(int row, int col, T value) {
        this.matrix[row][col] = value;
    }

    @Override
    public T get(int row, int col) {
        return this.matrix[row][col];
    }

    public Matrix<T> transposed() {
        Matrix<T> transposed = new Matrix<>(this.cols, this.rows);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                transposed.matrix[col][row] = this.matrix[row][col];
            }
        }
        return transposed;
    }


    /**
     *
     * @param row region center row
     * @param col region center column
     * @param radius radius from the center position. A radius of 1 is a 3x3, a radius of 2 is 5x5, etc.
     * @return
     */
    public IMatrix<T> viewRegionAround(int row, int col, int radius) {
        var pos = new MatrixPosition(row, col);
        var currentRegion = new MatrixRegion(pos);
        var expandedRegion = currentRegion.expand(radius);
        return new SubMatrix<>(this, expandedRegion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix<?> matrix1 = (Matrix<?>) o;
        return rows == matrix1.rows && cols == matrix1.cols && Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    @Override
    public String toString() {
        return IMatrix.toString(this);
    }

    @Override
    public MatrixIterator iterator() {
        return new MatrixIterator();
    }

    public class MatrixIterator implements Iterator<T> {
        private int lastRow = 0;
        private int lastCol = -1;

        @Override
        public boolean hasNext() {
            return inBounds(lastRow, lastCol + 1) || inBounds(lastRow + 1, 0);
        }

        @Override
        public T next() {
            if (inBounds(lastRow, lastCol + 1)) {
                return matrix[lastRow][++lastCol];
            } else {
                lastRow++;
                lastCol = 0;
                return matrix[lastRow][lastCol];
            }
        }

        public boolean hasPrevious() {
            return inBounds(lastRow, lastCol - 1) || inBounds(lastRow - 1, cols - 1);
        }

        public T previous() {
            if (inBounds(lastRow, lastCol - 1)) {
                return matrix[lastRow][--lastCol];
            } else {
                lastRow--;
                lastCol = cols - 1;
                return matrix[lastRow][lastCol];
            }
        }

        public void set(T t) {
            matrix[lastRow][lastCol] = t;
        }

        public T get(T t) {
            return matrix[lastRow][lastCol];
        }

        public int getRow() {
            return lastRow;
        }

        public int getCol() {
            return lastCol;
        }
    }

    public IMatrix<T> subMatrix(MatrixRegion region) {
        return new SubMatrix<>(this, region);
    }

    private static class SubMatrix<V> implements IMatrix<V> {
        private final Matrix<V> root;
        private final int rows;
        private final int cols;
        private final int rowOffset;
        private final int colOffset;

        private SubMatrix(Matrix<V> root, MatrixRegion region) {
            this.root = root;
            this.rows = region.rows();
            this.cols = region.cols();
            MatrixPosition pos = region.topLeft();
            this.rowOffset = pos.row();
            this.colOffset = pos.col();
        }

        @Override
        public int getRows() {
            return rows;
        }

        @Override
        public int getCols() {
            return cols;
        }

        @Override
        public void set(int row, int col, V value) {
            if (outOfBounds(row, col)) {
                throw new IndexOutOfBoundsException("[%d, %d] out of bounds for SubMatrix".formatted(row, col));
            }
            root.set(row + rowOffset, col + colOffset, value);
        }

        @Override
        public V get(int row, int col) {
            if (outOfBounds(row, col)) {
                throw new IndexOutOfBoundsException("[%d, %d] out of bounds for SubMatrix".formatted(row, col));
            }
            return root.get(row + rowOffset, col + colOffset);
        }

        @Override
        public String toString() {
            return IMatrix.toString(this);
        }

        @Override
        public MatrixBorder<V> getOuterBorder(MatrixRegion region) {
            return MatrixBorder.ofOutside(root, region.move(rowOffset, colOffset));
        }
    }

    public record Entry<T>(int row, int col, T value) {
        public MatrixPosition position() {
            return new MatrixPosition(row, col);
        }

        public static <T> Entry<T> of(MatrixPosition position, T value) {
            return new Matrix.Entry<>(position.row(), position.col(), value);
        }
    }

    public Stream<Entry<T>> stream() {
        return IntStream.range(0, rows)
                .boxed()
                .flatMap(row -> IntStream.range(0, cols)
                        .mapToObj(col -> new Entry<>(row, col, matrix[row][col]))
                );
    }

    public List<Entry<T>> getEntries() {
        return stream().toList();
    }
}
