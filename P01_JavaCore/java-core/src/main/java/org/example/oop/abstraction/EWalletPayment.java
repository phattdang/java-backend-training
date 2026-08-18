package org.example.oop.abstraction;

class EWalletPayment extends PaymentMethod {
    private double walletBalance;

    public EWalletPayment(String transactionId, double amount, double walletBalance) {
        super(transactionId, amount);
        this.walletBalance = walletBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public boolean pay() {
        if (amount <= 0) {
            return false;
        }

        double total = amount + calculateFee();
        if (walletBalance < total) {
            return false;
        }

        walletBalance -= total;
        return true;
    }

    @Override
    public double calculateFee() {
        if (amount > 0) {
            return amount * 0.01;
        }
        return 0;
    }
}
