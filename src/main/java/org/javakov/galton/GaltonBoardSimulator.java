package org.javakov.galton;

/**
 * Симулятор доски Гальтона: шарик проходит через {@code levels} рядов,
 * на каждом шаге случайно уходит вправо или остаётся в текущей колонке.
 */
public final class GaltonBoardSimulator {

    public GaltonBoardResult simulate(GaltonBoardConfig config) {
        final int slots = config.levels() + 1;
        final int[] histogram = new int[slots];

        for (int ball = 0; ball < config.balls(); ball++) {
            int position = 0;
            for (int level = 0; level < config.levels(); level++) {
                if (config.random().nextBoolean()) {
                    position++;
                }
            }
            histogram[position]++;
        }
        return new GaltonBoardResult(config.levels(), config.balls(), histogram);
    }
}

