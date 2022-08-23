package com.gngsn.apressbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Statement {

    private final Customer customer;
    private List<Account> accounts = new ArrayList<>();

    public Statement(Customer customer) {
        this.customer = customer;
    }
}
