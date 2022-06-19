package com.gngsn.apressbatch.config;

import com.gngsn.apressbatch.batch.paritioner.CustomMultiResourcePartitioner;
import com.gngsn.apressbatch.batch.paritioner.Transaction;
import com.gngsn.apressbatch.service.RecordFieldSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PartitionerJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ResourcePatternResolver resoursePatternResolver;

    @Bean(name = "partitionerJob")
    public Job partitionerJob()
        throws UnexpectedInputException, MalformedURLException, ParseException {
        return jobBuilderFactory.get("partitioningJob")
            .start(partitionStep())
            .build();
    }

    @Bean
    public Step partitionStep()
        throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory.get("partitionStep")
            .partitioner("slaveStep", partitioner())
            .step(slaveStep())
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Step slaveStep()
        throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory.get("slaveStep")
            .<Transaction, Transaction>chunk(1)
            .reader(itemReader(null))
            .writer(itemWriter(marshaller(), null))
            .build();
    }


    @Bean
    public Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);
        return marshaller;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(5);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public CustomMultiResourcePartitioner partitioner() {
        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
        Resource[] resources;

        try {
            resources = resoursePatternResolver
                .getResources("file:src/main/resources/input/*.csv");
        } catch (IOException e) {
            throw new RuntimeException("I/O problems when resolving"
                + " the input file pattern.", e);
        }

        partitioner.setResources(resources);
        return partitioner;
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Transaction> itemReader(
        @Value("#{stepExecutionContext[fileName]}") String filename)
            throws UnexpectedInputException, ParseException {

        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        String[] tokens = {"username", "userid", "transactiondate", "amount"};

        tokenizer.setNames(tokens);
        reader.setResource(new ClassPathResource("input/" + filename));

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());

        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<Transaction> itemWriter(Marshaller marshaller,
                                              @Value("#{stepExecutionContext[opFileName]}") String filename)
        throws MalformedURLException {
        StaxEventItemWriter<Transaction> itemWriter
            = new StaxEventItemWriter<Transaction>();
        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource(new ClassPathResource("xml/" + filename));
        return itemWriter;
    }
}
