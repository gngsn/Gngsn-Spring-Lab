package com.gngsn.service;

import com.gngsn.ConstServerNm;
import com.gngsn.service.delay.NoDelay;
import com.gngsn.service.delay.PotentialDelay;
import com.gngsn.service.failure.NoFailure;
import com.gngsn.service.failure.PotentialFailure;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;
    PotentialFailure potentialFailure = new NoFailure();
    PotentialDelay potentialDelay = new NoDelay();

    public TestService(io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
    public String successOrErrorWhenNumGreaterThan20(int num) {
        if (num > 20) {
            throw new RuntimeException();
        }
        return "Berlin-Server-1 is active";
    }

    private String fallback(Exception e) {
        return "Handled the exception when the CircuitBreaker is open | Exepction : " + e.getMessage();

    }
}
