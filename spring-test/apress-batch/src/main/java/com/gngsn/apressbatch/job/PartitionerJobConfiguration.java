package com.gngsn.apressbatch.config;

import com.gngsn.apressbatch.batch.CustomMultiResourcePartitioner;
import com.gngsn.apressbatch.partition.domain.Transaction;
import com.gngsn.apressbatch.service.RecordFieldSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.net.MalformedURLException;

@Configuration
@RequiredArgsConstructor
public class PartitionerJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ResourcePatternResolver resourcePatternResolver;

    @Bean
    public Job partitionerJob()
        throws UnexpectedInputException, MalformedURLException, ParseException {
        return jobBuilderFactory
            .get("partitionerJob")
            .incrementer(new RunIdIncrementer())
            .start(partitionStep())
            .build();
    }

    @Bean
    public Step partitionStep()
        throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory
            .get("partitionStep")
            .partitioner("slaveStep", partitioner())
            .step(slaveStep())
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Step slaveStep()
        throws UnexpectedInputException, ParseException {
        return stepBuilderFactory
            .get("slaveStep")
            .<Transaction, Transaction>chunk(1)
            .reader(flatFileItemReader(null))
            .writer(staxEventItemWriterBuilder(marshaller(), null))
            .build();
    }


    @Bean
    public Jaxb2Marshaller marshaller() {
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
            resources = resourcePatternResolver
                .getResources("file:src/main/resources/input/*.csv");
        } catch (IOException e) {
            throw new RuntimeException("I/O problems when resolving"
                + " the input file pattern.", e);
        }

        partitioner.setResources(resources);
        return partitioner;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> flatFileItemReader(
        @Value("#{jobParameters[fileName]}") String filename)
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

    @Bean(destroyMethod = "")
    @StepScope
    public StaxEventItemWriter<Transaction> staxEventItemWriterBuilder(
        Marshaller marshaller,
        @Value("#{jobParameters['opFileName']}") String filename) {

        return new StaxEventItemWriterBuilder<Transaction>()
            .name("staxEventItemWriterBuilder")
            .marshaller(marshaller)
            .rootTagName("transactionRecord")
            .resource(new FileSystemResource("src/main/resources/output/" + Math.random() + filename))
            .build();
    }
}
