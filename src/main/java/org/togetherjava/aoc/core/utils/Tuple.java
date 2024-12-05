package org.togetherjava.aoc.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Tuple<A, B> {

    private final A a;
    private final B b;

    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }
}