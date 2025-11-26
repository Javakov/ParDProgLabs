package org.javakov.tsp;

import java.time.Duration;

public record TspSolution(
        Tour tour,
        Duration duration
) {
}

