package com.gngsn.toby.transaction.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:jdbc.properties")
@ConfigurationProperties(prefix = "test")
public class TestJdbcProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}

