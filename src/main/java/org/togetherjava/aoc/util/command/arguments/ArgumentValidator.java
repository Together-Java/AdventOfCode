package org.togetherjava.aoc.util.command.arguments;

public interface ArgumentValidator<T> {
    boolean validateType(String arg);
    T get(String arg);
}
