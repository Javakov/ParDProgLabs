package org.javakov.galton;

import java.util.Random;

/**
 * Параметры симуляции доски Гальтона.
 *
 * @param levels количество уровней (рядов штифтов), итоговых слотов будет {@code levels + 1}
 * @param balls  число шариков, которые нужно уронить
 * @param random источник случайности для выбора направления на каждом уровне
 */
public record GaltonBoardConfig(int levels, int balls, Random random) {

    public GaltonBoardConfig {
        if (levels < 1) {
            throw new IllegalArgumentException("Количество уровней должно быть >= 1");
        }
        if (balls < 1) {
            throw new IllegalArgumentException("Количество шариков должно быть >= 1");
        }
        random = random == null ? new Random() : random;
    }

    public static GaltonBoardConfig withDefaults(int levels, int balls) {
        return new GaltonBoardConfig(levels, balls, new Random());
    }
}

