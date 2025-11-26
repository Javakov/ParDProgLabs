package org.javakov.knapsack;

import java.util.List;

/**
 * Описание задачи о рюкзаке 0/1.
 */
public record KnapsackProblem(int capacity, List<KnapsackItem> items) {

    public KnapsackProblem {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Вместимость должна быть > 0");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Необходимо передать хотя бы один предмет");
        }
    }

    public int size() {
        return items.size();
    }
}

