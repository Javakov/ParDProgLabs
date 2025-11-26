package org.javakov.md5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * Утилита полного перебора паролей по MD5-хэшу.
 * <p>
 * Класс не зависит от консоли/IO, поэтому может использоваться
 * как в демонстрационной утилите, так и в тестах или других проектах.
 */
public final class Md5BruteForcer {

    /**
     * Базовый алфавит: латиница в обоих регистрах + цифры.
     * Можно передать свой алфавит через конструктор.
     */
    private static final char[] DEFAULT_ALPHABET = (
            "abcdefghijklmnopqrstuvwxyz" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "0123456789").toCharArray();

    private final MessageDigest digest;
    private final char[] alphabet;

    /**
     * Создаёт переборщик с алфавитом по умолчанию.
     */
    public Md5BruteForcer() {
        this(DEFAULT_ALPHABET);
    }

    /**
     * Создаёт переборщик с кастомным алфавитом.
     *
     * @param alphabet символы, которые будут использоваться при переборе
     */
    public Md5BruteForcer(char[] alphabet) {
        this.digest = createDigest();
        this.alphabet = Arrays.copyOf(alphabet, alphabet.length);
    }

    /**
     * Запускает полный перебор для заданного MD5-хэша и диапазона длин пароля.
     *
     * @param targetHash целевой MD5
     * @param minLen     минимальная длина пароля (включительно)
     * @param maxLen     максимальная длина пароля (включительно)
     * @return результат с найденным паролем (или null), числом попыток и длительностью
     */
    public CrackResult crack(String targetHash, int minLen, int maxLen) {
        validateBounds(minLen, maxLen);
        final String normalizedHash = normalizeHash(targetHash);
        final Instant start = Instant.now();
        long attempts = 0L;

        for (int length = minLen; length <= maxLen; length++) {
            final char[] buffer = new char[length];
            final var result = search(buffer, 0, normalizedHash);
            attempts += result.attempts;
            if (result.password != null) {
                final Duration spent = Duration.between(start, Instant.now());
                return new CrackResult(result.password, attempts, spent);
            }
        }
        final Duration spent = Duration.between(start, Instant.now());
        return new CrackResult(null, attempts, spent);
    }

    /**
     * Рекурсивный обход пространства паролей.
     * Строим строку в {@code buffer}, двигаясь по позициям слева направо.
     */
    private SearchOutcome search(char[] buffer, int position, String targetHash) {
        long attempts = 0L;
        if (position == buffer.length) {
            attempts++;
            final String candidate = new String(buffer);
            final String candidateHash = hash(candidate);
            if (candidateHash.equals(targetHash)) {
                return new SearchOutcome(candidate, attempts);
            }
            return new SearchOutcome(null, attempts);
        }

        for (char symbol : alphabet) {
            buffer[position] = symbol;
            final SearchOutcome outcome = search(buffer, position + 1, targetHash);
            attempts += outcome.attempts;
            if (outcome.password != null) {
                return new SearchOutcome(outcome.password, attempts);
            }
        }
        return new SearchOutcome(null, attempts);
    }

    /**
     * Вычисляет MD5-хэш для произвольной строки (публично для удобства тестов/демо).
     */
    public String hash(String input) {
        final byte[] hashed = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        final StringBuilder sb = new StringBuilder(hashed.length * 2);
        for (byte b : hashed) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Проверяет границы перебора, чтобы избежать бессмысленных запусков.
     */
    private static void validateBounds(int minLen, int maxLen) {
        if (minLen < 1) {
            throw new IllegalArgumentException("Минимальная длина должна быть >= 1");
        }
        if (maxLen < minLen) {
            throw new IllegalArgumentException("Максимальная длина меньше минимальной");
        }
    }

    /**
     * Создаёт объект {@link MessageDigest}, выбрасывая понятное исключение при ошибке.
     */
    private static MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Алгоритм MD5 недоступен", e);
        }
    }

    /**
     * Приводит хэш к нижнему регистру и проверяет корректность длины.
     */
    private static String normalizeHash(String hash) {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Хэш не может быть пустым");
        }
        final String trimmed = hash.trim();
        if (trimmed.length() != 32) {
            throw new IllegalArgumentException("Ожидается 32-символьный MD5-хэш");
        }
        return trimmed.toLowerCase();
    }

    /**
     * Публичный DTO с итогом перебора:
     * пароль (если найден), количество попыток, длительность.
     */
    public record CrackResult(String password, long attempts, Duration duration) {
        public boolean success() {
            return password != null;
        }
    }

    /**
     * Внутренний DTO, возвращаемый рекурсией, чтобы накапливать попытки.
     */
    private record SearchOutcome(String password, long attempts) {
    }
}

