package org.togetherjava.aoc.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * test
 */
@Getter
@RequiredArgsConstructor
public enum Direction {

    NORTH(0, -1, 0), NORTH_EAST(1, -1, 45),
    EAST(1, 0, 90), SOUTH_EAST(1, 1, 135),
    SOUTH(0, 1, 180), SOUTH_WEST(-1, 1, 225),
    WEST(-1, 0, 270), NORTH_WEST(-1, -1, 315);

    public static final Direction[] CARDINAL = {NORTH, EAST, SOUTH, WEST};
    public static final Direction[] ORDINAL = {NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST};

    private final int xDirection;
    private final int yDirection;
    private final int degrees;

    public Direction turn(int degrees) {
        return getDirection(this.degrees + degrees);
    }

    public Direction opposite() {
        return turn(180);
    }

    public Direction right() {
        return turn(90);
    }

    public Direction halfRight() {return turn(45);}

    public Direction left() {
        return turn(-90);
    }

    public Direction halfLeft() {return turn(-45);}

    public static Direction getDirection(int degrees) {
        if (degrees < 0) {
            degrees = 360 + degrees;
        }
        degrees %= 360;
        for (Direction direction : values()) {
            if (direction.getDegrees() == degrees) {
                return direction;
            }
        }
        throw new IllegalArgumentException("That direction doesn't exist");
    }
}