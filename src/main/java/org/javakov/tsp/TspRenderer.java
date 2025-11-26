package org.javakov.tsp;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public record TspRenderer(
        int width,
        int height,
        int padding
) {

    public Path render(Tour tour, Path output) throws IOException {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2f));

        final List<City> cities = tour.order();
        final Bounds bounds = Bounds.of(cities);

        for (int i = 0; i < cities.size(); i++) {
            final City current = cities.get(i);
            final City next = cities.get((i + 1) % cities.size());
            drawLine(g, current, next, bounds);
        }

        g.setColor(new Color(0x2F80ED));
        for (City city : cities) {
            drawCity(g, city, bounds);
        }
        g.dispose();
        ImageIO.write(image, "png", output.toFile());
        return output;
    }

    private void drawLine(Graphics2D g, City from, City to, Bounds bounds) {
        final int x1 = translateX(from.x(), bounds);
        final int y1 = translateY(from.y(), bounds);
        final int x2 = translateX(to.x(), bounds);
        final int y2 = translateY(to.y(), bounds);
        g.drawLine(x1, y1, x2, y2);
    }

    private void drawCity(Graphics2D g, City city, Bounds bounds) {
        final int x = translateX(city.x(), bounds);
        final int y = translateY(city.y(), bounds);
        g.fillOval(x - 4, y - 4, 8, 8);
    }

    private int translateX(double x, Bounds bounds) {
        final double span = bounds.maxX - bounds.minX;
        final double normalized = span == 0.0 ? 0.5 : (x - bounds.minX) / span;
        return padding + (int) Math.round(normalized * (width - 2 * padding));
    }

    private int translateY(double y, Bounds bounds) {
        final double span = bounds.maxY - bounds.minY;
        final double normalized = span == 0.0 ? 0.5 : (y - bounds.minY) / span;
        return padding + (int) Math.round(normalized * (height - 2 * padding));
    }

    private record Bounds(double minX, double maxX, double minY, double maxY) {

        static Bounds of(List<City> cities) {
            double minX = Double.POSITIVE_INFINITY;
            double maxX = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            double maxY = Double.NEGATIVE_INFINITY;
            for (City city : cities) {
                minX = Math.min(minX, city.x());
                maxX = Math.max(maxX, city.x());
                minY = Math.min(minY, city.y());
                maxY = Math.max(maxY, city.y());
            }
            if (minX == Double.POSITIVE_INFINITY) {
                minX = maxX = minY = maxY = 0.0;
            }
            return new Bounds(minX, maxX, minY, maxY);
        }
    }
}

