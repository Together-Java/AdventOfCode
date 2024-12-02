package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.ExceptionUtils;
import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class IntegerValidator implements ArgumentValidator<Integer> {

    @Override
    public boolean validateType(String arg) {
        return !ExceptionUtils.doesThrow((() -> Integer.parseInt(arg)), NumberFormatException.class);
    }

    @Override
    public Integer get(String arg) {
        if(!validateType(arg)) {
            throw new IllegalArgumentException("Argument is not a valid integer");
        }
        return Integer.parseInt(arg);
    }
}
