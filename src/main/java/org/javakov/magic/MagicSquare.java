package org.javakov.magic;

import java.util.Arrays;

public record MagicSquare(
        int size,
        int[] cells
) {

    public MagicSquare(int size, int[] cells) {
        this.size = size;
        this.cells = Arrays.copyOf(cells, cells.length);
    }

    public int magicConstant() {
        return size * (size * size + 1) / 2;
    }

    public boolean isMagic() {
        final int target = magicConstant();
        // строки
        for (int row = 0; row < size; row++) {
            int sum = 0;
            for (int col = 0; col < size; col++) {
                sum += valueAt(row, col);
            }
            if (sum != target) {
                return false;
            }
        }
        // столбцы
        for (int col = 0; col < size; col++) {
            int sum = 0;
            for (int row = 0; row < size; row++) {
                sum += valueAt(row, col);
            }
            if (sum != target) {
                return false;
            }
        }
        // диагонали
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < size; i++) {
            diag1 += valueAt(i, i);
            diag2 += valueAt(i, size - 1 - i);
        }
        return diag1 == target && diag2 == target;
    }

    private int valueAt(int row, int col) {
        return cells[row * size + col];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                sb.append(String.format("%2d ", valueAt(row, col)));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}

