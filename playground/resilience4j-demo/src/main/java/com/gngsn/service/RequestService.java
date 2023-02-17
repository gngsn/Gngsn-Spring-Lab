package com.gngsn.service;

import com.gngsn.ConstantsName;
import com.gngsn.util.WebClientPlugin;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gngsn.config.WebClientConfiguration.COMMON_WEB_CLIENT;

@Service
public class RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    @CircuitBreaker(name = ConstantsName.TEST_SERVER, fallbackMethod = "throwExceptionFallback")
    public String failRequest() {
        log.info("FailRequest is triggered. Current time: {}", LocalDateTime.now().format(formatter));

        throw new RuntimeException("Request Failed");
    }

    private String throwExceptionFallback(Exception e) {
        return "<<<FALLBACK>>> Error occured | Exception : " + e.getMessage();
    }
    private String throwExceptionFallback(CallNotPermittedException e) {
        return "<<<FALLBACK>>> CircuitBreaker is open | Exception : " + e.getMessage();
    }
}
