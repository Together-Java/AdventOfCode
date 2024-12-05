package org.togetherjava.aoc.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Triple<A, B, C> {

	private final A a;
	private final B b;
	private final C c;

}
