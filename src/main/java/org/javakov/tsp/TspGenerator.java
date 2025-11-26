package org.javakov.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TspGenerator {

    public TspInstance grid(int width, int height, int count, long seed) {
        final Random random = new Random(seed);
        final List<City> cities = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final double x = random.nextDouble(width);
            final double y = random.nextDouble(height);
            cities.add(new City("C" + i, x, y));
        }
        return new TspInstance(cities);
    }
}

