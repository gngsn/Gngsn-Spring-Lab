package com.gngsn.webClientTest;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class WebclientTestApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(WebclientTestApplication.class, args);
    }

}
