package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

// Command pattern implementation
    public interface Command<T> {
        void execute(IMatrix<T> matrix);
        void undo(IMatrix<T> matrix);
        String getDescription();
    }