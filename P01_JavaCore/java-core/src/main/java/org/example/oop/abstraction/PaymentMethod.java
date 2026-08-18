package org.example.oop.abstraction;

abstract class PaymentMethod {
    protected String transactionId;
    protected double amount;

    public PaymentMethod(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public abstract boolean pay();

    public abstract double calculateFee();

    public void printReceipt() {
        double fee = calculateFee();

        System.out.println("Transaction: " + transactionId);
        System.out.println("Amount: " + amount);
        System.out.println("Fee: " + fee);
        System.out.println("Total: " + (amount + fee));
    }
}
