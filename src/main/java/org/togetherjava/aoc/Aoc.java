package org.togetherjava.aoc;

import org.togetherjava.aoc.internal.Runner;


public class Aoc {

    public static void main(String[] args) {
        String sessionCookie = System.getenv("AOC_SESSION_COOKIE");
        if (sessionCookie == null) {
            throw new IllegalStateException("You must set your AOC_SESSION_COOKIE environment variable in your gradle settings.");
        }

        Runner.run();
    }
}
