package org.togetherjava.aoc.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AocUtils {
    public static <T> T[][] deepCopy(T[][] source) {
        if (source == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[][] output = (T[][]) new Object[source.length][];
        for (int i = 0; i < source.length; ++i) {
            T[] row = source[i];
            if (row == null) {
                output[i] = null;
            } else {
                output[i] = Arrays.copyOf(row, row.length);
            }
        }
        return output;
    }

    public static <T> List<T> reversed(List<T> list) {
        List<T> result = new ArrayList<>(list.size());
        for (int i = list.size(); i --> 0;) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * [[1, 2, 3, 4], [1, 2, 3, 4]] to [[1, 1], [2, 2], [3, 3], [4, 4]]
     * @param lists
     * @return
     * @param <T>
     */
    public static <T> List<List<T>> zip(List<List<T>> lists) {
        if (lists == null || lists.isEmpty() || lists.get(0).isEmpty()) {
            return new ArrayList<>();
        }
        int maxSize = 0;
        for (List<T> list : lists) {
            maxSize = Math.max(list.size(), maxSize);
        }

        List<List<T>> result = new ArrayList<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            List<T> zippedRow = new ArrayList<>(lists.size());
            for (List<T> list : lists) {
                if (i < list.size()) {
                    zippedRow.add(list.get(i));
                } else {
                    zippedRow.add(null);
                }
            }
            result.add(zippedRow);
        }
        return result;
    }

}
