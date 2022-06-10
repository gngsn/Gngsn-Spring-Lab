package com.gngsn.apressbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ImportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    // Job은 JobBuilderFactory에 의해 정의되는데, importCustomerUpdates 스텝을 실행하도록 지정한 후 build 메서드를 호출
    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory.get("importJob")
            .start(importCustomerUpdates())
            .build();
    }

    // importCustomerUpdates Step은 StepBuilderFactory를 통해 정의되는데, 이 StepBuilderFactory를 사용하면 청크 기반으로 처리가 이뤄지도록 구성할 수 있는 StepBuilder를 가져올 수 있다.
    @Bean
    public Step importCustomerUpdates() throws Exception {
        return this.stepBuilderFactory.get("importCustomerUpdates")
            .<CustomerUpdate, CustomerUpdate> chunk(100)
            .reader(customerUpdateItemReader(null))
            .processor(customerValidatingItemProcessor(null))
            .writer(customerUpdateItemWriter())
            .build();
    }
}