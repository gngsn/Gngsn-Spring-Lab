package com.gngsn.apressbatch.config;

import com.gngsn.apressbatch.batch.CustomerUpdateClassifier;
import com.gngsn.apressbatch.batch.CustomerUpdateItemReader;
import com.gngsn.apressbatch.batch.CustomerUpdateItemWriter;
import com.gngsn.apressbatch.domain.CustomerUpdate;
import com.gngsn.apressbatch.valid.CustomerItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.Resource;


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ImportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final CustomerUpdateItemReader customerUpdateItemReader;
    private final CustomerUpdateItemWriter customerUpdateItemWriter;

    // Job은 JobBuilderFactory에 의해 정의되는데, importCustomerUpdates 스텝을 실행하도록 지정한 후 build 메서드를 호출
    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory.get("importJob")
            .start(importCustomerUpdates())
            .build();
    }

    /**
     * importCustomerUpdates Step
     *
     * StepBuilderFactory를 통해 정의되는데, 이 StepBuilderFactory를 사용하면 청크 기반으로 처리가 이뤄지도록 구성할 수 있는 StepBuilder를 가져올 수 있다.
     *
     * reader - CustomerUpdateItemReader(ItemReader)
     * writer - CustomerUpdateItemWriter(ItemWriter)
     *
     * @return
     * @throws Exception
     */
    @Bean
    public Step importCustomerUpdates() throws Exception {
        return this.stepBuilderFactory.get("importCustomerUpdates")
            .<CustomerUpdate, CustomerUpdate> chunk(100)
            .reader(getCustomerUpdateItemReader(null))
            .processor(getCustomerValidatingItemProcessor(null))
            .writer(getCustomerUpdateItemWriter())
            .build();
    }


    /**
     * customUpdateItemReader
     * Step Scope Bean - Job Parameter를 사용해서 읽을 파일의 위치를 지정
     *
     * @param inputFile
     * @return FlatFileItemReaderBuilder
     */
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerUpdate> getCustomerUpdateItemReader(
        @Value("#{jobParameters['customerUpdateFile']}") Resource inputFile
    ) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
            .name("customerUpdateItemReader")
            .resource(inputFile)
            .lineTokenizer(customerUpdateItemReader.customerUpdatesLineTokenizer())
            .fieldSetMapper(customerUpdateItemReader.customerUpdateFieldSetMapper())
            .build();
    }

    @Bean
    public ValidatingItemProcessor<CustomerUpdate> getCustomerValidatingItemProcessor(
        CustomerItemValidator validator
    ) {
        ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor =
            new ValidatingItemProcessor<>(validator);

        customerValidatingItemProcessor.setFilter(true);
        return customerValidatingItemProcessor;
    }

    @Bean
    public ClassifierCompositeItemWriter<CustomerUpdate> getCustomerUpdateItemWriter() {
        CustomerUpdateClassifier classifier =
            new CustomerUpdateClassifier(
                customerUpdateItemWriter.customerNameUpdateItemWriter(),
                customerUpdateItemWriter.customerAddressUpdateItemWriter(),
                customerUpdateItemWriter.customerContactUpdateItemWriter()
            );
        ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();

        compositeItemWriter.setClassifier(classifier);
        return compositeItemWriter;
    }

}