package org.example.collections;

import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> productStock = new HashMap<>();

        productStock.put("keyboard", 10);
        productStock.put("mouse", 25);
        productStock.put("monitor", 5);
        productStock.put("keyboard", 12);

        System.out.println("Stock map: " + productStock);
        System.out.println("Keyboard stock: " + productStock.get("keyboard"));
        System.out.println("Has mouse: " + productStock.containsKey("mouse"));

        productStock.remove("monitor");

        for (Map.Entry<String, Integer> entry : productStock.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
