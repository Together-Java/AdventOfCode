package org.togetherjava.aoc.core.math.matrix;

public class MatrixRegion {

    private final MatrixPosition topLeft;
    private final int cols;
    private final int rows;

    public MatrixRegion(MatrixPosition pos) {
        this(pos, pos);
    }

    public MatrixRegion(int row, int col) {
        this(new MatrixPosition(row, col));
    }

    public MatrixRegion(MatrixPosition pos1, MatrixPosition pos2) {
        int topRow = Math.min(pos1.row(), pos2.row());
        int topCol = Math.min(pos1.col(), pos2.col());
        int bottomRow = Math.max(pos1.row(), pos2.row());
        int bottomCol = Math.max(pos1.col(), pos2.col());

        this.topLeft = new MatrixPosition(topRow, topCol);
        this.rows = Math.abs(bottomRow - topRow) + 1;
        this.cols = Math.abs(bottomCol - topCol) + 1;
    }

    /**
     * Expand about the center in all directions by the given size.
     * @param size size to grow by
     * @return new region
     */
    public MatrixRegion expand(int size) {
        return new MatrixRegion(
                this.topLeft.move(-size, -size),
                this.bottomRight().move(size, size)
        );
    }

    public MatrixRegion move(int rows, int cols) {
        return new MatrixRegion(topLeft.move(rows, cols), bottomRight().move(rows, cols));
    }

    /**
     * Number of rows in this region
     * @return
     */
    public int rows() {
        return rows;
    }

    /**
     * Number of columns in this region
     * @return
     */
    public int cols() {
        return cols;
    }

    public MatrixPosition topLeft() {
        return topLeft;
    }

    public MatrixPosition bottomRight() {
        return new MatrixPosition(topLeft.row() + rows - 1, topLeft.col() + cols - 1);
    }

    public MatrixPosition topRight() {
        return new MatrixPosition(topLeft.row(), topLeft.col() + cols - 1);
    }

    public MatrixPosition bottomLeft() {
        return new MatrixPosition(topLeft.row() + rows - 1, topLeft.col());
    }
}
