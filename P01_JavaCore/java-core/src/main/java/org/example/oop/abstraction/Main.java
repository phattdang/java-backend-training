package org.example.oop.abstraction;

public class Main {
    public static void main(String[] args) {
        PaymentMethod p1 = new CreditCardPayment("TXN123", 100.0, "1234-5678-9012-3456");
        PaymentMethod p2 = new EWalletPayment("TXN456", 200.0, 350);
        PaymentMethod p3 = new BankTransferPayment("TXN789", 300.0, "BANK001");

        processPayment(p1);
        processPayment(p2);
        processPayment(p3);
    }

    public static void processPayment(PaymentMethod payment) {
        if (payment.pay()) {
            payment.printReceipt();
        } else {
            System.out.println("Payment failed");
        }
    }
}
