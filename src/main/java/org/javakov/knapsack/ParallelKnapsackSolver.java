package org.javakov.knapsack;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Параллельный решатель 0/1-задачи о рюкзаке на основе branch-and-bound + ForkJoinPool.
 */
public record ParallelKnapsackSolver(
        int parallelism
) {

    public ParallelKnapsackSolver {
        if (parallelism < 1) {
            throw new IllegalArgumentException("Число потоков должно быть >= 1");
        }
    }

    public KnapsackSolution solve(KnapsackProblem problem) {
        final List<KnapsackItem> sortedItems = problem.items().stream()
                .sorted(Comparator.comparingDouble(KnapsackItem::density).reversed())
                .toList();
        final int n = sortedItems.size();
        final int[] weights = new int[n];
        final int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            final KnapsackItem item = sortedItems.get(i);
            weights[i] = item.weight();
            values[i] = item.value();
        }

        final AtomicInteger bestValue = new AtomicInteger(0);
        final AtomicReference<boolean[]> bestSelection = new AtomicReference<>(new boolean[n]);

        final Instant started = Instant.now();
        final long explored;
        try (ForkJoinPool pool = new ForkJoinPool(parallelism)) {
            explored = pool.invoke(new KnapsackTask(
                    problem.capacity(),
                    weights,
                    values,
                    0,
                    0,
                    0,
                    new boolean[n],
                    bestValue,
                    bestSelection
            ));
        }
        final Duration duration = Duration.between(started, Instant.now());
        final List<KnapsackItem> chosen = extractItems(sortedItems, bestSelection.get());
        return new KnapsackSolution(bestValue.get(), chosen, explored, duration);
    }

    private static List<KnapsackItem> extractItems(List<KnapsackItem> items, boolean[] marks) {
        final List<KnapsackItem> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (marks[i]) {
                result.add(items.get(i));
            }
        }
        return result;
    }

    private static final class KnapsackTask extends RecursiveTask<Long> {

        private final int capacity;
        private final int[] weights;
        private final int[] values;
        private final int index;
        private final int currentWeight;
        private final int currentValue;
        private final boolean[] selection;
        private final AtomicInteger bestValue;
        private final AtomicReference<boolean[]> bestSelection;

        private KnapsackTask(int capacity,
                             int[] weights,
                             int[] values,
                             int index,
                             int currentWeight,
                             int currentValue,
                             boolean[] selection,
                             AtomicInteger bestValue,
                             AtomicReference<boolean[]> bestSelection) {
            this.capacity = capacity;
            this.weights = weights;
            this.values = values;
            this.index = index;
            this.currentWeight = currentWeight;
            this.currentValue = currentValue;
            this.selection = selection;
            this.bestValue = bestValue;
            this.bestSelection = bestSelection;
        }

        @Override
        protected Long compute() {
            long nodes = 1;
            if (currentWeight > capacity || index == weights.length) {
                updateBest();
                return nodes;
            }
            final double bound = bound(index, currentWeight, currentValue);
            if (bound <= bestValue.get()) {
                return nodes;
            }

            // Ветка с включением текущего предмета
            long includeNodes;
            if (currentWeight + weights[index] <= capacity) {
                final boolean[] includeSelection = selection.clone();
                includeSelection[index] = true;
                final KnapsackTask includeTask = new KnapsackTask(
                        capacity,
                        weights,
                        values,
                        index + 1,
                        currentWeight + weights[index],
                        currentValue + values[index],
                        includeSelection,
                        bestValue,
                        bestSelection
                );
                includeTask.fork();

                // Ветка без предмета обрабатывается в текущем потоке
                final boolean[] excludeSelection = selection.clone();
                final KnapsackTask excludeTask = new KnapsackTask(
                        capacity,
                        weights,
                        values,
                        index + 1,
                        currentWeight,
                        currentValue,
                        excludeSelection,
                        bestValue,
                        bestSelection
                );
                final long excludeNodes = excludeTask.compute();
                includeNodes = includeTask.join();
                nodes += includeNodes + excludeNodes;
            } else {
                // Предмет не помещается, идём только по ветке исключения
                final boolean[] excludeSelection = selection.clone();
                final KnapsackTask excludeTask = new KnapsackTask(
                        capacity,
                        weights,
                        values,
                        index + 1,
                        currentWeight,
                        currentValue,
                        excludeSelection,
                        bestValue,
                        bestSelection
                );
                nodes += excludeTask.compute();
            }
            return nodes;
        }

        private double bound(int startIndex, int weight, int value) {
            int remaining = capacity - weight;
            double result = value;
            for (int i = startIndex; i < weights.length && remaining > 0; i++) {
                if (weights[i] <= remaining) {
                    remaining -= weights[i];
                    result += values[i];
                } else {
                    result += values[i] * (remaining / (double) weights[i]);
                    break;
                }
            }
            return result;
        }

        private void updateBest() {
            int current = currentValue;
            while (true) {
                final int existing = bestValue.get();
                if (current <= existing) {
                    return;
                }
                if (bestValue.compareAndSet(existing, current)) {
                    bestSelection.set(selection);
                    return;
                }
            }
        }
    }
}

