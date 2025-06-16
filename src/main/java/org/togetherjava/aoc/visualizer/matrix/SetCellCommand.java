package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

public class SetCellCommand<T> implements Command<T> {
        private final int row;
        private final int col;
        private final T newValue;
        private T oldValue;

        public SetCellCommand(int row, int col, T newValue) {
            this.row = row;
            this.col = col;
            this.newValue = newValue;
        }

        @Override
        public void execute(IMatrix<T> matrix) {
            oldValue = matrix.get(row, col);
            matrix.set(row, col, newValue);
        }

        @Override
        public void undo(IMatrix<T> matrix) {
            matrix.set(row, col, oldValue);
        }

        @Override
        public String getDescription() {
            return String.format("Set cell (%d,%d) to %s", row, col, newValue);
        }
    }