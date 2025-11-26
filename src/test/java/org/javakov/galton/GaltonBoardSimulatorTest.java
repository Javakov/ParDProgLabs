package org.javakov.galton;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GaltonBoardSimulatorTest {

    @Test
    void simulateProducesDistributionWithExpectedSize() {
        final GaltonBoardConfig config = new GaltonBoardConfig(6, 1_000, new Random(42));
        final GaltonBoardSimulator simulator = new GaltonBoardSimulator();

        final GaltonBoardResult result = simulator.simulate(config);

        assertEquals(7, result.slots().length, "Слотов должно быть levels + 1");
        final int total = sum(result.slots());
        assertEquals(config.balls(), total, "Сумма распределения должна равняться количеству шариков");
    }

    @Test
    void rendererBuildsNonEmptyChart() {
        final GaltonBoardAsciiRenderer renderer = new GaltonBoardAsciiRenderer(10, '#');
        final String chart = renderer.render(new int[]{0, 10, 20, 10, 0});
        assertTrue(chart.contains("#"), "Диаграмма должна содержать заполненные ячейки");
    }

    private static int sum(int[] values) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return total;
    }
}

