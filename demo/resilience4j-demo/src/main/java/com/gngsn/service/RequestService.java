package com.gngsn.service;

import com.gngsn.ConstantsName;
import com.gngsn.util.WebClientPlugin;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gngsn.config.WebClientConfiguration.COMMON_WEB_CLIENT;

@Service
public class RequestService {

    private final WebClientPlugin webClientPlugin;

    private final WebClient webClient;

    public RequestService(@Qualifier(COMMON_WEB_CLIENT) WebClient webClient) {
        this.webClientPlugin = new WebClientPlugin(webClient);
        this.webClient = webClient;
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    @CircuitBreaker(name = ConstantsName.TEST_SERVER, fallbackMethod = "berlinServerFailFallback")
    public String failRequest() {
        System.out.println("successOrErrorWhenNumGreaterThan20 is triggered; current time = " + LocalDateTime.now().format(formatter));

        throw new RuntimeException("Request Failed");
    }

    @CircuitBreaker(name = ConstantsName.TEST_SERVER, fallbackMethod = "fallback")
    public String successRequest() {
        System.out.println("successOrErrorWhenNumGreaterThan20 is triggered; current time = " + LocalDateTime.now().format(formatter));


        return ConstantsName.TEST_SERVER + " is active";
    }

    private String berlinServerFailFallback(Exception e) {
        return "<<<<<<<FALLBACK>>>>>> Handled the exception when the CircuitBreaker is open | Exception : " + e.getMessage();

    }
}
