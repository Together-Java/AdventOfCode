package org.togetherjava.aoc.util.command.arguments.validators;

import org.togetherjava.aoc.util.command.arguments.ArgumentValidator;

public class StringValidator implements ArgumentValidator<String> {

    @Override
    public boolean validateType(String arg) {
        return true;
    }

    @Override
    public String get(String arg) {
        return arg;
    }
}
