package org.togetherjava.aoc.core.utils;


import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static int trimLeadingZeros(String input) {
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c != '0') {
                return Integer.parseInt(input.substring(i));
            }
        }
        return 0;
    }

    public static int maxWidth(List<?> objects) {
        int maxWidth = 0;
        for (Object o : objects) {
            String s = String.valueOf(o);
            maxWidth = Math.max(maxWidth, s.length());
        }
        return maxWidth;
    }

    public static String rightPad(String s, int totalLength) {
        return rightPad(s, totalLength, ' ');
    }

    public static String leftPad(String s, int totalLength) {
        return leftPad(s, totalLength, ' ');
    }

    public static String rightPad(String s, int totalLength, char padding) {
        return s + (String.valueOf(padding).repeat(totalLength - s.length()));
    }

    public static String leftPad(String s, int totalLength, char padding) {
        return (String.valueOf(padding).repeat(totalLength - s.length())) + s;
    }

    public static List<Character> chars(String input) {
        return input.chars().mapToObj(e -> (char) e).toList();
    }

    public static String joinChars(List<Character> chars) {
        return joinChars(chars, "");
    }

    public static String joinChars(List<Character> chars, String separator) {
        return chars.stream().map(String::valueOf).collect(Collectors.joining(separator));
    }



}
