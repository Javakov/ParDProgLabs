package org.javakov.galton;

/**
 * Консольная точка входа для демонстрации доски Гальтона.
 */
public final class GaltonBoardApp {

    private static final int DEFAULT_LEVELS = 12;
    private static final int DEFAULT_BALLS = 5_000;
    private static final int CHART_HEIGHT = 20;

    private GaltonBoardApp() {
    }

    public static void main(String[] args) {
        final int levels = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_LEVELS;
        final int balls = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_BALLS;

        final GaltonBoardConfig config = GaltonBoardConfig.withDefaults(levels, balls);
        final GaltonBoardSimulator simulator = new GaltonBoardSimulator();
        final GaltonBoardAsciiRenderer renderer = new GaltonBoardAsciiRenderer(CHART_HEIGHT, '#');

        System.out.printf("Доска Гальтона: уровни=%d, шариков=%d%n", config.levels(), config.balls());
        final GaltonBoardResult result = simulator.simulate(config);
        final String chart = renderer.render(result.slots());
        System.out.println(chart);
        System.out.printf("Макс. значение: %d шариков%n", result.maxCount());
    }
}

