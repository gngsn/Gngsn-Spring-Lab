package com.gngsn.webClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;

@SpringBootApplication
public class WebclientTestApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(WebclientTestApplication.class, args);
    }

}
