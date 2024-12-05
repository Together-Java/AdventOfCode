package org.togetherjava.aoc.core.utils;

import org.togetherjava.aoc.solutions.Day01;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Regex {

	public static List<MatchResult> findMatches(String regex, String input) {
		return Pattern.compile(regex).matcher(input).results().toList();
	}
}
