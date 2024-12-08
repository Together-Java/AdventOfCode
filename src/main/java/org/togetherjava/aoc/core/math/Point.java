package org.togetherjava.aoc.core.math;

public record Point(double x, double y) {

    public static final Point ORIGIN = new Point(0, 0);

    public Point move(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    public static Point of(double x, double y) {
        return new Point(x, y);
    }

}
