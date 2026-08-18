package org.example.collections;

import java.util.Set;
import java.util.TreeSet;

public class TreeSetExample {
    public static void main(String[] args) {
        Set<Integer> scores = new TreeSet<>();

        scores.add(80);
        scores.add(95);
        scores.add(70);
        scores.add(80);

        System.out.println("Sorted scores: " + scores);
        System.out.println("Duplicate 80 is stored only once");

        Set<String> names = new TreeSet<>();
        names.add("Phat");
        names.add("An");
        names.add("Nam");

        System.out.println("Sorted names: " + names);
    }
}
