package org.example.oop.abstraction;

class BankTransferPayment extends PaymentMethod {
    private String bankCode;

    public BankTransferPayment(String transactionId, double amount, String bankCode) {
        super(transactionId, amount);
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public boolean pay() {
        return amount > 0 && bankCode != null && !bankCode.isBlank();
    }

    @Override
    public double calculateFee() {
        return 10;
    }
}
