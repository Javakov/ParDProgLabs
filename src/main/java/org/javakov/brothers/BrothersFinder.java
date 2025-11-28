package org.javakov.brothers;

import java.util.ArrayList;
import java.util.List;

public final class BrothersFinder {

    private final DigitMultiset reference;

    public BrothersFinder(int left, int right, int result) {
        this.reference = DigitMultiset.of(left, right, result);
    }

    public List<BrotherTriple> findBrothers() {
        final List<BrotherTriple> triples = new ArrayList<>();
        // ограничимся 2-3-4-значными множителями (как в примере), чтобы не взорваться по поиску
        for (int left = 12; left <= 9876; left++) {
            for (int right = 12; right <= 9876; right++) {
                final int result = left * right;
                if (digitCount(left) + digitCount(right) + digitCount(result) != 9) {
                    continue;
                }
                final DigitMultiset candidate = DigitMultiset.of(left, right, result);
                if (candidate.equalsDigits(reference)) {
                    triples.add(new BrotherTriple(left, right, result));
                }
            }
        }
        return triples;
    }

    private static int digitCount(int number) {
        return String.valueOf(Math.abs(number)).length();
    }
}

