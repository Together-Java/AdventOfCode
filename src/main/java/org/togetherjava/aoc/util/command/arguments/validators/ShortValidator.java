package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.ExceptionUtils;
import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class ShortValidator implements ArgumentValidator<Short> {

	@Override
	public boolean validateType(String arg) {
		return !ExceptionUtils.doesThrow((() -> Short.parseShort(arg)), NumberFormatException.class);
	}

	@Override
	public Short get(String arg) {
		return Short.parseShort(arg);
	}
}
