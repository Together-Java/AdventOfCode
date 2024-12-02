package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class BooleanValidator implements ArgumentValidator<Boolean> {

    @Override
    public boolean validateType(String arg) {
        return arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false");
    }

    @Override
    public Boolean get(String arg) {
        if(!validateType(arg)) {
            throw new IllegalArgumentException("Argument is not a valid boolean");
        }
        return Boolean.parseBoolean(arg);
    }
}
