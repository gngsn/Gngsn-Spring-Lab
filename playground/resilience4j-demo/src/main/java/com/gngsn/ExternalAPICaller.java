package com.gngsn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExternalAPICaller {
    private final WebClient webClient;

    @Autowired
    public ExternalAPICaller(WebClient webClient) {
        this.webClient = webClient;
    }

    public String callApi() {
        return webClient.get().uri("/api/external").retrieve().bodyToMono(String.class).block();
    }

    public String callApiWithDelay() {
        String result = webClient.get().uri("/api/external").retrieve().bodyToMono(String.class).block();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
        }
        return result;
    }
}

