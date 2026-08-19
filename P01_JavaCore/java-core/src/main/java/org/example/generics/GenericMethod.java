package org.example.generics;

import java.util.List;

public class GenericMethod {
    public static void main(String[] args) {
        String name = first(List.of("Alice", "Bob", "Charlie"));
        Integer number = first(List.of(10, 20, 30));

        String copiedName = identity(name);
        Integer copiedNumber = identity(number);

        printPair("User ID", 1001L);
        printPair("Active", true);

        System.out.println("First String: " + name.toUpperCase());
        System.out.println("First Integer: " + (number + 5));
        System.out.println("Identity String: " + copiedName);
        System.out.println("Identity Integer: " + copiedNumber);
    }

    static <T> T first(List<T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }
        return items.getFirst();
    }

    static <T> T identity(T value) {
        return value;
    }

    static <K, V> void printPair(K key, V value) {
        System.out.println(key + " = " + value);
    }
}
