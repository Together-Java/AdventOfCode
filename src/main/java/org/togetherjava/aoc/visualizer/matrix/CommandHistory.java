package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

import java.util.ArrayList;
import java.util.List;

class CommandHistory<T> {

        private final List<Command<T>> commands = new ArrayList<>();
        private int currentIndex = -1;

        public void execute(Command<T> command, IMatrix<T> matrix) {
            // Remove any commands after current index (if we're not at the end)
            if (currentIndex < commands.size() - 1) {
                commands.subList(currentIndex + 1, commands.size()).clear();
            }

            // Execute and add the command
            command.execute(matrix);
            commands.add(command);
            currentIndex++;
        }

        public boolean canUndo() {
            return currentIndex >= 0;
        }

        public boolean canRedo() {
            return currentIndex < commands.size() - 1;
        }

        public void undo(IMatrix<T> matrix) {
            if (canUndo()) {
                commands.get(currentIndex).undo(matrix);
                currentIndex--;
            }
        }

        public void redo(IMatrix<T> matrix) {
            if (canRedo()) {
                currentIndex++;
                commands.get(currentIndex).execute(matrix);
            }
        }

        public void clear() {
            commands.clear();
            currentIndex = -1;
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public int getSize() {
            return commands.size();
        }

        public String getCurrentCommandDescription() {
            if (currentIndex >= 0 && currentIndex < commands.size()) {
                return commands.get(currentIndex).getDescription();
            }
            return "No command";
        }
    }