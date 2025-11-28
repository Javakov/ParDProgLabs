package org.javakov.brothers;

public record BrotherTriple(
        int left,
        int right,
        int result
) {

    @Override
    public String toString() {
        return left + " * " + right + " = " + result;
    }
}

