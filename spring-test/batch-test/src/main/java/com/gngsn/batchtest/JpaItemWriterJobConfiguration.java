package com.gngsn.batchtest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
//	private final JobBuilderFactory jobBuilderFactory;
//	private final StepBuilderFactory stepBuilderFactory;
//	private final DataSource dataSource;

	private static final int chunkSize = 10;

	@Bean
	public Job jpaItemWriterJob() {
//		return jobBuilderFactory.get("jpaItemWriterJob")
//			.start(jpaItemWriterStep())
//			.build();
		return null;
	}

	@Bean
	public Step jdbcStep() {
//		return stepBuilderFactory.get("jpaItemWriterStep")
//			.<User, User>chunk(chunkSize)
//			.reader(jpaItemWriterReader())
//			.processor(jpaItemProcessor())
//			.writer(jpaItemWriter())
//			.build();
//		return new ItemReader
		return null;
	}


//	@Bean
//	public ItemProcessor<User, User> jpaItemProcessor() {
//		return User -> new User("gngsn", "gngsn@email.com", UUID.randomUUID());
//	}

//	@Bean
//	public JdbcBatchItemWriter<User> jdbcBatchItemWriter() {
//		return new JdbcBatchItemWriterBuilder<User>()
//			.dataSource(dataSource)
//			.sql("INSERT INTO user(name, email, password) VALUES (:name, :email, :password)")
//			.beanMapped()
//			.build();
//	}
}