package org.togetherjava.aoc.core.math;

import org.togetherjava.aoc.core.math.matrix.MatrixPosition;

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

    public static int taxicab(MatrixPosition a, MatrixPosition b) {
        int dr = Math.abs(a.row() - b.row());
        int dc = Math.abs(a.col() - b.col());
        return dr + dc;
    }

    public static double chebyshev(Point a, Point b) {
        double dx = Math.abs(a.x() - b.x());
        double dy = Math.abs(a.y() - b.y());
        return Math.max(dx, dy);
    }
}
