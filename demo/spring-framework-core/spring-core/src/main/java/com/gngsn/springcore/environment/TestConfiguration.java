package com.gngsn.springcore.environment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public BookRepository getBookRepository() {
        return new TestBookRepository();
    }
}
