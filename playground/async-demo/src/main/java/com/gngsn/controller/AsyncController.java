package com.gngsn.controller;

import com.gngsn.service.AsyncFailureService;
import com.gngsn.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/v1")
public class AsyncController {

    final private Logger log = LoggerFactory.getLogger(AsyncController.class);

    final public AsyncService asyncService;

    final public AsyncFailureService asyncFailureService;

    public AsyncController(AsyncService asyncService, AsyncFailureService asyncFailureService) {
        this.asyncService = asyncService;
        this.asyncFailureService = asyncFailureService;
    }

    @GetMapping("/async/work")
    public Callable<HttpStatus> work() {
        asyncService.step1();
        asyncService.step2();
        asyncService.step3();

        return () -> HttpStatus.OK;
    }

    @GetMapping("/async/workWithFutureReturn")
    public Callable<HttpStatus> workWithFutureReturn() {
        Future<String> stringFuture = asyncService.asyncInvokeReturnFuture(5);

        while (true) {
            if (stringFuture.isDone() && !stringFuture.isCancelled()) {
                try {
                    log.info("Get Future Value {}", stringFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return () -> HttpStatus.OK;
    }


    @GetMapping("/async/work/methodsInSameClass")
    public Callable<HttpStatus> work1() {
        asyncFailureService.allInSameClass();
        return () -> HttpStatus.OK;
    }

    @GetMapping("/async/fail/methodsInSameClass")
    public Callable<HttpStatus> fail1() {
        log.info("test async not working");
        asyncFailureService.all();
        return () -> HttpStatus.OK;
    }
}