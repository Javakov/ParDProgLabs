package org.javakov.brothers;

import java.util.Arrays;

final class DigitMultiset {

    private final int[] counts = new int[10];

    static DigitMultiset of(int... numbers) {
        final DigitMultiset multiset = new DigitMultiset();
        for (int number : numbers) {
            int value = Math.abs(number);
            if (value == 0) {
                multiset.counts[0]++;
            }
            while (value > 0) {
                multiset.counts[value % 10]++;
                value /= 10;
            }
        }
        return multiset;
    }

    boolean equalsDigits(DigitMultiset other) {
        return Arrays.equals(counts, other.counts);
    }
}

