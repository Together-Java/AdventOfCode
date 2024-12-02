package org.togetherjava.aoc.util.command.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.togetherjava.aoc.util.command.arguments.validators.BooleanValidator;
import org.togetherjava.aoc.util.command.arguments.validators.IntegerValidator;
import org.togetherjava.aoc.util.command.arguments.validators.StringValidator;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ArgumentParser {

    private final String[] arguments;

    @Getter
    private static final Map<Class<?>, ArgumentValidator<?>> validators = new HashMap<>();

    static {
        validators.put(String.class, new StringValidator());
        validators.put(Boolean.class, new BooleanValidator());
        validators.put(Integer.class, new IntegerValidator());
        validators.put(Long.class, new IntegerValidator());
        validators.put(Float.class, new IntegerValidator());
        validators.put(Double.class, new IntegerValidator());
    }

    public boolean ensureTypes(Class<?>... classes) {
        if(arguments.length != classes.length) {
            return false;
        }
        for(int i = 0; i < arguments.length; i++) {
            String arg = arguments[i];
            Class<?> clazz = classes[i];
            if(arg == null || clazz == null) {
                return false;
            }
            ArgumentValidator<?> mapper = validators.get(clazz);
            if(mapper == null) {
                return false;
            }
            return mapper.validateType(arg);
        }
        return false;
    }

    public <T> T asType(int index, Class<T> type) {
        ArgumentValidator<T> validator = (ArgumentValidator<T>) validators.get(type);
        if(validator == null) {
            throw new NullPointerException("There is no validator for " + type.getName());
        }
        String arg = arguments[index];
        return validator.get(arg);
    }

    public String getString(int index) {
        return arguments[index];
    }

    public int getInt(int index) {
       return asType(index, Integer.class);
    }

    public long getLong(int index) {
        return asType(index, Long.class);
    }

    public boolean getBoolean(int index) {
        return asType(index, Boolean.class);
    }

    public float getFloat(int index) {
        return asType(index, Float.class);
    }

    public double getDouble(int index) {
        return asType(index, Double.class);
    }

    public int length() {
        return arguments.length;
    }

    @Override
    public String toString() {
        return String.join(" ", arguments);
    }
}
