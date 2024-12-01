package org.togetherjava.aoc.util;

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

    public void move(Direction direction, int magnitude) {
        x += direction.getXDirection() * magnitude;
        y += direction.getYDirection() * magnitude;
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