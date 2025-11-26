package org.javakov.tsp;

import java.io.IOException;
import java.nio.file.Path;

public final class TspApp {

    private TspApp() {
    }

    public static void main(String[] args) throws IOException {
        final Args options = Args.parse(args);
        final TspGenerator generator = new TspGenerator();
        final TspInstance instance = generator.grid(options.width, options.height, options.cities, options.seed);

        System.out.printf("TSP: городов=%d, потоки=%d%n", options.cities, options.threads);
        final ParallelTspSolver solver = new ParallelTspSolver(options.iterations, options.attempts, options.threads);
        final TspSolution solution = solver.solve(instance);

        System.out.printf("Длина маршрута: %.2f%n", solution.tour().distance());
        solution.tour().order().forEach(city ->
                System.out.printf("%s (%.1f, %.1f)%n", city.name(), city.x(), city.y()));
        System.out.printf("Время: %d мс%n", solution.duration().toMillis());

        if (options.output != null) {
            final TspRenderer renderer = new TspRenderer(800, 600, 40);
            final Path png = renderer.render(solution.tour(), options.output);
            System.out.printf("Маршрут сохранён в %s%n", png.toAbsolutePath());
        }
    }

    private record Args(
            int cities,
            int width,
            int height,
            int iterations,
            int attempts,
            int threads,
            long seed,
            Path output
    ) {

        private static Args parse(String[] args) {
            int cities = 25;
            int width = 100;
            int height = 100;
            int iterations = 100;
            int attempts = 20;
            int threads = Runtime.getRuntime().availableProcessors();
            long seed = System.currentTimeMillis();
            Path output = Path.of("C:\\Users\\dayof\\IdeaProjects\\ParDProgLabs\\src\\main\\resources\\path.png");

            for (int i = 0; i < args.length; i++) {
                final String arg = args[i];
                switch (arg) {
                    case "--cities" -> cities = Integer.parseInt(args[++i]);
                    case "--width" -> width = Integer.parseInt(args[++i]);
                    case "--height" -> height = Integer.parseInt(args[++i]);
                    case "--iterations" -> iterations = Integer.parseInt(args[++i]);
                    case "--attempts" -> attempts = Integer.parseInt(args[++i]);
                    case "--threads" -> threads = Integer.parseInt(args[++i]);
                    case "--seed" -> seed = Long.parseLong(args[++i]);
                    case "--output" -> output = Path.of(args[++i]);
                    case "--help", "-h" -> {
                        printUsage();
                        System.exit(0);
                    }
                    default -> throw new IllegalArgumentException("Неизвестный аргумент: " + arg);
                }
            }
            return new Args(cities, width, height, iterations, attempts, threads, seed, output);
        }
    }

    private static void printUsage() {
        System.out.println("""
                Использование:
                  ./gradlew runTsp --args "--cities 30 --threads 8 --output route.png"
                Аргументы:
                  --cities <n>      количество городов (по умолчанию 25)
                  --width <n>       ширина области генерации (100)
                  --height <n>      высота области (100)
                  --iterations <n>  число итераций 2-opt для каждого тура (100)
                  --attempts <n>    число случайных стартов на поток (20)
                  --threads <n>     количество потоков (кол-во ядер)
                  --seed <n>        seed генератора
                  --output <path>   путь для PNG изображения
                """);
    }
}

