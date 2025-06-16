package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

import java.util.HashMap;
import java.util.Map;

public class FillRegionCommand<T> implements Command<T> {
        private final int startRow, startCol, endRow, endCol;
        private final T newValue;
        private final Map<String, T> oldValues = new HashMap<>();

        public FillRegionCommand(int startRow, int startCol, int endRow, int endCol, T newValue) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.newValue = newValue;
        }

        @Override
        public void execute(IMatrix<T> matrix) {
            oldValues.clear();
            for (int row = startRow; row <= endRow; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    if (row >= 0 && row < matrix.getRows() && col >= 0 && col < matrix.getCols()) {
                        String key = row + "," + col;
                        oldValues.put(key, matrix.get(row, col));
                        matrix.set(row, col, newValue);
                    }
                }
            }
        }

        @Override
        public void undo(IMatrix<T> matrix) {
            for (int row = startRow; row <= endRow; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    if (row >= 0 && row < matrix.getRows() && col >= 0 && col < matrix.getCols()) {
                        String key = row + "," + col;
                        T oldValue = oldValues.get(key);
                        if (oldValue != null) {
                            matrix.set(row, col, oldValue);
                        }
                    }
                }
            }
        }

        @Override
        public String getDescription() {
            return String.format("Fill region (%d,%d) to (%d,%d) with %s",
                    startRow, startCol, endRow, endCol, newValue);
        }
    }