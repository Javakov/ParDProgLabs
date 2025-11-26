package org.javakov.knapsack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер текстового формата:
 * первая строка — вместимость,
 * затем строки "weight value [name]".
 */
public final class KnapsackInputParser {

    public KnapsackProblem parse(Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст");
        }
        final int capacity = Integer.parseInt(lines.getFirst().trim());
        final List<KnapsackItem> items = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            final String[] tokens = line.split("\\s+");
            if (tokens.length < 2) {
                throw new IllegalArgumentException("Строка должна содержать вес и ценность: " + line);
            }
            final int weight = Integer.parseInt(tokens[0]);
            final int value = Integer.parseInt(tokens[1]);
            final String name = tokens.length >= 3 ? line.substring(line.indexOf(tokens[1]) + tokens[1].length()).trim() : "item-" + (items.size() + 1);
            items.add(new KnapsackItem(name, weight, value));
        }
        return new KnapsackProblem(capacity, items);
    }
}

