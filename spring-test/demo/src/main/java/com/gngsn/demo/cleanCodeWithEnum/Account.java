package com.gngsn.demo.cleanCodeWithEnum;

import java.math.BigDecimal;

public class Account {

    private String user;
    private long balance;

    public void withdraw(long cash) {
        this.balance = this.balance - cash;
    }

    public void deposit(long cash) {
        this.balance = this.balance + cash;
    }
}
