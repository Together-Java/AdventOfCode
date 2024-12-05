package org.togetherjava.aoc.core.utils;

public class ExceptionUtils {

    public static boolean doesThrow(Runnable runnable) {
        return doesThrow(runnable, Throwable.class);
    }

    public static boolean doesThrow(Runnable runnable, Class<? extends Throwable> exception) {
        try {
            runnable.run();
            return false;
        } catch(Throwable t) {
            //fixme: this might be the other way around lol
            return t.getClass().isAssignableFrom(exception);
        }
    }
}
