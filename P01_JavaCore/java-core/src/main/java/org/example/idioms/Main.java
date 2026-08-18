package org.example.idioms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        // 1. calling equals on string literal
        String str1 = "Hello";
        String str2 = null;
        System.out.println(str1.equals(str2)); // false OK - best practice to avoid NPE
//        System.out.println(str2.equals(str1)); // NPE

        // 2. List.of() vs Arrays.asList()
        List<String> list1 = List.of("A", "B", "C"); // can not set or add new elements
        System.out.println(list1);
        // list1.add("D"); // UnsupportedOperationException - List.of() returns immutable list

        List<String> list2 = Arrays.asList("A", "B", "C"); // can set but can not add new elements
        System.out.println(list2);
        list2.set(0, "D"); // OK
        System.out.println(list2);
        // list2.add("E"); // UnsupportedOperationException - Arrays.asList() returns fixed

        List<String> list3 = new ArrayList<>(Arrays.asList("A", "B", "C")); // mutable list
        System.out.println(list3);
        list3.add("D"); // OK
        System.out.println(list3);
        list3.set(0, "E"); // OK
        System.out.println(list3);

        // 3. program to interface, not implementation
        List<String> names = new ArrayList<>();
        names.add("Phat");
        names.add("Nam");
        System.out.println(names);

        // 4. use Objects.equals() when both values can be null
        String email1 = null;
        String email2 = null;
        System.out.println(Objects.equals(email1, email2)); // true, no NPE

        // 5. return empty collection instead of null
        List<String> emptyOrders = findOrdersByUserId("unknown-user");
        System.out.println(emptyOrders.size());

        // 6. use enum instead of magic string
        Order order = new Order("ORD-001", OrderStatus.PAID);
        System.out.println(order.isPaid());

        // 7. favor immutability for simple data carriers
        Product product = new Product("P001", "Keyboard", 25.5);
        System.out.println(product.name());

        // 8. encapsulate state, expose behavior
        Wallet wallet = new Wallet(1000);
        wallet.withdraw(200);
        System.out.println(wallet.getBalance());
    }

    private static List<String> findOrdersByUserId(String userId) {
        return Collections.emptyList();
    }

    enum OrderStatus {
        NEW,
        PAID,
        CANCELLED
    }

    static class Order {
        private final String id;
        private final OrderStatus status;

        public Order(String id, OrderStatus status) {
            this.id = id;
            this.status = status;
        }

        public boolean isPaid() {
            return status == OrderStatus.PAID;
        }
    }

    record Product(String id, String name, double price) {
        Product {
            if (price < 0) {
                throw new IllegalArgumentException("Price must be >= 0");
            }
        }
    }

    static class Wallet {
        private double balance;

        public Wallet(double balance) {
            if (balance < 0) {
                throw new IllegalArgumentException("Balance must be >= 0");
            }
            this.balance = balance;
        }

        public double getBalance() {
            return balance;
        }

        public void withdraw(double amount) {
            if (amount <= 0 || amount > balance) {
                throw new IllegalArgumentException("Invalid amount");
            }
            balance -= amount;
        }
    }
}
