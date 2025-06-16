package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.BinaryMatrix;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Example {

	public static void main(String[] args) {
		BinaryMatrix matrix = new BinaryMatrix(10, 10);

		MatrixVisualizer<Boolean> visualizer = MatrixVisualizer.show(matrix, "Matrix Visualizer Demo");

		visualizer.setTextMapper(value -> value ? "0" : "1");

		visualizer.setColorMapper(value -> value ? Color.GREEN : Color.RED);

		for(int i = 0; i < 10; i++) {
			randomizeMatrix(matrix, visualizer);
		}
	}

	private static void randomizeMatrix(BinaryMatrix matrix, MatrixVisualizer<Boolean> visualizer) {
		List<Command<Boolean>> commands = new ArrayList<>();
		for(int i = 0; i < matrix.getRows(); i++) {
			for(int j = 0; j < matrix.getCols(); j++) {
				commands.add(new SetCellCommand<>(i, j, ThreadLocalRandom.current().nextBoolean()));
			}
		}
		visualizer.executeCommand(new BatchCommand<>(commands, "Updated batch"));
	}
}
