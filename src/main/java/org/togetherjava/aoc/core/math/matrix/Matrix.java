package org.togetherjava.aoc.core.math.matrix;


import org.togetherjava.aoc.core.math.Direction;
import org.togetherjava.aoc.core.utils.AocUtils;
import org.togetherjava.aoc.core.utils.Tuple;

import java.util.*;
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

    /**
     * @param target Anything that <code>equals()</code> this will be a <code>true</code> entry, otherwise <code>false</code>
     */
    public BinaryMatrix toBinaryMatrix(T target) {
        BinaryMatrix matrix = new BinaryMatrix(this, false);
        for (var entry : this.getEntries()) {
            if (Objects.equals(entry.value, target)) {
                matrix.set(entry.row, entry.col, true);
            }
        }
        return matrix;
    }

    public List<T> walkUnsafe(MatrixPosition start, String directions) {
        MatrixPosition current = start.move(0, 0);
        List<T> path = new ArrayList<>();
        path.add(get(current));
        for (char c : directions.toCharArray()) {
            var dir = Direction.ofASCII(c);
            current = current.move(dir);
            path.add(get(current));
        }
        return path;
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

    public Direction findAdjacent(MatrixPosition startPos, T target) {
        for (var dir : Direction.getCardinal()) {
            Optional<T> preview = this.tryGet(startPos.move(dir));
            if (preview.isPresent() && preview.get().equals(target)) {
                return dir;
            }
        }
        return null;
    }

    public Direction findAdjacentFacing(MatrixPosition startPos, T target, Direction facing) {
        List<Direction> options = List.of(facing, facing.rotateLeft(), facing.rotateRight());
        for (var dir : options) {
            Optional<T> preview = this.tryGet(startPos.move(dir));
            if (preview.isPresent() && preview.get().equals(target)) {
                return dir;
            }
        }
        return null;
    }

    public List<Matrix.Entry<T>> scanTaxicabBorder(MatrixPosition source, int distance) {
        List<Matrix.Entry<T>> entries = new ArrayList<>();
        for (int dr = -distance; dr <= distance; ++dr) {
            int dc = distance - Math.abs(dr);
            getEntry(source.move(dr, dc)).ifPresent(entries::add);
            if (dc != 0) {
                getEntry(source.move(dr, -dc)).ifPresent(entries::add);
            }
        }
        return entries;
    }

    public List<Matrix.Entry<T>> scanTaxicabArea(MatrixPosition source, int distance) {
        List<Matrix.Entry<T>> entries = new ArrayList<>();
        for (int dr = -distance; dr <= distance; ++dr) {
            int offset = distance - Math.abs(dr);
            for (int dc = -offset; dc <= offset; ++dc) {
                getEntry(source.move(dr, dc)).ifPresent(entries::add);
            }
        }
        return entries;
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

    public List<Entry<T>> floodFill(int row, int col) {
        return floodFill(new MatrixPosition(row, col));
    }

    public List<Entry<T>> floodFill(MatrixPosition source) {
        return floodFillWithVisited(source).getA();
    }

    public Tuple<List<Entry<T>>, BinaryMatrix> floodFillWithVisited(MatrixPosition position) {
        if (outOfBounds(position.row(), position.col())) {
            throw new IndexOutOfBoundsException("Starting position [%d, %d] is out of bounds.".formatted(position.row(), position.col()));
        }
        List<MatrixPosition> visitedPositions = new ArrayList<>();
        T target = get(position);

        BinaryMatrix visited = new BinaryMatrix(this);
        Queue<MatrixPosition> queue = new ArrayDeque<>();
        queue.add(position);

        // BFS
        while (!queue.isEmpty()) {
            MatrixPosition current = queue.poll();

            if (outOfBounds(current) || visited.get(current) || !Objects.equals(get(current), target)) {
                continue;
            }

            visited.set(current, true);
            visitedPositions.add(current);

            queue.add(current.move(Direction.NORTH));
            queue.add(current.move(Direction.EAST));
            queue.add(current.move(Direction.SOUTH));
            queue.add(current.move(Direction.WEST));
        }

        var entries = visitedPositions.stream().map(pos -> Entry.of(pos, this.get(pos))).toList();
        return new Tuple<>(entries, visited);
    }

    /**
     * Recursive depth-first search with an output argument.
     * @implNote Ignores {@code null} flood fills
     */
    private void floodFillHelper(T target, MatrixPosition current, BinaryMatrix visited, List<MatrixPosition> output, int depth) {
        if (target == null || outOfBounds(current) || visited.get(current) || !Objects.equals(target, get(current))) {
            return;
        }
        visited.set(current, true);
        output.add(current);
        for (Direction direction : Direction.getCardinal()) {
            floodFillHelper(target, current.move(direction), visited, output, depth + 1);
        }
    }

    public List<List<Entry<T>>> getAllConnectedComponents() {
        List<List<Entry<T>>> allConnectedComponents = new ArrayList<>();
        BinaryMatrix visited = new BinaryMatrix(this);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (visited.get(row, col)) {
                    continue;
                }
                // New source node
                var result = this.floodFillWithVisited(new MatrixPosition(row, col));
                var resultCC = result.getA();
                if (resultCC.isEmpty()) {
                    // Shouldn't really happen
                    continue;
                }
                // Update known components
                visited.orEquals(result.getB());
                allConnectedComponents.add(resultCC);
            }
        }
        return allConnectedComponents;
    }

    public List<Entry<T>> getLargestConnectedComponent() {
        var ccs = getAllConnectedComponents();
        List<Entry<T>> largestCC = List.of();
        for (var cc : ccs) {
            if (cc.size() > largestCC.size()) {
                largestCC = cc;
            }
        }
        return largestCC;
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

    public Optional<Entry<T>> getEntry(MatrixPosition pos) {
        if (this.outOfBounds(pos)) {
            return Optional.empty();
        }
        return Optional.of(Entry.of(pos, this.get(pos)));
    }

    public Stream<Entry<T>> filter(BinaryMatrix binaryMatrix) {
        return this.stream().filter(entry -> binaryMatrix.get(entry.position()));
    }

    public Entry<T> find(T target) {
        return stream()
                .filter(entry -> Objects.equals(entry.value, target))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Entry<T>> findAll(T target) {
        return stream()
                .filter(entry -> Objects.equals(entry.value, target))
                .toList();
    }
}
