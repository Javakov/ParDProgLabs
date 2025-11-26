package org.javakov.knapsack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelKnapsackSolverTest {

    @Test
    void solvesSmallInstance() {
        final List<KnapsackItem> items = List.of(
                new KnapsackItem("A", 10, 60),
                new KnapsackItem("B", 20, 100),
                new KnapsackItem("C", 30, 120)
        );
        final KnapsackProblem problem = new KnapsackProblem(50, items);
        final ParallelKnapsackSolver solver = new ParallelKnapsackSolver(4);

        final KnapsackSolution solution = solver.solve(problem);

        assertEquals(220, solution.bestValue());
        assertEquals(2, solution.chosenItems().size());
    }
}

