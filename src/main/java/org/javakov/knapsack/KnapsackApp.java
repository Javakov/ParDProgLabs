package org.javakov.knapsack;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Консольная точка входа для параллельной задачи о рюкзаке.
 */
public final class KnapsackApp {

    private static final Path DEFAULT_DATASET = Path.of("src/main/resources/knapsack/sample.txt");
    private static final int DEFAULT_PARALLELISM = Runtime.getRuntime().availableProcessors();

    private KnapsackApp() {
    }

    public static void main(String[] args) throws IOException {
        final CliOptions options = CliOptions.from(args);
        final KnapsackProblem problem = loadProblem(options.dataset());

        System.out.printf("Задача о рюкзаке: предметов=%d, вместимость=%d, потоки=%d%n",
                problem.size(), problem.capacity(), options.parallelism());

        final ParallelKnapsackSolver solver = new ParallelKnapsackSolver(options.parallelism());
        final KnapsackSolution solution = solver.solve(problem);

        System.out.printf("Лучшее значение: %d%n", solution.bestValue());
        System.out.printf("Выбрано предметов: %d%n", solution.chosenItems().size());
        for (KnapsackItem item : solution.chosenItems()) {
            System.out.printf(" - %s (w=%d, v=%d)%n", item.name(), item.weight(), item.value());
        }
        System.out.printf("Узлов исследовано: %d%n", solution.explored());
        System.out.printf("Время: %d мс%n", solution.duration().toMillis());
    }

    private static KnapsackProblem loadProblem(Path dataset) throws IOException {
        final Path path = dataset != null ? dataset : DEFAULT_DATASET;
        final KnapsackInputParser parser = new KnapsackInputParser();
        return parser.parse(path);
    }

    private record CliOptions(Path dataset, int parallelism) {

        private static CliOptions from(String[] args) {
            Path dataset = null;
            int parallelism = DEFAULT_PARALLELISM;
            for (int i = 0; i < args.length; i++) {
                final String arg = args[i];
                if ("--dataset".equals(arg) && i + 1 < args.length) {
                    dataset = Path.of(args[++i]);
                } else if ("--threads".equals(arg) && i + 1 < args.length) {
                    parallelism = Integer.parseInt(args[++i]);
                } else if ("--help".equals(arg) || "-h".equals(arg)) {
                    printUsageAndExit();
                } else {
                    System.err.println("Неизвестный аргумент: " + arg);
                    printUsageAndExit();
                }
            }
            return new CliOptions(dataset, parallelism);
        }
    }

    private static void printUsageAndExit() {
        System.out.println("""
                Использование: ./gradlew runKnapsack --args "--dataset path/to/file --threads 8"
                Аргументы:
                  --dataset <path>  Файл с входными данными (по умолчанию src/main/resources/knapsack/sample.txt)
                  --threads <n>     Число параллельных потоков (по умолчанию = числу ядер)
                Формат файла:
                  первая строка: вместимость (целое число)
                  далее: вес ценность [название]
                """);
        System.exit(0);
    }
}

