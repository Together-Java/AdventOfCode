package org.togetherjava.aoc.core.utils;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Regex {

    public static final Pattern WHITESPACES = Pattern.compile("\\s+");
    public static final Pattern INTEGERS = Pattern.compile("-?\\d+");
    public static final Pattern WORDS = Pattern.compile("\\w+");
    public static final Pattern EOL = Pattern.compile("\\R");
    public static final Pattern BLANK_LINES = Pattern.compile("\\R{2,}");

    /**
     * Find all the matches in the given input string and collect to an immutable list.
     * @param input input to search
     * @param regex pattern to match on
     * @return immutable list of matches
     */
    public static List<MatchResult> allMatches(Pattern regex, String input) {
        return regex.matcher(input).results().toList();
    }

    /**
     * Find all the matches in the given input string and collect to an immutable list.
     * @param input input to search
     * @param regex string pattern to match on
     * @return immutable list of matches
     */
    public static List<MatchResult> allMatches(String regex, String input) {
        return allMatches(Pattern.compile(regex), input);
    }

    /**
     * Extract a list of all the signed integers in the input string.
     * @param s input text
     * @return immutable List of 64-bit integer matches
     */
    public static List<Long> parseLongs(String s) {
        return INTEGERS.matcher(s)
                .results()
                .map(MatchResult::group)
                .map(Long::parseLong)
                .toList();
    }

    /**
     * Extract a list of all the signed integers in the input string.
     * @param s input text
     * @return immutable List of 32-bit integer matches
     */
    public static List<Integer> parseInts(String s) {
        return INTEGERS.matcher(s)
                .results()
                .map(MatchResult::group)
                .map(Integer::parseInt)
                .toList();
    }

    /**
     * Extract a list of all the contiguous words (\w+)
     * @param s input text
     * @return immutable List of string matches
     */
    public static List<String> parseStrings(String s) {
        return WORDS.matcher(s)
                .results()
                .map(MatchResult::group)
                .toList();
    }

    /**
     * Split the given input on the given regex delimiter.
     * @param regex Pattern to split on
     * @param input input to split
     * @return immutable List of string results after splitting
     */
    public static List<String> split(String regex, String input) {
        return List.of(input.split(regex));
    }

    /**
     * Split the given input on the given regex delimiter.
     * @param regex Pattern to split on
     * @param input input to split
     * @return immutable List of string results after splitting
     */
    public static List<String> split(Pattern regex, String input) {
        return List.of(regex.split(input));
    }

    /**
     * Split the given input on whitespaces. Multiple whitespaces are treated as one.
     * @param input input string
     * @return immutable List of strings
     */
    public static List<String> split(String input) {
        return split(WHITESPACES, input);
    }

}
