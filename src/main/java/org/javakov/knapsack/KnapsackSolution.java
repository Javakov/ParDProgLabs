package org.javakov.knapsack;

import java.time.Duration;
import java.util.List;

/**
 * Итоги решения задачи о рюкзаке.
 *
 * @param bestValue    максимальная ценность
 * @param chosenItems  список выбранных предметов
 * @param explored     количество рассмотренных узлов (для оценки сложности)
 * @param duration     время вычисления
 */
public record KnapsackSolution(
        int bestValue,
        List<KnapsackItem> chosenItems,
        long explored,
        Duration duration
) {
}

