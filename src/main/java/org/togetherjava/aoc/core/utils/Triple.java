package org.togetherjava.aoc.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Triple<A, B, C> {

	private final A a;
	private final B b;
	private final C c;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
		return Objects.equals(a, triple.a) && Objects.equals(b, triple.b) && Objects.equals(c, triple.c);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c);
	}

	@Override
	public String toString() {
		return "(" + a + ", " + b + ", " + c + ")";
	}
}
