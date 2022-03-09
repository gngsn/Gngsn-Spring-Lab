package com.gngsn.toby;

import ch.qos.logback.core.net.SocketConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.ResultSet;

@SpringBootApplication
@PropertySource(value = {
    "classpath:jdbc.properties"
})
public class TobyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TobyApplication.class, args);
    }

}
