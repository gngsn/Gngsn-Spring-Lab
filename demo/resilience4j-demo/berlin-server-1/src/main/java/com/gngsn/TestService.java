package com.gngsn;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public TestService(io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @CircuitBreaker(name = ConstServerNm.BERLIN_SERVER_1, fallbackMethod = "fallback")
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
