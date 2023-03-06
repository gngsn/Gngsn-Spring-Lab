package com.gngsn.s3batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.s3batch.vo.Movie;
import com.gngsn.s3batch.vo.MovieCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.AbstractSqlPagingQueryProvider;
import org.springframework.batch.item.database.support.Db2PagingQueryProvider;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CsvToS3JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final String GET_MOVIE_SELECT = "SELECT movie_id as movieId, title, budget, overview, runtime, revenue FROM movie";

    private final int chunkSize = 50;


    public CsvToS3JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job makeMovieDataCsvToS3Job() {
        return jobBuilderFactory
            .get("makeMovieDataCsvToS3Job")
            .start(simpleTestTaskletStep())
            .next(makeCsvFromDb())
            .build();
    }

    @Bean
    public TaskletStep simpleTestTaskletStep() {
        return stepBuilderFactory.get("simpleTestTaskletStep")
            .tasklet(new SetExecutionContextTasklet())
            .build();
    }

    @Bean
    public Step makeCsvFromDb() {
        return stepBuilderFactory.get("makeCsvFromDb")
            .<Movie, MovieCsv>chunk(chunkSize)
            .reader(jdbcCursorItemReader())
            .processor(itemProcessor())
            .writer(batchItemWriter(null))
            .build();
    }

//    @Bean
//    public JdbcPagingItemReader<Movie> jdbcCursorItemReader() {
//        Map<String, Order> sortKeys = new HashMap<>(1);
//        sortKeys.put("movie_id", Order.DESCENDING);
//
//        JdbcPagingItemReader<Movie> reader = new JdbcPagingItemReaderBuilder<Movie>()
//            .name("jdbcCursorItemReader")
//            .selectClause("SELECT movie_id, title, budget, overview, runtime, revenue")
//            .fromClause("movie")
//            .dataSource(dataSource)
//            .fetchSize(chunkSize)
//            .pageSize(10)
//            .maxItemCount(2)
//            .sortKeys(sortKeys)
////            .queryProvider(new H2PagingQueryProvider())
//            .queryProvider(new MySqlPagingQueryProvider() {
//                @Override
//                public Map<String, Order> getSortKeysWithoutAliases() {
//                    return super.getSortKeys();
//                }
//            })
//            .rowMapper((rs, rowNum) -> new Movie(
//                rs.getString(1),
//                rs.getString(2),
//                rs.getLong(3),
//                rs.getString(4),
//                rs.getString(5),
//                rs.getString(6),
//                rs.getString(7)
//                )
//            )
//            .build();
//        return reader;
//    }
    @Bean
    public JdbcCursorItemReader<Movie> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Movie>()
            .name("jdbcCursorItemReader")
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Movie.class))
            .sql(GET_MOVIE_SELECT)
            .build();
    }

    @Bean
    public ItemProcessor<Movie, MovieCsv> itemProcessor() {
        return movie -> mapper.convertValue(mapper, MovieCsv.class);
    }

    @Bean
    @JobScope
    public FlatFileItemWriter<MovieCsv> batchItemWriter(
        @Value("#{jobExecutionContext[fileName]}") String fileName
    ) {
        return new FlatFileItemWriterBuilder<MovieCsv>()
            .name("batchItemWriter")
            .resource(new FileSystemResource(fileName))
            .append(true)
            .lineAggregator(new DelimitedLineAggregator<>() {
                {
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                        {
                            setNames(new String[] { "movieId", "title", "budget", "overview", "runtime", "revenue" });
                        }
                    });
                }
            })
            .headerCallback(writer -> writer.write("Movie ID, Title, Overview, Runtime (Sec), Revenue"))
            .encoding(StandardCharsets.UTF_8.name())
            .build();
    }
}