package org.togetherjava.aoc.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Tuple<A, B> {

    private final A a;
    private final B b;

    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(a, tuple.a) && Objects.equals(b, tuple.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}