package org.example.oop.abstraction;

class CreditCardPayment extends PaymentMethod {
    private String cardNumber;

    public CreditCardPayment(String transactionId, double amount, String cardNumber) {
        super(transactionId, amount);
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay() {
        return amount > 0 && cardNumber != null && !cardNumber.isBlank();
    }

    @Override
    public double calculateFee() {
        if (amount > 0) {
            return amount * 0.02;
        }
        return 0;
    }
}
