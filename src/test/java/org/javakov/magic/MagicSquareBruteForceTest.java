package org.javakov.magic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MagicSquareBruteForceTest {

    @Test
    void generatesMagicSquaresFor3x3() {
        final MagicSquareBruteForce bruteForce = new MagicSquareBruteForce();
        final List<MagicSquare> squares = bruteForce.generate(3);
        assertFalse(squares.isEmpty());
        assertTrue(squares.getFirst().isMagic());
    }
}

