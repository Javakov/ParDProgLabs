package org.javakov.tsp;

public record City(String name, double x, double y) {

    public double distanceTo(City other) {
        final double dx = x - other.x;
        final double dy = y - other.y;
        return Math.hypot(dx, dy);
    }
}

