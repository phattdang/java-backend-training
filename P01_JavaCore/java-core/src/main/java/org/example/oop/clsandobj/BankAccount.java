package org.example.oop.clsandobj;

class BankAccount {
    private String accountNum;
    private String ownerName;
    private double balance;

    public BankAccount() {
    }

    public BankAccount(String accountNum, String ownerName, double balance) {
        this.accountNum = accountNum;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount){
        if(amount <= 0){
            System.out.println("Amount must be > 0!");
            return;
        }
        this.balance += amount;
    }

    public void withdraw(double amount){
        if(amount <= 0 || amount > this.balance){
            System.out.println("Invalid!!!");
            return;
        }
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNum='" + accountNum + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
