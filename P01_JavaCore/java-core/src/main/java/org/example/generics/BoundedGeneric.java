package org.example.generics;

import java.util.List;

public class BoundedGeneric {
    public static void main(String[] args) {
        List<Integer> scores = List.of(8, 10, 7, 9);
        List<Double> prices = List.of(12.5, 9.99, 20.0);
        List<Product> products = List.of(
                new Product("Keyboard", 79),
                new Product("Mouse", 35),
                new Product("Monitor", 220)
        );

        System.out.println("sum <T extends Number>: " + sum(scores));
        System.out.println("average <T extends Number>: " + average(prices));
        System.out.println("max <T extends Comparable<T>> number: " + max(scores));
        System.out.println("max <T extends Comparable<T>> product: " + max(products));
    }

    static <T extends Number> double sum(List<T> numbers) {
        double total = 0;
        for (T number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    static <T extends Number> double average(List<T> numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("numbers must not be empty");
        }
        return sum(numbers) / numbers.size();
    }

    static <T extends Comparable<T>> T max(List<T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }

        T max = items.getFirst();
        for (T item : items) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    record Product(String name, int price) implements Comparable<Product> {
        @Override
        public int compareTo(Product other) {
            return Integer.compare(this.price, other.price);
        }
    }
}
