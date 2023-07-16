package com.gngsn.demo.cleanCodeWithEnum;

public class Account {
    private long balance;

    public void withdraw(long cash) {
        this.balance = this.balance - cash;
    }

    public void deposit(long cash) {
        this.balance = this.balance + cash;
    }

    public Account(final long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }
}
