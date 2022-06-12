package com.gngsn.apressbatch.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
    private long transactionId;
    private long accountId;
    private String description;
    private BigDecimal credit;
    private BigDecimal debit;
    private Date timestamp;
}
