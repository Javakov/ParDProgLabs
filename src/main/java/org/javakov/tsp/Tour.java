package org.javakov.tsp;

import java.util.List;

import static org.javakov.tsp.ParallelTspSolver.TwoOptImprover.getDistance;

public final class Tour {

    private final List<City> order;
    private final double distance;

    public Tour(List<City> order) {
        this.order = List.copyOf(order);
        this.distance = computeDistance(order);
    }

    public List<City> order() {
        return order;
    }

    public double distance() {
        return distance;
    }

    private static double computeDistance(List<City> order) {
        return getDistance(order);
    }
}

