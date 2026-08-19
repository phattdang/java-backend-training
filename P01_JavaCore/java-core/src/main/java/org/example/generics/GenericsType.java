package org.example.generics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericsType {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String firstName = names.getFirst();

        Map<Long, User> usersById = new HashMap<>();
        usersById.put(1L, new User(1L, "Alice"));
        usersById.put(2L, new User(2L, "Bob"));

        User user = usersById.get(1L);

        System.out.println("List<String>: " + names);
        System.out.println("First name is String: " + firstName.toUpperCase());
        System.out.println("Map<Long, User>: " + usersById);
        System.out.println("User name from map: " + user.name());
    }

    record User(Long id, String name) {
    }
}
