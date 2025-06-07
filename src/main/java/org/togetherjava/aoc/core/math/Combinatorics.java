package org.togetherjava.aoc.core.math;


import java.util.ArrayList;
import java.util.List;

public class Combinatorics {

    /**
     * Arrange n from k with repetition allowed.
     * <br>
     * Permutations with repetitions allowed.
     *
     * @param n permutation resulting size (arrange n)
     * @param kValues from k values
     * @return all permutations, with repetitions allowed, of length n
     * @param <T> type of elements to permute
     */
    public static <T> List<List<T>> arrangeWithRepetition(int n, List<T> kValues) {
        List<List<T>> permutations = new ArrayList<>();
        arrangeWithRepetitionRecursive(kValues, new ArrayList<>(), n, permutations);
        return permutations;
    }

    private static <T> void arrangeWithRepetitionRecursive(List<T> values, List<T> current, int remaining, List<List<T>> permutations) {
        if (remaining == 0) {
            permutations.add(new ArrayList<>(current));
            return;
        }

        for (T value : values) {
            current.add(value);
            arrangeWithRepetitionRecursive(values, current, remaining - 1, permutations);
            current.removeLast();
        }
    }

    public static <T> List<List<T>> arrange(List<T> values, int length) {
        List<List<T>> permutations = new ArrayList<>();
        arrangeHelper(values, new ArrayList<>(), length, permutations);
        return permutations;
    }

    private static <T> void arrangeHelper(List<T> values, List<T> current, int remaining, List<List<T>> permutations) {
        if (remaining == 0) {
            permutations.add(new ArrayList<>(current));
            return;
        }

        for (T value : values) {
            if (current.contains(value)) {
                continue;
            }
            current.add(value);
            arrangeHelper(values, current, remaining - 1, permutations);
            current.removeLast();
        }
    }

    public static <T> List<List<T>> choose(List<T> values, int length) {
        List<List<T>> combinations = new ArrayList<>();
        chooseHelper(values, new ArrayList<>(), 0, length, combinations);
        return combinations;
    }

    private static <T> void chooseHelper(List<T> values, List<T> current, int start, int length, List<List<T>> combinations) {
        if (current.size() == length) {
            combinations.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < values.size(); i++) {
            current.add(values.get(i));
            chooseHelper(values, current, i + 1, length, combinations);
            current.remove(current.size() - 1);
        }
    }

    public static List<String> getRotations(String s) {
        List<String> rotations = new ArrayList<>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            String rotated = s.substring(i) + s.substring(0, i);
            rotations.add(rotated);
        }
        return rotations;
    }

}
