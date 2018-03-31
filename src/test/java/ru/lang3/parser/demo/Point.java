package ru.lang3.parser.demo;

import java.util.Objects;

/**
 * Created on 31.03.2018.
 */
public class Point {
    final int x;
    final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point dXY(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }
    Point dX(int x) {
        return new Point(this.x + x, this.y );
    }
    Point dY(int y) {
        return new Point(this.x, this.y + y);
    }
    Point setX(int x) {
        return new Point(x, this.y);
    }
    Point setY(int y) {
        return new Point(this.x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}