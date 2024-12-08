package org.togetherjava.aoc.core.math;

public class Distance {
    public static double euclidean(Point a, Point b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double taxicab(Point a, Point b) {
        double dx = Math.abs(a.x() - b.x());
        double dy = Math.abs(a.y() - b.y());
        return dx + dy;
    }

    public static double chebyshev(Point a, Point b) {
        double dx = Math.abs(a.x() - b.x());
        double dy = Math.abs(a.y() - b.y());
        return Math.max(dx, dy);
    }
}
