package org.javakov.magic;

import java.util.ArrayList;
import java.util.List;

public final class MagicSquareBruteForce {

    public List<MagicSquare> generate(int size) {
        final int n = size * size;
        final boolean[] used = new boolean[n + 1];
        final int[] cells = new int[n];
        final List<MagicSquare> result = new ArrayList<>();
        backtrack(size, 0, cells, used, result);
        return result;
    }

    private void backtrack(int size, int index, int[] cells, boolean[] used, List<MagicSquare> result) {
        final int n = size * size;
        if (index == n) {
            final MagicSquare square = new MagicSquare(size, cells);
            if (square.isMagic()) {
                result.add(square);
            }
            return;
        }
        for (int candidate = 1; candidate <= n; candidate++) {
            if (used[candidate]) {
                continue;
            }
            cells[index] = candidate;
            used[candidate] = true;
            if (isPartialValid(size, index, cells)) {
                backtrack(size, index + 1, cells, used, result);
            }
            used[candidate] = false;
        }
    }

    private boolean isPartialValid(int size, int index, int[] cells) {
        final int magic = size * (size * size + 1) / 2;
        final int row = index / size;
        final int col = index % size;

        // проверяем строку, если заполнена
        if (col == size - 1) {
            int sum = 0;
            for (int c = 0; c < size; c++) {
                sum += cells[row * size + c];
            }
            if (sum != magic) {
                return false;
            }
        }

        // проверяем столбец, если заполнен
        if (row == size - 1) {
            int sum = 0;
            for (int r = 0; r < size; r++) {
                sum += cells[r * size + col];
            }
            if (sum != magic) {
                return false;
            }
        }

        // диагонали только если заполнены
        if (row == size - 1 && col == size - 1) {
            int diag1 = 0;
            int diag2 = 0;
            for (int i = 0; i < size; i++) {
                diag1 += cells[i * size + i];
                diag2 += cells[i * size + (size - 1 - i)];
            }
            return diag1 == magic && diag2 == magic;
        }
        return true;
    }
}

