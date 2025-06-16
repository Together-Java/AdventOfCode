package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

import java.util.ArrayList;
import java.util.List;

public class BatchCommand<T> implements Command<T> {

        private final List<Command<T>> commands;
        private final String description;

        public BatchCommand(List<Command<T>> commands, String description) {
            this.commands = new ArrayList<>(commands);
            this.description = description;
        }

        @Override
        public void execute(IMatrix<T> matrix) {
            for (Command<T> command : commands) {
                command.execute(matrix);
            }
        }

        @Override
        public void undo(IMatrix<T> matrix) {
            // Undo in reverse order
            for (int i = commands.size() - 1; i >= 0; i--) {
                commands.get(i).undo(matrix);
            }
        }

        @Override
        public String getDescription() {
            return description;
        }
    }