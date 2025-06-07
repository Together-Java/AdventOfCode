package org.togetherjava.aoc.core.math;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    NORTH(0, 1, 0),
    NORTH_EAST(1, 1, 45),
    EAST(1, 0, 90),
    SOUTH_EAST(1, -1, 135),
    SOUTH(0, -1, 180),
    SOUTH_WEST(-1, -1, 225),
    WEST(-1, 0, 270),
    NORTH_WEST(-1, 1, 315);

    private final int x;
    private final int y;
    private final int angleInDegrees;

    Direction(int x, int y, int angleInDegrees) {
        this.x = x;
        this.y = y;
        this.angleInDegrees = angleInDegrees;
    }

    private static final List<Direction> DIRECTIONS = Stream.of(values())
            .sorted(Comparator.comparingInt(Direction::getAngleInDegrees)).toList();
    private static final List<Direction> CARDINAL = List.of(NORTH, EAST, SOUTH, WEST);
    private static final List<Direction> ORDINAL = List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    private static final Map<Integer, Direction> ANGLE_MAP = DIRECTIONS.stream()
            .collect(Collectors.toMap(Direction::getAngleInDegrees, Function.identity()));

    public static Direction getByAngle(int angle) {
        return ANGLE_MAP.get(angle % 360);
    }

    public static Direction getByPosition(int x, int y) {
        for (Direction direction : DIRECTIONS) {
            if (direction.x == x && direction.y == y) {
                return direction;
            }
        }
        return null;
    }

    public static Direction ofString(String direction) {
        return switch(direction.toLowerCase()) {
            case "east", "right", ">", "e", "r" -> Direction.EAST;
            case "west", "left", "<", "w", "l" -> Direction.WEST;
            case "north", "up", "^", "n", "u" -> Direction.NORTH;
            case "south", "down", "v", "s", "d" -> Direction.SOUTH;
            case "ne" -> Direction.NORTH_EAST;
            case "nw" -> Direction.NORTH_WEST;
            case "se" -> Direction.SOUTH_EAST;
            case "sw" -> Direction.SOUTH_WEST;
	        default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    public static List<Direction> getAll() {
        return DIRECTIONS;
    }

    public static List<Direction> getCardinal() {
        return CARDINAL;
    }

    public static List<Direction> getOrdinal() {
        return ORDINAL;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngleInDegrees() {
        return angleInDegrees;
    }

    public Direction rotate(int angleInDegrees) {
        return Direction.getByAngle(this.angleInDegrees + angleInDegrees);
    }

    public Direction rotateRight() {
        return rotate(90);
    }

    public Direction rotateLeft() {
        return rotate(270);
    }

    public Direction opposite() {
        return rotate(180);
    }

    public static Direction ofASCII(char c) {
        return switch(c) {
            case '^' -> NORTH;
            case '>' -> EAST;
            case '<' -> WEST;
            case 'v', 'V' -> SOUTH;
            default -> throw new IllegalArgumentException();
        };
    }


}
