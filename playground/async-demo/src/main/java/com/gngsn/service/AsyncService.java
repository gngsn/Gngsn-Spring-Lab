package com.gngsn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncService {
    final private Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Async
    public void step1(){
        log.info("step 1");
    }

    @Async("asyncExecutor")
    public void step2(){
        log.info("step 2");
    }

    @Async("fixedThreadPool")
    public void step3(){
        log.info("step 3");
    }


    @Async
    public Future<String> asyncInvokeReturnFuture(int i) {
        log.info("asyncInvokeReturnFuture, parameter={}", i);
        Future<String> future;
        try {
            Thread.sleep(1_000);
            future = new AsyncResult<String>("success:" + i);
        } catch (InterruptedException e) {
            future = new AsyncResult<String>("error");
        }
        return future;
    }
}