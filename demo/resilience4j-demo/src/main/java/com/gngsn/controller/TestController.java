package com.gngsn.controller;

import com.gngsn.service.RequestService;
import com.gngsn.dto.ResDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1")
public class TestController {

    private final CircuitBreaker circuitBreaker;

    private boolean isError = false;
    Logger log = LoggerFactory.getLogger(RequestService.class);

    public TestController(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @RequestMapping("/limit")
    public ResDTO limiter(@RequestParam int id) {

        return new ResDTO(200, "Request Success");
    }

}
