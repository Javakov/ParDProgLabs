package org.javakov.brothers;

import java.util.List;

public final class BrothersApp {

    private BrothersApp() {
    }

    public static void main(String[] args) {
        final int left = args.length > 0 ? Integer.parseInt(args[0]) : 48;
        final int right = args.length > 1 ? Integer.parseInt(args[1]) : 159;
        final int result = args.length > 2 ? Integer.parseInt(args[2]) : 7632;

        System.out.printf("Ищу собратьев для %d * %d = %d%n", left, right, result);
        final BrothersFinder finder = new BrothersFinder(left, right, result);
        final List<BrotherTriple> brothers = finder.findBrothers();

        System.out.printf("Найдено %d собратьев:%n", brothers.size());
        brothers.stream().limit(50).forEach(System.out::println);
        if (brothers.size() > 50) {
            System.out.println("... (показаны первые 50)");
        }
    }
}

