package com.gngsn.s3batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableBatchProcessing
@PropertySource({"classpath:application.properties"})
public class S3BatchDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(S3BatchDemoApplication.class, args);
    }

}
