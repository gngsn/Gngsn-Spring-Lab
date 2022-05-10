package com.gngsn.demo.cleanCodeWithEnum;

import java.math.BigDecimal;

public class Account {

    private String user;
    private double balance;

    public void withdraw(double cash) {
        this.balance = this.balance - cash;
    }

    public void deposit(double cash) {
        this.balance = this.balance + cash;
    }
}
