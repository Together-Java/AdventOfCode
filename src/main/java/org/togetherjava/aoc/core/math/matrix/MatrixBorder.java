package org.togetherjava.aoc.core.math.matrix;

import org.togetherjava.aoc.core.utils.AocUtils;
import org.togetherjava.aoc.core.utils.StringUtils;

import java.util.*;

public class MatrixBorder<T> implements Iterable<T> {
    private final T northWest;
    private final T northEast;
    private final T southWest;
    private final T southEast;
    // Stored left to right
    private final List<T> north;
    // Stored left to right
    private final List<T> south;
    // Stored top to bottom
    private final List<T> east;
    // Stored top to bottom
    private final List<T> west;

    public MatrixBorder(T northWest, T northEast, T southWest, T southEast, List<T> north, List<T> south, List<T> east, List<T> west) {
        this.northWest = northWest;
        this.northEast = northEast;
        this.southWest = southWest;
        this.southEast = southEast;
        this.north = Collections.unmodifiableList(north);
        this.south = Collections.unmodifiableList(south);
        this.east = Collections.unmodifiableList(east);
        this.west = Collections.unmodifiableList(west);
    }

    /**
     * Create a 1 wide border around the outside of the region.
     * @param matrix
     * @param region
     * @return
     * @param <T>
     */
    public static <T> MatrixBorder<T> ofOutside(IMatrix<T> matrix, MatrixRegion region) {
        return ofInside(matrix, region.expand(1));
    }

    /**
     * Create a 1 wide border around the outside of the region.
     * @param matrix
     * @param region
     * @return
     * @param <T>
     */
    public static <T> MatrixBorder<T> ofInside(IMatrix<T> matrix, MatrixRegion region) {
        var topLeft = region.topLeft();
        var topRight = region.topRight();
        var bottomLeft = region.bottomLeft();
        var bottomRight = region.bottomRight();

        int topRow = topLeft.row();
        int bottomRow = bottomLeft.row();
        int leftCol = topLeft.col();
        int rightCol = topRight.col();

        T northWest = matrix.tryGet(topLeft).orElse(null);
        T northEast = matrix.tryGet(topRight).orElse(null);
        T southWest = matrix.tryGet(bottomLeft).orElse(null);
        T southEast = matrix.tryGet(bottomRight).orElse(null);

        List<T> north = new ArrayList<>();
        List<T> south = new ArrayList<>();
        List<T> east = new ArrayList<>();
        List<T> west = new ArrayList<>();

        for (int col = leftCol + 1; col < rightCol; ++col) {
            north.add(matrix.tryGet(topRow, col).orElse(null));
            south.add(matrix.tryGet(bottomRow, col).orElse(null));
        }

        for (int row = topRow + 1; row < bottomRow; ++row) {
            west.add(matrix.tryGet(row, leftCol).orElse(null));
            east.add(matrix.tryGet(row, rightCol).orElse(null));
        }

        return new MatrixBorder<>(
                northWest,
                northEast,
                southWest,
                southEast,
                north,
                south,
                east,
                west
        );
    }

    @Override
    public Iterator<T> iterator() {
        return getAllNonNull().iterator();
    }

    /**
     * Get all elements in a clockwise rotation starting from the left side of north.
     * @return
     */
    public List<T> getAllNonNull() {
        List<T> elements = new ArrayList<>(north);
        elements.add(northEast);
        elements.addAll(east);
        elements.add(southEast);
        elements.addAll(AocUtils.reversed(south));
        elements.add(southWest);
        elements.addAll(AocUtils.reversed(west));
        elements.add(northWest);
        elements.removeIf(Objects::isNull);
        return elements;
    }

    public List<T> getAllSides() {
        List<T> elements = new ArrayList<>(north);
        elements.addAll(east);
        elements.addAll(AocUtils.reversed(south));
        elements.addAll(AocUtils.reversed(west));
        elements.removeIf(Objects::isNull);
        return elements;
    }

    public List<T> getCornersNonNull() {
        var list = getCorners();
        list.removeIf(Objects::isNull);
        return list;
    }

    public List<T> getCorners() {
        List<T> list = new ArrayList<>(4);
        list.add(northWest);
        list.add(northEast);
        list.add(southEast);
        list.add(southWest);
        return list;
    }

    public List<T> getLeft() {
        List<T> elements = new ArrayList<>();
        elements.add(northWest);
        elements.addAll(west);
        elements.add(southWest);
        return elements;
    }

    public List<T> getRight() {
        List<T> elements = new ArrayList<>();
        elements.add(northEast);
        elements.addAll(east);
        elements.add(southEast);
        return elements;
    }

    public List<T> getTop() {
        List<T> elements = new ArrayList<>();
        elements.add(northWest);
        elements.addAll(north);
        elements.add(northEast);
        return elements;
    }

    public List<T> getBottom() {
        List<T> elements = new ArrayList<>();
        elements.add(southWest);
        elements.addAll(south);
        elements.add(southEast);
        return elements;
    }

    public T getNorthWest() {
        return northWest;
    }

    public T getNorthEast() {
        return northEast;
    }

    public T getSouthWest() {
        return southWest;
    }

    public T getSouthEast() {
        return southEast;
    }

    public List<T> getNorth() {
        return north;
    }

    public List<T> getSouth() {
        return south;
    }

    public List<T> getEast() {
        return east;
    }

    public List<T> getWest() {
        return west;
    }

    public int width() {
        return this.north.size() + 2;
    }

    public int height() {
        return this.east.size() + 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixBorder<?> that = (MatrixBorder<?>) o;
        return Objects.equals(northWest, that.northWest) && Objects.equals(northEast, that.northEast) && Objects.equals(southWest, that.southWest) && Objects.equals(southEast, that.southEast) && Objects.equals(north, that.north) && Objects.equals(south, that.south) && Objects.equals(east, that.east) && Objects.equals(west, that.west);
    }

    @Override
    public int hashCode() {
        return Objects.hash(northWest, northEast, southWest, southEast, north, south, east, west);
    }

    @Override
    public String toString() {
        int[] colWidths = new int[width()];
        colWidths[0] = StringUtils.maxWidth(getLeft());
        colWidths[colWidths.length - 1] = StringUtils.maxWidth(getRight());
        for (int i = 0; i < north.size(); ++i) {
            colWidths[i + 1] = Math.max(
                    String.valueOf(north.get(i)).length(),
                    String.valueOf(south.get(i)).length()
            );
        }
        StringJoiner lines = new StringJoiner("\n");

        // Add top row
        StringJoiner sjTop = new StringJoiner(" ");
        List<T> topItems = getTop();
        for (int i = 0; i < width(); ++i) {
            sjTop.add(StringUtils.rightPad(String.valueOf(topItems.get(i)), colWidths[i]));
        }
        final String topStr = sjTop.toString();
        lines.add(topStr);

        // Add middle rows
        final int totalWidth = topStr.length();
        final int middleGapSize = totalWidth - colWidths[0] - colWidths[colWidths.length - 1];
        final String middleGap = " ".repeat(middleGapSize);
        for (int row = 0; row < east.size(); ++row) {
            lines.add(west.get(row) + middleGap + east.get(row));
        }

        // Add bottom row
        StringJoiner sjBottom = new StringJoiner(" ");
        List<T> bottomItems = getBottom();
        for (int i = 0; i < width(); ++i) {
            sjBottom.add(StringUtils.rightPad(String.valueOf(bottomItems.get(i)), colWidths[i]));
        }
        lines.add(sjBottom.toString());
        return lines.toString();
    }


}
