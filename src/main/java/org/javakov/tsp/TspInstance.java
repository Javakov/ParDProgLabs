package org.javakov.tsp;

import java.util.List;

public record TspInstance(List<City> cities) {

    public int size() {
        return cities.size();
    }
}

