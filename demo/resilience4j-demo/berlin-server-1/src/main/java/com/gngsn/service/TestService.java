package com.gngsn.service;

import com.gngsn.ConstServerNm;
import com.gngsn.service.delay.NoDelay;
import com.gngsn.service.delay.PotentialDelay;
import com.gngsn.service.failure.NoFailure;
import com.gngsn.service.failure.PotentialFailure;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TestService {
    final PotentialFailure potentialFailure;
    final PotentialDelay potentialDelay;

    public TestService() {
        this.potentialFailure = new NoFailure();
        this.potentialDelay = new NoDelay();
    }

    public TestService(PotentialFailure potentialFailure, PotentialDelay potentialDelay) {
        this.potentialFailure = potentialFailure;
        this.potentialDelay = potentialDelay;
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    @CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
    public String successOrErrorWhenNumGreaterThan20(int num) {
        System.out.println("successOrErrorWhenNumGreaterThan20 is triggered; current time = " + LocalDateTime.now().format(formatter));

        potentialDelay.occur();
        potentialFailure.occur();

        if (num > 20) {
            throw new RuntimeException();
        }
        return "Berlin-Server-1 is active";
    }

    @CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
    public String getServerName() {
        System.out.println("getServerName is triggered; current time = " + LocalDateTime.now().format(formatter));

        potentialDelay.occur();
        potentialFailure.occur();

        return "Berlin-Server-1 is active";
    }

    private String fallback(Exception e) {
        return "<<<<<<<FALLBACK>>>>>> Handled the exception when the CircuitBreaker is open | Exepction : " + e.getMessage();

    }
}
