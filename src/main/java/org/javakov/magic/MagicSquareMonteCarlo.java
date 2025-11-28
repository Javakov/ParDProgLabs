package org.javakov.magic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class MagicSquareMonteCarlo {

    private final Random random = new Random();

    public MagicSquare sample(int size, int maxAttempts) {
        final List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size * size; i++) {
            numbers.add(i);
        }
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Collections.shuffle(numbers, random);
            final int[] cells = numbers.stream().mapToInt(Integer::intValue).toArray();
            final MagicSquare square = new MagicSquare(size, cells);
            if (square.isMagic()) {
                return square;
            }
        }
        return null;
    }
}

