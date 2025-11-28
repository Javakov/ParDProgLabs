package org.javakov.magic;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public final class MagicSquareApp {

    private MagicSquareApp() {
    }

    public static void main(String[] args) {
        final Args options = Args.parse(args);
        if (options.method == Method.BRUTE_FORCE) {
            runBruteForce(options.size);
        } else {
            runMonteCarlo(options.size, options.attempts);
        }
    }

    private static void runBruteForce(int size) {
        final MagicSquareBruteForce bruteForce = new MagicSquareBruteForce();
        final Instant started = Instant.now();
        final List<MagicSquare> squares = bruteForce.generate(size);
        final Duration duration = Duration.between(started, Instant.now());

        System.out.printf("Перебор %dx%d: найдено %d квадратов за %d мс%n",
                size, size, squares.size(), duration.toMillis());
        squares.stream().limit(3).forEach(square -> {
            System.out.println(square);
            System.out.println("---");
        });
    }

    private static void runMonteCarlo(int size, int attempts) {
        final MagicSquareMonteCarlo monteCarlo = new MagicSquareMonteCarlo();
        final Instant started = Instant.now();
        final MagicSquare square = monteCarlo.sample(size, attempts);
        final Duration duration = Duration.between(started, Instant.now());
        if (square != null) {
            System.out.printf("Монте-Карло %dx%d: найден квадрат за %d мс%n", size, size, duration.toMillis());
            System.out.println(square);
        } else {
            System.out.printf("Монте-Карло %dx%d: не найдено за %d попыток%n", size, size, attempts);
        }
    }

    private enum Method {
        BRUTE_FORCE,
        MONTE_CARLO
    }

    private record Args(int size, Method method, int attempts) {

        private static Args parse(String[] args) {
            int size = 3;
            Method method = Method.BRUTE_FORCE;
            int attempts = 1_000_000;
            for (int i = 0; i < args.length; i++) {
                final String arg = args[i];
                switch (arg) {
                    case "--size" -> size = Integer.parseInt(args[++i]);
                    case "--method" -> method = Method.valueOf(args[++i].toUpperCase());
                    case "--attempts" -> attempts = Integer.parseInt(args[++i]);
                    case "--help", "-h" -> {
                        printUsage();
                        System.exit(0);
                    }
                    default -> throw new IllegalArgumentException("Неизвестный аргумент: " + arg);
                }
            }
            return new Args(size, method, attempts);
        }
    }

    private static void printUsage() {
        System.out.println("""
                Использование:
                  ./gradlew runMagic --args "--size 3 --method brute_force"
                  ./gradlew runMagic --args "--size 4 --method monte_carlo --attempts 100000"
                """);
    }
}

