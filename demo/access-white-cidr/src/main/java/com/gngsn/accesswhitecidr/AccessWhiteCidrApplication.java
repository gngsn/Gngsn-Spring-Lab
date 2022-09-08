package com.gngsn.accesswhitecidr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@PropertySource(value = {
    "classpath:jdbc.properties"
})
@SpringBootApplication
public class AccessWhiteCidrApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessWhiteCidrApplication.class, args);
    }

}
