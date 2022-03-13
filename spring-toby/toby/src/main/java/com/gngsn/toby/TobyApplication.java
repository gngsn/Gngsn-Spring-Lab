package com.gngsn.toby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {
    "classpath:jdbc.properties"
})
public class TobyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TobyApplication.class, args);
    }

}
