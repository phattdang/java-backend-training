package org.example.oop.inheritance;

public class Main {
    public static void main(String[] args) {
        Developer dev = new Developer("Phat", 1000, 5);
        Manager manager = new Manager("Nam", 1500, 500);

        System.out.println(dev);
        System.out.println(manager);

        System.out.println(dev.calculateSalary());
        System.out.println(manager.calculateSalary());
    }
}
