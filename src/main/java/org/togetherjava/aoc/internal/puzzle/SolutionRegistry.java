package org.togetherjava.aoc.internal.puzzle;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.togetherjava.aoc.core.utils.StringUtils;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class SolutionRegistry {

    public static class Entry {
        private final Class<? extends PuzzleSolution> solution;
        private final PuzzleDate date;

        public Entry(Class<? extends PuzzleSolution> solution, PuzzleDate date) {
            this.solution = solution;
            this.date = date;
        }

        public Class<? extends PuzzleSolution> getSolutionClass() {
            return solution;
        }

        public PuzzleDate getDate() {
            return date;
        }
    }

    private final Map<Integer, Map<Integer, List<Entry>>> calendarYears = new HashMap<>();
    private final Map<Class<? extends PuzzleSolution>, Entry> solutionInfoMap = new HashMap<>();
    private static SolutionRegistry INSTANCE = null;

    public static SolutionRegistry get() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }

    public Map<Integer, List<Entry>> getSolutionCalendar(int year) {
        return calendarYears.computeIfAbsent(year, ignored -> new HashMap<>());
    }

    public List<Entry> getSolutions(int year, int day) {
        var yearData = getSolutionCalendar(year);
        return yearData.computeIfAbsent(day, ignored -> new ArrayList<>());
    }

    public List<Entry> getSolutions(PuzzleDate puzzleDate) {
        return getSolutions(puzzleDate.year(), puzzleDate.day());
    }

    public void register(int year, int day, Class<? extends PuzzleSolution> solution) {
        register(new PuzzleDate(year, day), solution);
    }

    public void register(PuzzleDate date, Class<? extends PuzzleSolution> solution) {
        List<Entry> solutions = getSolutions(date);
        var info = new Entry(solution, date);
        solutions.add(info);
        solutionInfoMap.put(solution, info);
    }

    public Entry getEntry(Class<? extends PuzzleSolution> solutionClass) {
        return solutionInfoMap.get(solutionClass);
    }

    private static void init() {
        INSTANCE = new SolutionRegistry();

        Reflections reflections = new Reflections(
                new ConfigurationBuilder().forPackages("") // Scans all packages
                        .addScanners(Scanners.SubTypes, Scanners.TypesAnnotated));

        // Find all packages annotated with @AdventOfCodeCalendar to extract the year and solutions
        Set<Class<?>> packageInfoClasses = reflections.getTypesAnnotatedWith(AdventYear.class);
        for (Class<?> packageInfoClass : packageInfoClasses) {
            Package packageInfoPackage = packageInfoClass.getPackage();
            String packageName = packageInfoPackage.getName();

            AdventYear packageInfoAnnotation = packageInfoClass.getAnnotation(AdventYear.class);
            int year = packageInfoAnnotation.year();

            Reflections packageReflections = new Reflections(packageName);
            var puzzleSolutions = packageReflections.getSubTypesOf(PuzzleSolution.class);
            for (var solution : puzzleSolutions) {
                Optional<Integer> solutionDay = extractDay(solution.getSimpleName());
                Optional<Integer> dayOverride = getDayOverride(solution);
                if (dayOverride.isPresent()) {
                    solutionDay = dayOverride;
                }
                if (solutionDay.isEmpty()) {
                    throw new RuntimeException("""
                            Unable to detect a valid day value from solution type '%s'. \
                            Follow standard naming, or use @AdventDay() instead.\
                            """.formatted(solution.getCanonicalName())
                    );
                }
                get().register(year, solutionDay.get(), solution);
            }
        }
    }

    private static Optional<Integer> extractDay(String className) {
        Pattern digits = Pattern.compile("\\d+");
        return digits.matcher(className)
                .results()
                .map(MatchResult::group)
                .map(StringUtils::trimLeadingZeros)
                .filter(x -> 1 <= x && x <= 31)
                .findFirst();
    }

    private static Optional<Integer> getDayOverride(Class<? extends PuzzleSolution> clazz) {
        AdventDay adventDay = clazz.getAnnotation(AdventDay.class);
        if (adventDay == null) {
            return Optional.empty();
        }
        return Optional.of(adventDay.day());
    }
}
