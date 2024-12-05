package org.togetherjava.aoc.core.utils;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class FrequencyMap<T> {

    @Getter
    private final Map<T, AtomicInteger> backingMap = new LinkedHashMap<>();

    public int increment(T key) {
        AtomicInteger count = backingMap.computeIfAbsent(key, k -> new AtomicInteger());
        return count.incrementAndGet();
    }

    public int getCount(T key) {
        return backingMap.get(key).get();
    }

    public int getTotalCount() {
        return backingMap.entrySet().stream().mapToInt(e -> e.getValue().get()).sum();
    }

    public Tuple<T, Integer> getMin() {
        var entry = backingMap.entrySet().stream().min(Comparator.comparingInt(o -> o.getValue().get())).orElseThrow(NoSuchElementException::new);
        return Tuple.of(entry.getKey(), entry.getValue().get());
    }

    public Tuple<T, Integer> getMax() {
        var entry = backingMap.entrySet().stream().max(Comparator.comparingInt(o -> o.getValue().get())).orElseThrow(NoSuchElementException::new);
        return Tuple.of(entry.getKey(), entry.getValue().get());
    }

    public void forEach(BiConsumer<T, Integer> action) {
        backingMap.entrySet().iterator().forEachRemaining(e -> action.accept(e.getKey(), e.getValue().get()));
    }
}