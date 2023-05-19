package com.gngsn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataTransactionService {

    final private Logger log = LoggerFactory.getLogger(DataTransactionService.class);

    @Transactional
    public void allStep(){
        saveObj();
        updateObj();
    }

    public void saveObj(){
        log.info("Some data saved");
    }

    public void updateObj(){
        log.info("Some another data updated");
    }
}
