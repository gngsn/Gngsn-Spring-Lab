package com.gngsn.demo.cleanCodeWithEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {
    private long balance;

    public void withdraw(long cash) {
        this.balance = this.balance - cash;
    }

    public void deposit(long cash) {
        this.balance = this.balance + cash;
    }
}
