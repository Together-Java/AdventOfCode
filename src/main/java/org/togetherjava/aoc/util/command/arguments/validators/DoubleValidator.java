package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.ExceptionUtils;
import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class DoubleValidator implements ArgumentValidator<Double> {

    @Override
    public boolean validateType(String arg) {
        return ExceptionUtils.doesThrow(() -> Double.parseDouble(arg), NumberFormatException.class);
    }

    @Override
    public Double get(String arg) {
        if(!validateType(arg)) {
            throw new IllegalArgumentException("Argument is not a valid double");
        }
        return Double.parseDouble(arg);
    }
}
