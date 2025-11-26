package org.javakov.galton;

/**
 * Строит псевдографику распределения: столбчатая диаграмма фиксированной высоты.
 */
public record GaltonBoardAsciiRenderer(
        int maxHeight,
        char filledSymbol
) {

    public GaltonBoardAsciiRenderer {
        if (maxHeight < 1) {
            throw new IllegalArgumentException("Высота диаграммы должна быть >= 1");
        }
    }

    public String render(int[] counts) {
        final int max = max(counts);
        if (max == 0) {
            return "(нет данных)";
        }
        final int columns = counts.length;
        final StringBuilder builder = new StringBuilder((maxHeight + 2) * (columns + 4));

        for (int row = maxHeight; row >= 1; row--) {
            builder.append(String.format("%3d |", row));
            for (int count : counts) {
                final int height = scale(count, max);
                builder.append(height >= row ? filledSymbol : ' ');
                builder.append(' ');
            }
            builder.append(System.lineSeparator());
        }
        builder.append("    +");
        builder.append("— ".repeat(columns));
        builder.append(System.lineSeparator());
        builder.append("     ");
        for (int i = 0; i < columns; i++) {
            builder.append(i % 10);
            builder.append(' ');
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private int scale(int value, int max) {
        if (value == 0) {
            return 0;
        }
        return Math.max(1, (int) Math.round((value / (double) max) * maxHeight));
    }

    private static int max(int[] counts) {
        int max = 0;
        for (int count : counts) {
            if (count > max) {
                max = count;
            }
        }
        return max;
    }
}

