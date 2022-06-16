package com.gngsn.apressbatch.batch.paritioner;

import lombok.Data;

import java.time.LocalDateTime;

@SuppressWarnings("restriction")
@Data
public class Transaction {
    private String username;
    private int userId;
    private int age;
    private String postCode;
    private LocalDateTime transactionDate;
    private double amount;

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}