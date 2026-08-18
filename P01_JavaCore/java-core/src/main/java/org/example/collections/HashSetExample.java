package org.example.collections;

import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<String> emails = new HashSet<>();

        emails.add("phat@gmail.com");
        emails.add("nam@gmail.com");
        emails.add("phat@gmail.com");

        System.out.println("Emails: " + emails);
        System.out.println("Size: " + emails.size());
        System.out.println("Contains Phat email: " + emails.contains("phat@gmail.com"));

        emails.remove("nam@gmail.com");

        System.out.println("After remove: " + emails);
    }
}
