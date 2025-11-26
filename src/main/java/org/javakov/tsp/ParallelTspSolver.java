package org.javakov.tsp;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Параллельный решатель TSP: стартуем несколько случайных туров и
 * параллельно улучшаем их локальными перестановками (2-opt).
 */
public record ParallelTspSolver(
        int iterations, int attempts, int threads
) {

    public TspSolution solve(TspInstance instance) {
        final AtomicReference<Tour> best = new AtomicReference<>(null);
        final Instant start = Instant.now();
        final CountDownLatch latch = new CountDownLatch(threads);
        final List<Future<?>> futures = new ArrayList<>();
        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
            for (int t = 0; t < threads; t++) {
                futures.add(executor.submit(() -> {
                    try {
                        final RandomTourBuilder builder = new RandomTourBuilder(instance);
                        for (int i = 0; i < attempts; i++) {
                            Tour tour = builder.randomTour();
                            for (int j = 0; j < iterations; j++) {
                                final Tour improved = TwoOptImprover.improve(tour);
                                if (improved.distance() >= tour.distance()) {
                                    break;
                                }
                                tour = improved;
                            }
                            updateBest(best, tour);
                        }
                    } finally {
                        latch.countDown();
                    }
                }));
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            futures.forEach(future -> future.cancel(true));
        }
        final Duration duration = Duration.between(start, Instant.now());
        return new TspSolution(best.get(), duration);
    }

    private void updateBest(AtomicReference<Tour> best, Tour candidate) {
        while (true) {
            final Tour current = best.get();
            if (current == null || candidate.distance() < current.distance()) {
                if (best.compareAndSet(current, candidate)) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static final class RandomTourBuilder {
        private final TspInstance instance;
        private final Random random = new Random();

        private RandomTourBuilder(TspInstance instance) {
            this.instance = instance;
        }

        private Tour randomTour() {
            final List<City> shuffled = new ArrayList<>(instance.cities());
            Collections.shuffle(shuffled, random);
            return new Tour(shuffled);
        }
    }

    static final class TwoOptImprover {

        private static Tour improve(Tour tour) {
            final List<City> order = new ArrayList<>(tour.order());
            double bestDistance = tour.distance();
            boolean improved = true;
            while (improved) {
                improved = false;
                for (int i = 1; i < order.size() - 1; i++) {
                    for (int j = i + 1; j < order.size(); j++) {
                        final List<City> newOrder = twoOptSwap(order, i, j);
                        final double distance = distanceOf(newOrder);
                        if (distance < bestDistance) {
                            order.clear();
                            order.addAll(newOrder);
                            bestDistance = distance;
                            improved = true;
                        }
                    }
                }
            }
            return new Tour(order);
        }

        private static List<City> twoOptSwap(List<City> order, int i, int j) {
            final List<City> result = new ArrayList<>(order.subList(0, i));
            final List<City> reversed = new ArrayList<>(order.subList(i, j));
            Collections.reverse(reversed);
            result.addAll(reversed);
            result.addAll(order.subList(j, order.size()));
            return result;
        }

        private static double distanceOf(List<City> order) {
            return getDistance(order);
        }

        static double getDistance(List<City> order) {
            double sum = 0.0;
            for (int i = 0; i < order.size(); i++) {
                final City current = order.get(i);
                final City next = order.get((i + 1) % order.size());
                sum += current.distanceTo(next);
            }
            return sum;
        }
    }
}

