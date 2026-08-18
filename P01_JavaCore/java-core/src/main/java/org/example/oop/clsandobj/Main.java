package org.example.oop.clsandobj;

public class Main {
    public static void main(String[] args) {
        BankAccount b1 = new BankAccount("1", "Phat 1", 15);
        BankAccount b2 = new BankAccount("2", "Phat 2", 5);
        System.out.println(b1);
        System.out.println(b2);

        b1.deposit(15.5);
        b2.withdraw(3);
        b2.withdraw(3);

        System.out.println(b1);
        System.out.println(b2);
    }
}
