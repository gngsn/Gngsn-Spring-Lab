package com.gngsn.demo.cleanCodeWithEnum;

import java.math.BigDecimal;

public class Account {

    private String user;
    private BigDecimal deposit;

    public void withdraw(BigDecimal cash) {
        this.deposit = this.deposit.subtract(cash);
    }
}
