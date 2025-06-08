package org.togetherjava.aoc.core.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * A counter of keys (type T) to values.
 * <br>
 * For example,
 * <pre>
 * Counter&lt;String&gt; counter = new Counter&lt;&gt;();
 * counter.increment("Hello");
 * counter.getCount("Hello"); // 1
 * counter.getCount("World"); // 0
 * </pre>
 * @param <T> Type T to use as a key in the counter map
 */
public class Counter<T> {

    private final Map<T, LongAdder> counter = new ConcurrentHashMap<>();

    public Counter() {}

    public Counter(Collection<? extends T> collection) {
        addAll(collection);
    }

    /**
     * Get the underlying Map implementation
     */
    public Map<T, LongAdder> getMap() {
        return counter;
    }

    private LongAdder getAdder(T key) {
        return counter.computeIfAbsent(key, ignored -> new LongAdder());
    }

    public void addAll(Collection<? extends T> collection) {
        collection.forEach(this::increment);
    }

    public void subtractAll(Collection<? extends T> collection) {
        collection.forEach(this::decrement);
    }

    /**
     * Increment the count by 1
     */
    public void increment(T key) {
        getAdder(key).increment();
    }

    /**
     * Add an amount to the counter
     */
    public void add(T key, long amount) {
        getAdder(key).add(amount);
    }

    /**
     * Decrement the count by 1
     */
    public void decrement(T key) {
        getAdder(key).decrement();
    }

    /**
     * Subtract an amount from the counter
     */
    public void subtract(T key, long amount) {
        getAdder(key).add(-amount);
    }

    /**
     * Reset the counter to 0 for the given key
     * @param key
     */
    public void reset(T key) {
        getAdder(key).reset();
    }

    /**
     * Get the count for the given key
     */
    public long getCount(T key) {
        return getAdder(key).longValue();
    }

    /**
     * Get the list of elements from least to greatest count
     */
    public List<T> getListAscending() {
        return counter.entrySet()
                .stream()
                .sorted(Comparator.comparingLong(counterEntry -> counterEntry.getValue().longValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Get the list of elements from greatest to lowest count
     */
    public List<T> getListDecending() {
        return counter.entrySet()
                .stream()
                .sorted(Comparator.comparingLong(counterEntry -> -counterEntry.getValue().longValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Get the total count across all keys
     */
    public long getTotalCount() {
        long result = 0;
        for (var value : counter.values()) {
            result += value.longValue();
        }
        return result;
    }

    /**
     * Get the entry with the lowest count
     */
    public Tuple<T, Long> getMin() {
        var entry = counter.entrySet()
                .stream()
                .min(Comparator.comparingLong(o -> o.getValue().longValue()))
                .orElseThrow(NoSuchElementException::new);
        return Tuple.of(entry.getKey(), entry.getValue().longValue());
    }

    /**
     * Get the entry with the highest count
     */
    public Tuple<T, Long> getMax() {
        var entry = counter.entrySet()
                .stream()
                .max(Comparator.comparingLong(o -> o.getValue().longValue()))
                .orElseThrow(NoSuchElementException::new);
        return Tuple.of(entry.getKey(), entry.getValue().longValue());
    }

    @Override
    public String toString() {
        return counter.entrySet()
                .stream()
                .sorted(Comparator.comparingLong(counterEntry -> counterEntry.getValue().longValue()))
                .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter<?> counter1 = (Counter<?>) o;
        return Objects.equals(counter, counter1.counter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counter);
    }
}