package org.togetherjava.aoc.util;

public class ExceptionUtils {

    public static boolean doesThrow(Runnable runnable) {
        return doesThrow(runnable, Throwable.class);
    }

    public static boolean doesThrow(Runnable runnable, Class<? extends Throwable> exception) {
        try {
            runnable.run();
            return false;
        } catch(Throwable t) {
            return t.getClass().isAssignableFrom(exception);
        }
    }
}
