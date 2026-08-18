package org.example.collections;

import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Phat");
        names.add("Nam");
        names.add("An");
        names.add("Phat");

        System.out.println("Original list: " + names);
        System.out.println("First item: " + names.get(0));
        System.out.println("Size: " + names.size());

        names.set(1, "Minh");
        names.remove("An");

        System.out.println("After update/remove: " + names);

        for (String name : names) {
            System.out.println("Name: " + name);
        }
    }
}
