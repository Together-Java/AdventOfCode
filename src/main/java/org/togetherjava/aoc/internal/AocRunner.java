package org.togetherjava.aoc.internal;

import org.togetherjava.aoc.core.puzzle.PuzzleInput;
import org.togetherjava.aoc.core.puzzle.PuzzleInputFactory;
import org.togetherjava.aoc.core.puzzle.PuzzleSolution;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AocRunner {

    /**
     * Run part 1 and part 2 of today's puzzle
     */
    public static void run() {
        run(currentPuzzleSolution());
    }

    /**
     * Run part 1 and part 2 of the given puzzle year and day
     */
    public static void run(int year, int day) {
        run(new PuzzleDate(year, day));
    }

    /**
     * Run part 1 and part 2 of the given puzzle date
     */
    public static void run(PuzzleDate date) {
        run(getPuzzleSolution(date));
    }

    /**
     * Run part 1 and part 2 of the given puzzle solution
     * @param solution Puzzle solution to invoke
     */
    public static void run(Class<? extends PuzzleSolution> solution) {
        PuzzleSolution impl = construct(solution);
        PuzzleInput input = getInput(solution);
        System.out.println("#".repeat(50));
        runPart1(impl, input);
        runPart2(impl, input);
        System.out.println("#".repeat(50));
    }

    /**
     * Run part 1 of today's puzzle
     */
    public static void runPart1() {
        runPart1(currentPuzzleSolution());
    }

    /**
     * Run part 1 of the given puzzle year and day
     */
    public static void runPart1(int year, int day) {
        runPart1(new PuzzleDate(year, day));
    }

    /**
     * Run part 1 of the given puzzle date
     */
    public static void runPart1(PuzzleDate date) {
        runPart1(getPuzzleSolution(date));
    }

    /**
     * Run part 1 of the given puzzle solution
     * @param solution Puzzle solution to invoke
     */
    public static void runPart1(Class<? extends PuzzleSolution> solution) {
        runPart1(construct(solution), getInput(solution));
    }

    private static void runPart1(PuzzleSolution solution, PuzzleInput input) {
        Object answer = solution.part1(input);
        logAnswer(answer, solution, 1);
    }

    /**
     * Run part 2 of today's puzzle
     */
    public static void runPart2() {
        runPart2(currentPuzzleSolution());
    }

    /**
     * Run part 2 of the given puzzle year and day
     */
    public static void runPart2(int year, int day) {
        runPart2(new PuzzleDate(year, day));
    }

    /**
     * Run part 2 of the given puzzle date
     */
    public static void runPart2(PuzzleDate date) {
        runPart2(getPuzzleSolution(date));
    }

    /**
     * Run part 2 of the given puzzle solution
     * @param solution Puzzle solution to invoke
     */
    public static void runPart2(Class<? extends PuzzleSolution> solution) {
        runPart2(construct(solution), getInput(solution));
    }

    private static void runPart2(PuzzleSolution solution, PuzzleInput input) {
        Object answer = solution.part2(input);
        logAnswer(answer, solution, 2);
    }

    private static SolutionRegistry.Entry getEntry(Class<? extends PuzzleSolution> solution) {
        var entry = SolutionRegistry.get().getEntry(solution);
        if (entry == null) {
            throw new RuntimeException("Class '%s' could not be found in the registry".formatted(solution.getCanonicalName()));
        }
        return entry;
    }

    private static PuzzleInput getInput(Class<? extends PuzzleSolution> solution) {
        var entry = getEntry(solution);
        return PuzzleInputFactory.of(entry.getDate());
    }

    private static PuzzleSolution construct(Class<? extends PuzzleSolution> solution) {
        try {
            Constructor<? extends PuzzleSolution> defaultConstructor = solution.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Default constructor for class '" + solution.getCanonicalName() + "' not found.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to construct class '" + solution.getCanonicalName() + "'", e);
        }
    }

    private static Class<? extends PuzzleSolution> currentPuzzleSolution() {
         return getPuzzleSolution(PuzzleDate.now());
    }

    private static Class<? extends PuzzleSolution> getPuzzleSolution(PuzzleDate date) {
        var solutions = SolutionRegistry.get().getSolutions(date);
        if (solutions.isEmpty()) {
            throw new RuntimeException("No PuzzleSolution implementation found for " + date);
        }
        var chosen = solutions.get(0);
        if (solutions.size() > 1) {
            System.err.printf("[WARN] - %d solutions found for %s, using %s.%n", solutions.size(), date, chosen.getSolutionClass().getSimpleName());
        }
        return chosen.getSolutionClass();
    }

    private static void logAnswer(Object answer, PuzzleSolution solutionImpl, int part) {
        var date = getEntry(solutionImpl.getClass()).getDate();
        System.out.printf("%d-%02d part %d: %s%n", date.year(), date.day(), part, answer);
    }
}
