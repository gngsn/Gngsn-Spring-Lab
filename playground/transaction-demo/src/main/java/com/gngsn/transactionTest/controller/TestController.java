package com.gngsn.webClient.controller;

import com.gngsn.transactionTest.controller.ResponseView;
import com.gngsn.webClient.exception.BadWebClientRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value = "/200")
    public ResponseEntity<ResponseView> res200() {
        log.info("Request res 200");
        return ResponseEntity.ok(ResponseView.success("Request Success"));
    }

    @RequestMapping(value = "/400")
    public ResponseEntity res400() {
        log.info("Request res400");
        return new ResponseEntity(ResponseView.success("Bad Request"), HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/500")
    public ResponseEntity<ResponseView> res500() {
        log.info("Request res500");
        throw new BadWebClientRequestException(500, "Server Error");
    }


    @RequestMapping(value = "/timeout")
    public ResponseView timeout() throws InterruptedException {
        log.info("timeout test");
        Thread.sleep(10_000);
        return ResponseView.success("timeout test");
    }
}
