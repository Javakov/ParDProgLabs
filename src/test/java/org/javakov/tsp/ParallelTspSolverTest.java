package org.javakov.tsp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ParallelTspSolverTest {

    @Test
    void findsReasonableTourForTriangle() {
        final List<City> cities = List.of(
                new City("A", 0, 0),
                new City("B", 1, 0),
                new City("C", 0, 1)
        );
        final TspInstance instance = new TspInstance(cities);
        final ParallelTspSolver solver = new ParallelTspSolver(50, 10, 2);

        final TspSolution solution = solver.solve(instance);

        assertTrue(solution.tour().distance() <= 3.5);
    }
}

