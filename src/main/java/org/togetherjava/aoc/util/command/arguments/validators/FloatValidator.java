package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.ExceptionUtils;
import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class FloatValidator implements ArgumentValidator<Float> {

    @Override
    public boolean validateType(String arg) {
        return !ExceptionUtils.doesThrow((() -> Float.parseFloat(arg)), NumberFormatException.class);
    }

    @Override
    public Float get(String arg) {
        if(!validateType(arg)) {
            throw new IllegalArgumentException("Argument is not a valid float");
        }
        return Float.parseFloat(arg);
    }
}
