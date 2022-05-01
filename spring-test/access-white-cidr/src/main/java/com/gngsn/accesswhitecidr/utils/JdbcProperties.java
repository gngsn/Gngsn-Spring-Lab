package com.gngsn.accesswhitecidr.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "db")
public class JdbcProperties {
    private String driverClass;
    private String url;
    private String username;
    private String password;
}
