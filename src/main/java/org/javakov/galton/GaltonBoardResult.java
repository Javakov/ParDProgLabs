package org.javakov.galton;

import java.util.Arrays;

/**
 * Итоги симуляции доски Гальтона: распределение шариков по слотам.
 */
public record GaltonBoardResult(
        int levels,
        int balls,
        int[] slots
) {

    public GaltonBoardResult(int levels, int balls, int[] slots) {
        this.levels = levels;
        this.balls = balls;
        this.slots = Arrays.copyOf(slots, slots.length);
    }

    /**
     * @return копия массива распределения, чтобы внешние клиенты не модифицировали данные
     */
    @Override
    public int[] slots() {
        return Arrays.copyOf(slots, slots.length);
    }

    public int maxCount() {
        return Arrays.stream(slots).max().orElse(0);
    }
}

