package org.togetherjava.aoc.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  Wrapper for x/y coordinates
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Coordinate {

    private int x;
    private int y;

    public Coordinate move(Direction direction, int magnitude) {
        int x = this.x + (direction.getXDirection() * magnitude);
        int y = this.y + (direction.getYDirection() * magnitude);
        return new Coordinate(x, y);
    }

    public void move(Direction direction) {
        move(direction, 1);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Coordinate offset) {
        this.x += offset.getX();
        this.y += offset.getY();
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }
}