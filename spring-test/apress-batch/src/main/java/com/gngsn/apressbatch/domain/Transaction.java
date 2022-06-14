package com.gngsn.apressbatch.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@XStreamAlias("transaction")
public class Transaction {

    @XStreamAlias("transactionId")
    private long transactionId;

    @XStreamAlias("accountId")
    private long accountId;

    @XStreamAlias("description")
    private String description;

    @XStreamAlias("credit")
    private BigDecimal credit;

    @XStreamAlias("debit")
    private BigDecimal debit;

    @XStreamAlias("timestamp")
    private Date timestamp;
//
//    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public BigDecimal getTimestampAmount() {
//        if (credit != null) {
//            if (debit != null) {
//                return credit.add(debit);
//            } else {
//                return credit;
//            }
//        } else if (debit != null) {
//            return debit;
//        } else {
//            return new BigDecimal(0);
//        }
//    }
}
