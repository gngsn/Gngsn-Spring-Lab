package com.gngsn.apressbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Account {

    private final long id;
    private final BigDecimal balance;
    private final Date lastStatementDate;
    private final List<Transaction> transactions = new ArrayList<>();

}
