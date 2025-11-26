package org.javakov.knapsack;

/**
 * Предмет с весом и ценностью для задачи о рюкзаке.
 */
public record KnapsackItem(String name, int weight, int value) {

    public KnapsackItem {
        if (weight <= 0) {
            throw new IllegalArgumentException("Вес должен быть > 0");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Ценность должна быть > 0");
        }
        name = name == null || name.isBlank() ? "item" : name;
    }

    public double density() {
        return (double) value / weight;
    }
}

