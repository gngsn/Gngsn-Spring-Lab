package com.gngsn.apressbatch.service;

import com.gngsn.apressbatch.partition.domain.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.time.format.DateTimeFormatter;

public class RecordFieldSetMapper  implements FieldSetMapper<Transaction> {

    public Transaction mapFieldSet(FieldSet fieldSet) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyy");

        Transaction transaction = new Transaction();
        transaction.setUsername(fieldSet.readString("username"));
        transaction.setAmount(fieldSet.readDouble(3));

        // Converting the date
//        String dateString = fieldSet.readString(2);
//        transaction.setTransactionDate(LocalDate.parse(dateString, formatter).atStartOfDay());

        return transaction;

    }

}