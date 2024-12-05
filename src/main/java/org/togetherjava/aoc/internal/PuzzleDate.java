package org.togetherjava.aoc.internal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record PuzzleDate(int year, int day) {

    public static PuzzleDate now() {
        ZonedDateTime easternTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        LocalDate easternDate = easternTime.toLocalDate();
        int year = easternDate.getYear();
        int day = easternDate.getDayOfMonth();
        return new PuzzleDate(year, day);
    }
}
