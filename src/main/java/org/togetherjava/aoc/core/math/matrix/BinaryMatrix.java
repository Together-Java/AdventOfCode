package org.togetherjava.aoc.core.math.matrix;

import java.util.BitSet;
import java.util.StringJoiner;

public class BinaryMatrix implements IMatrix<Boolean> {
    private final BitSet bitSet;
    private final int rows;
    private final int cols;

    private static BitSet copyBitSet(BitSet bitSet) {
        BitSet result = new BitSet();
        result.or(bitSet);
        return result;
    }

    public BinaryMatrix(int rows, int cols, BitSet bitSet) {
        this.rows = rows;
        this.cols = cols;
        this.bitSet = copyBitSet(bitSet);
        if (bitSet.length() > rows * cols) {
            throw new IllegalArgumentException(
                    "BitSet size was %d, expected [%d * %d = %d]"
                            .formatted(bitSet.size(), rows, cols, rows * cols)
            );
        }
    }

    /**
     * Construct a binary matrix of the given size, initialized to false
     * @param rows number of rows
     * @param cols number of columns
     */
    public BinaryMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.bitSet = new BitSet(rows * cols);
    }

    /**
     * Construct a binary matrix of the given size, initialized to false
     * @param size matrix to copy the size of
     */
    public BinaryMatrix(IMatrix<?> size) {
        this(size.getRows(), size.getCols());
    }

    public BinaryMatrix(int rows, int cols, boolean initialValue) {
        this(rows, cols);
        this.setAll(initialValue);
    }

    public BinaryMatrix(IMatrix<?> size, boolean initialValue) {
        this(size);
        this.setAll(initialValue);
    }

    /**
     * Copy constructor
     * @param toCopy matrix to copy
     */
    public BinaryMatrix(BinaryMatrix toCopy) {
        this(toCopy.rows, toCopy.cols, toCopy.bitSet);
    }

    public static BinaryMatrix copy(BinaryMatrix matrix) {
        return new BinaryMatrix(matrix);
    }


    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    private int getOffset(int row, int col) {
        return (row * cols) + col;
    }

    @Override
    public void set(int row, int col, Boolean value) {
        bitSet.set(getOffset(row, col), value);
    }

    @Override
    public Boolean get(int row, int col) {
        return bitSet.get(getOffset(row, col));
    }

    @Override
    public void setAll(Boolean value) {
        this.bitSet.set(0, rows * cols, value);
    }

    /**
     * Logical AND this matrix and the given matrix.
     * Mutates {@code this}.
     * @param other matrix to AND with
     */
    public void andEquals(BinaryMatrix other) {
        this.bitSet.and(other.bitSet);
    }

    /**
     * Logical OR this matrix and the given matrix.
     * Mutates {@code this}.
     * @param other matrix to OR with
     */
    public void orEquals(BinaryMatrix other) {
        this.bitSet.or(other.bitSet);
    }

    public void xorEquals(BinaryMatrix other) {
        this.bitSet.xor(other.bitSet);
    }

    public BinaryMatrix and(BinaryMatrix b) {
        var result = new BinaryMatrix(this);
        result.andEquals(b);
        return result;
    }

    public BinaryMatrix or(BinaryMatrix b) {
        var result = new BinaryMatrix(this);
        result.orEquals(b);
        return result;
    }

    public BinaryMatrix xor(BinaryMatrix b) {
        var result = new BinaryMatrix(this);
        result.xorEquals(b);
        return result;
    }

    /**
     * Count number of ones
     * @return
     */
    public int countOnes() {
        return bitSet.cardinality();
    }

    public int countZeros() {
        return size() - bitSet.cardinality();
    }

    public int size() {
        return rows * cols;
    }

    public int countColumn(int col) {
        int count = 0;
        for (int row = 0; row < rows; ++row) {
            if (this.get(row, col)) {
                count++;
            }
        }
        return count;
    }

    public int countRow(int row) {
        int count = 0;
        for (int col = 0; col < cols; ++col) {
            if (this.get(row, col)) {
                count++;
            }
        }
        return count;
    }

    public boolean allTrue() {
        return countOnes() == size();
    }

    public boolean allFalse() {
        return countZeros() == size();
    }

    public boolean anyTrue() {
        return countOnes() > 0;
    }

    public boolean anyFalse() {
        return countZeros() > 0;
    }

    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        for (int row = 0; row < rows; ++row) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < cols; ++col) {
                sb.append(this.get(row, col) ? "1" : "0");
            }
            sj.add(sb);
        }
        return sj.toString();
    }
}
