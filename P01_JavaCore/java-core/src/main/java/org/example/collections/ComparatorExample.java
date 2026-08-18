package org.example.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparatorExample {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Keyboard", 25.5));
        products.add(new Product("Mouse", 12.0));
        products.add(new Product("Monitor", 150.0));

        products.sort(Comparator.comparing(Product::getName));
        System.out.println("Products sorted by name:");
        printProducts(products);

        products.sort(Comparator.comparing(Product::getPrice).reversed());
        System.out.println("Products sorted by price descending:");
        printProducts(products);
    }

    private static void printProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    static class Product {
        private final String name;
        private final double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
}
