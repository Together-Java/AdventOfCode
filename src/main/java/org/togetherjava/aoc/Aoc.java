package org.togetherjava.aoc;

import org.togetherjava.aoc.api.AbstractDay;
import org.togetherjava.aoc.solutions.Day01;

public class Aoc {

    public static void main(String[] args) {
        /*
         * TODO: cache user input in file instead of scraping website every time
         *  ond or make it optional
         */
        String sessionCookie = System.getenv("session_cookie");
        if(sessionCookie == null) {
            throw new IllegalStateException("You must set your session_cookie environment variable in your gradle settings.");
        }
        AbstractDay day = new Day01();
        System.out.println("#".repeat(50));
        System.out.println("Part 1 solution: %s".formatted(day.part1Solution()));
        System.out.println("Part 2 solution: %s".formatted(day.part2Solution()));
        System.out.println("#".repeat(50));
    }
}
