package com.gngsn.demo.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:jdbc.properties")
@ConfigurationProperties(prefix = "db")
public class JdbcProperties {
    private String driverClass;
    private String url;
    private String username;
    private String password;
}
