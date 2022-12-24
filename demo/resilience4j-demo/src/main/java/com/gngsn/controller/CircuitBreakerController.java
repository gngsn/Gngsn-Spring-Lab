package com.gngsn.controller;

import com.gngsn.dto.ResDTO;
import com.gngsn.service.RequestService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.gngsn.ConstantsName.TEST_SERVER;

@RestController("/circuitBreaker")
public class CircuitBreakerController {

    private final Logger log = LoggerFactory.getLogger(RequestService.class);
    private boolean isError = false;

    @GetMapping("/")
    public ResDTO circuitBreaker() {
        for (int i = 0; i < 20; i++) {
            circuitBreakerTestMethod(i);
        }

        return ResDTO.ok();
    }


    @CircuitBreaker(name = TEST_SERVER, fallbackMethod = "fallback")
    public String circuitBreakerTestMethod(int id) {
        log.info("## circuitBreakerTestMethod param1 : {}", id);

        if (id % 2 == 0) {
            throw new RuntimeException("Request Failed");
        }

        return "Request Success";
    }

    private String fallback(int id, RuntimeException e) {
        return "Handled the exception when the CircuitBreaker is open | param1 : " + id;
    }
}
