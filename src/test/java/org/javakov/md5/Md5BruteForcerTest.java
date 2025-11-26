package org.javakov.md5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Md5BruteForcerTest {

    @Test
    void crackFindsPasswordWithinBounds() {
        final Md5BruteForcer bruteForcer = new Md5BruteForcer();
        final String password = "p4rd";
        final String hash = bruteForcer.hash(password);

        final Md5BruteForcer.CrackResult result = bruteForcer.crack(hash, 4, 5);

        assertTrue(result.success(), "Пароль должен быть найден");
        assertEquals(password, result.password());
    }
}

