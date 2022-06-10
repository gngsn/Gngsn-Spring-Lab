package com.gngsn.apressbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    ## @EnableBatchProcessing 추가
    EnableBatchProcessing이 제공하는 JobBuilderFactory, StepBuilderFactory를 오토와이어링할 수 있음
 */
@EnableBatchProcessing
@SpringBootApplication
public class ApressBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApressBatchApplication.class, args);
    }

}
