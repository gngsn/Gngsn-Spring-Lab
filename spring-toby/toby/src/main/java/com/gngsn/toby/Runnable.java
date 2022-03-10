package com.gngsn.toby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

public class Runnable implements ApplicationRunner {
    TransactionTemplate transactionTemplate;

    @Autowired
    MemberDAO memberDAO;

    void init (PlatformTransactionManager transactionManager) {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Member> members = new ArrayList<>();
        this.transactionTemplate.execute(status ->
                members.stream().peek(member -> memberDAO.add(member))
        );
    }
}
