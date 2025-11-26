package org.javakov.md5;

/**
 * Точка входа для демонстрации полного перебора MD5.
 */
public final class Md5BruteForceApp {

    private static final String SAMPLE_PASSWORD = "p42d";
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 5;

    private Md5BruteForceApp() {
    }

    public static void main(String[] args) {
        final Md5BruteForcer bruteForcer = new Md5BruteForcer();
        final String targetHash = resolveTargetHash(args, bruteForcer);

        System.out.println("Начинаю перебор...");

        final Md5BruteForcer.CrackResult result;
        try (ProgressBar progressBar = new ProgressBar("Перебор", 120)) {
            progressBar.start();
            result = bruteForcer.crack(targetHash, MIN_LENGTH, MAX_LENGTH);
        }

        if (result.success()) {
            System.out.printf("Пароль найден: %s%n", result.password());
        } else {
            System.out.println("Пароль не найден в заданном диапазоне.");
        }
        System.out.printf("Попыток: %d%n", result.attempts());
        System.out.printf("Время перебора: %d мс%n", result.duration().toMillis());
    }

    private static String resolveTargetHash(String[] args, Md5BruteForcer bruteForcer) {
        if (args.length > 0) {
            return args[0];
        }
        final String hash = bruteForcer.hash(SAMPLE_PASSWORD);
        System.out.printf("Пароль: %s, его MD5: %s%n", SAMPLE_PASSWORD, hash);
        return hash;
    }
}

