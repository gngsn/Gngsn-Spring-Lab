package com.gngsn.service;

import com.gngsn.ConstServerNm;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderService {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    @CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
    public String successOrErrorWhenNumGreaterThan20(int num) {
        System.out.println("successOrErrorWhenNumGreaterThan20 is triggered; current time = " + LocalDateTime.now().format(formatter));

        if (num > 20) {
            throw new RuntimeException();
        }
        return "Berlin-Server-1 is active";
    }

    @CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
    public String getServerName() {
        System.out.println("getServerName is triggered; current time = " + LocalDateTime.now().format(formatter));

        return "Berlin-Server-1 is active";
    }

    private String fallback(Exception e) {
        return "<<<<<<<FALLBACK>>>>>> Handled the exception when the CircuitBreaker is open | Exepction : " + e.getMessage();

    }
}
